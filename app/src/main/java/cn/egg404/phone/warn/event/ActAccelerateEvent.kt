package cn.egg404.phone.warn.event


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import cn.egg404.phone.warn.WarnAlarmManager
import cn.egg404.phone.warn.data.WarnAlarmData
import kotlin.math.abs


/**
 * 加速度传感器，模拟微信摇一摇
 */
class ActAccelerateEvent {
    private var sensorManager: SensorManager? = null
    private var listener: SensorEventListener? = null

    fun event(context: Context, warnData: WarnAlarmData) {

        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                //加速度可能会是负值，所以要取它们的绝对值
                val xValue = abs(event.values[0])
                val yValue = abs(event.values[1])
                val zValue = abs(event.values[2])

                if (xValue > 15 || yValue > 15 || zValue > 15) {
                    if (WarnAlarmManager.currentWarn == null) {
                        WarnAlarmManager.warnAlarm(warnData)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager?.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)

    }


    fun stop() {
        if (sensorManager != null) {
            sensorManager!!.unregisterListener(listener)
        }
    }
}

