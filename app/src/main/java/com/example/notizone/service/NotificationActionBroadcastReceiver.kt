package com.example.notizone.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.notizone.database.Channel
import com.example.notizone.database.NotiDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotificationActionBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "PUT_UNDER_PROBATION")
            semiBlockNotification(context, intent)
        if (intent?.action == "RELEASE_FROM_PROBATION")
            releaseNotification(context, intent)
    }

    private fun semiBlockNotification(context: Context?, intent: Intent) {
        val channelId = intent.getStringExtra("channel_id") ?: "default"
        val channelName = intent.getStringExtra("channel_name") ?: "default"
        val dao = NotiDatabase.getDatabase(context!!).channelDao()
        GlobalScope.launch {
            dao.insert(Channel(channelId, channelName, true))
        }
    }

    private fun releaseNotification(context: Context?, intent: Intent) {
        val channelId = intent.getStringExtra("channel_id") ?: "default"
        val dao = NotiDatabase.getDatabase(context!!).channelDao()
        GlobalScope.launch {
            dao.updateChannelSemiBlockedFlag(channelId, false)
        }
    }
}