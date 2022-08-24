package by.iapsit.notikeep.compose.ui.screens.applications

import by.iapsit.core.model.PackageInfo
import by.iapsit.notikeep.base.intents.UiAction
import by.iapsit.notikeep.base.intents.UiEffect
import by.iapsit.notikeep.base.intents.UiState
import by.iapsit.notikeep.utils.UiText

class ApplicationsContract {

    sealed class Action : UiAction {
        data class FavouriteButtonClick(val packageName: String) : Action()
        data class ItemSwiped(val packageName: String): Action()
        data class UndoDeletingButtonClick(val packageName: String) : Action()
        object MiddleReached : Action()
    }

    sealed class State : UiState {
        object ShowLoading : State()
        data class ShowApplicationList(val packages: List<PackageInfo>) : State()
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
