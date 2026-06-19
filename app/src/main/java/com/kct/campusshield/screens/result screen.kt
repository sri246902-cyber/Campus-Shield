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

    val redirectCount =
        RedirectState.redirectCount

    val threatDetected =
        ThreatState.isThreat

    val finalScore =
        (
                riskResult.score +
                        WhoisState.domainAgeScore +
                        DNSState.dnsScore +

                        when {
                            redirectCount >= 3 -> 20
                            redirectCount >= 1 -> 10
                            else -> 0
                        } +

                        when {
                            threatDetected == true -> 50
                            else -> 0
                        }
                ).coerceAtMost(100)
    val result = when {

        threatDetected == true ->
            "PHISHING"

        finalScore >= 70 ->
            "PHISHING"

        finalScore >= 35 ->
            "SUSPICIOUS"

        else ->
            "SAFE"
    }


    LaunchedEffect(Unit) {

        StatisticsManager.incrementTotal(
            context
        )

        when (result) {

            "SAFE" -> {

                StatisticsManager.incrementSafe(
                    context
                )

            }

            "SUSPICIOUS" -> {

                StatisticsManager
                    .incrementSuspicious(
                        context
                    )

            }

            "PHISHING" -> {

                StatisticsManager
                    .incrementPhishing(
                        context
                    )

            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            modifier = Modifier.fillMaxWidth()
        ) {

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

        Text(
            text = "Risk Score : $finalScore/100",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )

        LinearProgressIndicator(
            progress = { finalScore / 100f },
            modifier = Modifier.fillMaxWidth()
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

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
                    text = "↪ Redirects : $redirectCount"
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
                    containerColor = Color(0xFFF5F5F5)
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

                Button(
                    onClick = {
                        openUrlAndCloseApp(
                            activity,
                            url
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Open Anyway")

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
        }
    }
}
