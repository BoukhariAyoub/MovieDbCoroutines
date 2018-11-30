package com.boukharist.moviedb.view.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.boukharist.moviedb.base.DisposableViewModel
import com.boukharist.moviedb.data.repository.MovieRepository
import com.boukharist.moviedb.util.SchedulerProvider
import com.boukharist.moviedb.view.ErrorState
import com.boukharist.moviedb.view.ViewModelState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailViewModel(private val movieRepository: MovieRepository,
                      private val schedulerProvider: SchedulerProvider)
    : DisposableViewModel(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object {
        const val TAG = "DetailViewModel"
    }

    private val _states = MutableLiveData<ViewModelState>()
    val states: LiveData<ViewModelState>
        get() = _states

    fun getMovie(id: String) {
        launch {
            val config = movieRepository.getConfig()
            _states.postValue(ErrorState(Throwable(config.toString())))
        }

        /* launchDisposable {


             Single.zip(movieRepository.getConfig(), movieRepository.getMovieById(id), { config, movie -> })
                     .subscribeOn(schedulerProvider.io())
                     .observeOn(schedulerProvider.ui())
                     .doOnSubscribe { _states.postValue(LoadingState) }
                     .subscribe({ movie ->
                         _states.postValue(LoadedState(movie))
                     }, { throwable ->
                         Log.e(TAG, throwable.message, throwable)
                         _states.postValue(ErrorState(throwable))
                     })
         } */
    }


    data class LoadedState(val value: MovieDetailItem) : ViewModelState()
}