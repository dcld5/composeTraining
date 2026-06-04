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

val databaseModule = module {

    single { AppDatabase.create(androidContext()) }

    single { get<AppDatabase>().photoDao() }
    single { get<AppDatabase>().profileDao() }
}

val appModule = module {

    single { AuthPreferences(androidContext()) }
    single { SettingsPreferences(androidContext()) }

    single { ThemeManager(androidContext()) }

    single { AuthRepository(get()) }
    single { PhotoRepository(androidContext(), get()) }
    single { ProfileRepository(get()) }

    viewModel { LoginViewModel(get()) }
    viewModel { DashboardViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}
