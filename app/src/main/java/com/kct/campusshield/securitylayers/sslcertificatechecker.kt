package com.kct.campusshield.security

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object SSLChecker {

    suspend fun isValidSSL(
        url: String
    ): Boolean {

        return withContext(
            Dispatchers.IO
        ) {

            try {

                val connection =
                    URL(url).openConnection()
                            as HttpsURLConnection

                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                connection.connect()

                Log.d(
                    "SSL_TEST",
                    "SSL Valid: $url"
                )

                true

            } catch (e: Exception) {

                Log.e(
                    "SSL_TEST",
                    "SSL Error",
                    e
                )

                false

            }

        }

    }

}