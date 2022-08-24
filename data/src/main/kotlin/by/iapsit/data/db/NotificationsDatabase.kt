package by.iapsit.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import by.iapsit.data.db.dao.NotificationsDao
import by.iapsit.data.db.dao.PackagesDao
import by.iapsit.data.db.entities.NotificationEntity
import by.iapsit.data.db.entities.PackageEntity

@Database(entities = [NotificationEntity::class, PackageEntity::class], version = 1)
internal abstract class NotificationsDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "notifications"
    }

    abstract fun getNotificationsDao(): NotificationsDao

    abstract fun getPackagesDao(): PackagesDao
}