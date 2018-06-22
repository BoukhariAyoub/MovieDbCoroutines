package com.boukharist.moviedb.data.datasource.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.boukharist.moviedb.data.datasource.local.config.ConfigDao
import com.boukharist.moviedb.data.datasource.local.config.ConfigEntity
import com.boukharist.moviedb.data.datasource.local.movie.MovieDao
import com.boukharist.moviedb.data.datasource.local.movie.MovieEntity

@Database(entities = [MovieEntity::class,ConfigEntity::class], version = 3)
abstract class MoviesDataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun configDao(): ConfigDao
}