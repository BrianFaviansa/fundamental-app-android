package com.faviansa.dicodingevent.data.remote.retrofit

import com.faviansa.dicodingevent.data.remote.response.EventResponse
import retrofit2.http.GET

interface ApiService {
    @GET("events?active=1")
    suspend fun getUpcomingEvents(): EventResponse

    @GET("events?active=0")
    suspend fun getFinishedEvents(): EventResponse
}