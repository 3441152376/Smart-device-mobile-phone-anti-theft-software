package cn.egg404.phone.warn.event

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import cn.egg404.phone.warn.WarnAlarmManager
import cn.egg404.phone.warn.data.WarnAlarmData

class FlyEvent {
    private var warnData: WarnAlarmData? = null
    fun event(context: Context, warnData: WarnAlarmData) {
        this.warnData = warnData
        val receiver = FlyReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        context.registerReceiver(receiver, filter)
    }

    inner class FlyReceiver() : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
                if (intent.hasExtra("state")) {
                    if (intent.getBooleanExtra("state", false)) {
                        if (WarnAlarmManager.currentWarn == null) {
                            warnData?.let { WarnAlarmManager.warnAlarm(it) }
                        }
                    }
                }
            }
        }
    }
}