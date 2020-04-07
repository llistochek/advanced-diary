package com.llistochek.advanceddiary.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ESchoolApi {
    @GET("login")
    fun login(
        @Query("login") login: String,
        @Query("password") password: String,
        @Query("deviceTokenAndroid") token: String,
        @Query("appPackageName") packageName: String
    ): Call<LoginResponse>
}