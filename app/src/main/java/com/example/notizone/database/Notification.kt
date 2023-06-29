package com.example.notizone.database

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notification")
data class Notification(
    @PrimaryKey val id: Int,
    val package_name: String,
    val app_name: String,
    val title: String,
    val text: String,
    val text_big: String,
    val post_time_unix_miliseconds: Long,
    val channel_id: String
)