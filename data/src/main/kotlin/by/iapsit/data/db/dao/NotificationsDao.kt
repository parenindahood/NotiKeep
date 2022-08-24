package by.iapsit.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import by.iapsit.data.db.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NotificationsDao {

    @Insert
    suspend fun insertNotification(notificationEntity: NotificationEntity)

    @Query("SELECT * FROM notifications WHERE package_name = :packageName AND is_deleted = 0")
    fun getNotifications(packageName: String): Flow<List<NotificationEntity>>

    @Query("DELETE FROM notifications WHERE is_deleted = 1")
    suspend fun deleteNotifications()

    @Query("UPDATE notifications SET is_deleted = :isDeleted WHERE package_name = :packageName")
    suspend fun softDeleteNotifications(packageName: String, isDeleted: Boolean)

    @Query("UPDATE notifications SET is_deleted = :isDeleted")
    suspend fun softDeleteAllNotifications(isDeleted: Boolean)

    @Query("UPDATE notifications SET is_deleted = :isDeleted WHERE id = :id")
    suspend fun softDeleteNotification(id: Long, isDeleted: Boolean)
}