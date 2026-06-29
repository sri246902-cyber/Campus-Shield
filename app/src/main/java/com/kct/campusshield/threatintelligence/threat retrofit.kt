package com.kct.campusshield.threat

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ThreatRetrofit {

    private const val BASE_URL =
        "https://campus-shield-backend-dexh.onrender.com"

    val api: ThreatApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ThreatApi::class.java)

    }

}