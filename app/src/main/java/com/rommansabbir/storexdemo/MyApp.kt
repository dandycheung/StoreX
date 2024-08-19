package com.rommansabbir.storexdemo

import android.app.Application
import com.rommansabbir.storex.StoreXCore
import com.rommansabbir.storex.v2.SmartStoreXInitializer

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
//        StoreXCore.init(this, getString(R.string.app_name))
        StoreXCore.init(
            this, mutableListOf(
                StoreXIdentifiers.mainConfig,
                StoreXIdentifiers.anotherConfig,
            )
        )
        SmartStoreXInitializer.init(this)
//        StoreXCore.setEncryptionKey("something_key")
    }
}