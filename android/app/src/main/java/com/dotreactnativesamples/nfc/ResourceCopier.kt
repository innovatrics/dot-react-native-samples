package com.dotreactnativesamples.nfc

import android.content.res.Resources
import java.io.File

class ResourceCopier(
    private val resources: Resources,
) {

    companion object {
        private const val BUFFER_SIZE = 1024 * 32
    }

    fun copyToFile(resourceId: Int, destinationFile: File) = resources.openRawResource(resourceId).use { inputStream ->
        destinationFile.outputStream().use { outputStream ->
            val buffer = ByteArray(BUFFER_SIZE)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
        }
    }
}
