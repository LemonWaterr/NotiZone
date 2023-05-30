package com.example.notizone.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room
import com.example.notizone.database.Channel
import com.example.notizone.database.NotiDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotificationActionBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "PUT_UNDER_PROBATION")
            putNotificationUnderProbation(context, intent)
        if (intent?.action == "RELEASE_FROM_PROBATION")
            releaseNotificationFromProbation(context, intent)
    }

    private fun putNotificationUnderProbation(context: Context?, intent: Intent) {
        val channelId = intent.getStringExtra("channel_id") ?: "default"
        val channelName = intent.getStringExtra("channel_name") ?: "default"
        val dao = NotiDatabase.getDatabase(context!!).channelDao()
        GlobalScope.launch {
            dao.insert(Channel(channelId, channelName, true))
        }
    }

    private fun releaseNotificationFromProbation(context: Context?, intent: Intent) {
        val channelId = intent.getStringExtra("channel_id") ?: "default"
        val dao = NotiDatabase.getDatabase(context!!).channelDao()
        GlobalScope.launch {
            dao.updateChannelProbationFlag(channelId, false)
        }
    }
}