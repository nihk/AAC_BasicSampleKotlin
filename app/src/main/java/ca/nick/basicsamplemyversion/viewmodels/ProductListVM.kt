package ca.nick.basicsamplemyversion.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import ca.nick.basicsamplemyversion.DataRepository
import ca.nick.basicsamplemyversion.MyBasicApp
import ca.nick.basicsamplemyversion.db.entities.ProductEntity
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class ProductListVM(application: Application) : AndroidViewModel(application) {

    private val _observableProducts = MediatorLiveData<List<ProductEntity>>()
    private val repository: DataRepository

    val observableProducts: LiveData<List<ProductEntity>>
        get() = _observableProducts

    init {
        _observableProducts.value = null
        repository = (application as MyBasicApp).getRepository()
        _observableProducts.addSource(repository.observableProducts, _observableProducts::setValue)
    }

    fun addProduct(product: ProductEntity) {
        async(repository.addProduct(product))
        Log.d("Nick", "Adding product: ${product.id}")
    }

    fun deleteAll() {
        async(repository.deleteAll())
    }

    private fun async(completable: Completable) {
        completable
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}