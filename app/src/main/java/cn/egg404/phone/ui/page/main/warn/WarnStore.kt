package cn.egg404.phone.ui.page.main.warn

import cn.egg404.phone.data.bean.WarnData
import cn.egg404.phone.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object WarnStore {
    private var alarmList: String by SharedPref(
        "warn_list", "[]".trimIndent()
    )

    fun getWarnList(): ArrayList<WarnData> {
        val gson = Gson()
        return gson.fromJson<ArrayList<WarnData>>(alarmList, getWarnListType())
    }

    fun updateWarnList(al: ArrayList<WarnData>) {
        alarmList = toJson(al)
    }

    fun addWarn(warn: WarnData) {
        val wl = getWarnList()
        wl.add(warn)
        alarmList = toJson(wl)
    }

    private fun toJson(l: ArrayList<WarnData>): String {
        val gson = Gson()
        return gson.toJson(l, getWarnListType())
    }

    private fun getWarnListType(): Type {
        return object : TypeToken<ArrayList<WarnData>>() {}.type
    }
}