package com.example.notizone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Channel::class], version = 1)
abstract class NotiDatabase : RoomDatabase() {

    abstract fun channelDao(): ChannelDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NotiDatabase? = null

        fun getDatabase(context: Context): NotiDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotiDatabase::class.java,
                    "noti_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}