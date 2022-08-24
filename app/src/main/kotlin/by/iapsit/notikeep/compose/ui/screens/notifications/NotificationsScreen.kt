package by.iapsit.notikeep.compose.ui.screens.notifications

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.iapsit.core.model.NotificationInfo
import by.iapsit.notikeep.utils.Constants
import by.iapsit.notikeep.compose.ui.common.Loading
import by.iapsit.notikeep.compose.ui.common.NoData
import by.iapsit.notikeep.compose.ui.common.SwipeToDelete
import by.iapsit.notikeep.utils.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NotificationsScreen(
    packageName: String,
    showSnackBarWithAction: (
        message: String,
        actionLabel: String,
        callback: () -> Unit
    ) -> Unit,
    viewModel: NotificationsViewModel = getViewModel { parametersOf(packageName) }
) {
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current
    val vibrator = remember(context) { context.getVibrator() }
    Surface(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is NotificationsContract.State.ShowLoading -> Loading()
            is NotificationsContract.State.ShowNotifications -> NotificationsList(
                timedNotifications = state.notifications,
                onSwipe = { id ->
                    viewModel.setAction(NotificationsContract.Action.ItemSwiped(id))
                },
                onMiddleReached = {
                    viewModel.setAction(NotificationsContract.Action.MiddleReached)
                }
            )
            NotificationsContract.State.ShowNoData -> NoData(
                iconTint = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxSize()
            )
        }
        LaunchedEffect(Unit) {
            viewModel.effect.collectLatest {
                when (it) {
                    is NotificationsContract.Effect.Vibrate -> vibrator.makeSimpleVibration(it.duration)
                    is NotificationsContract.Effect.ShowSnackBarWithAction -> showSnackBarWithAction(
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
private fun NotificationsList(
    timedNotifications: Map<String, List<NotificationInfo>>,
    onSwipe: (id: Long) -> Unit,
    onMiddleReached: () -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        reverseLayout = true
    ) {
        timedNotifications.forEach {
            val notifications = it.value
            itemsIndexed(
                items = notifications,
                key = { _, notification -> notification.id }
            ) { index, notification ->
                SwipeToDelete(
                    onSwipe = { onSwipe(notification.id) },
                    onMiddleReached = onMiddleReached,
                    modifier = Modifier.animateItemPlacement(),
                    elevation = 0.dp
                ) {
                    NotificationItem(
                        notificationInfo = notification,
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .padding(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp)
                    )
                }
                if (!notifications.isIndexLast(index)) Divider(modifier = Modifier.animateItemPlacement())
            }
            item(key = it.value) {
                CardHeader(
                    text = it.key,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }
        }
    }
}

private const val EXPANDED_LINE_INDEX = 3

@Composable
private fun NotificationItem(notificationInfo: NotificationInfo, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var expandable by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { if (expandable) expanded = !expanded }
        ) {
            with(notificationInfo) {
                Row {
                    Text(
                        text = title,
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        modifier = if (expandable) {
                            Modifier.fillMaxWidth(0.9F)
                        } else Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    if (expandable) {
                        Icon(
                            imageVector = if (expanded) {
                                Icons.Filled.KeyboardArrowUp
                            } else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                }
                Text(
                    text = text,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (expanded) Int.MAX_VALUE else EXPANDED_LINE_INDEX,
                    onTextLayout = { if (it.isLineEllipsized(it.lastLineIndex)) expandable = true },
                    modifier = Modifier.fillMaxWidth()
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = remember {
                            postTime.toDate(Constants.DatePatterns.NOTIFICATION_LIST_ITEM)
                        },
                        modifier = Modifier.align(Alignment.End),
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CardHeader(text: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(36.dp),
        backgroundColor = MaterialTheme.colors.onBackground.copy(0.15F),
        modifier = modifier
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(4.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, color = Color.White)
        )
    }
}

@Preview(name = "CardHeader")
@Composable
private fun CardHeaderPreview() {
    CardHeader("Test")
}

@Preview(name = "Notification")
@Composable
private fun NotificationItemPreview() {
    NotificationItem(
        NotificationInfo("test", "test", "test", 1000L)
    )
}

