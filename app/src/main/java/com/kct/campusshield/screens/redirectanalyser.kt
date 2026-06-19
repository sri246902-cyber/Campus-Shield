package com.kct.campusshield.redirect

import java.net.HttpURLConnection
import java.net.URL

object RedirectAnalyzer {

    fun analyze(
        url: String
    ): Int {

        var currentUrl = url

        var count = 0

        try {

            while (count < 10) {

                val connection =
                    URL(currentUrl)
                        .openConnection()
                            as HttpURLConnection

                connection.instanceFollowRedirects =
                    false

                connection.connectTimeout = 5000

                connection.readTimeout = 5000

                val responseCode =
                    connection.responseCode

                if (

                    responseCode == HttpURLConnection.HTTP_MOVED_PERM ||

                    responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||

                    responseCode == HttpURLConnection.HTTP_SEE_OTHER

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

                    currentUrl = location

                    count++

                } else {

                    break

                }

            }

        } catch (e: Exception) {

            e.printStackTrace()

        }

        return count

    }

}