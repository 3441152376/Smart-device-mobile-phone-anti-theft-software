package cn.egg404.phone.ui.page.main.warn

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cn.egg404.phone.data.bean.WarnData

object WarnObj {
    var warnList: MutableState<ArrayList<WarnData>> = mutableStateOf(ArrayList<WarnData>())
}