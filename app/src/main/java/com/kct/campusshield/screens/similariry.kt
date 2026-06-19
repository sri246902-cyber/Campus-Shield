package com.kct.campusshield.security

object DomainSimilarity {

    private val trustedDomains = listOf(
        "google.com",
        "facebook.com",
        "amazon.com",
        "paypal.com",
        "instagram.com",
        "microsoft.com",
        "whatsapp.com",
        "youtube.com",
        "linkedin.com",
        "kct.ac.in"
    )

    fun isSimilarDomain(
        url: String
    ): Boolean {

        val domain =
            extractDomain(url)

        for (trusted in trustedDomains) {

            val distance =
                levenshteinDistance(
                    domain,
                    trusted
                )

            if (
                distance in 1..2 &&
                domain != trusted
            ) {

                return true

            }

        }

        return false

    }

    private fun extractDomain(
        url: String
    ): String {

        return url
            .replace("https://", "")
            .replace("http://", "")
            .substringBefore("/")
            .lowercase()

    }

    private fun levenshteinDistance(
        a: String,
        b: String
    ): Int {

        val dp =
            Array(a.length + 1) {
                IntArray(b.length + 1)
            }

        for (i in 0..a.length)
            dp[i][0] = i

        for (j in 0..b.length)
            dp[0][j] = j

        for (i in 1..a.length) {

            for (j in 1..b.length) {

                val cost =
                    if (a[i - 1] == b[j - 1])
                        0
                    else
                        1

                dp[i][j] =
                    minOf(
                        dp[i - 1][j] + 1,
                        dp[i][j - 1] + 1,
                        dp[i - 1][j - 1] + cost
                    )

            }

        }

        return dp[a.length][b.length]

    }

}