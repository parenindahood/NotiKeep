package by.iapsit.notikeep.compose.ui.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import by.iapsit.notikeep.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationGraphicsApi::class)
@Composable
fun SwipeToDelete(
    onSwipe: () -> Unit,
    onMiddleReached: () -> Unit,
    modifier: Modifier = Modifier,
    directions: Set<DismissDirection> = setOf(
        DismissDirection.StartToEnd,
        DismissDirection.EndToStart
    ),
    swipeActionDelay: Long = 200L,
    backgroundColor: Color = MaterialTheme.colors.primary,
    iconTint: Color = MaterialTheme.colors.onPrimary,
    iconSize: Dp = 28.dp,
    elevation: Dp = 1.dp,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = modifier.wrapContentSize()) {
        val constraints = this
        val density = LocalDensity.current
        val coroutineScope = rememberCoroutineScope()
        val dismissState = rememberDismissState {
            if (it != DismissValue.Default) {
                coroutineScope.launch {
                    delay(swipeActionDelay)
                    onSwipe()
                }
            }
            true
        }
        val swipeInProcess by remember { derivedStateOf { dismissState.offset.value !in (-8F..8F) } }
        val shapeRadius by animateDpAsState(if (swipeInProcess) 8.dp else 0.dp)
        val middleReached by remember {
            derivedStateOf {
                abs(dismissState.offset.value) > with(density) { constraints.maxWidth.toPx() } / 2
            }
        }
        LaunchedEffect(middleReached) {
            if (middleReached) onMiddleReached()
        }
        SwipeToDismiss(
            state = dismissState,
            directions = directions,
            background = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                        .padding(24.dp)
                ) {
                    Icon(
                        painter = rememberAnimatedVectorPainter(
                            animatedImageVector = AnimatedImageVector.animatedVectorResource(R.drawable.ic_trash_cart),
                            atEnd = middleReached
                        ),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier
                            .align(
                                when (dismissState.dismissDirection) {
                                    DismissDirection.StartToEnd -> Alignment.CenterStart
                                    DismissDirection.EndToStart -> Alignment.CenterEnd
                                    null -> Alignment.Center
                                }
                            )
                            .size(iconSize)
                    )
                }
            },
            dismissContent = {
                Card(
                    shape = when (dismissState.dismissDirection) {
                        DismissDirection.StartToEnd -> RoundedCornerShape(
                            topStart = shapeRadius,
                            bottomStart = shapeRadius
                        )
                        DismissDirection.EndToStart -> RoundedCornerShape(
                            topEnd = shapeRadius,
                            bottomEnd = shapeRadius
                        )
                        null -> RoundedCornerShape(shapeRadius)
                    },
                    elevation = elevation
                ) {
                    content()
                }
            }
        )
    }
}