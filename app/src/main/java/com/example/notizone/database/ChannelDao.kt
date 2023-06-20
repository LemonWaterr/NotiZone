package com.example.notizone.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(channel: Channel)

    @Delete
    suspend fun delete(channel: Channel)

    @Query("UPDATE Channel SET semi_blocked = :flag WHERE id = :id")
    suspend fun updateChannelSemiBlockedFlag(id: String, flag: Boolean)

    @Query("SELECT * from Channel where id = :id LIMIT 1")
    suspend fun getChannel(id: String): Channel

    @Query("SELECT semi_blocked from Channel where id = :id LIMIT 1")
    suspend fun getChannelSemiBlockedFlag(id: String): Boolean?

    @Query("SELECT * from Channel")
    fun getAllChannels(): Flow<List<Channel>>
}