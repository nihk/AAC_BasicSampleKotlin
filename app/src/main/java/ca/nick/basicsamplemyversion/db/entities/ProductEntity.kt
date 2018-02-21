package ca.nick.basicsamplemyversion.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import ca.nick.basicsamplemyversion.models.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    override val id: Int,
    override val name: String,
    override val description: String,
    override val price: Int
) : Product