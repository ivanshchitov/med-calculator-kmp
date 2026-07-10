package org.dishch.medcalculator.data

import platform.Foundation.*

actual fun dataStorePath(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        NSDocumentDirectory,
        NSUserDomainMask,
        true,
        null
    )
    return requireNotNull(documentDirectory?.path) + "/" + dataStoreFileName
}
