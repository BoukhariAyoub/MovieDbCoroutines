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
        fun from(movieEntity: MovieEntity, configEntity: ConfigEntity) = with(movieEntity) {
            MovieDetailItem(
                    id,
                    title,
                    configEntity.getBackDropPrefixUrl() + backdropPath,
                    configEntity.getPosterPrefixUrl() + posterPath,
                    overview,
                    tagline,
                    rating)
        }
    }
}
