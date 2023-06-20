package com.example.notizone.database

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notification")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val package_name: String,
    val app_name: String,
    val title: String,
    val text: String
)