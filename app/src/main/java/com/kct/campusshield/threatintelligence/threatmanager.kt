package com.kct.campusshield.threat

import android.util.Log

object ThreatManager {

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
                    request
                )

            Log.d("THREAT_TEST", "HTTP Code = ${response.code()}")
            Log.d("THREAT_TEST", "Response = ${response.body()}")
            Log.d("THREAT_TEST", "Error = ${response.errorBody()?.string()}")
            Log.d("THREAT_DEBUG", "Calling backend...")
            Log.d("THREAT_DEBUG", "Response code = ${response.code()}")

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