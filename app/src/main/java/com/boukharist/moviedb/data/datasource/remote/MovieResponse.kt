package com.boukharist.moviedb.data.datasource.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieListResponse(
        @SerializedName("page")
        @Expose
        val page: Int?,
        @SerializedName("results")
        @Expose
        val results: List<MovieResponse>?)

data class MovieResponse(
        @SerializedName("id")
        @Expose val id: String?,
        @SerializedName("title")
        @Expose val title: String?,
        @SerializedName("backdrop_path")
        @Expose val backdropPath: String?,
        @SerializedName("poster_path")
        @Expose val posterPath: String?,
        @SerializedName("overview")
        @Expose val overview: String?,
        @SerializedName("tagline")
        @Expose val tagline: String?,
        @SerializedName("vote_average")
        @Expose val rating: String?)