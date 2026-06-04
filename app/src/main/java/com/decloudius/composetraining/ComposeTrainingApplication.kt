package com.decloudius.composetraining

import android.app.Application
import com.decloudius.composetraining.di.appModule
import com.decloudius.composetraining.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Application class is the first thing that runs when your app starts.
 * We use it to initialize Koin (our dependency injection framework).
 * Think of Koin as a "box of ingredients" that automatically provides objects
 * to any class that asks for them. You declare the ingredients once (modules)
 * and Koin handles creating them and passing them around.
 */
class ComposeTrainingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin: tell it to use this Application as the Android context,
        // enable logging, and load our modules.
        startKoin {
            androidLogger()
            androidContext(this@ComposeTrainingApplication)
            modules(
                appModule,      // ViewModels, Repositories, Preferences
                databaseModule  // Room database and DAOs
            )
        }
    }
}
