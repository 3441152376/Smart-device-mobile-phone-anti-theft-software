package cn.egg404.phone.warn.event

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import cn.egg404.phone.warn.WarnAlarmManager
import cn.egg404.phone.warn.data.WarnAlarmData
import kotlin.math.abs

class MoveEvent {

    private var oldY = 0f
    private var subY = 0f
    private var context: Context? = null
    private var warnData: WarnAlarmData? = null

    fun event(context: Context, warnData: WarnAlarmData) {
        this.context = context
        this.warnData = warnData

        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorManager.registerListener(
            PhoneMoveEventListener(),
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    private inner class PhoneMoveEventListener : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                smartAwake(event)
            }
        }

        private fun smartAwake(event: SensorEvent) {
            val values = event.values
            val x = values[0]
            val y = values[1]
            val z = values[2]
            subY = y - oldY

            if (abs(x) > 3.5 || abs(y) > 3.5 || abs(z) > 11.5) {
                if (WarnAlarmManager.currentWarn == null) {

                    warnData?.let { WarnAlarmManager.warnAlarm(it) }
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }
}