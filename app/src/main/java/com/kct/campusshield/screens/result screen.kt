package com.kct.campusshield.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kct.campusshield.security.KctVerifier
import com.kct.campusshield.security.UrlAnalyzer
import androidx.compose.runtime.LaunchedEffect
import com.kct.campusshield.utils.StatisticsManager
import com.kct.campusshield.security.SSLState
import com.kct.campusshield.redirect.RedirectState
import com.kct.campusshield.threat.ThreatState
import com.kct.campusshield.whoisfreaks.WhoisState
import androidx.compose.foundation.layout.statusBarsPadding
import com.kct.campusshield.dns.DNSState
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.kct.campusshield.redirect.RedirectAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.runtime.*

private fun openUrlAndCloseApp(
    activity: Activity,
    url: String
) {

    try {

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )

        intent.setPackage(
            "com.android.chrome"
        )

        activity.startActivity(intent)

    } catch (e: Exception) {

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )

        activity.startActivity(intent)

    }

    activity.finishAffinity()
}

@Composable
fun ResultScreen(
    url: String
) {

    val context = LocalContext.current
    val activity = context as Activity

    var redirectCount by remember {
        mutableStateOf<Int?>(null)
    }

    LaunchedEffect(url) {

        redirectCount = withContext(
            Dispatchers.IO
        ) {
            RedirectAnalyzer.analyze(url)
        }

    }

    if (redirectCount == null) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CircularProgressIndicator()

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Analyzing URL..."
                )
            }
        }

        return
    }
    val redirects = redirectCount!!

    val riskResult =
        UrlAnalyzer.analyze(url)

    val kctResult =
        KctVerifier.verify(url)

    val whoisStatus =
        WhoisState.whoisStatus

    val domainAgeScore =
        WhoisState.domainAgeScore

    val sslValid =
        SSLState.isValid



    val threatDetected =
        ThreatState.isThreat

    val finalScore =
        (
                riskResult.score +
                        WhoisState.domainAgeScore +
                        DNSState.dnsScore +

                        when {
                            redirects >= 3 -> 20
                            redirects >= 1 -> 10
                            else -> 0
                        } +

                        when {
                            threatDetected == true -> 50
                            else -> 0
                        }
                ).coerceAtMost(100)
    val noRuleMatched =
        riskResult.score == 0

    val noEvidence =
        WhoisState.domainAgeYears == 0 &&
                !DNSState.dnsResolved &&
                sslValid != true

    val result = when {

        threatDetected == true ->
            "PHISHING"

        noRuleMatched && noEvidence ->
            "UNDETECTED"

        finalScore >= 70 ->
            "PHISHING"

        finalScore >= 35 ->
            "SUSPICIOUS"

        else ->
            "SAFE"
    }


    LaunchedEffect(Unit) {

        StatisticsManager.incrementTotal(context)

        when(result) {

            "SAFE" -> {
                StatisticsManager.incrementSafe(context)
            }

            "SUSPICIOUS" -> {
                StatisticsManager.incrementSuspicious(context)
            }

            "PHISHING" -> {
                StatisticsManager.incrementPhishing(context)
            }

            "UNDETECTED" -> {
                // nothing
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 70.dp,
                bottom = 24.dp
            ),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = result,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color =
                when (result) {

                    "SAFE" -> Color(0xFF4CAF50)
                    "SUSPICIOUS" -> Color(0xFFFF9800)
                    else -> Color.Red
                }
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFECECEC)
            )
        ){

            Text(
                text = url,
                modifier = Modifier.padding(16.dp)
            )

        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(

            text =
                when {

                    kctResult.verified ->
                        "🛡 ${kctResult.message}"

                    kctResult.message ==
                            "Possible KCT Impersonation" ->
                        "⚠ ${kctResult.message}"

                    else ->
                        "ℹ ${kctResult.message}"

                },

            color =
                when {

                    kctResult.verified ->
                        Color(0xFF4CAF50)

                    kctResult.message ==
                            "Possible KCT Impersonation" ->
                        Color.Red

                    else ->
                        Color.Gray

                },

            fontWeight = FontWeight.Bold

        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            CircularProgressIndicator(
                progress = { finalScore / 100f },
                modifier = Modifier.size(140.dp),
                strokeWidth = 10.dp,
                color = when (result) {
                    "SAFE" -> Color(0xFF4CAF50)
                    "SUSPICIOUS" -> Color(0xFFFF9800)
                    else -> Color.Red
                }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "$finalScore",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "/100",
                    color = Color.Gray
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = when(result) {
                "SAFE" -> "Safe"
                "SUSPICIOUS" -> "Suspicious"
                else -> "Phishing"
            },
            fontWeight = FontWeight.Bold,
            color =
                when (result) {
                    "SAFE" -> Color(0xFF4CAF50)
                    "SUSPICIOUS" -> Color(0xFFFF9800)
                    "UNDETECTED" -> Color.Gray
                    else -> Color.Red
                }
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFECECEC)
            )
        )

        {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "🛡 Security Report",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "🌐 Domain Age : ${WhoisState.domainAgeYears} Years"
                )
                Text(
                    text =
                        if (DNSState.dnsResolved)
                            "🌍 DNS Resolved"
                        else
                            "⚠ DNS Resolution Failed"
                )


                Text(
                    text = if (sslValid == true)
                        "🔒 SSL : Valid"
                    else
                        "⚠ SSL : Invalid"
                )

                Text(
                    text = "↪ Redirects : $redirects"
                )

                Text(
                    text =
                        if (threatDetected == true)
                            "☣ Threat Found"
                        else
                            "☣ Threat Database Clean"
                )

            }
        }

        if (riskResult.reasons.isNotEmpty()) {

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFECECEC)
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "⚠ Detection Reasons",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    riskResult.reasons.forEach { reason ->

                        Text(
                            text = "• $reason",
                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )



        Spacer(
            modifier = Modifier.height(30.dp)
        )
        Spacer(
            modifier = Modifier.height(30.dp)
        )

        when (result) {

            "UNDETECTED" -> {

                Text(
                    text = "⚠ Unable to confidently classify this URL",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "CampusShield could not gather enough evidence to determine whether this URL is safe or malicious. We recommend verifying it manually using trusted security services before opening.",
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(
                    onClick = {
                        openUrlAndCloseApp(
                            activity,
                            url
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Open Carefully")

                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedButton(
                    onClick = {
                        activity.finishAffinity()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Close")

                }
            }

            "SAFE" -> {

                Text(
                    text = "✓ This URL appears safe.",
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(
                    onClick = {
                        openUrlAndCloseApp(
                            activity,
                            url
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Open in Chrome")

                }
            }

            "SUSPICIOUS" -> {

                Text(
                    text = "⚠ Open carefully. This URL looks suspicious.",
                    color = Color(0xFFFF9800),
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(
                    onClick = {
                        openUrlAndCloseApp(
                            activity,
                            url
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Open Carefully")

                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedButton(
                    onClick = {

                        activity.finishAffinity()

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Close")

                }
            }

            "PHISHING" -> {

                Text(
                    text = "⚠ Recommended not to open.",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                OutlinedButton(
                    onClick = {

                        activity.finishAffinity()

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Close")

                }
            }
        }
    }
}
