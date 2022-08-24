package by.iapsit.notikeep.compose.ui.screens.notifications

import androidx.lifecycle.viewModelScope
import by.iapsit.notikeep.R
import by.iapsit.core.repositories.NotificationsRepository
import by.iapsit.notikeep.utils.Constants
import by.iapsit.notikeep.base.viewmodel.StatefulViewModel
import by.iapsit.notikeep.utils.UiText
import by.iapsit.notikeep.utils.toDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationsViewModel(
    private val packageName: String,
    private val repository: NotificationsRepository,
) : StatefulViewModel<NotificationsContract.Action, NotificationsContract.State, NotificationsContract.Effect>() {

    companion object {
        private const val VIBRATION_DURATION = 50L
    }

    override fun createInitialState() = NotificationsContract.State.ShowLoading

    override suspend fun handleAction(action: NotificationsContract.Action) {
        when (action) {
            is NotificationsContract.Action.ItemSwiped -> {
                repository.softDeleteNotification(action.id, true)
                setEffect {
                    NotificationsContract.Effect.ShowSnackBarWithAction(
                        UiText.StringResource(R.string.snack_bar_undo_deleting_message),
                        UiText.StringResource(R.string.snack_bar_undo_deleting_action_label),
                    ) {
                        setAction(NotificationsContract.Action.UndoDeletingButtonClick(action.id))
                    }
                }
            }
            is NotificationsContract.Action.UndoDeletingButtonClick -> {
                repository.softDeleteNotification(action.id, false)
            }
            NotificationsContract.Action.MiddleReached -> {
                setEffect { NotificationsContract.Effect.Vibrate(VIBRATION_DURATION) }
            }
        }
    }

    init {
        viewModelScope.launch {
            repository.getNotifications(packageName).collect {
                if (it.isNotEmpty()) {
                    val notifications = withContext(Dispatchers.IO) {
                        it.reversed().groupBy { notification ->
                            notification.postTime.toDate(Constants.DatePatterns.NOTIFICATION_LIST_HEADER)
                        }
                    }
                    setState { NotificationsContract.State.ShowNotifications(notifications) }
                } else setState { NotificationsContract.State.ShowNoData }
            }
        }
    }
}