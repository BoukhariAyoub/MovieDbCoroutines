package com.boukharist.moviedb.di

import android.content.Context
import com.boukharist.moviedb.data.datasource.remote.MovieRemoteDataSource
import com.boukharist.moviedb.di.DatasourceProperties.SERVER_URL
import com.boukharist.moviedb.util.ApiKeyInterceptor
import com.boukharist.moviedb.util.ConnectivityInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Remote Web Service datasource
 */
val remoteDatasourceModule = applicationContext {
    bean { createOkHttpClient(get()) }
    bean { createWebService<MovieRemoteDataSource>(get(), getProperty(SERVER_URL)) }
}


object DatasourceProperties {
    const val SERVER_URL = "SERVER_URL"
}

fun createOkHttpClient(context: Context): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    val noConnectivityInterceptor = ConnectivityInterceptor(context)
    val apiKeyInterceptor = ApiKeyInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(noConnectivityInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}
