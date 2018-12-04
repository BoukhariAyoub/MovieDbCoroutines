package com.boukharist.moviedb.util

import kotlinx.coroutines.Dispatchers

class AppDispatcherProvider : DispatcherProvider {
    override fun ui() = Dispatchers.Main
}