package com.boukharist.moviedb.view.detail

import com.boukharist.moviedb.data.datasource.local.config.ConfigEntity
import com.boukharist.moviedb.data.datasource.local.movie.MovieEntity

data class MovieDetailItem(val id: String,
                           val title: String,
                           val backdropPath: String?,
                           val posterPath: String?,
                           val overview: String?,
                           val tagLine: String?,
                           val rating: String?) {
    companion object {
        fun from(movieEntity: MovieEntity, configEntity: ConfigEntity) = MovieDetailItem(
                movieEntity.id,
                movieEntity.title,
                configEntity.getBackDropPrefixUrl() + movieEntity.backdropPath,
                configEntity.getPosterPrefixUrl() + movieEntity.posterPath,
                movieEntity.overview,
                movieEntity.tagline,
                movieEntity.rating
        )
    }
}
