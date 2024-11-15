package cn.egg404.phone.ui.page.main.me

import androidx.compose.runtime.mutableStateOf
import cn.bmob.v3.BmobUser

object MeObj {
    val loginState = mutableStateOf(BmobUser.isLogin())
    val loginLoadState = mutableStateOf(false)
}