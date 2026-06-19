package com.kct.campusshield.dns

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress

object DNSAnalyzer {

    suspend fun resolve(
        domain: String
    ): Pair<Boolean, String> {

        return withContext(Dispatchers.IO) {

            try {

                Log.d(
                    "DNS_TEST",
                    "Resolving $domain"
                )

                val address =
                    InetAddress.getByName(domain)

                Log.d(
                    "DNS_TEST",
                    "IP = ${address.hostAddress}"
                )

                Pair(
                    true,
                    address.hostAddress ?: ""
                )

            } catch (e: Exception) {

                Log.e(
                    "DNS_EXCEPTION",
                    e.toString()
                )

                Pair(
                    false,
                    ""
                )
            }
        }
    }
}




