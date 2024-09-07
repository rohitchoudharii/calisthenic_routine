package com.calisthenics.routine.application

import android.app.Application
import android.util.Log

class ErrorLogApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("ErrorLogApplication", "Uncaught exception in thread: ${thread.name}", throwable)
        }
    }
}