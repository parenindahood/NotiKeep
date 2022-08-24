package by.iapsit.notikeep.compose.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun Loading() {
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}