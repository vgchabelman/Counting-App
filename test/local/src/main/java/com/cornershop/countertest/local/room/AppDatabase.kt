package com.cornershop.countertest.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cornershop.countertest.local.dao.CounterDao
import com.cornershop.countertest.local.entity.CounterEntity

@Database(
    entities = [CounterEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun counterDao(): CounterDao
}