package cn.egg404.phone.ui.widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.egg404.phone.theme.H4
import cn.egg404.phone.theme.H5
import cn.egg404.phone.theme.PhoneTheme

@Composable
fun TimeLine(time: String, con: String, index: Int, size: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Text(
            text = time,
            color = if (index == size) PhoneTheme.colors.theme else PhoneTheme.colors.textPrimary.copy(
                alpha = 0.4f
            ),
            modifier = Modifier.width(60.dp)
        )

        Column(
            modifier = Modifier
                .width(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .border(
                        BorderStroke(
                            2.dp,
                            if (index == size) PhoneTheme.colors.theme else PhoneTheme.colors.gray
                        ), RoundedCornerShape(100)
                    )
                    .size(12.dp)
            )

            Spacer(
                modifier = Modifier
                    .background(if (index == size) PhoneTheme.colors.theme else PhoneTheme.colors.gray)
                    .width(3.dp)
                    .fillMaxHeight()
            )
        }

        val shakingOffset = remember {
            Animatable(0f)
        }

        LaunchedEffect(Unit) {
            if (index == size) {
                while (true) {
                    shakingOffset.animateTo(
                        0f,
                        animationSpec = spring(0.3f, 600f),
                        initialVelocity = -180f,
                    )
                }
            }

        }

        Row(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = con,
                fontSize = H5,
                color = if (index == size) PhoneTheme.colors.textPrimary else PhoneTheme.colors.textPrimary.copy(
                    alpha = 0.4f
                )
            )

            FontIcon(
                iconName = "\ue630",
                color = if (index == size) PhoneTheme.colors.error else PhoneTheme.colors.textPrimary.copy(
                    alpha = 0.4f
                ),
                modifier = Modifier
                    .padding(start = 6.dp)
                    .offset(shakingOffset.value.dp, shakingOffset.value.dp),
                fontSize = H4
            )
        }
    }
}