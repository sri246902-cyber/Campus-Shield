package com.kct.campusshield.security

import java.net.URI

object KctVerifier {

    private val verifiedDomains = listOf(

        "kct.ac.in",
        "portal.kct.ac.in",
        "erp.kct.ac.in",
        "mail.kct.ac.in",

        "yugam.in",

        "placements.kct.ac.in",
        "library.kct.ac.in",

        "kumaraguru.in"
    )

    fun verify(
        url: String
    ): KctVerificationResult {

        return try {

            val host =
                URI(url)
                    .host
                    ?.lowercase()
                    ?.removePrefix("www.")
                    ?: ""

            when {

                verifiedDomains.any {
                    host == it.removePrefix("www.")
                } -> {

                    KctVerificationResult(
                        verified = true,
                        message = "KCT Verified Link"
                    )

                }

                host.contains("kct") ||
                        host.contains("kumaraguru") ||
                        host.contains("yugam") -> {

                    KctVerificationResult(
                        verified = false,
                        message = "Possible KCT Impersonation"
                    )

                }

                else -> {

                    KctVerificationResult(
                        verified = false,
                        message = "External Domain"
                    )

                }

            }

        } catch (e: Exception) {

            KctVerificationResult(
                verified = false,
                message = "Unknown Domain"
            )

        }

    }

}