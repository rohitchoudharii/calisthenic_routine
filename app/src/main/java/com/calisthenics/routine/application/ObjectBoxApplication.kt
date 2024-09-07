package com.calisthenics.routine.application

import android.app.Application
import com.calisthenics.routine.models.MyObjectBox
import io.objectbox.BoxStore

class ObjectBoxApplication : Application() {

    companion object {
        private var boxStore: BoxStore? = null

        // Singleton method to get the BoxStore instance
        fun getBoxStore(): BoxStore {
            return boxStore ?: throw IllegalStateException("BoxStore is not initialized")
        }
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize the BoxStore instance
        if (boxStore == null) {
            boxStore = MyObjectBox.builder()
                .androidContext(this)
                .build()
        }
    }
}