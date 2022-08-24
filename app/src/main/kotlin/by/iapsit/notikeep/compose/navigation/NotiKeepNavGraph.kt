package by.iapsit.notikeep.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import by.iapsit.notikeep.compose.ui.screens.applications.ApplicationsScreen
import by.iapsit.notikeep.compose.ui.screens.notifications.NotificationsScreen
import by.iapsit.notikeep.compose.ui.screens.settings.SettingsScreen

@Composable
fun NotiKeepNavGraph(
    navController: NavHostController,
    startDestination: NavRoutes,
    showSnackBarWithAction: (
        message: String,
        actionLabel: String,
        callback: () -> Unit
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(NavRoutes.Applications.route) {
            ApplicationsScreen(
                navController = navController,
                favourite = false,
                showSnackBarWithAction = showSnackBarWithAction
            )
        }
        composable(NavRoutes.Notifications.route) {
            val packageName = it.arguments?.getString(NavRoutes.Notifications.PACKAGE_NAME_ARGUMENT)
                ?: throw IllegalArgumentException("Missing argument: ${NavRoutes.Notifications.PACKAGE_NAME_ARGUMENT}")
            NotificationsScreen(
                packageName = packageName,
                showSnackBarWithAction = showSnackBarWithAction
            )
        }
        composable(NavRoutes.Favourites.route) {
            ApplicationsScreen(
                navController = navController,
                favourite = true,
                showSnackBarWithAction = showSnackBarWithAction
            )
        }
        composable(NavRoutes.Settings.route) {
            SettingsScreen()
        }
    }
}