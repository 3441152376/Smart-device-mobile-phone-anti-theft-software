package cn.egg404.phone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cn.egg404.phone.common.AppInfo
import cn.egg404.phone.utils.SharedPref
import com.tencent.tauth.Tencent


object MainData {
    val isDark = mutableStateOf(false)
    var isOnce by SharedPref("is_once", true)
    var browseOnly by mutableStateOf(false)
    var isO by mutableStateOf(isOnce)
    var mTencent: Tencent? = null
}