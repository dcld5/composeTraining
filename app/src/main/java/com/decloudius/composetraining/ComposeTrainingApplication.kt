package com.decloudius.composetraining

import android.app.Application
import com.decloudius.composetraining.di.appModule
import com.decloudius.composetraining.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ComposeTrainingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ComposeTrainingApplication)
            modules(
                appModule,
                databaseModule
            )
        }
    }
}
