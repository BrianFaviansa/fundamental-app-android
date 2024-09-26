package com.faviansa.dicodingevent.data.retrofit

import com.faviansa.dicodingevent.data.response.EventResponse
import com.faviansa.dicodingevent.data.response.SpecificEventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events?active=1")
    fun getUpcomingEvents(): Call<EventResponse>

    @GET("events?active=0")
    fun getFinishedEvents(): Call<EventResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: String
    ): Call<SpecificEventResponse>

    @GET("events")
    fun searchEvent(
        @Query("active") active: Int,
        @Query("q") query: String
    ): Call<EventResponse>
}