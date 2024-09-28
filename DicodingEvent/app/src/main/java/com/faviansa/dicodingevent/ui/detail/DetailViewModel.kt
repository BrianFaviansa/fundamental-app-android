package com.faviansa.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faviansa.dicodingevent.data.repository.EventRepository
import com.faviansa.dicodingevent.data.response.ListEventsItem

class DetailViewModel : ViewModel() {
    private val repository = EventRepository()

    private val _event = MutableLiveData<ListEventsItem?>()
    val event: LiveData<ListEventsItem?> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Error?>()

    fun getDetailEvent(eventId: String) {
        _isLoading.value = true
        repository.getEventById(eventId) { event ->
            _isLoading.value = false
            if (event != null) {
                _event.value = event
            } else {
                _error.value = Error("Error: Failed to get event")
            }
        }
    }

}