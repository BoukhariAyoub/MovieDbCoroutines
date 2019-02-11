package com.boukharist.moviedb.util

import kotlinx.coroutines.Dispatchers


/**

 *  Dispatcher Provider

 */

class AppDispatcherProvider : DispatcherProvider {
    override fun ui() = Dispatchers.Main
}