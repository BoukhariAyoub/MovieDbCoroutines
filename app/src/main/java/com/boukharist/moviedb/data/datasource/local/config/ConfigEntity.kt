package com.boukharist.moviedb.data.datasource.local.config

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.boukharist.moviedb.data.datasource.remote.ConfigResponse

@Entity(tableName = "config")
data class ConfigEntity(
        @PrimaryKey
        val id: Long,
        val baseUrl: String,
        val backdropPreferredSize: String,
        val posterPreferredSize: String) {

    fun getBackDropPrefixUrl() = baseUrl + backdropPreferredSize
    fun getPosterPrefixUrl() = baseUrl + posterPreferredSize

    companion object {
        private const val DEFAULT_SIZE = "original"

        fun from(response: ConfigResponse): ConfigEntity {
            val preferredBackdrop = response.backdrop_sizes?.dropLast(1)?.lastOrNull()
                    ?: DEFAULT_SIZE
            val preferredPosterSize = response.poster_sizes?.dropLast(1)?.lastOrNull()
                    ?: DEFAULT_SIZE



            return ConfigEntity(
                    1,//we only want one unique config Entity
                    response.base_url!!,
                    preferredBackdrop,
                    preferredPosterSize
            )
        }
    }
}