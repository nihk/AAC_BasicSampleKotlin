package ca.nick.basicsamplemyversion.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import ca.nick.basicsamplemyversion.models.Comment
import java.util.*

@Entity(
    tableName = "comments",
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["productId"])]
)
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    override val productId: Int,
    override val text: String,
    override val postedAt: Date
) : Comment