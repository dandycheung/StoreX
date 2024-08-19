package com.rommansabbir.storexdemo

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.rommansabbir.storex.StoreAbleObject
import com.rommansabbir.storex.StoreX
import com.rommansabbir.storex.Subscriber
import com.rommansabbir.storex.callbacks.EventCallback
import com.rommansabbir.storex.v2.config.StoreXSmartConfig
import com.rommansabbir.storex.v2.smartstorex.SmartStoreX
import com.rommansabbir.storex.v2.strategy.StoreXCachingStrategy
import com.rommansabbir.storex.v2.subscription.StoreXSubscription
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity(), EventCallback {

    private fun askAndroid10Perm() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            121
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        askAndroid10Perm()

        // Create a config
        val testConfig = StoreXSmartConfig(
            WeakReference(this),
            "filename",
            StoreAbleObject(), //Object to be saved, must be a instance of StoreAbleObject.
            StoreXCachingStrategy.CacheDir, // Caching directory: CacheDir-FilesDir-Others
            overwriteExistingFile = true
        )

        //To store object
        val isWriteDone = SmartStoreX.getInstance.write(testConfig)
        Log.d("Write Object", isWriteDone.toString())

        // To get an stored object
        val storedObject: StoreAbleObject? =
            SmartStoreX.getInstance.read(testConfig, StoreAbleObject::class.java)
        Log.d("Written Object", "Object ID: " + (storedObject?.objectId ?: "null"))

        // To delete an object
        val isDeleted = SmartStoreX.getInstance.delete(testConfig)
        Log.d("Is Delete Done", isDeleted.toString())

        // To subscribe for callback (Write/Delete)
        SmartStoreX.registerSubscriber(
            testConfig.fileName,
            subscriber = object : StoreXSubscription {
                override fun <T : StoreAbleObject> onCallback(fileName: String, updatedObject: T) {
                    Log.d("Write Callback", updatedObject.objectId)
                }

                override fun <T : StoreAbleObject> onDelete(fileName: String, deletedObject: T) {
                    Log.d("Delete Callback", deletedObject.objectId)
                }
            })

        // To remove
        SmartStoreX.removeSubscriber(testConfig.fileName)

        // To remove all subscriber
        SmartStoreX.removeAllSubscriber()
    }

    override fun onDataChanges(subscriber: Subscriber, instance: StoreX) {

    }
}