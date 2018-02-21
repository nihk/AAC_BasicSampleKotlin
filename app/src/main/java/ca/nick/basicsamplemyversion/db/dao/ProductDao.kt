package ca.nick.basicsamplemyversion.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import ca.nick.basicsamplemyversion.db.entities.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun loadAllProducts(): LiveData<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId")
    fun loadProduct(productId: Int): LiveData<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: ProductEntity)

    @Query("DELETE FROM products")
    fun nuke()
}