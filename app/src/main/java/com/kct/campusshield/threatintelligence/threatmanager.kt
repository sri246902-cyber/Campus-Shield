package com.kct.campusshield.threat

import android.util.Log
import com.kct.campusshield.BuildConfig

object ThreatManager {


    val apiKey = BuildConfig.THREAT_API_KEY


    suspend fun checkUrl(
        url: String
    ): Boolean {

        return try {

            val request = ThreatRequest(

                client = Client(
                    clientId = "CampusShield",
                    clientVersion = "1.0"
                ),

                threatInfo = ThreatInfo(

                    threatTypes = listOf(
                        "MALWARE",
                        "SOCIAL_ENGINEERING",
                        "UNWANTED_SOFTWARE"
                    ),

                    platformTypes = listOf(
                        "ANY_PLATFORM"
                    ),

                    threatEntryTypes = listOf(
                        "URL"
                    ),

                    threatEntries = listOf(
                        ThreatEntry(url)
                    )
                )
            )

            val response =
                ThreatRetrofit.api.checkThreat(
                    BuildConfig.THREAT_API_KEY,
                    request
                )

            Log.d(
                "THREAT_TEST",
                "HTTP Code = ${response.code()}"
            )

            Log.d(
                "THREAT_TEST",
                "Body = ${response.body()}"
            )

            response.body()?.matches?.isNotEmpty() == true

        } catch (e: Exception) {

            Log.e(
                "THREAT_TEST",
                "Threat Error",
                e
            )

            false
        }
    }
}