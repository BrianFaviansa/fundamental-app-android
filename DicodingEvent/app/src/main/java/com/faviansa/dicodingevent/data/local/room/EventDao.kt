package com.faviansa.dicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.faviansa.dicodingevent.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE isActive = 1 ORDER BY date(beginTime) ASC")
    suspend fun getUpcomingEvents(): List<EventEntity>

    @Query("SELECT * FROM events WHERE isActive = 0 ORDER BY date(beginTime) DESC")
    suspend fun getFinishedEvents(): List<EventEntity>

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    suspend fun getEventById(id: String): EventEntity?

    @Query("SELECT * FROM events WHERE isFavorite = 1")
    fun getFavoriteEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM events WHERE isActive = :active AND name LIKE '%' || :query || '%' ORDER BY date(beginTime) ASC")
    suspend fun searchEvents(active: Int, query: String): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("DELETE FROM events WHERE isActive = 1")
    suspend fun deleteUpcomingEvents()

    @Query("DELETE FROM events WHERE isActive = 0")
    suspend fun deleteFinishedEvents()

    @Query("SELECT * FROM events WHERE isActive = 1 AND date(beginTime) >= :currentTime ORDER BY date(beginTime) ASC LIMIT 1")
    suspend fun getClosestActiveEvent(currentTime: Long): EventEntity?

    @Query("SELECT EXISTS(SELECT * FROM events WHERE id = :id AND isFavorite = 1)")
    suspend fun isEventFavorite(id: String): Boolean
}