package by.iapsit.notikeep.compose

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import by.iapsit.notikeep.R
import by.iapsit.notikeep.compose.navigation.NavRoutes
import by.iapsit.notikeep.compose.navigation.NotiKeepNavGraph
import by.iapsit.notikeep.compose.ui.common.BottomNavigationBar
import by.iapsit.notikeep.compose.common.BroadcastReceiver
import by.iapsit.notikeep.services.NotificationAccessService
import by.iapsit.notikeep.utils.showSnackBarWithAction
import kotlinx.coroutines.launch

@Composable
fun NKApp() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    BroadcastReceiver(action = NotificationAccessService.NO_NOTIFICATION_ACCESS_ACTION) { _, context ->
        if (context == null) return@BroadcastReceiver
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackBarWithAction(
                message = context.getString(R.string.error_no_notification_access_desc),
                actionLabel = context.getString(R.string.snackbar_no_notification_access_action_label),
            ) {
                context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                backgroundColor = MaterialTheme.colors.primary,
                elevation = 4.dp
            )
        },
        bottomBar = {
            with(navController) {
                BottomNavigationBar(
                    items = NavRoutes.bottomNavigationItems,
                    elevation = 4.dp,
                    onItemSelected = { route ->
                        navigate(route) {
                            popUpTo(graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onItemReselected = { route ->
                        popBackStack(route, false)
                    }
                )
            }
        }
    ) { padding ->
        NotiKeepNavGraph(
            navController = navController,
            startDestination = NavRoutes.Applications,
            showSnackBarWithAction = { message, actionLabel, callback ->
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackBarWithAction(
                        message = message,
                        actionLabel = actionLabel,
                        callback = callback
                    )
                }
            },
            modifier = Modifier.padding(padding)
        )
    }
}
