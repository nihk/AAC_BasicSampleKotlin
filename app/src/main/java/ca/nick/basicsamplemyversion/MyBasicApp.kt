package ca.nick.basicsamplemyversion

import android.app.Application
import ca.nick.basicsamplemyversion.db.AppDatabase

class MyBasicApp : Application() {

    private val appExecutors: AppExecutors = AppExecutors()

    fun getDatabase() = AppDatabase.getInstance(this, appExecutors)

    fun getRepository() = DataRepository.getInstance(getDatabase())
}