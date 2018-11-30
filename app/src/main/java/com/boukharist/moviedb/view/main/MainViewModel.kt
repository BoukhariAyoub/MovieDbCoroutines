package com.boukharist.moviedb.view.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.boukharist.moviedb.base.DisposableViewModel
import com.boukharist.moviedb.data.datasource.local.config.ConfigEntity
import com.boukharist.moviedb.data.datasource.local.movie.MovieEntity
import com.boukharist.moviedb.data.repository.MovieRepository
import com.boukharist.moviedb.util.SchedulerProvider
import com.boukharist.moviedb.view.ErrorState
import com.boukharist.moviedb.view.LoadingState
import com.boukharist.moviedb.view.ViewModelState
import com.boukharist.moviedb.view.main.list.MovieListItem
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel(private val movieRepository: MovieRepository,
                    private val schedulerProvider: SchedulerProvider)
    : DisposableViewModel() , CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object {
        const val TAG = "MainViewModel"
    }

    private val _states = MutableLiveData<ViewModelState>()
    val states: LiveData<ViewModelState>
        get() = _states


    fun getAllMovies(page: Int) {

        launch(Dispatchers.IO) {
            val config = movieRepository.getConfig()
            _states.postValue(ErrorState(Throwable(config.toString())))
        }


     /*   launchDisposable {
            Single.zip(movieRepository.getConfig(), movieRepository.getTopMovies(page), MovieListMapper())
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe { _states.postValue(LoadingState) }
                    .subscribe({ list ->
                        _states.postValue(LoadedState(list))
                    }, { throwable ->
                        _states.postValue(ErrorState(throwable))
                        Log.e(TAG, throwable.message, throwable)
                    })
        } */
    }

    class MovieListMapper : BiFunction<ConfigEntity, List<MovieEntity>, MutableList<MovieListItem>> {
        override fun apply(config: ConfigEntity, items: List<MovieEntity>): MutableList<MovieListItem> {
            val posterPrefix = config.getPosterPrefixUrl()
            return items
                    .map { MovieListItem.from(posterPrefix, it) }
                    .toMutableList()
        }
    }

    data class LoadedState(val value: MutableList<MovieListItem>) : ViewModelState()
}