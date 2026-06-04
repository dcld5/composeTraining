package com.decloudius.composetraining.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

                LoginScreen(viewModel = viewModel)
            }
        }
    }

}
