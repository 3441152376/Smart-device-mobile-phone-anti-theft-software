package cn.egg404.phone.warn.event

import android.content.Context
import cn.egg404.phone.utils.ProximitySensorManager
import cn.egg404.phone.warn.WarnAlarmManager
import cn.egg404.phone.warn.data.WarnAlarmData

class PocketDetectorEvent {
    private var type_proximity = true
    private var mProximitySensorManager: ProximitySensorManager? = null

    fun event(context: Context, warnData: WarnAlarmData) {
        if (mProximitySensorManager == null) {
            mProximitySensorManager =
                ProximitySensorManager(
                    context,
                    object : ProximitySensorManager.Listener {
                        override fun onNear() {
                            // 靠近口袋事件
                            type_proximity = true
                        }

                        override fun onFar() {
                            println("靠2")
                            if (WarnAlarmManager.currentWarn == null) {
                                warnData.let { WarnAlarmManager.warnAlarm(it) }
                            }

                            type_proximity = false
                        }
                    })
            mProximitySensorManager!!.enable()
        } else if (mProximitySensorManager != null) {
            mProximitySensorManager!!.disable(false)
            mProximitySensorManager = null
        }
    }
}