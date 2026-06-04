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

                LoginScreen(viewModel = viewModel)
            }
        }
    }

}
