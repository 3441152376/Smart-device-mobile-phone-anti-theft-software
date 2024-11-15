package cn.egg404.phone.ui.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import cn.egg404.phone.R


@Composable
fun FontIcon(
    iconName: String,
    modifier: Modifier = Modifier,
    color: Color,
    fontSize: TextUnit = 0.sp,
    style: TextStyle? = null
) {
    if (style == null) {
        Text(
            text = iconName,
            fontFamily = FontFamily(Font(R.font.iconfont)),
            modifier = modifier,
            color = color, fontSize = fontSize
        )
    } else {
        Text(
            text = iconName, color = color,
            fontFamily = FontFamily(Font(R.font.iconfont)),
            modifier = modifier,
            style = style
        )
    }

}