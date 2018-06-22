package com.boukharist.moviedb.view.main.list

import com.boukharist.moviedb.data.datasource.local.movie.MovieEntity

data class MovieListItem(val id: String, val name: String, val pictureUrl: String?) {
    companion object {
        fun from(posterPrefix: String, source: MovieEntity) = MovieListItem(
                source.id,
                source.title,
                posterPrefix + source.posterPath
        )
    }
}