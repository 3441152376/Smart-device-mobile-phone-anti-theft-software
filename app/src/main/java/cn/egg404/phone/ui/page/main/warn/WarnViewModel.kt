package cn.egg404.phone.ui.page.main.warn

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cn.egg404.phone.data.bean.WarnData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WarnViewModel @Inject constructor() : ViewModel() {
    var viewStates by mutableStateOf(WarnViewState())
        private set

    init {
        viewStates = viewStates.copy(
            warnAlarmList = WarnStore.getWarnList()
        )
    }

    fun dispatch(warnViewAction: WarnViewAction) {
        when (warnViewAction) {
            is WarnViewAction.AddWarn -> {
                addWarn(warnViewAction.warn)
            }

            is WarnViewAction.UpdateWarnList -> {
                updateWarnList(warnViewAction.warnList)
            }
        }
    }

    private fun updateWarnList(al: ArrayList<WarnData>) {
        WarnStore.updateWarnList(al)
        cyL()
    }

    private fun addWarn(warn: WarnData) {
        WarnStore.addWarn(warn)
        cyL()
    }

    private fun cyL() {
        viewStates = viewStates.copy(
            warnAlarmList = WarnStore.getWarnList(), random = getRandom()
        )
    }

    private fun getRandom(): String {
        return (0..10000).random().toString()
    }
}

data class WarnViewState(
    val warnAlarmList: ArrayList<WarnData> = ArrayList(),
    val random: String = ""
)

sealed class WarnViewAction {
    data class UpdateWarnList(val warnList: ArrayList<WarnData> = ArrayList()) :
        WarnViewAction()

    data class AddWarn(val warn: WarnData) : WarnViewAction()
}