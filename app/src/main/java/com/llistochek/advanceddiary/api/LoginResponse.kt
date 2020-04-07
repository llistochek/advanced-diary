package com.llistochek.advanceddiary.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @Expose
    @SerializedName("success")
    val success: Boolean,
    @Expose
    @SerializedName("message")
    val message: String?,
    // Лол, в названии значения ошибка, child в множественном числе - children
    // Хотя может хотели сэкономить трафик и уменьшить размер JSON'a на 2 байта ¯\_(ツ)_/¯
    @Expose
    @SerializedName("childs")
    val children: Array<Array<String>>?,
    @Expose
    @SerializedName("id")
    val id: Int?
)