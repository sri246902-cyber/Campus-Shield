package com.kct.campusshield.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kct.campusshield.utils.AppState

@Composable
fun EnableProtectionScreen() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Enable Protection",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Set CampusShield as the default app for opening web links."
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Button(
            onClick = {

                AppState.setProtectionEnabled(
                    context,
                    true
                )

                try {

                    context.startActivity(
                        Intent(
                            Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS
                        )
                    )

                } catch (_: Exception) {

                    context.startActivity(
                        Intent(
                            Settings.ACTION_SETTINGS
                        )
                    )

                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Enable Protection"
            )

        }

    }

}