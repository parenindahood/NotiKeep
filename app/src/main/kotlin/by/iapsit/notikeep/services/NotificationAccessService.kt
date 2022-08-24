package by.iapsit.notikeep.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.*

class NotificationAccessService : Service() {

    companion object {
        const val NO_NOTIFICATION_ACCESS_ACTION = "by.iapsit.notikeep.NO_NOTIFICATION_ACCESS"
        private const val CHECK_DELAY = 30000L
    }

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        checkAccess()
        return START_STICKY
    }

    private fun checkAccess() = serviceScope.launch {
        while (true) {
            if (!isNotificationListenerEnabled(packageName)) {
                sendBroadcast(Intent(NO_NOTIFICATION_ACCESS_ACTION))
            }
            delay(CHECK_DELAY)
        }
    }

    private fun isNotificationListenerEnabled(packageName: String) =
        NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}