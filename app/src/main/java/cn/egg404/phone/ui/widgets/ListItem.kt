package cn.egg404.phone.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cn.egg404.phone.data.bean.AlarmSettingItemData
import cn.egg404.phone.data.bean.OutWardItemData
import cn.egg404.phone.theme.*
import cn.egg404.phone.warn.data.WarnAlarmData


@Composable
fun CardItem(
    icon: String,
    text: String,
    onclick: () -> Unit = {},
    bgColor: Color = PhoneTheme.colors.surface,
    outLine: Boolean = false
) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .border(
                BorderStroke(1.dp, if (outLine) bgColor else PhoneTheme.colors.surface),
                RoundedCornerShape(if (outLine) 8.dp else 0.dp)
            )
            .clip(RoundedCornerShape(if (outLine) 0.dp else 8.dp))
            .background(if (outLine) PhoneTheme.colors.gray else bgColor)
            .clickable(onClick = onclick)
            .fillMaxWidth()
            .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        FontIcon(
            iconName = icon,
            color = if (outLine) bgColor else PhoneTheme.colors.bgTextColor,
            modifier = Modifier.padding(start = 16.dp), fontSize = H3
        )

        Text(
            text = text, modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f), color = if (outLine) bgColor else PhoneTheme.colors.bgTextColor
        )
    }
}

@Composable
fun AudioItem(
    text: String,
    onclick: () -> Unit = {},
    bgColor: Color = PhoneTheme.colors.surface,
    selected: Boolean? = null, outLine: Boolean = false
) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .border(
                BorderStroke(1.dp, if (outLine) bgColor else PhoneTheme.colors.surface),
                RoundedCornerShape(if (outLine) 8.dp else 0.dp)
            )
            .clip(RoundedCornerShape(if (outLine) 0.dp else 8.dp))
            .background(if (outLine) Color.White else bgColor)
            .clickable(onClick = onclick)
            .fillMaxWidth()
            .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text, modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f), color = if (outLine) bgColor else PhoneTheme.colors.bgTextColor
        )

        if (selected != null) {
            Switch(selected = selected, modifier = Modifier.padding(end = 16.dp))
        }
    }
}

@Composable
fun PermissionItem(name: String, icon: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(PhoneTheme.colors.theme2)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FontIcon(iconName = icon, color = PhoneTheme.colors.bgTextColor, fontSize = H4)
        Text(
            text = name,
            color = PhoneTheme.colors.bgTextColor,
            fontSize = H5,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Composable
fun PermissionItem(name: String, icon: String, enabled: Boolean) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (enabled) PhoneTheme.colors.theme2 else PhoneTheme.colors.surface)
            .width(100.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FontIcon(iconName = icon, color = PhoneTheme.colors.bgTextColor, fontSize = H4)

        Text(
            text = name,
            color = PhoneTheme.colors.bgTextColor,
            fontSize = H5,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}


@Composable
fun AlarmSettingsItem(
    alarmSettingItemData: AlarmSettingItemData,
    selected: Boolean,
    onChangeClick: (selected: Boolean) -> Unit
) {
    val isEvenNum = (alarmSettingItemData.index + 1) % 2 == 0
    Row(
        modifier = Modifier
            .padding(
                start = if (isEvenNum) 16.dp else 0.dp,
                end = if (!isEvenNum) 16.dp else 0.dp,
                top = if (alarmSettingItemData.index == 0 || alarmSettingItemData.index == 1) 0.dp else 16.dp
            )
            .clip(RoundedCornerShape(ItemRadius))
            .clickable {
                onChangeClick(!selected)
            }
            .fillMaxWidth()
            .background(PhoneTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = alarmSettingItemData.name,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis, color = PhoneTheme.colors.bgTextColor
        )
        if (alarmSettingItemData.name != "设置解锁密码") {
            Switch(selected = selected)
        } else {
            Spacer(
                modifier = Modifier
                    .size(30.dp)
            )
        }
    }
}

@Composable
fun OutWardSettingsItem(
    outWardSettingItemData: OutWardItemData,
    selected: Boolean,
    onChangeClick: (selected: Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(
                top = if (outWardSettingItemData.index == 0 || outWardSettingItemData.index == 1) 0.dp else 16.dp
            )
            .clip(RoundedCornerShape(ItemRadius))
            .clickable {
                onChangeClick(!selected)
            }
            .fillMaxWidth()
            .background(PhoneTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = outWardSettingItemData.name,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis, color = PhoneTheme.colors.textPrimary
        )
        Switch(selected = selected)
    }
}

//开关列表项样式
@Composable
fun SwitchListItem(
    selected: Boolean,
    alarmItemData: WarnAlarmData, index: Int,
    onChangeClick: (selected: Boolean) -> Unit
) {
    val isEvenNum = (index) % 2 == 0

    Row(
        modifier = Modifier
            .padding(
                start = if (isEvenNum) 16.dp else 0.dp,
                end = if (!isEvenNum) 16.dp else 0.dp,
                top = if (index == 1 || index == 2) 0.dp else 16.dp
            )
            .clickable {
                onChangeClick(!selected)
            }
            .fillMaxWidth()
            .clip(RoundedCornerShape(ItemRadius))
            .background(PhoneTheme.colors.surface)
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                FontIcon(
                    iconName = alarmItemData.icon,
                    color = PhoneTheme.colors.bgTextColor,
                    fontSize = H4
                )

                Text(
                    text = alarmItemData.name,
                    modifier = Modifier
                        .padding(start = 6.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = PhoneTheme.colors.bgTextColor
                )
            }


            Text(
                text = alarmItemData.description,
                color = PhoneTheme.colors.bgTextColor.copy(alpha = 0.5f),
                modifier = Modifier.padding(start = 2.dp, top = 6.dp)
            )


        }

        Switch(selected = selected, modifier = Modifier.padding(end = 8.dp))

        if (alarmItemData.isConfig) {
            FontIcon(
                iconName = "\ue8b7",
                color = Color.White,
                fontSize = H4,
                modifier = Modifier.clickable {
                    alarmItemData.configData
                })
        }
    }
}