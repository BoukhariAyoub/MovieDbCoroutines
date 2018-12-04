package com.boukharist.moviedb.view

/**
 * Abstract ViewModel State
 */
open class ViewModelState

/**
 * Generic Loading ViewModel State
 */
object LoadingState : ViewModelState()


data class ErrorState(val error: Throwable) : ViewModelState()

data class LoadedState<T>(val data: T) : ViewModelState()