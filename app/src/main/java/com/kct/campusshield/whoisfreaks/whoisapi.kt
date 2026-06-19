package com.kct.campusshield.whois

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WhoisApi {

    @GET("v2.0/whois/live")
    suspend fun getWhois(

        @Query("apiKey")
        apiKey: String,

        @Query("domainName")
        domainName: String,

        @Query("format")
        format: String = "json"

    ): Response<WhoisResponse>
}