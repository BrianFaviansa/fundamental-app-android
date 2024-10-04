package com.faviansa.dicodingevent.ui

import androidx.lifecycle.ViewModel
import com.faviansa.dicodingevent.data.EventRepository
import com.faviansa.dicodingevent.data.local.entity.EventEntity

class MainViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getUpcomingEvents() = eventRepository.getUpcomingEvents()
    fun getFinishedEvents() = eventRepository.getFinishedEvents()
    fun searchEvents(active: Int, query: String) = eventRepository.searchEvents(active, query)
    fun getEventById(id: String) = eventRepository.getEventById(id)
    fun getFavoriteEvents() = eventRepository.getFavoriteEvents()
    suspend fun setFavoriteEvent(event: EventEntity, state: Boolean) = eventRepository.setFavoriteEvent(event, state)
}