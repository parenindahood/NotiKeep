package by.iapsit.notikeep.compose.ui.screens.applications

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import by.iapsit.notikeep.R
import by.iapsit.core.model.PackageInfo
import by.iapsit.notikeep.compose.navigation.NavRoutes
import by.iapsit.notikeep.compose.ui.common.Loading
import by.iapsit.notikeep.compose.ui.common.NoData
import by.iapsit.notikeep.compose.ui.common.SwipeToDelete
import by.iapsit.notikeep.utils.getVibrator
import by.iapsit.notikeep.utils.makeSimpleVibration
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ApplicationsScreen(
    navController: NavController,
    favourite: Boolean,
    showSnackBarWithAction: (
        message: String,
        actionLabel: String,
        callback: () -> Unit
    ) -> Unit,
    viewModel: ApplicationsViewModel = getViewModel { parametersOf(favourite) }
) {
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current
    val vibrator = remember(context) { context.getVibrator() }
    Surface(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is ApplicationsContract.State.ShowLoading -> Loading()
            is ApplicationsContract.State.ShowApplicationList -> {
                ApplicationList(
                    packages = state.packages,
                    onFavouriteButtonClick = {
                        viewModel.setAction(ApplicationsContract.Action.FavouriteButtonClick(it))
                    },
                    onItemClick = { navController.navigate(NavRoutes.Notifications(it)) },
                    onSwipe = {
                        viewModel.setAction(ApplicationsContract.Action.ItemSwiped(it))
                    },
                    onMiddleReached = {
                        viewModel.setAction(ApplicationsContract.Action.MiddleReached)
                    }
                )
            }
            is ApplicationsContract.State.ShowNoData -> NoData(
                iconTint = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxSize()
            )
        }
        LaunchedEffect(Unit) {
            viewModel.effect.collectLatest {
                when (it) {
                    is ApplicationsContract.Effect.Vibrate -> vibrator.makeSimpleVibration(it.duration)
                    is ApplicationsContract.Effect.ShowSnackBarWithAction -> showSnackBarWithAction(
                        it.message.asString(context),
                        it.actionLabel.asString(context).uppercase(),
                        it.callback
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ApplicationList(
    packages: List<PackageInfo>,
    onFavouriteButtonClick: (packageName: String) -> Unit,
    onItemClick: (packageName: String) -> Unit,
    onSwipe: (packageName: String) -> Unit,
    onMiddleReached: () -> Unit
) {
    LazyColumn {
        items(
            items = packages,
            key =  { it.packageName }
        ) {
            SwipeToDelete(
                onSwipe = { onSwipe(it.packageName) },
                onMiddleReached = onMiddleReached,
                modifier = Modifier.animateItemPlacement(),
                elevation = 2.dp
            ) {
                ApplicationItem(
                    data = it,
                    onFavouriteButtonClick = { onFavouriteButtonClick(it.packageName) },
                    onItemClick = { onItemClick(it.packageName) },
                )
            }
            Divider(modifier = Modifier.animateItemPlacement())
        }
    }
}

@Composable
private fun ApplicationItem(
    data: PackageInfo,
    onFavouriteButtonClick: () -> Unit,
    onItemClick: () -> Unit
) {
    val context = LocalContext.current
    val deletedIcon = remember {
        AppCompatResources.getDrawable(context, R.drawable.ic_android)
            ?: throw IllegalStateException("Drawable is null")
    }
    val applicationData = remember(data.packageName) {
        context.packageManager.getApplicationItem(data.packageName, deletedIcon)
    }
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .clickable(onClick = onItemClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberDrawablePainter(applicationData.icon),
            contentDescription = null,
            modifier = Modifier.size(52.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .fillMaxWidth(0.9F),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = applicationData.title,
                style = TextStyle(fontSize = 20.sp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = applicationData.packageName,
                    style = TextStyle(fontSize = 16.sp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
        Icon(
            painter = if (data.isFavourite) {
                painterResource(R.drawable.ic_filled_heart)
            } else painterResource(R.drawable.ic_unfilled_heart),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onFavouriteButtonClick
                ),
            tint = if (data.isFavourite) {
                MaterialTheme.colors.primary
            } else MaterialTheme.colors.onBackground
        )
    }
}

@Immutable
private data class ApplicationItem(
    val packageName: String,
    val title: String,
    val icon: Drawable
)

private fun PackageManager.getApplicationItem(
    packageName: String,
    deletedApplicationIcon: Drawable
) = try {
    val applicationInfo = getApplicationInfo(packageName, 0)
    ApplicationItem(
        packageName,
        getApplicationLabel(applicationInfo).toString(),
        getApplicationIcon(applicationInfo)
    )
} catch (e: PackageManager.NameNotFoundException) {
    ApplicationItem(
        packageName,
        packageName,
        deletedApplicationIcon
    )
}
