package cn.egg404.phone.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = PhoneColors(
    theme = lightTheme,
    theme2 = Color(0XFF3B85F5),
    surface = lightSurface,
    background = lightBackground,
    bgTextColor = lightBackground,
    listItem = lightSurface,
    divider = lightDivider,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    card = lightSurface,
    icon = lightTheme,
    info = textPrimary,
    warn = error,
    success = success,
    error = error,
    primaryBtn = btnPrimary,
    secondBtn = btnSecondary,
    hot = lightBackground,
    placeholder = lightPlaceholder,
    gray = gray
)

private val DarkColorPalette =
    PhoneColors(
        theme = darkTheme,
        theme2 = Color(0XFF3B85F5),
        surface = darkSurface,
        background = darkBackground,
        bgTextColor = lightBackground,
        listItem = darkSurface,
        divider = darkDivider,
        textPrimary = Color(0XFFEFEFEF),
        textSecondary = textSecondary,
        card = darkSurface,
        icon = darkTheme,
        info = textPrimary,
        warn = error,
        success = success,
        error = error,
        primaryBtn = btnPrimary,
        secondBtn = btnSecondary,
        hot = darkBackground,
        placeholder = darkPlaceholder,
        gray = gray
    )

var LocalAppColors = compositionLocalOf {
    LightColorPalette
}

@Stable
object PhoneTheme {
    val colors: PhoneColors
        @Composable
        get() = LocalAppColors.current

    enum class Theme {
        Light, Dark
    }
}

@Stable
class PhoneColors(
    theme: Color,
    theme2: Color,
    surface: Color,
    background: Color,
    bgTextColor: Color,
    listItem: Color,
    divider: Color,
    textPrimary: Color,
    textSecondary: Color,
    card: Color,
    icon: Color,
    info: Color,
    warn: Color,
    success: Color,
    error: Color,
    primaryBtn: Color,
    secondBtn: Color,
    hot: Color,
    placeholder: Color,
    gray: Color
) {
    var theme: Color by mutableStateOf(theme)
        private set
    var theme2: Color by mutableStateOf(theme2)
        private set
    var surface: Color by mutableStateOf(surface)
        private set
    var background: Color by mutableStateOf(background)
        private set
    var bgTextColor: Color by mutableStateOf(bgTextColor)
        private set
    var listItem: Color by mutableStateOf(listItem)
        private set
    var divider: Color by mutableStateOf(divider)
        private set
    var textPrimary: Color by mutableStateOf(textPrimary)
        private set
    var textSecondary: Color by mutableStateOf(textSecondary)
        private set
    var card: Color by mutableStateOf(card)
        private set
    var icon: Color by mutableStateOf(icon)
        private set
    var info: Color by mutableStateOf(info)
        private set
    var warn: Color by mutableStateOf(warn)
        private set
    var success: Color by mutableStateOf(success)
        private set
    var error: Color by mutableStateOf(error)
        private set
    var primaryBtn: Color by mutableStateOf(primaryBtn)
        internal set
    var secondBtn: Color by mutableStateOf(secondBtn)
        private set
    var hot: Color by mutableStateOf(hot)
        private set
    var placeholder: Color by mutableStateOf(placeholder)
        private set
    var gray: Color by mutableStateOf(gray)
        private set
}


@Composable
fun PhoneTheme(isDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (isDarkTheme) {
        DarkColorPalette.primaryBtn = darkTheme
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val durationMillis = 600
    val theme = animateColorAsState(targetValue = colors.theme, TweenSpec(durationMillis))
    val theme2 = animateColorAsState(targetValue = colors.theme2, TweenSpec(durationMillis))
    val background = animateColorAsState(targetValue = colors.background, TweenSpec(durationMillis))
    val bgTextColor =
        animateColorAsState(targetValue = colors.bgTextColor, TweenSpec(durationMillis))
    val listItem = animateColorAsState(targetValue = colors.listItem, TweenSpec(durationMillis))
    val divider = animateColorAsState(targetValue = colors.divider, TweenSpec(durationMillis))
    val textPrimary =
        animateColorAsState(targetValue = colors.textPrimary, TweenSpec(durationMillis))
    val textSecondary =
        animateColorAsState(targetValue = colors.textSecondary, TweenSpec(durationMillis))
    val card = animateColorAsState(targetValue = colors.card, TweenSpec(durationMillis))
    val icon = animateColorAsState(targetValue = colors.icon, TweenSpec(durationMillis))
    val info = animateColorAsState(targetValue = colors.info, TweenSpec(durationMillis))
    val warn = animateColorAsState(targetValue = colors.warn, TweenSpec(durationMillis))
    val success = animateColorAsState(targetValue = colors.success, TweenSpec(durationMillis))
    val error = animateColorAsState(targetValue = colors.error, TweenSpec(durationMillis))
    val hot = animateColorAsState(targetValue = colors.hot, TweenSpec(durationMillis))
    val primaryBtn = animateColorAsState(targetValue = colors.primaryBtn, TweenSpec(durationMillis))
    val secondBtn = animateColorAsState(targetValue = colors.secondBtn, TweenSpec(durationMillis))
    val placeholder =
        animateColorAsState(targetValue = colors.placeholder, TweenSpec(durationMillis))
    val surface =
        animateColorAsState(targetValue = colors.surface, TweenSpec(durationMillis))
    val gray = animateColorAsState(targetValue = colors.gray, TweenSpec(durationMillis))

    val phoneColors =
        PhoneColors(
            theme = theme.value,
            theme2 = theme2.value,
            background = background.value,
            bgTextColor = bgTextColor.value,
            listItem = listItem.value,
            divider = divider.value,
            textPrimary = textPrimary.value,
            textSecondary = textSecondary.value,
            card = card.value,
            icon = icon.value,
            info = info.value,
            warn = warn.value,
            success = success.value,
            error = error.value,
            hot = hot.value,
            primaryBtn = primaryBtn.value,
            secondBtn = secondBtn.value,
            placeholder = placeholder.value,
            surface = surface.value,
            gray = gray.value
        )


    val systemUi = rememberSystemUiController()
    systemUi.setStatusBarColor(phoneColors.surface)

    ProvideWindowInsets {
        CompositionLocalProvider(LocalAppColors provides phoneColors, content = content)
    }
}