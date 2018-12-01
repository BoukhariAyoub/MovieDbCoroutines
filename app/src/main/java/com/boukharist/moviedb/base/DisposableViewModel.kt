package com.boukharist.moviedb.base

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class DisposableViewModel : ViewModel(), CoroutineScope {

    private var parentJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + parentJob


    protected fun execute(block: suspend () -> Unit) {
        val errorHandler = CoroutineExceptionHandler { _, throwable -> throw Throwable(throwable) }

        launch(coroutineContext + errorHandler) {
            block()
        }
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}