package com.faviansa.dicodingevent.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faviansa.dicodingevent.data.response.ListEventsItem

class DetailViewModel : ViewModel() {
    private val _event = MutableLiveData<ListEventsItem>()
    val event: MutableLiveData<ListEventsItem> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Error?>()
    val error: MutableLiveData<Error?> = _error

    fun getDetailEvent(id: Int) {
        _isLoading.value = true

    }
}