package com.boukharist.moviedb.data.datasource.local.movie

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.boukharist.moviedb.data.datasource.remote.MovieResponse

@Entity(tableName = "movie")
data class MovieEntity(
        @PrimaryKey
        val id: String,
        val title: String,
        val backdropPath: String?,
        val posterPath: String?,
        val overview: String?,
        val tagline: String?,
        val rating: String?) {

    companion object {
        fun from(response: MovieResponse) = MovieEntity(
                response.id ?: "NO_ID",
                response.title ?: "NO_TITLE",
                response.backdropPath,
                response.posterPath,
                response.overview,
                response.tagline,
                response.rating)
    }
}