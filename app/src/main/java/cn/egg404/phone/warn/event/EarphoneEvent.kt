package cn.egg404.phone.warn.event

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import cn.egg404.phone.warn.WarnAlarmManager
import cn.egg404.phone.warn.data.WarnAlarmData

class EarphoneEvent {
    private var warnData: WarnAlarmData? = null
    fun event(context: Context, warnData: WarnAlarmData) {
        this.warnData = warnData
        val receiver = EarphoneReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_HEADSET_PLUG)
        context.registerReceiver(receiver, filter)
    }

    inner class EarphoneReceiver() : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.hasExtra("state")) {
                if (intent.getIntExtra("state", 0) == 0) {
                    if (WarnAlarmManager.currentWarn == null) {
                        warnData?.let { WarnAlarmManager.warnAlarm(it) }
                    }
                }
            }
        }
    }
}