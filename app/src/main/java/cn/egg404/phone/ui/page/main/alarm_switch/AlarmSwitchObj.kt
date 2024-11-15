package cn.egg404.phone.ui.page.main.alarm_switch

import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import cn.egg404.phone.warn.data.WarnAlarmData

object AlarmSwitchObj {
    val isService = mutableStateOf(true)
    var viewModel: AlarmSwitchViewModel? = null
    var justNowAlarm: WarnAlarmData? = null






}