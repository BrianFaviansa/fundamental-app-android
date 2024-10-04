package com.faviansa.dicodingevent.di

import android.content.Context
import com.faviansa.dicodingevent.data.EventRepository
import com.faviansa.dicodingevent.data.local.room.EventDatabase
import com.faviansa.dicodingevent.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        return EventRepository.getInstance(apiService, dao)
    }
    fun provideWorkManager(context: Context): androidx.work.WorkManager {
        return androidx.work.WorkManager.getInstance(context)
    }
}