package com.example.notizone.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(channel: Channel)

    @Delete
    suspend fun delete(channel: Channel)

    @Query("UPDATE Channel SET under_probation = :flag WHERE id = :id")
    suspend fun updateChannelProbationFlag(id: String, flag: Boolean)

    @Query("SELECT * from Channel where id = :id LIMIT 1")
    suspend fun getChannel(id: String): Channel

    @Query("SELECT under_probation from Channel where id = :id LIMIT 1")
    suspend fun getChannelProbationFlag(id: String): Boolean?

    @Query("SELECT * from Channel")
    fun getAllChannels(): Flow<List<Channel>>
}