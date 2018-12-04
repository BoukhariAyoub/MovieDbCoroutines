package com.boukharist.moviedb.util

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    fun ui(): CoroutineDispatcher

}