package com.boukharist.moviedb

import android.app.Application
import com.boukharist.moviedb.di.movieApp
import org.koin.android.ext.android.startKoin

class MovieDbApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // start Koin context
        startKoin(this, movieApp, extraProperties = mapOf(Pair("SERVER_URL", BuildConfig.SERVER_URL)))
    }
}