package by.iapsit.notikeep.services

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import by.iapsit.core.model.NotificationInfo
import by.iapsit.core.model.PackageInfo
import by.iapsit.core.repositories.NotificationsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class NotificationService : NotificationListenerService() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private val repository: NotificationsRepository by inject()

    override fun onNotificationPosted(notification: StatusBarNotification?) {
        super.onNotificationPosted(notification)

        val notificationModel = notification?.toModel() ?: return
        val packageInfo = PackageInfo(notificationModel.packageName)

        serviceScope.launch {
            with(repository) {
                saveNotification(notificationModel)
                savePackage(packageInfo)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    private fun StatusBarNotification.toModel(): NotificationInfo? = with(notification.extras) {
        val text = getString(Notification.EXTRA_TEXT) ?: return null
        val title = getString(Notification.EXTRA_TITLE) ?: return null
        return NotificationInfo(packageName, text, title, postTime)
    }
}