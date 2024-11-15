package cn.egg404.phone.ui.page.main.alarm_switch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cn.egg404.phone.warn.data.WarnAlarmData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmSwitchViewModel @Inject constructor() : ViewModel() {

    var viewStates by mutableStateOf(AlarmSwitchPageViewState())
        private set

    init {
        viewStates = viewStates.copy(
            alarmItemDats = AlarmSwitchStore.getAlarmList()
        )
    }

    fun dispatch(alarmSwitchViewAction: AlarmSwitchViewAction) {
        when (alarmSwitchViewAction) {
            is AlarmSwitchViewAction.UpdateAlarmList -> {
                updateAlarmList(alarmSwitchViewAction.warnList)
            }

        }
    }

    private fun updateAlarmList(warnList: ArrayList<WarnAlarmData>) {
        AlarmSwitchStore.updateAlarmList(warnList)
        viewStates = viewStates.copy(alarmItemDats = warnList, random = getRandom())
    }

    private fun getRandom(): String {
        return (0..10000).random().toString()
    }

}

data class AlarmSwitchPageViewState(
    val alarmItemDats: ArrayList<WarnAlarmData> = ArrayList(), val random: String = ""
)

sealed class AlarmSwitchViewAction {
    data class UpdateAlarmList(val warnList: ArrayList<WarnAlarmData> = ArrayList()) :
        AlarmSwitchViewAction()

}
/*
listOf(
                AlarmItemData("移动", "\ue624", "移动手机报警"),
                AlarmItemData("拔耳机", "\uea10", "拔掉耳机报警"),
                AlarmItemData("拔充电器", "\ue601", "拔掉充电器报警"),
                AlarmItemData("亮屏", "\ue63c", "手机亮起报警"),
                AlarmItemData("分贝超标", "\ue7e1", "周围声音超过指定分贝报警"),
                AlarmItemData("开启飞行模式", "\ue761", "开启飞行模式报警"),
                AlarmItemData("海拔过高", "\ue70c", "手机到指定高度报警"),
                AlarmItemData("手机翻转", "\ue64d", "手机翻过来报警"),
                AlarmItemData("APP使用时长超标", "\ue728", "某APP使用超过指定时间报警"),
                AlarmItemData("定位报警", "\ue619", "离开某区域报警"),
                AlarmItemData("蓝牙断开", "\ueadb", "蓝牙断开报警")
AlarmItemData("抬起", "\ue624", "抬起报警", true)
)*/
