package com.boukharist.moviedb.di

import android.arch.persistence.room.Room
import com.boukharist.moviedb.data.datasource.local.MoviesDataBase
import com.boukharist.moviedb.data.repository.MovieRepository
import com.boukharist.moviedb.data.repository.MovieRepositoryImpl
import com.boukharist.moviedb.util.AppScheduleProvider
import com.boukharist.moviedb.util.SchedulerProvider
import com.boukharist.moviedb.view.detail.DetailViewModel
import com.boukharist.moviedb.view.main.MainViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext

val appModule = applicationContext {

    viewModel { MainViewModel(get(), get()) }
    viewModel { DetailViewModel(get(), get()) }

    //Repository
    bean { MovieRepositoryImpl(get(), get(),get()) as MovieRepository }

    // Schedulers
    bean { AppScheduleProvider() as SchedulerProvider }

    // Database
    bean {
        Room.databaseBuilder(androidApplication(), MoviesDataBase::class.java, "movie-db")
                .fallbackToDestructiveMigration()
                .build()
    }

    bean { get<MoviesDataBase>().movieDao() }
    bean { get<MoviesDataBase>().configDao() }
}

val movieApp = listOf(appModule, remoteDatasourceModule)
