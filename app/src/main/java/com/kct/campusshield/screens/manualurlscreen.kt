package com.kct.campusshield.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.net.URLEncoder

@Composable
fun ManualUrlScreen(navController: NavController) {

    var url by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Enter URL")

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                if (url.isNotEmpty()) {

                    val encoded = URLEncoder.encode(url, "UTF-8")

                    navController.navigate("analysis/$encoded")
                }
            }
        ) {
            Text("Analyze URL")
        }
    }
}