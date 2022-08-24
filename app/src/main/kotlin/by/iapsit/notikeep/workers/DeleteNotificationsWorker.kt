package by.iapsit.notikeep.workers

import android.content.Context
import androidx.work.*
import by.iapsit.core.repositories.NotificationsRepository
import java.util.concurrent.TimeUnit

class DeleteNotificationsWorker(
    private val repository: NotificationsRepository,
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    companion object {
        private const val UNIQUE_WORK_TAG = "delete-notifications"
        private const val REPEAT_INTERVAL = 24L
        private const val FLEX_TIME_INTERVAL = 1L
        private val request = PeriodicWorkRequestBuilder<DeleteNotificationsWorker>(
            REPEAT_INTERVAL,
            TimeUnit.HOURS,
            FLEX_TIME_INTERVAL,
            TimeUnit.HOURS
        ).build()

        fun enqueue(context: Context) {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_WORK_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }

    override suspend fun doWork(): Result {
        repository.deleteNotifications()
        return Result.success()
    }
}