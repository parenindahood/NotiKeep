package by.iapsit.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.iapsit.core.model.NotificationInfo

@Entity(tableName = "notifications")
internal data class NotificationEntity(
    @ColumnInfo(name = "package_name") val packageName: String,
    val text: String,
    val title: String,
    @ColumnInfo(name = "post_time") val postTime: Long,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

internal fun NotificationInfo.toEntity() = NotificationEntity(
    packageName = packageName,
    text = text,
    title = title,
    postTime = postTime,
    isDeleted = false
)

internal fun NotificationEntity.toModel() = NotificationInfo(
    packageName = packageName,
    text = text,
    title = title,
    postTime = postTime,
    id = id
)

internal fun List<NotificationEntity>.toModel() = map { it.toModel() }