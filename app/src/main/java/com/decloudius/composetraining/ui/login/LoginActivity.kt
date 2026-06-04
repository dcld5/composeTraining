package com.decloudius.composetraining.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import com.decloudius.composetraining.ui.BaseLocaleActivity
import com.decloudius.composetraining.ui.dashboard.DashboardActivity
import com.decloudius.composetraining.ui.theme.ComposeTrainingTheme
import org.koin.androidx.compose.koinViewModel

class LoginActivity : BaseLocaleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ComposeTrainingTheme {

                val viewModel: LoginViewModel = koinViewModel()

                val navigate by viewModel.navigateToDashboard.collectAsState()
                if (navigate) {
                    LaunchedEffect(navigate) {

                        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                        viewModel.onNavigationHandled()
                        finish()
                    }
                }

                LoginScreen(
                    viewModel = viewModel,
                    onBiometricClick = { showBiometricPrompt(viewModel) }
                )
            }
        }
    }

    private fun showBiometricPrompt(viewModel: LoginViewModel) {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                viewModel.onBiometricSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)

            }
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Log in using your fingerprint or face")
            .setNegativeButtonText("Cancel")
            .build()

        BiometricPrompt(this, executor, callback).authenticate(promptInfo)
    }
}
