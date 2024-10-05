package com.faviansa.dicodingevent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.faviansa.dicodingevent.data.EventRepository
import com.faviansa.dicodingevent.data.local.entity.EventEntity
import com.faviansa.dicodingevent.ui.settings.SettingPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val eventRepository: EventRepository, private val preferences: SettingPreferences) : ViewModel() {
    fun getUpcomingEvents() = eventRepository.getUpcomingEvents()
    fun getFinishedEvents() = eventRepository.getFinishedEvents()
    fun searchEvents(active: Int, query: String) = eventRepository.searchEvents(active, query)
    fun getEventById(id: String) = eventRepository.getEventById(id)
    fun getFavoriteEvents() = eventRepository.getFavoriteEvents()
    fun setFavoriteEventState(event: EventEntity, state: Boolean){
        viewModelScope.launch {
            eventRepository.setFavoriteEvent(event, state)
        }
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            preferences.setThemeSetting(isDarkModeActive)
        }
    }
}