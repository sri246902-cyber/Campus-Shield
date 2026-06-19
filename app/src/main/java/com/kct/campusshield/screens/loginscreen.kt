package com.kct.campusshield.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
fun LoginScreen(
    navController: NavController
) {

    val context = LocalContext.current

    var email by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            text = "Sign in using your KCT Email",
            color = Color.Black
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                error = ""
            },
            label = {
                Text(
                    text = "Enter KCT Email",
                    color = Color.Black
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                cursorColor = Color.Black,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        if (error.isNotEmpty()) {

            Text(
                text = error,
                color = Color.Red
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),

            onClick = {

                if (email.isBlank()) {

                    error = "Email cannot be empty"
                    return@Button
                }

                if (!email.endsWith("@kct.ac.in")) {

                    error = "Use a valid KCT email"
                    return@Button
                }

                AppState.setLoggedIn(
                    context,
                    true
                )

                navController.navigate("home") {

                    popUpTo("login") {
                        inclusive = true
                    }

                }

            }
        ) {

            Text("Continue")

        }

    }

}