package com.example.notizone

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.app.NotificationCompat
import com.example.notizone.ui.theme.NotiZoneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiZoneTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background


                ) {
                    MyApp()
                }
            }

            FloatingActionButton(
                content = { Icon(painterResource(id = R.drawable.ic_semiblock_notification), contentDescription = null) },
                onClick = { Toast.makeText(this, "This is a quick popup message", Toast.LENGTH_SHORT).show(); postTestNotification() }
            )
        }
    }

    private fun postTestNotification() {

        val channel = NotificationChannel("test_channel_A", "testChannelA", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "test_channel_A_description"
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(this, "test_channel_A")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Test Notification A")
            .setContentText("This is a test notification")
            .setCategory("test_category")

        notificationManager.notify(1, notificationBuilder.build())
    }
}

@Composable
fun MyApp() {
    Text(text = "Hello seo!")
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