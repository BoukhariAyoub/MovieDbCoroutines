package com.boukharist.moviedb.data.datasource.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ParentConfigResponse(
        @SerializedName("images")
        @Expose val configResponse: ConfigResponse)


data class ConfigResponse(
        @SerializedName("base_url")
        @Expose val base_url: String?,
        @SerializedName("backdrop_sizes")
        @Expose var backdrop_sizes: List<String>? = emptyList(),
        @SerializedName("poster_sizes")
        @Expose var poster_sizes: List<String>? = emptyList())