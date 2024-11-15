package cn.egg404.phone.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.egg404.phone.R
import cn.egg404.phone.theme.*

@Composable
fun MainTitleBar(title: String = "别动我手机", settingOnclick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PhoneTheme.colors.surface)
            .padding(horizontal = ContentHorPadding)
            .height(80.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.mipmap.logo),
            contentDescription = "logo",
            Modifier.size(30.dp)
        )

        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = TextStartPadding),
            fontSize = H4, color = PhoneTheme.colors.bgTextColor
        )

        FontIcon(
            iconName = "\ue612",
            color = PhoneTheme.colors.bgTextColor,
            fontSize = H3,
            modifier = Modifier.clickable(onClick = settingOnclick)
        )
    }
}

@Composable
fun ContentTitleBar(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 26.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .clip(RoundedCornerShape(100))
                .size(10.dp)
                .background(PhoneTheme.colors.gray)
        )

        Text(
            text = title,
            modifier = Modifier.padding(start = TextStartPadding),
            fontSize = H5,
            color = PhoneTheme.colors.textPrimary
        )
    }
}

@Composable
fun SecondPageTitleBar(title: String, backOnclick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PhoneTheme.colors.surface)
            .padding(horizontal = ContentHorPadding)
            .height(SecondTitleHeight), verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(100))
                .clickable(onClick = backOnclick)
                .background(PhoneTheme.colors.surface)
                .padding(10.dp)
        ) {
            FontIcon(
                iconName = "\ue600",
                color = PhoneTheme.colors.bgTextColor,
                fontSize = H4, modifier = Modifier.background(PhoneTheme.colors.surface)
            )
        }

        Text(
            text = title,
            modifier = Modifier.padding(start = 2.dp),
            fontSize = H4,
            color = PhoneTheme.colors.bgTextColor
        )
    }
}