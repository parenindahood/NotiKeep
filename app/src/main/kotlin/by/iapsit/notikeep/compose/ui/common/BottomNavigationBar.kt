package by.iapsit.notikeep.compose.ui.common

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import by.iapsit.notikeep.compose.navigation.NavRoutes

@Immutable
data class BottomNavRoute(
    val navRoute: NavRoutes,
    val label: @Composable (() -> Unit)?,
    val icon: @Composable () -> Unit
)

private const val INITIAL_SELECTED_POSITION = 0

@Composable
fun BottomNavigationBar(
    items: List<BottomNavRoute>,
    onItemSelected: (route: String) -> Unit,
    onItemReselected: (route: String) -> Unit,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = MaterialTheme.colors.onPrimary,
    initialSelectedPosition: Int = INITIAL_SELECTED_POSITION,
    elevation: Dp = 0.dp
) {
    BottomNavigation(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation
    ) {
        var selectedPosition by rememberSaveable { mutableStateOf(initialSelectedPosition) }
        items.forEachIndexed { index, screen ->
            BottomNavigationItem(
                icon = screen.icon,
                label = screen.label,
                selected = selectedPosition == index,
                onClick = {
                    val route = screen.navRoute.route
                    if (selectedPosition != index) {
                        onItemSelected(route)
                        selectedPosition = index
                    } else onItemReselected(route)
                }
            )
        }
    }
}
