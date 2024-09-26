package com.faviansa.dicodingevent.ui.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faviansa.dicodingevent.data.response.EventResponse
import com.faviansa.dicodingevent.data.response.ListEventsItem
import com.faviansa.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {
    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: MutableLiveData<List<ListEventsItem>> = _upcomingEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Error?>()
    val error: MutableLiveData<Error?> = _error

    init {
        getUpcomingEvents()
    }

    fun getUpcomingEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUpcomingEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _upcomingEvents.value = response.body()?.listEvents ?: emptyList()
                } else {
                    _isLoading.value = false
                    _error.value = Error("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _error.value = Error("Failure: ${t.message}")
            }
        })
    }

}