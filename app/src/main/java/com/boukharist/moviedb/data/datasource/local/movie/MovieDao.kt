package com.boukharist.moviedb.data.datasource.local.movie

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(movieEntity: MovieEntity)

    @Query("SELECT * FROM movie")
    fun findAll(): Single<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE id=:id")
    fun findById(id: String): Maybe<MovieEntity>
}