package com.kct.campusshield.security
object UrlAnalyzer {

    fun analyze(url: String): RiskResult {

        val lowerUrl =
            url.lowercase()
                .replace("\\", "/")
                .trim()

        var score = 0
        val reasons =
            mutableListOf<String>()

        // Fake Domains

        val fakeDomains = listOf(
            "g00gle",
            "g00le",
            "go0gle",
            "g0ogle",
            "paypa1",
            "amaz0n",
            "arnazon",
            "faceb00k",
            "micr0soft",
            "instagrarn",
            "whatsaap",
            "appLe"
        )

        fakeDomains.forEach {

            if (lowerUrl.contains(it)) {

                score += 60

                reasons.add(
                    "Fake domain detected"
                )

            }

        }

        // HTTPS Check

        if (!lowerUrl.startsWith("https://")) {

            score += 20

            reasons.add(
                "Connection is not HTTPS"
            )

        }

        // URL Shorteners

        val shorteners = listOf(
            "bit.ly",
            "tinyurl.com",
            "goo.gl",
            "ow.ly",
            "t.co",
            "is.gd"
        )

        if (
            shorteners.any {
                lowerUrl.contains(it)
            }
        ) {

            score += 25

            reasons.add(
                "URL Shortener detected"
            )

        }

        // IP Address URL

        val ipRegex =
            Regex(
                """https?://\d{1,3}(\.\d{1,3}){3}.*"""
            )

        if (
            ipRegex.matches(lowerUrl)
        ) {

            score += 50

            reasons.add(
                "IP Address URL detected"
            )

        }

        // Suspicious Keywords

        val suspiciousWords = listOf(
            "login",
            "verify",
            "password",
            "bank",
            "secure",
            "update",
            "otp",
            "reward",
            "bonus",
            "gift",
            "claim",
            "signin",
            "account",
            "wallet",
            "credential",
            "authenticate",
            "recovery",
            "security",
            "billing",
            "payment"
        )

        suspiciousWords.forEach {

            if (lowerUrl.contains(it)) {

                score += 10

                reasons.add(
                    "Suspicious keyword: $it"
                )

            }

        }

        // Long URL

        if (
            lowerUrl.length > 100
        ) {

            score += 10

            reasons.add(
                "Very long URL"
            )

        }

        // Too Many Subdomains

        val dotCount =
            lowerUrl.count {
                it == '.'
            }

        if (dotCount > 4) {

            score += 20

            reasons.add(
                "Too many subdomains"
            )

        }

        // Suspicious TLDs

        val suspiciousTlds = listOf(
            ".xyz",
            ".top",
            ".click",
            ".gq",
            ".cf",
            ".tk",
            ".work",
            ".support"
        )

        if (
            suspiciousTlds.any {
                lowerUrl.contains(it)
            }
        ) {

            score += 25

            reasons.add(
                "Suspicious domain extension"
            )

        }

        // Brand Impersonation

        val brands = listOf(
            "paypal",
            "google",
            "amazon",
            "facebook",
            "instagram",
            "microsoft",
            "apple",
            "netflix",
            "whatsapp",
            "leboncoin"
        )

        brands.forEach { brand ->

            if (
                lowerUrl.contains("$brand.") &&
                !lowerUrl.contains("$brand.com") &&
                !lowerUrl.contains("$brand.fr")
            ) {

                score += 40

                reasons.add(
                    "Brand name used in subdomain"
                )
            }
        }

        // Multiple @ Symbols

        if (
            lowerUrl.contains("@")
        ) {

            score += 20

            reasons.add(
                "Contains @ symbol"
            )

        }

        // Multiple Hyphens

        if (
            lowerUrl.count {
                it == '-'
            } > 3
        ) {

            score += 15

            reasons.add(
                "Excessive hyphens in URL"
            )

        }
        if (url.contains("social_engineering", true)) {
            score += 50
            reasons.add("Social Engineering keyword detected")
        }

        if (url.contains("phishing", true)) {
            score += 50
            reasons.add("Phishing keyword detected")
        }

        if (url.contains("malware", true)) {
            score += 50
            reasons.add("Malware keyword detected")
        }

        val status = when {

            score >= 70 ->
                "PHISHING"

            score >= 35 ->
                "SUSPICIOUS"

            else ->
                "SAFE"

        }

        return RiskResult(
            status = status,
            score = score.coerceAtMost(100),
            reasons = reasons
        )

    }

}