package com.blankj_custom.base_transform.util

import com.google.gson.GsonBuilder


object JsonUtils {

    private val GSON = GsonBuilder().setPrettyPrinting().create()

    fun getFormatJson(obj: Any): String {
        return GSON.toJson(obj)
    }
}