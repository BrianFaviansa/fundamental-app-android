package com.faviansa.dicodingevent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faviansa.dicodingevent.data.repository.EventRepository
import com.faviansa.dicodingevent.data.response.ListEventsItem

class MainViewModel : ViewModel() {
    private val repository = EventRepository()

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>?>()
    val upcomingEvents: LiveData<List<ListEventsItem>?> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>?>()
    val finishedEvents: LiveData<List<ListEventsItem>?> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private var errorHandled = false


    fun getAllUpcomingEvents() {
        _isLoading.value = true
        repository.getAllUpcomingEvents { listEvents, error ->
            _isLoading.value = false
            if (listEvents != null) {
                _upcomingEvents.value = listEvents
            } else {
                _error.value = error
                resetErrorHandled()
            }
        }
    }

    fun getAllFinishedEvents() {
        _isLoading.value = true
        repository.getAllFinishedEvents { listEvents, error ->
            _isLoading.value = false
            if (listEvents != null) {
                _finishedEvents.value = listEvents
            } else {
                _error.value = error
                resetErrorHandled()
            }
        }
    }

    fun searchUpcomingEvents(query: String) {
        _isLoading.value = true
        repository.searchUpcomingEvents(query) { listEvents, error ->
            _isLoading.value = false
            if (listEvents != null) {
                _upcomingEvents.value = listEvents
            } else {
                _error.value = error
                resetErrorHandled()
            }
        }
    }

    fun searchFinishedEvents(query: String) {
        _isLoading.value = true
        repository.searchFinishedEvents(query) { listEvents, error ->
            _isLoading.value = false
            if (listEvents != null) {
                _finishedEvents.value = listEvents
            } else {
                _error.value = error
                resetErrorHandled()
            }
        }
    }

    fun getHomeEvents() {
        _isLoading.value = true
        repository.getHomeUpcomingEvents { listEvents, error ->
            if (listEvents != null) {
                _upcomingEvents.value = listEvents
            } else {
                _error.value = error
                resetErrorHandled()
            }
        }

        repository.getHomeFinishedEvents { listEvents, error ->
            if (listEvents != null) {
                _finishedEvents.value = listEvents
            } else {
                _error.value = error
                resetErrorHandled()
            }
        }
    }


    fun errorHandled() {
        errorHandled = true
        _error.value = null
    }

    fun isErrorHandled(): Boolean {
        return errorHandled
    }

    private fun resetErrorHandled() {
        errorHandled = false
    }

}