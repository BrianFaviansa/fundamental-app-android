package com.faviansa.dicodingevent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.faviansa.dicodingevent.data.EventRepository
import com.faviansa.dicodingevent.data.local.entity.EventEntity
import com.faviansa.dicodingevent.ui.reminder.MyReminderWorker
import com.faviansa.dicodingevent.ui.settings.SettingPreferences
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val eventRepository: EventRepository,
    private val preferences: SettingPreferences,
    private val workManager: WorkManager
) : ViewModel() {
    private val _isReminderActive = MutableLiveData<Boolean>()
    val isReminderActive: LiveData<Boolean> = _isReminderActive

    fun getUpcomingEvents() = eventRepository.getUpcomingEvents()
    fun getFinishedEvents() = eventRepository.getFinishedEvents()
    fun searchEvents(active: Int, query: String) = eventRepository.searchEvents(active, query)
    fun getEventById(id: String) = eventRepository.getEventById(id)
    fun getFavoriteEvents() = eventRepository.getFavoriteEvents()
    fun setFavoriteEventState(event: EventEntity, state: Boolean) {
        viewModelScope.launch {
            eventRepository.setFavoriteEvent(event, state)
        }
    }

    init {
        viewModelScope.launch {
            preferences.getReminderSetting()
                .distinctUntilChanged()
                .collect {
                    _isReminderActive.value = it
                }
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

    fun setReminderState(isActive: Boolean) {
        viewModelScope.launch {
            preferences.setReminderSetting(isActive)
            _isReminderActive.value = isActive
            updateReminderSchedule(isActive)
        }
    }

    private fun updateReminderSchedule(isActive: Boolean) {
        viewModelScope.launch {
            val reminderRequest = PeriodicWorkRequestBuilder<MyReminderWorker>(1, TimeUnit.DAYS)
                .addTag(MyReminderWorker.WORK_NAME)
                .build()
            if (isActive) {
                workManager.enqueueUniquePeriodicWork(
                    MyReminderWorker.WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    reminderRequest
                )
            } else {
                workManager.cancelAllWorkByTag(MyReminderWorker.WORK_NAME)
            }
        }
    }

}