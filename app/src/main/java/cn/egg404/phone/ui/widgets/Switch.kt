package cn.egg404.phone.ui.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cn.egg404.phone.theme.H5
import cn.egg404.phone.theme.PhoneTheme

@Composable
fun Switch(
    selected: Boolean,
    selColor: Color = PhoneTheme.colors.theme,
    unSel: Color = PhoneTheme.colors.background,
    onChange: ((b: Boolean) -> Unit)? = null, modifier: Modifier = Modifier
) {
    val bgColor = animateColorAsState(
        targetValue = if (selected) selColor else unSel,
        TweenSpec(600)
    )

    var mod: Modifier =Modifier
    mod = if (onChange == null) {
        modifier
            .clip(RoundedCornerShape(100))
            .background(bgColor.value)
            .size(30.dp)
    } else {
        modifier
            .clip(RoundedCornerShape(100))
            .clickable(onClick = {
                onChange(!selected)
            })
            .background(bgColor.value)
            .size(30.dp)
    }

    Row(
        modifier = mod,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val color = animateColorAsState(
            targetValue = if (selected) PhoneTheme.colors.background else PhoneTheme.colors.textPrimary,
            TweenSpec(600)
        )

        FontIcon(
            iconName = if (selected) "\ueaf1" else "\ue623",
            color = color.value,
            fontSize = H5
        )
    }
}