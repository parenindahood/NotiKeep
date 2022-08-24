package by.iapsit.data.repositories

import by.iapsit.data.db.dao.NotificationsDao
import by.iapsit.data.db.dao.PackagesDao
import by.iapsit.data.db.entities.toEntity
import by.iapsit.data.db.entities.toModel
import by.iapsit.core.model.NotificationInfo
import by.iapsit.core.model.PackageInfo
import by.iapsit.core.repositories.NotificationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class NotificationsRepositoryImpl(
    private val notificationsDao: NotificationsDao,
    private val packagesDao: PackagesDao
) : NotificationsRepository {

    override suspend fun saveNotification(notificationInfo: NotificationInfo) =
        notificationsDao.insertNotification(notificationInfo.toEntity())

    override suspend fun getNotifications(packageName: String) =
        notificationsDao.getNotifications(packageName).map {
            it.toModel()
        }

    override suspend fun savePackage(packageInfo: PackageInfo) =
        packagesDao.insertPackage(packageInfo.toEntity())

    override suspend fun getPackages(isFavourite: Boolean): Flow<List<PackageInfo>> =
        packagesDao.getPackages(isFavourite).map {
            it.toModel()
        }

    override suspend fun manageFavouritePackages(packageName: String, isFavourite: Boolean) =
        packagesDao.updateFavouritePackage(packageName, isFavourite)

    override suspend fun deleteNotifications() {
        notificationsDao.deleteNotifications()
        packagesDao.deleteEmptyPackages()
    }

    override suspend fun softDeleteNotifications(packageName: String?, isDeleted: Boolean) =
        packageName?.let {
            notificationsDao.softDeleteNotifications(it, isDeleted)
        } ?: run { notificationsDao.softDeleteAllNotifications(isDeleted) }

    override suspend fun softDeleteNotification(id: Long, isDeleted: Boolean) =
        notificationsDao.softDeleteNotification(id, isDeleted)
}
