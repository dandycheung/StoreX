@file:Suppress("UNCHECKED_CAST")

package com.rommansabbir.storex

import android.app.Application
import android.content.SharedPreferences
import com.google.gson.Gson
import com.rommansabbir.storex.callbacks.GetCallback
import com.rommansabbir.storex.callbacks.SaveCallback
import com.rommansabbir.storex.execptions.DuplicateKeyFoundException
import com.rommansabbir.storex.execptions.NoStoreAbleObjectFound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


internal class StoreXInstance(
    private val application: Application,
    private val prefRef: String,
    private val serializer: Gson,
    private val writeOrGetAsFileUsingCacheDirectory: Boolean
) : BaseStoreXInstance(application, prefRef), StoreX {
    internal var listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key != null) {
                notifyClients(key, this)
            }
        }

    override fun put(key: String, value: StoreAbleObject): Boolean {
        val serializedValue: String = serializer.toJson(value)
        val valueToBeSaved =
            if (StoreXCore.encryptionKey == StoreXCore.NO_ENCRYPTION) serializedValue else EncryptionTool.encrypt(
                serializedValue
            )
        doCache(key, valueToBeSaved, writeOrGetAsFileUsingCacheDirectory)
        notifyClientsManuallyIfNeeded(key)
        return true
    }

    override fun put(
        scope: CoroutineScope,
        key: String,
        value: StoreAbleObject
    ) {
        scope.launch {
            try {
                val serializedValue: String = serializer.toJson(value)
                if (StoreXCore.encryptionKey == StoreXCore.NO_ENCRYPTION) {
                    doCache(key, serializedValue, writeOrGetAsFileUsingCacheDirectory)
                    notifyClientsManuallyIfNeeded(key)
                } else {
                    val encryptedValue = EncryptionTool.encrypt(serializedValue)
                    doCache(key, encryptedValue, writeOrGetAsFileUsingCacheDirectory)
                    notifyClientsManuallyIfNeeded(key)
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }


    override suspend fun putS(key: String, value: StoreAbleObject): Boolean =
        withContext(Dispatchers.IO) {
            val serializedValue: String = serializer.toJson(value)
            val valueToBeSaved =
                if (StoreXCore.encryptionKey == StoreXCore.NO_ENCRYPTION) serializedValue else EncryptionTool.encrypt(
                    serializedValue
                )
            doCache(key, valueToBeSaved, writeOrGetAsFileUsingCacheDirectory)
            notifyClientsManuallyIfNeeded(key)
            return@withContext true
        }

    override fun <T : StoreAbleObject> put(
        key: String,
        value: StoreAbleObject,
        callback: SaveCallback<T>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val serializedValue: String = serializer.toJson(value)
                if (StoreXCore.encryptionKey == StoreXCore.NO_ENCRYPTION) {
                    doCache(key, serializedValue, writeOrGetAsFileUsingCacheDirectory)
                    notifyClientsManuallyIfNeeded(key)
                    withContext(Dispatchers.Main) {
                        callback.onDone(value as T, null)
                    }
                } else {
                    val encryptedValue = EncryptionTool.encrypt(serializedValue)
                    doCache(key, encryptedValue, writeOrGetAsFileUsingCacheDirectory)
                    notifyClientsManuallyIfNeeded(key)
                    withContext(Dispatchers.Main) {
                        callback.onDone(value as T, null)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onDone(value as T, e)
                }
            }
        }
    }

    override fun <T : StoreAbleObject> put(
        scope: CoroutineScope,
        key: String,
        value: StoreAbleObject,
        callback: SaveCallback<T>
    ) {
        scope.launch {
            try {
                val serializedValue: String = serializer.toJson(value)
                if (StoreXCore.encryptionKey == StoreXCore.NO_ENCRYPTION) {
                    doCache(key, serializedValue, writeOrGetAsFileUsingCacheDirectory)
                    notifyClientsManuallyIfNeeded(key)
                    withContext(Dispatchers.Main) {
                        callback.onDone(value as T, null)
                    }
                } else {
                    val encryptedValue = EncryptionTool.encrypt(serializedValue)
                    doCache(key, encryptedValue, writeOrGetAsFileUsingCacheDirectory)
                    notifyClientsManuallyIfNeeded(key)
                    withContext(Dispatchers.Main) {
                        callback.onDone(value as T, null)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onDone(value as T, e)
                }
            }
        }
    }

    private fun notifyClientsManuallyIfNeeded(key: String) {
        if (writeOrGetAsFileUsingCacheDirectory) {
            notifyClients(key, this)
        }
    }


    override fun <T : StoreAbleObject> get(
        key: String,
        objectType: Class<T>
    ): T {
        try {
            when (val value = getCache(key, writeOrGetAsFileUsingCacheDirectory)) {
                null -> {
                    throw NoStoreAbleObjectFound()
                }
                else -> {
                    return when (StoreXCore.encryptionKey == StoreXCore.NO_ENCRYPTION) {
                        true -> {
                            serializer.fromJson(value, objectType)
                        }
                        else -> {
                            val decryptedValue = EncryptionTool.decrypt(value)
                            serializer.fromJson(decryptedValue, objectType)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override fun <T : StoreAbleObject> get(
        key: String,
        objectType: Class<T>,
        callback: GetCallback<T>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                when (val value = getCache(key, writeOrGetAsFileUsingCacheDirectory)) {
                    null -> {
                        withContext(Dispatchers.Main) {
                            callback.onSuccess(null, NoStoreAbleObjectFound())
                        }
                    }
                    else -> {
                        when (StoreXCore.encryptionKey == StoreXCore.NO_ENCRYPTION) {
                            true -> {
                                withContext(Dispatchers.Main) {
                                    callback.onSuccess(serializer.fromJson(value, objectType))
                                }
                            }
                            else -> {
                                val decryptedValue = EncryptionTool.decrypt(value)
                                withContext(Dispatchers.Main) {
                                    callback.onSuccess(
                                        serializer.fromJson(
                                            decryptedValue,
                                            objectType
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onSuccess(null, e)
                }
            }
        }
    }

    override fun <T : StoreAbleObject> get(
        scope: CoroutineScope,
        key: String,
        objectType: Class<T>,
        callback: GetCallback<T>
    ) {
        scope.launch {
            try {
                when (val value = getCache(key, writeOrGetAsFileUsingCacheDirectory)) {
                    null -> {
                        withContext(Dispatchers.Main) {
                            callback.onSuccess(null, NoStoreAbleObjectFound())
                        }
                    }
                    else -> {
                        when (StoreXCore.encryptionKey == StoreXCore.NO_ENCRYPTION) {
                            true -> {
                                withContext(Dispatchers.Main) {
                                    callback.onSuccess(serializer.fromJson(value, objectType))
                                }
                            }
                            else -> {
                                val decryptedValue = EncryptionTool.decrypt(value)
                                withContext(Dispatchers.Main) {
                                    callback.onSuccess(
                                        serializer.fromJson(
                                            decryptedValue,
                                            objectType
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onSuccess(null, e)
                }
            }
        }
    }

    override fun addSubscriber(subscriber: Subscriber) {
        if (StoreXCore.subscriberList().containsKey(subscriber.getKey())) {
            throw DuplicateKeyFoundException()
        }
        StoreXCore.addSubscriber(subscriber)
    }

    override fun addSubscriber(subscribers: List<Subscriber>) {
        subscribers.forEach {
            addSubscriber(it)
        }
    }

    override fun removeSubscriber(subscriber: Subscriber) {
        StoreXCore.removeSubscriber(subscriber)
    }

    override fun removeSubscriber(subscribers: List<Subscriber>) {
        subscribers.forEach {
            removeSubscriber(it)
        }
    }

    override fun remove(key: String) {
        if (writeOrGetAsFileUsingCacheDirectory) {
            clearCacheFromCacheDir(key)
        } else {
            clearCacheByKey(key)
        }
    }

    override fun removeFromCacheDir(key: List<String>): Boolean {
        key.forEach { clearCacheFromCacheDir(it) }
        return true
    }

    override fun removeAll() {
        clearAllCache()
    }

}