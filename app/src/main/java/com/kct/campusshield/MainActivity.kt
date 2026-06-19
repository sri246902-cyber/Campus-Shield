package com.kct.campusshield

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kct.campusshield.screens.AnalysisScreen
import com.kct.campusshield.screens.EnableProtectionScreen
import com.kct.campusshield.screens.HomeScreen
import com.kct.campusshield.screens.LoginScreen
import com.kct.campusshield.screens.ResultScreen
import com.kct.campusshield.screens.SplashScreen
import com.kct.campusshield.ui.theme.CampusShieldTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        requestNotificationPermission()

        val incomingUrl =
            intent?.data?.toString()

        setContent {

            CampusShieldTheme {

                val navController =
                    rememberNavController()

                val startDestination =
                    if (incomingUrl != null)
                        "analysis/${Uri.encode(incomingUrl)}"
                    else
                        "splash"

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {

                    composable("splash") {

                        SplashScreen(navController)

                    }

                    composable("login") {

                        LoginScreen(navController)

                    }

                    composable("home") {

                        HomeScreen(navController)

                    }

                    composable("enable") {

                        EnableProtectionScreen()

                    }

                    composable(
                        route = "analysis/{url}"
                    ) { backStackEntry ->

                        val url =
                            backStackEntry.arguments
                                ?.getString("url") ?: ""

                        AnalysisScreen(
                            navController = navController,
                            url = url
                        )

                    }

                    composable(
                        route = "result/{url}"
                    ) { backStackEntry ->

                        val url =
                            backStackEntry.arguments
                                ?.getString("url") ?: ""

                        ResultScreen(
                            url = url
                        )

                    }

                }

            }

        }

    }

    private fun requestNotificationPermission() {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {

            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    1001
                )

            }

        }

    }

}