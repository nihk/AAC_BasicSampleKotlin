package ca.nick.basicsamplemyversion.db

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import android.os.SystemClock
import ca.nick.basicsamplemyversion.AppExecutors
import ca.nick.basicsamplemyversion.db.converters.DateConverter
import ca.nick.basicsamplemyversion.db.dao.CommentDao
import ca.nick.basicsamplemyversion.db.dao.ProductDao
import ca.nick.basicsamplemyversion.db.entities.CommentEntity
import ca.nick.basicsamplemyversion.db.entities.ProductEntity

@Database(entities = [ProductEntity::class, CommentEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun commentDao(): CommentDao

    abstract fun productDao(): ProductDao

    val isDatabaseCreated: MutableLiveData<Boolean> = MutableLiveData()

    companion object {
        const val DATABASE_NAME = "sample"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, appExecutors: AppExecutors): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(
                    context.applicationContext,
                    appExecutors
                ).also {
                    INSTANCE = it
                    it.updateDatabaseCreated(context.applicationContext)
                }
            }

        private fun buildDatabase(
            applicationContext: Context,
            appExecutors: AppExecutors
        ) =
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, DATABASE_NAME
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    appExecutors.diskIo.execute {
                        fakeDelay(4)

                        // Generate data
                        val database = getInstance(applicationContext, appExecutors)
                        val products = DataGenerator.generateProducts()
                        val comments = DataGenerator.generateCommentsForProducts(products)
                        insertGeneratedData(database, products, comments)

                        database.setDatabaseCreated()
                    }
                }
            }).build()

        private fun insertGeneratedData(
            appDatabase: AppDatabase,
            products: List<ProductEntity>,
            comments: List<CommentEntity>
        ) =
            with(appDatabase) {
                runInTransaction {
                    productDao().insertAll(products)
                    commentDao().insertAll(comments)
                }
            }

        private fun fakeDelay(seconds: Long) {
            SystemClock.sleep(seconds * 1000)
        }
    }

    private fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated()
        }
    }

    private fun setDatabaseCreated() {
        isDatabaseCreated.postValue(true)
    }
}