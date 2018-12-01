package com.boukharist.moviedb.view.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.boukharist.moviedb.base.DisposableViewModel
import com.boukharist.moviedb.data.datasource.local.config.ConfigEntity
import com.boukharist.moviedb.data.datasource.local.movie.MovieEntity
import com.boukharist.moviedb.data.repository.MovieRepository
import com.boukharist.moviedb.view.ErrorState
import com.boukharist.moviedb.view.ViewModelState
import com.boukharist.moviedb.view.main.list.MovieListItem
import kotlinx.coroutines.*

class MainViewModel(private val movieRepository: MovieRepository) : DisposableViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    private val _states = MutableLiveData<ViewModelState>()
    val states: LiveData<ViewModelState>
        get() = _states


    fun getAllMovies(page: Int) {
        launch {
            supervisorScope {
                try {
                    val config = async { movieRepository.getConfig() }
                    val topMovies = async { movieRepository.getTopMovies(page) }
                    val movies = MovieListMapper(config.await(), topMovies.await())
                    withContext(Dispatchers.Main) { _states.postValue(LoadedState(movies)) }
                } catch (throwable: Throwable) {
                    withContext(Dispatchers.Main) { _states.postValue(ErrorState(throwable)) }
                    Log.e(TAG, throwable.message, throwable)
                }
            }
        }
    }

    object MovieListMapper : (ConfigEntity, List<MovieEntity>) -> MutableList<MovieListItem> {
        override fun invoke(config: ConfigEntity, items: List<MovieEntity>): MutableList<MovieListItem> {
            val posterPrefix = config.getPosterPrefixUrl()
            return items
                    .map { MovieListItem.from(posterPrefix, it) }
                    .toMutableList()
        }
    }

    data class LoadedState(val value: MutableList<MovieListItem>) : ViewModelState()
}