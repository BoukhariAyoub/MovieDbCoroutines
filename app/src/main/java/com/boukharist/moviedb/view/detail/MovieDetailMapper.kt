package com.boukharist.moviedb.view.detail

import com.boukharist.moviedb.data.datasource.local.config.ConfigEntity
import com.boukharist.moviedb.data.datasource.local.movie.MovieEntity
import io.reactivex.functions.BiFunction

class MovieDetailMapper : BiFunction<ConfigEntity, MovieEntity, MovieDetailItem> {
    override fun apply(config: ConfigEntity, item: MovieEntity): MovieDetailItem {
        return MovieDetailItem.from(item, config)
    }
}