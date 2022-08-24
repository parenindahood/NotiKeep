package by.iapsit.notikeep.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.ui.text.TextLayoutResult
import java.text.SimpleDateFormat
import java.util.*

fun List<Any>.isIndexLast(index: Int) = index == lastIndex

suspend fun SnackbarHostState.showSnackBarWithAction(
    message: String,
    actionLabel: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
    callback: () -> Unit
) {
    val result = showSnackbar(message, actionLabel, duration)
    if (result == SnackbarResult.ActionPerformed) callback()
}

fun Vibrator.makeSimpleVibration(milliseconds: Long) {
    if (hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
        } else vibrate(milliseconds)
    }
}

fun Context.getVibrator() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
} else getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

fun Long.toDate(pattern: String): String = with(SimpleDateFormat(pattern, Locale.getDefault())) {
    format(this@toDate)
}

val TextLayoutResult.lastLineIndex get() = lineCount - 1