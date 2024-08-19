package com.rommansabbir.storex.v2.extensions

import android.content.Context
import com.rommansabbir.storex.StoreAbleObject
import com.rommansabbir.storex.v2.SmartStoreXInitializer
import com.rommansabbir.storex.v2.config.StoreXSmartConfig
import com.rommansabbir.storex.v2.strategy.StoreXCachingStrategy


inline fun <T : StoreAbleObject> StoreXSmartConfig<T>.getCacheDir(crossinline onPath: (String) -> Unit = {}): String {
    val context = getContext()
    val path = context?.applicationContext?.cacheDir?.absolutePath ?: ""
    onPath.invoke(path)
    return path
}

fun <T : StoreAbleObject> StoreXSmartConfig<T>.getContext(): Context? {
    val context = this.context?.get() ?: SmartStoreXInitializer.getApplicationContext()
    if (context == null) {
        throwException("Context can't be null")
    }
    return context
}

inline fun <T : StoreAbleObject> StoreXSmartConfig<T>.getFilesDir(crossinline onPath: (String) -> Unit = {}): String {
    val context = getContext()
    val path = context?.applicationContext?.filesDir?.absolutePath ?: ""
    onPath.invoke(path)
    return path
}

inline fun <T : StoreAbleObject> StoreXSmartConfig<T>.getOtherDir(crossinline onPath: (String) -> Unit = {}): String {
    getContext()
    val path =
        (this.storeXCachingStrategy as StoreXCachingStrategy.OtherDir).externalStorageFile.absolutePath
            ?: ""
    onPath.invoke(path)
    return path
}

fun throwException(message: String) {
    throw Exception(message)
}