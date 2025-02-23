package com.faviansa.dicodingevent.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.faviansa.dicodingevent.data.local.entity.EventEntity
import com.faviansa.dicodingevent.data.local.entity.FavoriteEntity
import com.faviansa.dicodingevent.data.local.room.EventDao
import com.faviansa.dicodingevent.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
) {

    fun getUpcomingEvents(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val localData = eventDao.getUpcomingEvents()
            if (localData.isNotEmpty()) {
                emit(Result.Success(localData))
            } else {
                try {
                    val response = apiService.getUpcomingEvents()
                    val events = response.listEvents
                    val eventList = events.map { event ->
                        EventEntity(
                            event.id.toString(),
                            event.name.toString(),
                            event.summary.toString(),
                            event.description.toString(),
                            event.imageLogo,
                            event.mediaCover,
                            event.category.toString(),
                            event.ownerName.toString(),
                            event.cityName.toString(),
                            event.quota!!.toInt(),
                            event.registrants!!.toInt(),
                            event.beginTime.toString(),
                            event.endTime.toString(),
                            event.link,
                            isActive = true,
                        )
                    }
                    eventDao.deleteUpcomingEvents()
                    eventDao.insertEvents(eventList)
                    emit(Result.Success(eventList))
                } catch (e: Exception) {
                    emit(Result.Error("Network connection error and no local data found"))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message.toString()}"))
        }
    }

    fun getFinishedEvents(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val localData = eventDao.getFinishedEvents()
            if (localData.isNotEmpty()) {
                emit(Result.Success(localData))
            } else {
                try {
                    val response = apiService.getFinishedEvents()
                    val events = response.listEvents
                    val eventList = events.map { event ->
                        EventEntity(
                            event.id.toString(),
                            event.name.toString(),
                            event.summary.toString(),
                            event.description.toString(),
                            event.imageLogo,
                            event.mediaCover,
                            event.category.toString(),
                            event.ownerName.toString(),
                            event.cityName.toString(),
                            event.quota!!.toInt(),
                            event.registrants!!.toInt(),
                            event.beginTime.toString(),
                            event.endTime.toString(),
                            event.link,
                            isActive = false,
                        )
                    }
                    eventDao.deleteFinishedEvents()
                    eventDao.insertEvents(eventList)
                    emit(Result.Success(eventList))
                } catch (e: Exception) {
                    emit(Result.Error("Network connection error and no local data found"))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message.toString()}"))
        }
    }

    fun getEventById(id: String): LiveData<Result<EventEntity>> = liveData {
        emit(Result.Loading)
        try {
            val eventLocalData = eventDao.getEventById(id)
            if (eventLocalData != null) {
                emit(Result.Success(eventLocalData))
            } else {
                emit(Result.Error("Oops, event not found"))
            }
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message.toString()}"))
        }
    }

    fun searchEvents(active: Int, query: String): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val eventLocalData = eventDao.searchEvents(active, query)
            emitSource(eventLocalData.map { eventList ->
                if (eventList.isNotEmpty()) {
                    Result.Success(eventList)
                } else {
                    Result.Error("No event found")
                }
            })
        } catch (e: Exception) {
            emit(Result.Error("An error occurred: ${e.message.toString()}"))
        }
    }

    suspend fun addToFavorites(eventId: String) {
        eventDao.insertFavorite(FavoriteEntity(eventId))
    }

    suspend fun removeFromFavorites(eventId: String) {
        eventDao.deleteFavorite(FavoriteEntity(eventId))
    }

    fun isEventFavorite(eventId: String): Flow<Boolean> {
        return eventDao.isEventFavorite(eventId)
    }

    fun getFavoriteEvents(): Flow<List<EventEntity>> {
        return eventDao.getFavoriteEvents()
    }

    suspend fun getClosestEvent(): EventEntity? {
        val closestEvent = eventDao.getClosestActiveEvent(System.currentTimeMillis())
        return closestEvent
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao)
            }.also { instance = it }
    }
}