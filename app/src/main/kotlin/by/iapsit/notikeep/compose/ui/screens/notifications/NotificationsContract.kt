package by.iapsit.notikeep.compose.ui.screens.notifications

import by.iapsit.core.model.NotificationInfo
import by.iapsit.notikeep.base.intents.UiAction
import by.iapsit.notikeep.base.intents.UiEffect
import by.iapsit.notikeep.base.intents.UiState
import by.iapsit.notikeep.utils.UiText

class NotificationsContract {

    sealed class Action : UiAction {
        data class ItemSwiped(val id: Long) : Action()
        data class UndoDeletingButtonClick(val id: Long) : Action()
        object MiddleReached : Action()
    }

    sealed class State : UiState {
        object ShowLoading : State()
        data class ShowNotifications(val notifications: Map<String, List<NotificationInfo>>) : State()
        object ShowNoData : State()
    }

    sealed class Effect : UiEffect {
        data class Vibrate(val duration: Long) : Effect()
        data class ShowSnackBarWithAction(
            val message: UiText,
            val actionLabel: UiText,
            val callback: () -> Unit
        ) : Effect()
    }
}