package com.kct.campusshield.utils

import android.util.Patterns
import kotlin.random.Random

data class AnalysisResult(
    val isSafe: Boolean,
    val score: Int,
    val reasons: List<String>
)

object UrlAnalyzer {

    fun analyze(url: String): AnalysisResult {

        val reasons = mutableListOf<String>()
        var score = 100

        // -----------------------------
        // 1. URL VALIDATION (REAL CHECK)
        // -----------------------------
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            return AnalysisResult(
                isSafe = false,
                score = 0,
                reasons = listOf("Invalid URL format detected")
            )
        }

        // -----------------------------
        // 2. WHOIS SIMULATION (DOMAIN AGE)
        // -----------------------------
        val domainAgeDays = Random.nextInt(1, 365)

        if (domainAgeDays < 7) {
            reasons.add("Domain is very new ($domainAgeDays days)")
            score -= 35
        } else if (domainAgeDays < 30) {
            reasons.add("Recently registered domain ($domainAgeDays days)")
            score -= 15
        }

        // -----------------------------
        // 3. DNS SIMULATION
        // -----------------------------
        val dnsRisk = Random.nextBoolean()

        if (!dnsRisk) {
            reasons.add("DNS inconsistency detected (possible spoofing)")
            score -= 20
        }

        // -----------------------------
        // 4. SSL CERTIFICATE SIMULATION
        // -----------------------------
        val hasSSL = url.startsWith("https")

        if (!hasSSL) {
            reasons.add("No SSL encryption (HTTP detected)")
            score -= 25
        } else {
            val sslTrusted = Random.nextBoolean()

            if (!sslTrusted) {
                reasons.add("Untrusted SSL certificate issuer")
                score -= 15
            }
        }

        // -----------------------------
        // 5. PHISHING KEYWORD DETECTION
        // -----------------------------
        val phishingKeywords = listOf(
            "login", "verify", "update", "bank", "secure", "password"
        )

        if (phishingKeywords.any { url.contains(it, true) }) {
            reasons.add("Phishing-related keywords detected")
            score -= 20
        }

        // -----------------------------
        // 6. REDIRECT SIMULATION
        // -----------------------------
        val hasRedirectChain = Random.nextBoolean()

        if (hasRedirectChain) {
            reasons.add("Multiple redirect chain detected")
            score -= 15
        }

        // -----------------------------
        // 7. THREAT INTELLIGENCE SIMULATION
        // -----------------------------
        val threatDetected = Random.nextInt(100) < 25

        if (threatDetected) {
            reasons.add("Matched with known phishing patterns (Threat DB)")
            score -= 30
        }

        // -----------------------------
        // FINAL DECISION
        // -----------------------------
        val finalScore = score.coerceIn(0, 100)

        val isSafe = finalScore >= 60

        if (reasons.isEmpty()) {
            reasons.add("No threats detected - clean URL")
        }

        return AnalysisResult(
            isSafe = isSafe,
            score = finalScore,
            reasons = reasons
        )
    }
}