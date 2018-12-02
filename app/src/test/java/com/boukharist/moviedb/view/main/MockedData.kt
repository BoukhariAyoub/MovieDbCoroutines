package com.boukharist.moviedb.view.main

import com.boukharist.moviedb.data.datasource.local.config.ConfigEntity
import com.boukharist.moviedb.data.datasource.local.movie.MovieEntity
import com.boukharist.moviedb.view.main.list.MovieListItem


val configEntity = ConfigEntity(
        1L,
        "BaseURL",
        "backdropSize",
        "posterSize")

val topMovies = listOf(
        MovieEntity(
                "ID",
                "title",
                "backPath",
                "posterPath",
                "overview",
                "tagline",
                "2.3"
        )
)

val movieItems = listOf(
        MovieListItem(
                "ID",
                "Name",
                "PictureUrl"
        )
)