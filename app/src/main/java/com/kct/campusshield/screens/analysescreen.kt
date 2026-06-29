package com.kct.campusshield.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.kct.campusshield.whois.WhoisManager
import com.kct.campusshield.whoisfreaks.WhoisState

import com.kct.campusshield.security.SSLChecker
import com.kct.campusshield.security.SSLState

import com.kct.campusshield.redirect.RedirectAnalyzer
import com.kct.campusshield.redirect.RedirectState

import com.kct.campusshield.threat.ThreatManager
import com.kct.campusshield.threat.ThreatState

import com.kct.campusshield.dns.DNSAnalyzer
import com.kct.campusshield.dns.DNSState

@Composable
fun AnalysisScreen(
    navController: NavController,
    url: String
) {

    LaunchedEffect(Unit) {

        // WHOIS Analysis
        try {

            val whoisResponse =
                WhoisManager.getWhoisStatus(url)
            Log.d("AGE_DEBUG", whoisResponse.toString())
            Log.d("AGE_DEBUG", "create_date = ${whoisResponse?.create_date}")

            WhoisState.whoisStatus =
                if (whoisResponse?.domain_registered == "yes")
                    "registered"
                else
                    "available"

            Log.d(
                "WHOIS_RESPONSE",
                whoisResponse.toString()
            )

            if (!whoisResponse?.create_date.isNullOrEmpty()) {

                try {

                    val year =
                        whoisResponse!!.create_date!!
                            .substring(0, 4)
                            .toInt()

                    val currentYear =
                        java.util.Calendar
                            .getInstance()
                            .get(java.util.Calendar.YEAR)

                    val age =
                        currentYear - year

                    WhoisState.domainAgeYears = age
                    Log.d("AGE_DEBUG", "Age calculated = $age")

                    WhoisState.domainAgeScore =
                        when {

                            age < 1 -> 30

                            age < 2 -> 20

                            age < 5 -> 10

                            else -> 0

                        }

                } catch (e: Exception) {

                    WhoisState.domainAgeYears = 0
                    WhoisState.domainAgeScore = 20

                }

            }
            Log.d(
                "WHOIS_TEST",
                "Registered = ${whoisResponse?.domain_registered}"
            )

            Log.d(
                "WHOIS_TEST",
                "Created = ${whoisResponse?.create_date}"
            )
        } catch (e: Exception) {

            WhoisState.whoisStatus = null

            WhoisState.domainAgeScore = 20

            WhoisState.domainAgeYears = 0

            Log.e(
                "WHOIS_TEST",
                "WHOIS Error",
                e
            )
        }
        // DNS Analysis
        try {

            val host =
                Uri.parse(url).host ?: ""

            Log.d(
                "DNS_HOST",
                "Host = $host"
            )

            val dns =
                DNSAnalyzer.resolve(host)

            DNSState.dnsResolved =
                dns.first

            DNSState.ipAddress =
                dns.second

            DNSState.dnsScore =
                if (dns.first) 0 else 20

            Log.d(
                "DNS_STATE",
                "Resolved=${DNSState.dnsResolved}, IP=${DNSState.ipAddress}"
            )

        } catch (e: Exception) {

            DNSState.dnsResolved = false

            DNSState.ipAddress = ""

            DNSState.dnsScore = 20

            Log.e(
                "DNS_TEST",
                "DNS Analysis Error",
                e
            )
        }

        // Threat Intelligence Analysis
        try {

            val threatDetected =
                ThreatManager.checkUrl(url)

            ThreatState.isThreat =
                threatDetected

            Log.d(
                "THREAT_TEST",
                "Threat Detected = $threatDetected"
            )

        } catch (e: Exception) {

            ThreatState.isThreat = null

            Log.e(
                "THREAT_TEST",
                "Threat Intelligence Error",
                e
            )

        }

        // SSL Analysis
        try {

            val sslValid =
                SSLChecker.isValidSSL(url)

            SSLState.isValid =
                sslValid

        } catch (e: Exception) {

            SSLState.isValid = null

            Log.e(
                "SSL_TEST",
                "SSL Error",
                e
            )

        }

        // Redirect Analysis
        try {

            val redirectCount =
                RedirectAnalyzer.analyze(url)

            RedirectState.redirectCount =
                redirectCount

        } catch (e: Exception) {

            RedirectState.redirectCount = 0

            Log.e(
                "REDIRECT_TEST",
                "Redirect Error",
                e
            )

        }

        val encodedUrl =
            Uri.encode(url)

        navController.navigate(
            "result/$encodedUrl"
        ) {

            popUpTo(
                "analysis/$url"
            ) {
                inclusive = true
            }

        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        CircularProgressIndicator()

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Analyzing URL...",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = url,
            color = Color.Gray
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Checking Security Layers...",
            color = Color.DarkGray
        )

    }

}