package com.example.notizone.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notification: Notification)

    @Delete
    suspend fun delete(notification: Notification)

    @Query("SELECT * from Notification")
    fun getAllNotifications(): Flow<List<Notification>>
}