package com.kct.campusshield.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kct.campusshield.utils.AppState

@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {

        if (AppState.isLoggedIn(context)) {

            navController.navigate("home") {
                popUpTo("splash") {
                    inclusive = true
                }
            }

        } else {

            navController.navigate("login") {
                popUpTo("splash") {
                    inclusive = true
                }
            }

        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "🛡",
                fontSize = 72.sp
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "CampusShield",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "KCT Secure Link Detection",
                fontSize = 18.sp,
                color = Color.DarkGray
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Protecting Students from Phishing Links",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}