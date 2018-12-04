package com.boukharist.moviedb.base

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class DisposableViewModel : ViewModel(), CoroutineScope {

    private var parentJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + parentJob

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}