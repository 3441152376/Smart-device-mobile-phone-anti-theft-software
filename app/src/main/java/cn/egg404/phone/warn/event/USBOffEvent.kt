package cn.egg404.phone.warn.event

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import cn.egg404.phone.warn.WarnAlarmManager
import cn.egg404.phone.warn.data.WarnAlarmData


class USBOffEvent() {
    private var warnData: WarnAlarmData? = null
    fun event(context: Context, warnData: WarnAlarmData) {
        this.warnData = warnData
        val receiver = USBReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        context.registerReceiver(receiver, filter)
    }

    inner class USBReceiver() : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (Intent.ACTION_POWER_DISCONNECTED == action) {
                if (WarnAlarmManager.currentWarn == null) {
                    warnData?.let { WarnAlarmManager.warnAlarm(it) }
                }
            }
        }
    }

}
