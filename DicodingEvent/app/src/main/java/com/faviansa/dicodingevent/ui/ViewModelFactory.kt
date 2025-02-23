package com.faviansa.dicodingevent.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.faviansa.dicodingevent.data.EventRepository
import com.faviansa.dicodingevent.di.Injection
import com.faviansa.dicodingevent.ui.settings.SettingPreferences

class ViewModelFactory private constructor(
    private val eventRepository: EventRepository,
    private val preferences: SettingPreferences,
    private val workManager: WorkManager,
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(eventRepository, preferences, workManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, preferences: SettingPreferences): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    preferences,
                    Injection.provideWorkManager(context)
                )
                    .also { instance = it }
            }
    }
}