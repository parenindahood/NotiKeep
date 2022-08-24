package by.iapsit.core.model

data class NotificationInfo(
    val packageName: String,
    val text: String,
    val title: String,
    val postTime: Long,
    val id: Long = 0
)