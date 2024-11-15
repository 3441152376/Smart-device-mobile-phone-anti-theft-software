package cn.egg404.phone.warn

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cn.egg404.phone.utils.SharedPref

object WarnMObj {
    var password: String by SharedPref("password", "")
    var pass: MutableState<String> = mutableStateOf(password)
}