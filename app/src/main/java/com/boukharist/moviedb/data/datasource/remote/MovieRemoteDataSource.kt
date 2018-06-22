package com.boukharist.moviedb.data.datasource.remote

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieRemoteDataSource {
    @GET("movie/popular")
    fun getLatestMovies(@Query("page") page: Int): Single<MovieListResponse>

    @GET("movie/{id}")
    fun getMovieById(@Path("id") id: String): Single<MovieResponse>

    //CONFIGURATIONS
    @GET("configuration")
    fun getConfiguration(): Single<ParentConfigResponse>
}