package ca.nick.basicsamplemyversion.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import ca.nick.basicsamplemyversion.db.entities.CommentEntity

@Dao
interface CommentDao {

    @Query("SELECT * FROM comments WHERE productId = :productId")
    fun loadComments(productId: Int): LiveData<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(comments: List<CommentEntity>)
}