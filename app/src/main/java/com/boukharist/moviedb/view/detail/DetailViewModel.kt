package com.boukharist.moviedb.view.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.boukharist.moviedb.base.DisposableViewModel
import com.boukharist.moviedb.data.repository.MovieRepository
import com.boukharist.moviedb.util.getTag
import com.boukharist.moviedb.view.ErrorState
import com.boukharist.moviedb.view.LoadedState
import com.boukharist.moviedb.view.ViewModelState
import com.boukharist.moviedb.view.main.MainViewModel
import kotlinx.coroutines.*

class DetailViewModel(private val movieRepository: MovieRepository)
    : DisposableViewModel() {
    private val _states = MutableLiveData<ViewModelState>()
    val states: LiveData<ViewModelState>
        get() = _states

    fun getMovie(id: String) {
        launch {
            supervisorScope {
                try {
                    val deferredConfig = async { movieRepository.getConfig() }
                    val deferredMovie = async { movieRepository.getMovieById(id) }
                    val movie = MovieDetailMapper(deferredConfig.await(), deferredMovie.await())
                    withContext(Dispatchers.Main) { _states.postValue(LoadedState(movie)) }
                } catch (throwable: Throwable) {
                    withContext(Dispatchers.Main) { _states.postValue(ErrorState(throwable)) }
                    Log.e(getTag(), throwable.message, throwable)
                }
            }
        }
    }
}