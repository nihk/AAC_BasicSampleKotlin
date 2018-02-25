package ca.nick.basicsamplemyversion.db

import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.database.sqlite.SQLiteConstraintException
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.matcher.ViewMatchers.assertThat
import ca.nick.basicsamplemyversion.blockingObserve
import ca.nick.basicsamplemyversion.db.dao.CommentDao
import ca.nick.basicsamplemyversion.db.dao.ProductDao
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue
import kotlin.test.fail

@RunWith(AndroidJUnit4::class)
class CommentDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var commentDao: CommentDao
    private lateinit var productDao: ProductDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        commentDao = database.commentDao()
        productDao = database.productDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun getCommentsWhenNoCommentInserted() {
        val comments =
            commentDao.loadComments(TestData.COMMENT_ENTITY.productId).blockingObserve()

        assertTrue(comments.isEmpty())
    }

    @Test
    fun cantInsertCommentsWithoutProduct() {
        try {
            commentDao.insertAll(TestData.COMMENTS)
            fail("SQLiteConstraintException expected")
        } catch (ignore: SQLiteConstraintException) { }
    }

    @Test
    fun getCommentsAfterInserted() {
        productDao.insertAll(TestData.PRODUCTS)
        commentDao.insertAll(TestData.COMMENTS)

        val comments =
            commentDao.loadComments(TestData.COMMENT_ENTITY.productId).blockingObserve()

        assertThat(comments.size, `is`(1))
    }
}