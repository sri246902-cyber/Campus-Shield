package com.kct.campusshield.whois

import android.net.Uri
import android.util.Log
import com.kct.campusshield.BuildConfig

object WhoisManager {

    val apiKey = BuildConfig.WHOIS_API_KEY

    suspend fun getWhoisStatus(
        url: String
    ): WhoisResponse? {

        return try {

            val host =
                Uri.parse(url).host ?: return null
            Log.d("WHOIS_KEY", BuildConfig.WHOIS_API_KEY)

            val response =
                RetrofitClient.api.getWhois(
                    apiKey = BuildConfig.WHOIS_API_KEY,
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