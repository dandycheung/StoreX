![StoreX](https://github.com/rommansabbir/StoreX/blob/master/art/storex_logo.png)

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)](#getting-started-quick)

<p align="center">
    <a href="https://android-arsenal.com/details/1/8362"><img alt="Maintained" src="https://img.shields.io/badge/Android%20Arsenal-StoreX-green.svg?style=flat" height="20"/></a>
    <a href="https://github.com/rommansabbir/StoreX"><img alt="Maintained" src="https://img.shields.io/badge/Maintained_Actively%3F-Yes-green.svg" height="20"/></a>
    <a href="https://jitpack.io/#rommansabbir/StoreX"><img alt="JitPack" src="https://img.shields.io/badge/JitPack-Yes-green.svg?style=flat" height="20"/></a>
</p>

<h1 align="center"> ⚡ Latest Version: 3.1.0 | Changelog 🔰</h1>

- Significant changes compared to `v2.1.0`.
- Encryption is no longer bundled with the library; it's now the developer's responsibility to implement it.
- `SmartStoreX` is now entirely storage-based (CacheDir/FilesDir/Others), with no reliance on `SharedPref`.
- Introduced `StoreXSmartConfig` for enhanced configuration with `SmartStoreX`.
- Added an alternative initializer for `StoreXSmartConfig`.

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)](#getting-started-quick)

<h1 align="center">Installation</h1>

## Step 1: Add the JitPack repository to your build file

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

## Step 2: Add the dependency

```gradle
dependencies {
    implementation 'com.github.rommansabbir:StoreX:3.1.0'
}
```

## Step 3: Access `SmartStoreX`

### Initialize `SmartStoreXInitializer`:

```kotlin
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SmartStoreXInitializer.init(this)
        // other initialization code
    }
}
```

### Create an instance of `StoreXSmartConfig`:

```kotlin
val testConfig = StoreXSmartConfig(
    context = WeakReference(this) ?: null, // Pass null if SmartStoreXInitializer.init() is called
    fileName = "filename",
    storeAbleObject = StoreAbleObject(), // Object to be saved, must extend StoreAbleObject.
    cachingStrategy = StoreXCachingStrategy.CacheDir, // Options: CacheDir, FilesDir, Others
    overwriteExistingFile = true
)
```

### Write an object:

```kotlin
val isWriteSuccessful = SmartStoreX.getInstance().write(testConfig)
Log.d("Write Object", isWriteSuccessful.toString())
```

### Retrieve a stored object:

```kotlin
val storedObject: StoreAbleObject? = 
    SmartStoreX.getInstance().read(testConfig, StoreAbleObject::class.java)
Log.d("Retrieved Object", "Object ID: " + (storedObject?.objectId ?: "null"))
```

### Delete a stored object:

```kotlin
val isDeleted = SmartStoreX.getInstance().delete(testConfig)
Log.d("Is Delete Successful", isDeleted.toString())
```

### Manage Subscriptions (`Write/Delete`):

#### Register a subscriber:

```kotlin
SmartStoreX.registerSubscriber(
    fileName = testConfig.fileName,
    subscriber = object : StoreXSubscription {
        override fun <T : StoreAbleObject> onCallback(fileName: String, updatedObject: T) {
            Log.d("Write Callback", updatedObject.objectId)
        }

        override fun <T : StoreAbleObject> onDelete(fileName: String, deletedObject: T) {
            Log.d("Delete Callback", deletedObject.objectId)
        }
    }
)
```

#### Remove a subscriber:

```kotlin
SmartStoreX.removeSubscriber(testConfig.fileName)
```

#### Remove all subscribers:

```kotlin
SmartStoreX.removeAllSubscribers()
```

### Supported `StoreAbleObject` Types:

- `StoreAbleString("Value")`
- `StoreAbleInt(1)`
- `StoreAbleDouble(1.0)`
- `StoreAbleLong(1234567890879)`
- `StoreAbleByte(Byte)`
- `StoreAbleChar(Char)`
- `StoreAbleShort(Short)`
- `StoreAbleArrayList(ArrayList)`
- `StoreAbleMutableList(MutableList)`
- `StoreAbleSet(Set)`
- `StoreAbleMutableSet(MutableSet)`
- `StoreAbleHashMap(HashMap)`
- `StoreAbleHashSet(HashSet)`

For documentation on older versions, please refer to the [previous README](https://github.com/rommansabbir/StoreX/blob/master/README_OLD.md).

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)](#getting-started-quick)

## Contact

✔ [LinkedIn](https://www.linkedin.com/in/rommansabbir/)

✔ [Website](https://rommansabbir.com)

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)](#getting-started-quick)

### License

[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```html
Copyright (C) 2023 Romman Sabbir

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```