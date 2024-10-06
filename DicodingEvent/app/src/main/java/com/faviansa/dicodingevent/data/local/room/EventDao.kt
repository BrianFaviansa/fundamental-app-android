package com.faviansa.dicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.faviansa.dicodingevent.data.local.entity.EventEntity
import com.faviansa.dicodingevent.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE isActive = 1 ORDER BY date(beginTime) ASC")
    suspend fun getUpcomingEvents(): List<EventEntity>

    @Query("SELECT * FROM events WHERE isActive = 0 ORDER BY date(beginTime) DESC")
    suspend fun getFinishedEvents(): List<EventEntity>

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    suspend fun getEventById(id: String): EventEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM events WHERE isActive = 1")
    suspend fun deleteUpcomingEvents()

    @Query("DELETE FROM events WHERE isActive = 0")
    suspend fun deleteFinishedEvents()

    @Query("SELECT * FROM events WHERE isActive = 1 AND date(beginTime) >= :currentTime ORDER BY date(beginTime) ASC LIMIT 1")
    suspend fun getClosestActiveEvent(currentTime: Long): EventEntity?

    @Query("SELECT * FROM events WHERE isActive = :active AND name LIKE '%' || :query || '%' ORDER BY date(beginTime) ASC")
    fun searchEvents(active: Int, query: String): LiveData<List<EventEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE eventId = :eventId)")
    fun isEventFavorite(eventId: String): Flow<Boolean>

    @Query("SELECT * FROM events WHERE id IN (SELECT eventId FROM favorites)")
    fun getFavoriteEvents(): Flow<List<EventEntity>>
}