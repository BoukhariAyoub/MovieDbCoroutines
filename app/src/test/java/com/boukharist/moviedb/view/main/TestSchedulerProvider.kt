package com.boukharist.moviedb.view.main

import com.boukharist.moviedb.util.DispatcherProvider
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider : DispatcherProvider {
    override fun ui() = Dispatchers.Unconfined
}