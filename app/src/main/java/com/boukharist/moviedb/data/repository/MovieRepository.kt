package com.boukharist.moviedb.data.repository

import com.boukharist.moviedb.data.datasource.local.config.ConfigDao
import com.boukharist.moviedb.data.datasource.local.config.ConfigEntity
import com.boukharist.moviedb.data.datasource.local.movie.MovieDao
import com.boukharist.moviedb.data.datasource.local.movie.MovieEntity
import com.boukharist.moviedb.data.datasource.remote.MovieRemoteDataSource
import io.reactivex.Single

interface MovieRepository {
    fun insertMovie(movieEntity: MovieEntity)
    fun insertConfig(configEntity: ConfigEntity)
    fun getMovieById(id: String): Single<MovieEntity>
    fun getTopMovies(page: Int): Single<List<MovieEntity>>
    fun getConfig(): Single<ConfigEntity>
}

class MovieRepositoryImpl(private val remoteDataSource: MovieRemoteDataSource,
                          private val configDataSource: ConfigDao,
                          private val localDataSource: MovieDao) : MovieRepository {

    override fun insertConfig(configEntity: ConfigEntity) {
        return configDataSource.save(configEntity)
    }

    override fun getConfig(): Single<ConfigEntity> {
        return configDataSource.findConfig()
                .switchIfEmpty(remoteDataSource.getConfiguration()
                        .map { ConfigEntity.from(it.configResponse) }
                        .doOnSuccess { configDataSource.save(it) })
    }

    override fun insertMovie(movieEntity: MovieEntity) {
        return localDataSource.save(movieEntity)
    }

    override fun getMovieById(id: String): Single<MovieEntity> {
        return localDataSource.findById(id)
                .switchIfEmpty(remoteDataSource.getMovieById(id)
                        .map { MovieEntity.from(it) }
                        .doOnSuccess { localDataSource.save(it) })
    }

    override fun getTopMovies(page: Int): Single<List<MovieEntity>> {
        return remoteDataSource.getLatestMovies(page)
                .filter { it.results != null }
                .map { it.results }
                .flattenAsObservable { it }
                .map { MovieEntity.from(it) }
                .toList()
    }


}