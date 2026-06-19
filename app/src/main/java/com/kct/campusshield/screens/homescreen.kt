package com.kct.campusshield.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.kct.campusshield.notifications.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.kct.campusshield.utils.StatisticsManager

@Composable
fun HomeScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val activity = context as Activity
    val scope = rememberCoroutineScope()

    val totalLinks =
        StatisticsManager.getTotal(
            context
        )

    val safeLinks =
        StatisticsManager.getSafe(
            context
        )

    val suspiciousLinks =
        StatisticsManager.getSuspicious(
            context
        )

    val phishingLinks =
        StatisticsManager.getPhishing(
            context
        )

    var url by remember {
        mutableStateOf("")
    }

    var protectionEnabled by remember {
        mutableStateOf(
            isDefaultBrowser(context)
        )
    }

    var settingsOpened by remember {
        mutableStateOf(false)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->

            if (event == Lifecycle.Event.ON_RESUME) {

                val currentStatus =
                    isDefaultBrowser(context)

                val previousStatus =
                    protectionEnabled

                protectionEnabled =
                    currentStatus

                if (settingsOpened) {

                    settingsOpened = false

                    scope.launch {

                        if (
                            !previousStatus &&
                            currentStatus
                        ) {

                            NotificationHelper
                                .showNotification(
                                    context,
                                    "CampusShield",
                                    "🟢 Protection Enabled"
                                )

                            delay(1200)

                            activity.finishAffinity()
                        }

                        else if (
                            previousStatus &&
                            !currentStatus
                        ) {

                            NotificationHelper
                                .showNotification(
                                    context,
                                    "CampusShield",
                                    "🔴 Protection Disabled"
                                )
                        }
                    }
                }
            }
        }

        lifecycleOwner.lifecycle
            .addObserver(observer)

        onDispose {

            lifecycleOwner.lifecycle
                .removeObserver(observer)

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

        Text(
            text = "🛡 CampusShield",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Secure Link Detection",
            color = Color.DarkGray,
            fontSize = 18.sp
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Text(
            text = "Protection Status",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        if (protectionEnabled) {

            Text(
                text = "🟢 Protection Enabled",
                color = Color(0xFF4CAF50),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

        } else {

            Text(
                text = "🔴 Protection Disabled",
                color = Color.Red,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Button(
            onClick = {

                settingsOpened = true

                try {

                    context.startActivity(
                        Intent(
                            Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS
                        )
                    )

                } catch (e: Exception) {

                    context.startActivity(
                        Intent(
                            Settings.ACTION_SETTINGS
                        )
                    )

                }

            },

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        if (protectionEnabled)
                            Color.Red
                        else
                            MaterialTheme
                                .colorScheme
                                .primary
                ),

            modifier = Modifier.fillMaxWidth()

        ) {

            Text(
                text =
                    if (protectionEnabled)
                        "Disable Protection"
                    else
                        "Enable Protection"
            )

        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        if (!protectionEnabled) {

            Divider()

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            Text(
                text = "Manual URL Analysis",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            OutlinedTextField(
                value = url,

                onValueChange = {
                    url = it
                },

                label = {

                    Text(
                        text = "Paste URL",
                        color = Color.Black
                    )

                },

                textStyle =
                    LocalTextStyle.current.copy(
                        color = Color.Black
                    ),

                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedTextColor =
                            Color.Black,

                        unfocusedTextColor =
                            Color.Black,

                        focusedLabelColor =
                            Color.Black,

                        unfocusedLabelColor =
                            Color.Black,

                        cursorColor =
                            Color.Black,

                        focusedBorderColor =
                            MaterialTheme
                                .colorScheme
                                .primary,

                        unfocusedBorderColor =
                            Color.Gray
                    ),

                modifier = Modifier.fillMaxWidth(),

                singleLine = true
            )

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            Button(
                onClick = {

                    if (url.isNotBlank()) {

                        navController.navigate(
                            "analysis/${Uri.encode(url)}"
                        )

                    }

                },
                modifier = Modifier.fillMaxWidth()
            ){

                Text(
                    text = "Analyze URL"
                )

            }
            Spacer(
                modifier = Modifier.height(25.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Security Statistics",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = "Links Checked : $totalLinks",
                        color = Color.Black
                    )

                    Text(
                        text = "🟢 Safe Links : $safeLinks",
                        color = Color.Black
                    )

                    Text(
                        text = "🟠 Suspicious Links : $suspiciousLinks",
                        color = Color.Black
                    )

                    Text(
                        text = "🔴 Phishing Links : $phishingLinks",
                        color = Color.Black
                    )
                }
            }

        }

    }

}

fun isDefaultBrowser(
    context: Context
): Boolean {

    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("http://")
    )

    val resolveInfo =
        context.packageManager
            .resolveActivity(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )

    return resolveInfo
        ?.activityInfo
        ?.packageName ==
            context.packageName
}