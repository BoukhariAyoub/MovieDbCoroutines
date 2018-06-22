package com.boukharist.moviedb.view.detail

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.boukharist.moviedb.base.DisposableViewModel
import com.boukharist.moviedb.data.repository.MovieRepository
import com.boukharist.moviedb.util.SchedulerProvider
import com.boukharist.moviedb.view.ErrorState
import com.boukharist.moviedb.view.LoadingState
import com.boukharist.moviedb.view.ViewModelState
import io.reactivex.Single

class DetailViewModel(private val movieRepository: MovieRepository,
                      private val schedulerProvider: SchedulerProvider)
    : DisposableViewModel() {
    companion object {
        const val TAG = "DetailViewModel"
    }

    private val _states = MutableLiveData<ViewModelState>()
    val states: LiveData<ViewModelState>
        get() = _states

    @SuppressLint("RxLeakedSubscription")
    fun getMovie(id: String) {
        launch {
            Single.zip(movieRepository.getConfig(), movieRepository.getMovieById(id), MovieDetailMapper())
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe { _states.postValue(LoadingState) }
                    .subscribe({ movie ->
                        _states.postValue(LoadedState(movie))
                    }, { throwable ->
                        Log.e(TAG, throwable.message, throwable)
                        _states.postValue(ErrorState(throwable))
                    })
        }
    }


    data class LoadedState(val value: MovieDetailItem) : ViewModelState()
}