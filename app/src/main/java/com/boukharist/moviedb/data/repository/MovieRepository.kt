package com.boukharist.moviedb.data.repository

import com.boukharist.moviedb.data.datasource.local.config.ConfigDao
import com.boukharist.moviedb.data.datasource.local.config.ConfigEntity
import com.boukharist.moviedb.data.datasource.local.movie.MovieDao
import com.boukharist.moviedb.data.datasource.local.movie.MovieEntity
import com.boukharist.moviedb.data.datasource.remote.MovieRemoteDataSource

interface MovieRepository {
    suspend fun insertMovie(movieEntity: MovieEntity)
    suspend fun getMovieById(id: String): MovieEntity
    suspend fun getTopMovies(page: Int): List<MovieEntity>

    suspend fun insertConfig(configEntity: ConfigEntity)
    suspend fun getConfig(): ConfigEntity
}

class MovieRepositoryImpl(private val remoteDataSource: MovieRemoteDataSource,
                          private val configDataSource: ConfigDao,
                          private val localDataSource: MovieDao) : MovieRepository {

    override suspend fun insertConfig(configEntity: ConfigEntity) {
        return configDataSource.save(configEntity)
    }

    override suspend fun getConfig(): ConfigEntity {
        return configDataSource.findConfig() ?: remoteDataSource.getConfiguration().await()
                .let { ConfigEntity.from(it.configResponse) }
                .also { configDataSource.save(it) }
    }

    override suspend fun insertMovie(movieEntity: MovieEntity) {
        return localDataSource.save(movieEntity)
    }

    override suspend fun getMovieById(id: String): MovieEntity {
        return localDataSource.findById(id) ?: remoteDataSource.getMovieById(id).await()
                .let { MovieEntity.from(it) }
                .also { localDataSource.save(it) }
    }

    override suspend fun getTopMovies(page: Int): List<MovieEntity> {
        return remoteDataSource.getLatestMovies(page).await()
                .takeIf { movieListResponse -> movieListResponse.results != null }
                ?.let { it.results }
                ?.map { MovieEntity.from(it) }
                ?: emptyList()
    }
}