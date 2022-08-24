package by.iapsit.notikeep.compose.ui.screens.applications

import androidx.lifecycle.viewModelScope
import by.iapsit.notikeep.R
import by.iapsit.core.repositories.NotificationsRepository
import by.iapsit.notikeep.base.viewmodel.StatefulViewModel
import by.iapsit.notikeep.utils.UiText
import kotlinx.coroutines.launch

class ApplicationsViewModel(
    private val isFavourite: Boolean,
    private val repository: NotificationsRepository,
) : StatefulViewModel<ApplicationsContract.Action, ApplicationsContract.State, ApplicationsContract.Effect>() {

    companion object {
        private const val VIBRATION_DURATION = 50L
    }

    override fun createInitialState() = ApplicationsContract.State.ShowLoading

    override suspend fun handleAction(action: ApplicationsContract.Action) {
        when (action) {
            is ApplicationsContract.Action.FavouriteButtonClick -> {
                repository.manageFavouritePackages(action.packageName, !isFavourite)
                setEffect { ApplicationsContract.Effect.Vibrate(VIBRATION_DURATION) }
            }
            is ApplicationsContract.Action.ItemSwiped -> {
                val packageName = action.packageName
                repository.softDeleteNotifications(packageName, true)
                setEffect {
                    ApplicationsContract.Effect.ShowSnackBarWithAction(
                        UiText.StringResource(R.string.snack_bar_undo_deleting_message),
                        UiText.StringResource(R.string.snack_bar_undo_deleting_action_label),
                    ) {
                        setAction(ApplicationsContract.Action.UndoDeletingButtonClick(packageName))
                    }
                }
            }
            is ApplicationsContract.Action.UndoDeletingButtonClick -> {
                repository.softDeleteNotifications(action.packageName, false)
            }
            ApplicationsContract.Action.MiddleReached -> setEffect {
                ApplicationsContract.Effect.Vibrate(VIBRATION_DURATION)
            }
        }
    }

    init {
        viewModelScope.launch {
            repository.getPackages(isFavourite).collect {
                setState {
                    if (it.isEmpty()) {
                        ApplicationsContract.State.ShowNoData
                    } else ApplicationsContract.State.ShowApplicationList(it)
                }
            }
        }
    }
}
