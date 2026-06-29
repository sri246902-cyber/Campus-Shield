package com.kct.campusshield.redirect

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

object RedirectAnalyzer {

    fun analyze(url: String): Int {

        var currentUrl = url
        var count = 0

        try {

            while (count < 10) {

                val connection =
                    URL(currentUrl)
                        .openConnection() as HttpURLConnection

                connection.instanceFollowRedirects = false

                connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0"
                )

                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val responseCode =
                    connection.responseCode

                Log.d(
                    "CampusShield",
                    "URL=$currentUrl CODE=$responseCode"
                )

                if (
                    responseCode == 301 ||
                    responseCode == 302 ||
                    responseCode == 303 ||
                    responseCode == 307 ||
                    responseCode == 308
                ) {

                    val location =
                        connection.getHeaderField(
                            "Location"
                        )

                    if (
                        location.isNullOrEmpty()
                    ) {
                        break
                    }

                    currentUrl =
                        if (
                            location.startsWith("http")
                        ) {
                            location
                        } else {
                            URL(
                                URL(currentUrl),
                                location
                            ).toString()
                        }

                    Log.d(
                        "CampusShield",
                        "Redirect Found -> $currentUrl"
                    )

                    count++

                } else {

                    break

                }
            }

        } catch (e: Exception) {

            Log.e(
                "CampusShield",
                "Redirect Error",
                e
            )

        }

        Log.d(
            "CampusShield",
            "Total Redirects = $count"
        )

        return count
    }

}