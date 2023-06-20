package com.example.notizone.database

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Channel")
data class Channel(
    @PrimaryKey val id: String,
    val name: String,
    val semi_blocked: Boolean
)