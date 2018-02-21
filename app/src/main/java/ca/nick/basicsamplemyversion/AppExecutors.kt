package ca.nick.basicsamplemyversion

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors {

    val diskIo: Executor
    val networkIo: Executor
    val mainThread: Executor

    init {
        diskIo = Executors.newSingleThreadExecutor()
        networkIo = Executors.newFixedThreadPool(3)
        mainThread = MainThreadExecutor()
    }

    private class MainThreadExecutor : Executor {

        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}
