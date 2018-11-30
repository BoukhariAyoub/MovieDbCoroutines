package com.boukharist.moviedb

import android.app.Application
import com.boukharist.moviedb.di.DatasourceProperties.SERVER_URL
import com.boukharist.moviedb.di.movieApp
import org.koin.android.ext.android.startKoin

class MovieDbApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // start Koin context
        startKoin(this, movieApp, extraProperties = mapOf(SERVER_URL to BuildConfig.SERVER_URL))
    }
}