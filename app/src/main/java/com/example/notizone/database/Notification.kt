package com.example.notizone.database

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notification")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String
)