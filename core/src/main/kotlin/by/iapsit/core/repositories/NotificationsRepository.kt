package by.iapsit.core.repositories

import by.iapsit.core.model.NotificationInfo
import by.iapsit.core.model.PackageInfo
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {

    suspend fun saveNotification(notificationInfo: NotificationInfo)

    suspend fun getNotifications(packageName: String): Flow<List<NotificationInfo>>

    suspend fun savePackage(packageInfo: PackageInfo)

    suspend fun getPackages(isFavourite: Boolean): Flow<List<PackageInfo>>

    suspend fun manageFavouritePackages(packageName: String, isFavourite: Boolean)

    suspend fun deleteNotifications()

    suspend fun softDeleteNotifications(packageName: String?, isDeleted: Boolean)

    suspend fun softDeleteNotification(id: Long, isDeleted: Boolean)
}