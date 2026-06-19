package com.kct.campusshield.engine

import java.net.URL

object SecurityEngine {

    fun analyze(url: String): SecurityResult {

        val u = url.lowercase()
        var score = 0
        val reasons = mutableListOf<String>()

        // 🔥 HTTPS CHECK
        if (!u.startsWith("https")) {
            score += 25
            reasons.add("No HTTPS encryption")
        }

        // 🔥 PHISHING KEYWORDS
        val phishingKeywords = listOf(
            "login", "verify", "bank", "update", "password", "secure"
        )

        phishingKeywords.forEach {
            if (u.contains(it)) {
                score += 20
                reasons.add("Suspicious keyword: $it")
            }
        }

        // 🔥 SHORTENED URL DETECTION
        val shortUrls = listOf("bit.ly", "tinyurl", "t.co", "cutt.ly")

        shortUrls.forEach {
            if (u.contains(it)) {
                score += 25
                reasons.add("Shortened URL detected")
            }
        }

        // 🔥 SUSPICIOUS DOMAIN
        if (u.contains(".xyz") || u.contains(".top") || u.contains(".click")) {
            score += 20
            reasons.add("Suspicious domain extension")
        }

        // 🔥 LENGTH CHECK
        if (u.length > 80) {
            score += 10
            reasons.add("Unusually long URL")
        }

        val status = when {
            score <= 30 -> "SAFE"
            score <= 70 -> "SUSPICIOUS"
            else -> "PHISHING"
        }

        return SecurityResult(score, status, reasons)
    }
}

// 🔥 RESULT MODEL
data class SecurityResult(
    val score: Int,
    val status: String,
    val reasons: List<String>
)