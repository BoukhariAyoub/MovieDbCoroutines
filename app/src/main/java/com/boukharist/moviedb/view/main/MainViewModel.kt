package com.boukharist.moviedb.view.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.boukharist.moviedb.base.DisposableViewModel
import com.boukharist.moviedb.data.datasource.local.config.ConfigEntity
import com.boukharist.moviedb.data.datasource.local.movie.MovieEntity
import com.boukharist.moviedb.data.repository.MovieRepository
import com.boukharist.moviedb.util.DispatcherProvider
import com.boukharist.moviedb.util.getTag
import com.boukharist.moviedb.view.ErrorState
import com.boukharist.moviedb.view.LoadedState
import com.boukharist.moviedb.view.LoadingState
import com.boukharist.moviedb.view.ViewModelState
import com.boukharist.moviedb.view.main.list.MovieListItem
import kotlinx.coroutines.*

class MainViewModel(private val movieRepository: MovieRepository,
                    private val dispatcherProvider: DispatcherProvider) : DisposableViewModel() {

    private val _states = MutableLiveData<ViewModelState>()
    val states: LiveData<ViewModelState>
        get() = _states


    fun getAllMovies(page: Int) {
        launch {
            supervisorScope {
                try {
                    //start = CoroutineStart.LAZY
                    val topMovies = async { movieRepository.getTopMovies(page) }
                    val config = async { movieRepository.getConfig() }
                    val movies = MovieListMapper(config.await(), topMovies.await())
                    withContext(dispatcherProvider.ui()) {
                        _states.value = LoadedState(movies)
                    }
                } catch (throwable: Throwable) {
                    withContext(dispatcherProvider.ui()) { _states.value = ErrorState(throwable) }
                    Log.e(getTag(), throwable.message, throwable)
                }
            }
        }
    }

    object MovieListMapper : (ConfigEntity, List<MovieEntity>) -> List<MovieListItem> {
        override fun invoke(config: ConfigEntity, items: List<MovieEntity>): List<MovieListItem> {
            return items.map { movie -> MovieListItem.from(config.getPosterPrefixUrl(), movie) }
        }
    }
}