package cn.egg404.phone.ui.page.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cn.egg404.phone.MainData
import cn.egg404.phone.MyApplication
import cn.egg404.phone.data.bean.AlarmSettingItemData
import cn.egg404.phone.data.bean.OutWardItemData
import cn.egg404.phone.data.bean.PermissionData
import cn.egg404.phone.utils.EggUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingPageViewModel @Inject constructor() : ViewModel() {
    var viewStates by mutableStateOf(SettingPageViewStates())
        private set

    init {
        viewStates = viewStates.copy(
            alarmList = listOf(
                AlarmSettingItemData(name = "震动", index = 0),
                AlarmSettingItemData(name = "闪光灯", index = 1),
                AlarmSettingItemData(name = "报警音", index = 2),
                AlarmSettingItemData(name = "设置解锁密码", index = 3)
            ),
            outWardList = listOf(OutWardItemData(name = "夜间模式", index = 0)),
            darkModeSelected = SettingPageStore.isDarkMode, permissionList = listOf(
                PermissionData("悬浮窗", "\ue60b", EggUtil.canDrawOverlays(MyApplication.CONTEXT))
            )
        )
    }

    fun dispatch(settingPageAction: SettingPageAction) {
        when (settingPageAction) {
            is SettingPageAction.UpdateDarkModeSel -> {
                updateDarkModeSel(settingPageAction.darkModeSelected)
            }

            is SettingPageAction.UpdateWarnSound -> {
                updateWarnSound(settingPageAction.sound)
            }
        }
    }

    private fun updateDarkModeSel(sel: Boolean) {
        SettingPageStore.isDarkMode = sel
        MainData.isDark.value = sel
        viewStates = viewStates.copy(darkModeSelected = sel)
    }

    private fun updateWarnSound(sel: Boolean) {
        SettingObj.warnSound.value = sel
    }
}

data class SettingPageViewStates(
    val alarmList: List<AlarmSettingItemData> = emptyList(),
    val outWardList: List<OutWardItemData> = emptyList(),
    val darkModeSelected: Boolean = false,
    val warnSound: Boolean = false,
    val permissionList: List<PermissionData> = emptyList()
)

sealed class SettingPageAction {
    data class UpdateDarkModeSel(val darkModeSelected: Boolean) : SettingPageAction()
    data class UpdateWarnSound(val sound: Boolean) : SettingPageAction()
}