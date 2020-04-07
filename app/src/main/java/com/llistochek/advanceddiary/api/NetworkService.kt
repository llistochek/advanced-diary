package com.llistochek.advanceddiary.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkService {
    private val retrofit: Retrofit
    init {
        val client = OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://shkola.nso.ru/rest/")
            .client(client)
            .build()
    }
    val schoolApi: ESchoolApi = retrofit.create(ESchoolApi::class.java)
}