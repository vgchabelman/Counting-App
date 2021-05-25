package com.cornershop.countertest.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cornershop.countertest.local.entity.CounterEntity

@Dao
interface CounterDao {

    @Query("SELECT * FROM CounterEntity WHERE id = :id")
    suspend fun getCounter(id: String): CounterEntity

    @Query("SELECT * FROM CounterEntity")
    suspend fun getAllCounters(): List<CounterEntity>

    @Insert
    suspend fun create(counterEntity: CounterEntity)

    @Delete
    suspend fun delete(counterEntity: CounterEntity)

    @Update
    suspend fun update(counterEntity: CounterEntity)
}