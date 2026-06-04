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

/**
 * DashboardActivity is the SECOND screen after login.
 * It hosts the bottom navigation and the three sub-menus (Home, Profile, Settings).
 * We extend BaseLocaleActivity so the selected language is respected.
 */
class DashboardActivity : BaseLocaleActivity() {

    // We inject AuthRepository here only for the logout action.
    private val authRepository: AuthRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Ask Koin for the DashboardViewModel scoped to this Activity.
            val dashboardViewModel: DashboardViewModel = koinViewModel()

            // Observe the theme mode and decide whether we are in dark mode.
            val themeMode by dashboardViewModel.themeMode.collectAsState()
            val isSystemDark = isSystemInDarkTheme()
            val isDark = when (themeMode) {
                "dark" -> true
                "light" -> false
                else -> isSystemDark
            }

            // When language changes, the ViewModel emits recreateNeeded.
            // The Activity collects it and calls recreate() so the new locale applies.
            LaunchedEffect(Unit) {
                dashboardViewModel.recreateNeeded.collect {
                    recreate()
                }
            }

            // Wrap the entire UI in our custom theme.
            ComposeTrainingTheme(darkTheme = isDark) {
                DashboardScreen(
                    dashboardViewModel = dashboardViewModel,
                    onLogout = {
                        // Clear saved PIN and biometric flag.
                        authRepository.clear()
                        // Go back to login and remove this Activity from the back stack.
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}
