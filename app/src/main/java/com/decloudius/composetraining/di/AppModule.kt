package com.decloudius.composetraining.di

import com.decloudius.composetraining.data.local.db.AppDatabase
import com.decloudius.composetraining.data.local.prefs.AuthPreferences
import com.decloudius.composetraining.data.local.prefs.SettingsPreferences
import com.decloudius.composetraining.data.repository.AuthRepository
import com.decloudius.composetraining.data.repository.PhotoRepository
import com.decloudius.composetraining.data.repository.ProfileRepository
import com.decloudius.composetraining.ui.dashboard.DashboardViewModel
import com.decloudius.composetraining.ui.home.HomeViewModel
import com.decloudius.composetraining.ui.login.LoginViewModel
import com.decloudius.composetraining.ui.profile.ProfileViewModel
import com.decloudius.composetraining.ui.theme.ThemeManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin Modules = recipes that tell Koin HOW to create your objects.
 * - `single { ... }` means: create this object once and reuse it everywhere (singleton).
 * - `viewModel { ... }` means: create this ViewModel and let Koin inject its dependencies.
 *
 * When a class constructor asks for an AuthRepository, Koin looks at these recipes,
 * sees AuthRepository needs AuthPreferences, and automatically provides it.
 */

/** Module for database and DAOs (shared singletons). */
val databaseModule = module {
    // Build the Room database once.
    single { AppDatabase.create(androidContext()) }

    // Ask the database for its DAOs.
    single { get<AppDatabase>().photoDao() }
    single { get<AppDatabase>().profileDao() }
}

/** Module for preferences, repositories, ViewModels, and theme manager. */
val appModule = module {
    // Preferences
    single { AuthPreferences(androidContext()) }
    single { SettingsPreferences(androidContext()) }

    // Theme manager (handles light/dark/system preference)
    single { ThemeManager(androidContext()) }

    // Repositories
    single { AuthRepository(get()) }
    single { PhotoRepository(androidContext(), get()) }
    single { ProfileRepository(get()) }

    // ViewModels - Koin automatically injects the repository arguments.
    viewModel { LoginViewModel(get()) }
    viewModel { DashboardViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}
