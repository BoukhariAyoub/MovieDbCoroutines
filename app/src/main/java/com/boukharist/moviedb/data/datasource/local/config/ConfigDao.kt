package com.boukharist.moviedb.data.datasource.local.config

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import kotlinx.coroutines.Deferred

@Dao
interface ConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(configEntity: ConfigEntity)

    @Query("SELECT * FROM config LIMIT 1")
    fun findConfig(): ConfigEntity?
}