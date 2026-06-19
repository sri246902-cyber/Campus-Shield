package com.kct.campusshield.threat

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ThreatApi {

    @POST("v4/threatMatches:find")
    suspend fun checkThreat(

        @Query("key")
        apiKey: String,

        @Body
        request: ThreatRequest

    ): Response<ThreatResponse>

}