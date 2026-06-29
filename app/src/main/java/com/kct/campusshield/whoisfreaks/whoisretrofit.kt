package com.kct.campusshield.whois

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // CHANGE THIS TO YOUR LAPTOP'S IP ADDRESS
    private const val BASE_URL =
        "https://campus-shield-backend-dexh.onrender.com/"

    val api: WhoisApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(WhoisApi::class.java)

    }
}