package com.kct.campusshield.whois

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WhoisApi {

    @GET("whois")
    suspend fun getWhois(

        @Query("domain")
        domain: String

    ): Response<WhoisResponse>

}