package com.example.notizone

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.app.NotificationCompat
import com.example.notizone.database.NotiDatabase
import com.example.notizone.ui.NotificationViewModel
import com.example.notizone.ui.NotificationViewModelFactory
import com.example.notizone.ui.theme.NotiZoneTheme

class MainActivity : ComponentActivity() {

    private val notificationViewModel: NotificationViewModel by viewModels{
        NotificationViewModelFactory(NotiDatabase.getDatabase(this).notificationDao())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiZoneTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background


                ) {
                    MyApp(notificationViewModel)
                }
            }

            FloatingActionButton(
                content = { Icon(painterResource(id = R.drawable.ic_semiblock_notification), contentDescription = null) },
                onClick = { Toast.makeText(this, "This is a quick popup message", Toast.LENGTH_SHORT).show(); postTestNotification(1) }
            )
            FloatingActionButton(
                content = { Icon(painterResource(id = R.drawable.ic_semiblock_notification), contentDescription = null) },
                onClick = { Toast.makeText(this, "This is a quick popup message 3", Toast.LENGTH_SHORT).show(); postTestNotification(3) }
            )
        }
    }

    private var testNotificationNumber = 0
    private fun postTestNotification(id : Int) {

        val channel = NotificationChannel("test_channel_$id", "testChannel$id", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "test_channel_" + id +"_description"
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(this, "test_channel_$id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Test Notification $testNotificationNumber")
            .setContentText("This is a test notification")
            .setCategory("test_category")

        notificationManager.notify(id, notificationBuilder.build())

        testNotificationNumber++
    }
}

@Composable
fun MyApp(viewModel: NotificationViewModel) {
    val allNotifications by viewModel.allNotifications.observeAsState(emptyList())
    LazyColumn{
        allNotifications.forEach{notification ->
            item{
                Text(notification.title)
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NotiZoneTheme {
        Greeting("Android")
    }
}
*/