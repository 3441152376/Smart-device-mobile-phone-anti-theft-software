package cn.egg404.phone.ui.widgets

import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.runtime.Composable
import cn.egg404.phone.theme.PhoneTheme

const val SNACK_INFO = ""
const val SNACK_WARN = " "
const val SNACK_ERROR = "  "
const val SNACK_SUCCESS = "OK"

@Composable
fun AppSnackBar(data: SnackbarData) {
    Snackbar(
        snackbarData = data,
        backgroundColor = when (data.actionLabel) {
            SNACK_INFO -> PhoneTheme.colors.theme
            SNACK_WARN -> PhoneTheme.colors.warn
            SNACK_ERROR -> PhoneTheme.colors.error
            SNACK_SUCCESS -> PhoneTheme.colors.success
            else -> PhoneTheme.colors.theme
        },
        actionColor = PhoneTheme.colors.textPrimary,
        contentColor = PhoneTheme.colors.textPrimary,
    )
}