package by.iapsit.notikeep.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import by.iapsit.notikeep.compose.NKApp
import by.iapsit.notikeep.compose.ui.theme.NotiKeepTheme
import by.iapsit.notikeep.services.NotificationAccessService

class MainActivity : ComponentActivity() {

    private val notificationAccessServiceIntent by lazy {
        Intent(this, NotificationAccessService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiKeepTheme {
                NKApp()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startService(notificationAccessServiceIntent)
    }

    override fun onStop() {
        super.onStop()
        stopService(notificationAccessServiceIntent)
    }
}