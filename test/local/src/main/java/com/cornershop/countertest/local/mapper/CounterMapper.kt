package com.cornershop.countertest.local.mapper

import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.local.entity.CounterEntity

fun CounterEntity.toCounter(): Counter =
    Counter(
        id = id,
        title = title,
        count = count
    )

fun Counter.toEntity(): CounterEntity =
    CounterEntity(
        id = id,
        title = title,
        count = count
    )