package cn.egg404.phone.ui.page.main.alarm_switch

import androidx.compose.runtime.mutableStateOf
import cn.egg404.phone.utils.SharedPref
import cn.egg404.phone.warn.data.WarnAlarmData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object AlarmSwitchStore {
    var isService by SharedPref("is_service", false)


    private var alarmList: String by SharedPref(
        "alarm_list_5",
        """
            [
            {"description":"抬起报警","enabled":false,"icon":"\ue624","name":"抬起","audio":"","warnContent":"有人抬起了你的手机！","isConfig":false},
            {"description":"手机移动触发报警","enabled":false,"icon":"\ue624","name":"移动","audio":"","warnContent":"有人碰了你的手机！","isConfig":false},
            {"description":"拔充电器报警","enabled":false,"icon":"\ue601","name":"拔充电器","audio":"","warnContent":"有人拔了你充电器！","isConfig":false},
            {"description":"拔掉耳机报警","enabled":false,"icon":"\ue601","name":"拔耳机","audio":"","warnContent":"有人拔了你耳机！","isConfig":false},
            {"description":"开启飞行模式报警","enabled":false,"icon":"\ue601","name":"飞行模式","audio":"","warnContent":"手机开启了飞行模式！","isConfig":false},
            {"description":"开启口袋模式报警","enabled":false,"icon":"\ue6dc","name":"口袋模式","audio":"","warnContent":"手机离开了口袋","isConfig":false}
            ]
        """.trimIndent()
    )

    init {
        /*alarmList = "[]"
        val l = getAlarmList()

        l.add(
            WarnAlarmData(
                index = 1,
                name = "抬起",
                warnContent = "有人抬起了你的手机",
                audio = "",
                enabled = false,
                icon = "\ue624",
                description = "抬起报警"
            )
        )

        l.add(
            WarnAlarmData(
                index = 2,
                name = "移动",
                warnContent = "有人碰了你的手机",
                audio = "",
                enabled = false,
                icon = "\ue624",
                description = "手机移动触发报警"
            )
        )*/

/*        l.add(
            WarnAlarmData(
                index = 3,
                name = "拔充电器",
                warnContent = "有人拔了你充电器",
                audio = "",
                enabled = false,
                icon = "\ue601",
                description = "拔充电器报警"
            )
        )*/

//        alarmList = toJson(l)
    }

    fun getAlarmList(): ArrayList<WarnAlarmData> {
        val gson = Gson()
        return gson.fromJson<ArrayList<WarnAlarmData>>(alarmList, getAlarmListType())
    }

    fun updateAlarmList(al: ArrayList<WarnAlarmData>) {
        alarmList = toJson(al)
    }

    fun updateAlarm(al: WarnAlarmData) {
        val l = getAlarmList()
        var i = 0

        l.forEach {
            if (it.name == al.name) {
                l[i] = al
            }
            i++
        }


        alarmList = toJson(l)
    }

    private fun toJson(al: ArrayList<WarnAlarmData>): String {
        val gson = Gson()
        return gson.toJson(al, getAlarmListType())
    }

    private fun getAlarmListType(): Type {
        return object : TypeToken<ArrayList<WarnAlarmData>>() {}.type
    }
}