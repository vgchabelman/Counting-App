package com.cornershop.countertest.remote.mapper

import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.remote.dto.CounterDto

fun CounterDto.toCounter(): Counter =
    Counter(
        id = id,
        title = title,
        count = count
    )