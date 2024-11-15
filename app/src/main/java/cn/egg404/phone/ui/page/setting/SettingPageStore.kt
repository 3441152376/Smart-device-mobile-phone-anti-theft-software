package cn.egg404.phone.ui.page.setting

import cn.egg404.phone.utils.SharedPref

object SettingPageStore {
    var isDarkMode by SharedPref("is_dark", false)
}