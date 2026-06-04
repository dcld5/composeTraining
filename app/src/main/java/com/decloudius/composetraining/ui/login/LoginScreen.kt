package com.decloudius.composetraining.ui.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decloudius.composetraining.R
import com.decloudius.composetraining.ui.theme.ComposeTrainingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onBiometricClick: () -> Unit = {}
) {

    val pin by viewModel.pin.collectAsState()
    val isPinSet by viewModel.isPinSet.collectAsState()
    val error by viewModel.error.collectAsState()

    LoginScreenContent(
        pin = pin,
        isPinSet = isPinSet,
        error = error,
        onPinChange = viewModel::onPinChange,
        onLoginClick = viewModel::onLoginClick,
        onCreatePinClick = viewModel::onCreatePinClick,
        onBiometricClick = onBiometricClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    pin: String,
    isPinSet: Boolean,
    error: String?,
    onPinChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCreatePinClick: () -> Unit,
    onBiometricClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {

            TopAppBar(
                title = { Text(stringResource(R.string.login)) }
            )
        }
    ) { innerPadding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = if (isPinSet) {

                    stringResource(R.string.enter_pin)
                } else {

                    "Create a 6-digit PIN"
                },

                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = pin,
                onValueChange = onPinChange,
                label = { Text("PIN") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = error != null,
                modifier = Modifier.fillMaxWidth()
            )

            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isPinSet) {

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.login))
                }
            } else {

                Button(
                    onClick = onCreatePinClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save PIN")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "First time - create PIN")
@Composable
fun PreviewLoginScreenFirstTime() {

    MaterialTheme {
        LoginScreenContent(
            pin = "",
            isPinSet = false,
            error = null,
            onPinChange = {},
            onLoginClick = {},
            onCreatePinClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    name = "Returning user")
@Composable
fun PreviewLoginScreenReturningUser() {
    ComposeTrainingTheme (darkTheme = true) {
        LoginScreenContent(
            pin = "123456",
            isPinSet = true,
            error = "Wrong PIN",
            onPinChange = {},
            onLoginClick = {},
            onCreatePinClick = {}
        )
    }
}
