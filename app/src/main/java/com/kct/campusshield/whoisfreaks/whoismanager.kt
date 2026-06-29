package com.kct.campusshield.whois

import android.net.Uri
import android.util.Log

object WhoisManager {

    suspend fun getWhoisStatus(
        url: String
    ): WhoisResponse? {

        return try {

            val host =
                Uri.parse(url).host ?: return null

            val response =
                RetrofitClient.api.getWhois(
                    domain = host
                )

            Log.d("WHOIS_HOST", host)
            Log.d("WHOIS_CODE", response.code().toString())
            Log.d("WHOIS_BODY", response.body().toString())
            Log.d("WHOIS_ERROR", response.errorBody()?.string() ?: "None")

            if (response.isSuccessful) {

                response.body()

            } else {

                null

            }

        } catch (e: Exception) {

            Log.e(
                "WHOIS_EXCEPTION",
                e.message ?: ""
            )

            null

        }

    }

}