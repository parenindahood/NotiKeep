package by.iapsit.notikeep.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {

    data class Dynamically(val value: String): UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    @Composable
    fun asString() = when (this) {
        is Dynamically -> value
        is StringResource -> stringResource(resId, *args)
    }

    fun asString(context: Context) = when (this) {
        is Dynamically -> value
        is StringResource -> context.getString(resId, *args)
    }
}