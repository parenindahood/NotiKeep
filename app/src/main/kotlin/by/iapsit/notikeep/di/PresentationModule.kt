package by.iapsit.notikeep.di

import by.iapsit.notikeep.compose.ui.screens.applications.ApplicationsViewModel
import by.iapsit.notikeep.compose.ui.screens.notifications.NotificationsViewModel
import by.iapsit.notikeep.workers.DeleteNotificationsWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val presentationModule = module {
    viewModel { (isFavourite: Boolean) -> ApplicationsViewModel(isFavourite, get()) }
    viewModel { (packageName: String) -> NotificationsViewModel(packageName, get()) }
    worker { DeleteNotificationsWorker(get(), androidContext(), get()) }
}
