package ca.nick.basicsamplemyversion

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.os.SystemClock
import ca.nick.basicsamplemyversion.db.AppDatabase
import ca.nick.basicsamplemyversion.db.DataGenerator
import ca.nick.basicsamplemyversion.db.entities.ProductEntity
import io.reactivex.Completable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class DataRepository private constructor(private val appDatabase: AppDatabase) {

    private val _observableProducts = MediatorLiveData<List<ProductEntity>>()

    val observableProducts: LiveData<List<ProductEntity>>
        get() = _observableProducts

    init {
        _observableProducts.addSource(appDatabase.productDao().loadAllProducts()) { productEntities ->
            appDatabase.isDatabaseCreated.value?.let {
                _observableProducts.postValue(productEntities)
            }
        }
    }

    companion object {
        private var INSTANCE: DataRepository? = null

        fun getInstance(appDatabase: AppDatabase) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataRepository(appDatabase).also { INSTANCE = it }
            }
    }

    fun loadProduct(productId: Int) =
        appDatabase.productDao().loadProduct(productId)

    fun loadComments(productId: Int) =
        appDatabase.commentDao().loadComments(productId)

    fun addProduct(product: ProductEntity): Completable {
        val comments = DataGenerator.generateCommentsForProducts(listOf(product))

        val insertProduct = Completable.fromAction { appDatabase.productDao().insert(product) }
        val insertComments = Completable.fromAction { appDatabase.commentDao().insertAll(comments) }

        return Completable.concatArray(insertProduct, insertComments)
    }

    fun deleteAll() = Completable.fromAction { appDatabase.productDao().nuke() }
}