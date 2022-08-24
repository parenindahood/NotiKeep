package by.iapsit.notikeep.compose.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import by.iapsit.notikeep.R

@Composable
fun NoData(iconTint: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_no_notifications),
            contentDescription = null,
            tint = if (isSystemInDarkTheme()) iconTint else iconTint.copy(0.4F),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(0.3F)
        )
    }
}