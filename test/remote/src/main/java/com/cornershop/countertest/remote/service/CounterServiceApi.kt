package com.cornershop.countertest.remote.service

import com.cornershop.countertest.remote.dto.CounterDto
import com.cornershop.countertest.remote.dto.CounterRequestDto
import com.cornershop.countertest.remote.dto.CreateCounterRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface CounterServiceApi {

    @GET("counters")
    suspend fun getCounters(): List<CounterDto>

    @POST("counter")
    suspend fun addCounter(@Body request: CreateCounterRequestDto): List<CounterDto>

    @POST("counter/inc")
    suspend fun increment(@Body request: CounterRequestDto): List<CounterDto>

    @POST("counter/dec")
    suspend fun decrement(@Body request: CounterRequestDto): List<CounterDto>

    @DELETE("counter")
    suspend fun removeCounter(@Body request: CounterRequestDto): List<CounterDto>
}