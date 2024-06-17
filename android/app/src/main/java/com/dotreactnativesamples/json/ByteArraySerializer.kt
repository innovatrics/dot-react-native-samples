package com.dotreactnativesamples.json

import android.util.Base64
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ByteArraySerializer : JsonSerializer<ByteArray> {

    companion object {
        private const val MAX_BYTE_ARRAY_SIZE_FOR_DIGEST = 20
    }

    override fun serialize(src: ByteArray?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val value = when {
            src == null -> "null"
            src.size <= MAX_BYTE_ARRAY_SIZE_FOR_DIGEST -> formatSize(src) + " " + formatContentSuffix(src)
            else -> formatSize(src)
        }

        return JsonPrimitive(value)
    }

    private fun formatSize(bytes: ByteArray) = "${bytes.size} bytes"

    private fun formatContentSuffix(bytes: ByteArray) = "[" + Base64.encodeToString(bytes, Base64.NO_WRAP).takeLast(7) + "]"
}
