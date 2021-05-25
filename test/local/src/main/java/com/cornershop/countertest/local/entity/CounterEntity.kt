package com.cornershop.countertest.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CounterEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val count: Int
)
