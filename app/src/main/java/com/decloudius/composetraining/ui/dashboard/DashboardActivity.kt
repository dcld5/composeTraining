package com.decloudius.composetraining.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.decloudius.composetraining.data.repository.AuthRepository
import com.decloudius.composetraining.ui.BaseLocaleActivity
import com.decloudius.composetraining.ui.login.LoginActivity
import com.decloudius.composetraining.ui.theme.ComposeTrainingTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel

class DashboardActivity : BaseLocaleActivity() {

    private val authRepository: AuthRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val dashboardViewModel: DashboardViewModel = koinViewModel()

            val themeMode by dashboardViewModel.themeMode.collectAsState()
            val isSystemDark = isSystemInDarkTheme()
            val isDark = when (themeMode) {
                "dark" -> true
                "light" -> false
                else -> isSystemDark
            }

            LaunchedEffect(Unit) {
                dashboardViewModel.recreateNeeded.collect {
                    recreate()
                }
            }

            ComposeTrainingTheme(darkTheme = isDark) {
                DashboardScreen(
                    dashboardViewModel = dashboardViewModel,
                    onLogout = {

                        authRepository.clear()

                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}
