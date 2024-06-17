package com.dotreactnativesamples.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder

fun createGson(): Gson {
    return GsonBuilder()
        .registerTypeAdapter(ByteArray::class.java, ByteArraySerializer())
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create()
}
