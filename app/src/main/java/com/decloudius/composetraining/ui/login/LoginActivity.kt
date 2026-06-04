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

/**
 * LoginActivity is the FIRST screen the user sees.
 * In Compose, an Activity is just a shell: it calls setContent { } and places your Composable tree.
 * We extend BaseLocaleActivity so the correct language is applied before anything else.
 */
class LoginActivity : BaseLocaleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // ComposeTrainingTheme wraps everything so all screens share colors/fonts.
            ComposeTrainingTheme {
                // koinViewModel asks Koin to give us the LoginViewModel (with all dependencies injected).
                val viewModel: LoginViewModel = koinViewModel()

                // Observe the one-shot navigation event.
                // LaunchedEffect runs when the key (navigate) changes.
                val navigate by viewModel.navigateToDashboard.collectAsState()
                if (navigate) {
                    LaunchedEffect(navigate) {
                        // Open DashboardActivity and close Login so the user can't press Back to return.
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

    /** Displays the system BiometricPrompt (fingerprint / face). */
    private fun showBiometricPrompt(viewModel: LoginViewModel) {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Tell the ViewModel to proceed.
                viewModel.onBiometricSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // User cancelled or device had an error; do nothing.
            }
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Log in using your fingerprint or face")
            .setNegativeButtonText("Cancel")
            .build()

        // Note: BaseLocaleActivity -> AppCompatActivity -> FragmentActivity,
        // so "this" is valid as the first argument here.
        BiometricPrompt(this, executor, callback).authenticate(promptInfo)
    }
}
