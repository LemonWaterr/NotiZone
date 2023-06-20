package com.example.notizone.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notizone.R
import com.example.notizone.database.NotiDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class NotificationListener : NotificationListenerService() {

    private lateinit var notificationManagerCompat: NotificationManagerCompat

    override fun onCreate() {
        super.onCreate()
        notificationManagerCompat = NotificationManagerCompat.from(this)
        prepareSemiBlockChannel()
    }

    override fun onNotificationPosted(statusBarNotification: StatusBarNotification) {
        super.onNotificationPosted(statusBarNotification)

        if(isIgnoredNotificationType((statusBarNotification)))
            return

        val notification = statusBarNotification.notification

        notification.extras.putBoolean("repostedByNotiBoy", true)
        val notificationBuilderRecovered = NotificationCompat.Builder(this, notification)
            .setAutoCancel(true)

        if(checkIfSemiBlocked(notification)) {
            notificationBuilderRecovered
                .addAction(makeReleaseAction(notification))
                .setChannelId(_semiBlockChannelId)
        }else{
            notificationBuilderRecovered
                .addAction(makeSemiBlockAction(notification))
        }

        notificationManagerCompat.notify(statusBarNotification.id, notificationBuilderRecovered.build())
    }

    private fun prepareSemiBlockChannel(){
        val channel = NotificationChannel(_semiBlockChannelId, "ProbationChannel", NotificationManager.IMPORTANCE_LOW).apply {
            description = "Notifications that are naughty but unsure to be blocked are sent to this channel"
        }
        notificationManagerCompat.createNotificationChannel(channel)
    }

    private fun isIgnoredNotificationType(statusBarNotification: StatusBarNotification): Boolean {
        val notification = statusBarNotification.notification
        val ignoreCriteria = mutableListOf<Boolean>()
        ignoreCriteria.add(notification.extras.getBoolean(_repostedFlag)) // reposted by this app
        ignoreCriteria.add(notification.fullScreenIntent != null) // urgent - fullScreenIntent
        ignoreCriteria.add(notification.extras.containsKey(Notification.EXTRA_PROGRESS_MAX) && notification.extras.getInt(Notification.EXTRA_PROGRESS_MAX) != 0) // progress bar
        ignoreCriteria.add(notification.extras.containsKey(Notification.EXTRA_MEDIA_SESSION)) // mediaStyle
        ignoreCriteria.add((notification.flags and Notification.FLAG_GROUP_SUMMARY) != 0) // group summary notification
        ignoreCriteria.add((notification.flags and Notification.FLAG_FOREGROUND_SERVICE) != 0) // foreground notification

        return ignoreCriteria.any { it }
    }

    private fun checkIfSemiBlocked(notification: Notification): Boolean {
        val dao = NotiDatabase.getDatabase(this).channelDao()

        val originalChannel = notification.channelId?.let { notificationManagerCompat.getNotificationChannel(it) }
        val channelId: String = originalChannel?.id ?: notification.extras.getString(Notification.EXTRA_TITLE, "")

        val isChannelSemiBlocked = runBlocking{
            withContext(Dispatchers.Default) {
                dao.getChannelSemiBlockedFlag(channelId)
            }
        }

        return isChannelSemiBlocked ?: false
    }

    private fun makeSemiBlockAction(notification: Notification): NotificationCompat.Action {
        val intent = makeIntentForAction(_semiBlockActionName, notification)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Action.Builder(R.drawable.ic_semiblock_notification,"Semi-Block", pendingIntent).build()
    }

    private fun makeReleaseAction(notification: Notification): NotificationCompat.Action {
        val intent = makeIntentForAction(_releaseActionName, notification)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Action.Builder(R.drawable.ic_block_notification,"Release", pendingIntent).build()
    }

    private fun makeIntentForAction(actionName : String, notification: Notification): Intent {
        val intent = Intent(this, NotificationActionBroadcastReceiver::class.java)
        intent.action = actionName
        val originalChannel = notification.channelId?.let { notificationManagerCompat.getNotificationChannel(it) }
        intent.putExtra("channel_id", originalChannel?.id ?: notification.extras?.getString(Notification.EXTRA_TITLE))
        intent.putExtra("channel_name", originalChannel?.name ?: notification.extras?.getString(Notification.EXTRA_TITLE))
        return intent
    }

    private val _repostedFlag = "repostedByNotiBoy"
    private val _semiBlockChannelId = "semi_block_channel"
    private val _semiBlockActionName = "SEMI_BLOCK"
    private val _releaseActionName = "RELEASE"
}