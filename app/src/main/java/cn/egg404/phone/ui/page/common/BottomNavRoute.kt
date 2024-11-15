package cn.egg404.phone.ui.page.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import cn.egg404.phone.R

sealed class BottomNavRoute(
    var routeName: String,
    @StringRes var stringId: Int,
    val icon: String
) {
    object Alarm : BottomNavRoute(RouteName.ALARM, R.string.alarm, "\ue8b9")
    object AlarmSwitch :
        BottomNavRoute(RouteName.ALARM_SWITCH, R.string.alarm_switch, "\ue661")

    object Me : BottomNavRoute(RouteName.ME, R.string.me, "\ue69b")
}