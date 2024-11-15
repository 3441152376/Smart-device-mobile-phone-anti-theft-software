package cn.egg404.phone.warn.data

data class WarnAlarmData(
    val index: Int, //索引
    val name: String, //警报名字
    val warnContent: String, //警报提示
    val audio: String, //报警音频
    var enabled: Boolean,
    val icon: String, //图标
    val description: String, //解释
    val isConfig: Boolean, //是否需要配置,
    val configData: Any
)