package by.iapsit.notikeep.compose.navigation

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.res.stringResource
import by.iapsit.notikeep.R
import by.iapsit.notikeep.compose.ui.common.BottomNavRoute

private const val APPLICATIONS_ROUTE = "application"
private const val FAVOURITES_ROUTE = "favourites"
private const val SETTINGS_ROUTE = "settings"
private const val NOTIFICATIONS_ROUTE =
    "notifications/{${NavRoutes.Notifications.PACKAGE_NAME_ARGUMENT}}"

sealed class NavRoutes(val route: String) {

    object Applications : NavRoutes(APPLICATIONS_ROUTE)

    object Favourites : NavRoutes(FAVOURITES_ROUTE)

    object Settings : NavRoutes(SETTINGS_ROUTE)

    object Notifications : NavRoutes(NOTIFICATIONS_ROUTE) {
        private const val NOTIFICATIONS_BASE_ROUTE = "notifications"
        const val PACKAGE_NAME_ARGUMENT = "packageName"

        operator fun invoke(packageName: String) = "$NOTIFICATIONS_BASE_ROUTE/$packageName"
    }

    companion object {
        val bottomNavigationItems = listOf(
            BottomNavRoute(
                Applications,
                { Text(text = stringResource(id = R.string.bottom_nav_notifications_item_title)) },
                { Icon(Icons.Filled.Notifications, null) }
            ),
            BottomNavRoute(
                Favourites,
                { Text(text = stringResource(id = R.string.bottom_nav_favourites_item_title)) },
                { Icon(Icons.Filled.Favorite, null) }
            ),
            BottomNavRoute(
                Settings,
                { Text(text = stringResource(id = R.string.bottom_nav_settings_item_title)) },
                { Icon(Icons.Filled.Settings, null) }
            )
        )
    }
}
