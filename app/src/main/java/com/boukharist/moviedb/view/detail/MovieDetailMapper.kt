package com.boukharist.moviedb.view.detail

import com.boukharist.moviedb.data.datasource.local.config.ConfigEntity
import com.boukharist.moviedb.data.datasource.local.movie.MovieEntity

object MovieDetailMapper : (ConfigEntity, MovieEntity) -> MovieDetailItem {
    override fun invoke(config: ConfigEntity, movie: MovieEntity): MovieDetailItem {
        return MovieDetailItem.from(movie, config)
    }
}