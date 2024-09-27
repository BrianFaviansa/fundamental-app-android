package com.faviansa.dicodingevent.data.repository

import com.faviansa.dicodingevent.data.response.DetailEventResponse
import com.faviansa.dicodingevent.data.response.EventResponse
import com.faviansa.dicodingevent.data.response.ListEventsItem
import com.faviansa.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class EventRepository {
    fun getHomeUpcomingEvents(callback: (List<ListEventsItem>?, String?) -> Unit) {
        val client = ApiConfig.getApiService().getUpcomingEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.listEvents?.take(5), null)
                } else {
                    callback(null, "Failed to load upcoming events. Please try again later.")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                val errorMessage = when (t) {
                    is IOException -> "No internet connection. Please check your network settings."
                    else -> "An unexpected error occurred. Please try again."
                }
                callback(null, errorMessage)
            }
        })
    }

    fun getHomeFinishedEvents(callback: (List<ListEventsItem>?, String?) -> Unit) {
        val client = ApiConfig.getApiService().getFinishedEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.listEvents?.take(5), null)
                } else {
                    callback(null, "Failed to load finished events. Please try again later.")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                val errorMessage = when (t) {
                    is IOException -> "No internet connection. Please check your network settings."
                    else -> "An unexpected error occurred. Please try again."
                }
                callback(null, errorMessage)
            }
        })
    }

    fun getAllUpcomingEvents(callback: (List<ListEventsItem>?, String?) -> Unit) {
        val client = ApiConfig.getApiService().getUpcomingEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.listEvents, null)
                } else {
                    callback(null, "Failed to load upcoming events. Please try again later.")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                val errorMessage = when (t) {
                    is IOException -> "No internet connection. Please check your network settings."
                    else -> "An unexpected error occurred. Please try again."
                }
                callback(null, errorMessage)
            }

        })
    }

    fun getAllFinishedEvents(callback: (List<ListEventsItem>?, String?) -> Unit) {
        val client = ApiConfig.getApiService().getFinishedEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.listEvents, null)
                } else {
                    callback(null, "Failed to load finished events. Please try again later.")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                val errorMessage = when (t) {
                    is IOException -> "No internet connection. Please check your network settings."
                    else -> "An unexpected error occurred. Please try again."
                }
                callback(null, errorMessage)
            }
        })
    }

    fun getEventById(eventId: String, callback: (ListEventsItem?) -> Unit) {
        val client = ApiConfig.getApiService().getDetailEvent(eventId)
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body()?.event)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun searchUpcomingEvents(query: String, callback: (List<ListEventsItem>?, String?) -> Unit) {
        val client = ApiConfig.getApiService().searchEvents(1, query)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.listEvents, null)
                } else {
                    callback(null, "Failed to search upcoming events. Please try again later.")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                val errorMessage = when (t) {
                    is IOException -> "No internet connection. Please check your network settings."
                    else -> "An unexpected error occurred. Please try again."
                }
                callback(null, errorMessage)
            }
        })
    }

    fun searchFinishedEvents(query: String, callback: (List<ListEventsItem>?, String?) -> Unit) {
        val client = ApiConfig.getApiService().searchEvents(0, query)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.listEvents, null)
                } else {
                    callback(null, "Failed to search finished events. Please try again later.")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                val errorMessage = when (t) {
                    is IOException -> "No internet connection. Please check your network settings."
                    else -> "An unexpected error occurred. Please try again."
                }
                callback(null, errorMessage)
            }
        })
    }


}