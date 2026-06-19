package com.kct.campusshield.whois

import android.net.Uri
import android.util.Log

object WhoisManager {

    private const val API_KEY =
        "d3e46839b8c041ae84bd333919b609fa"

    suspend fun getWhoisStatus(
        url: String
    ): WhoisResponse? {

        return try {

            val host =
                Uri.parse(url).host ?: return null

            val response =
                RetrofitClient.api.getWhois(
                    apiKey = API_KEY,
                    domainName = host
                )

            Log.d("WHOIS_HOST", host)
            Log.d("WHOIS_CODE", response.code().toString())
            Log.d("WHOIS_MESSAGE", response.message())
            Log.d("WHOIS_BODY", response.body().toString())
            Log.d(
                "WHOIS_ERROR",
                response.errorBody()?.string() ?: "none"
            )

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }

        } catch (e: Exception) {

            Log.e(
                "WHOIS_EXCEPTION",
                e.message ?: "Unknown Error"
            )

            null
        }
    }
}