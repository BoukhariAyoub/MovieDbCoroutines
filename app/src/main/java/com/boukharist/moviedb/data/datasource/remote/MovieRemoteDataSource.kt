package com.boukharist.moviedb.data.datasource.remote

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieRemoteDataSource {
    @GET("movie/popular")
    fun getLatestMovies(@Query("page") page: Int): Deferred<MovieListResponse>

    @GET("movie/{id}")
    fun getMovieById(@Path("id") id: String): Deferred<MovieResponse>

    //CONFIGURATIONS
    @GET("configuration")
    fun getConfiguration(): Deferred<ParentConfigResponse>
}