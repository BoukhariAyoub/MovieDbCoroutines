package com.boukharist.moviedb.util

import kotlinx.coroutines.CoroutineDispatcher



/**

 *  Dispatcher Provider

 */

interface DispatcherProvider {
    fun ui(): CoroutineDispatcher

}