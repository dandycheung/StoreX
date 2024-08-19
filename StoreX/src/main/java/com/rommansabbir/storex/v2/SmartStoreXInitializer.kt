package com.rommansabbir.storex.v2

import android.app.Application

object SmartStoreXInitializer {
    private var _applicationContext: Application? = null
    fun init(application: Application) {
        synchronized(Any()) {
            _applicationContext = application
        }
    }

    fun getApplicationContext(): Application? {
        return _applicationContext
    }
}