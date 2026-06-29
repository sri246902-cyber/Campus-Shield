package com.kct.campusshield.threat

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ThreatApi {

    @POST("safebrowsing")
    suspend fun checkThreat(

        @Body
        request: ThreatRequest

    ): Response<ThreatResponse>

}