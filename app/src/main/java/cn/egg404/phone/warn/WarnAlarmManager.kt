package cn.egg404.phone.warn

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.Vibrator
import androidx.core.net.toUri
import cn.egg404.phone.MyApplication
import cn.egg404.phone.R
import cn.egg404.phone.common.Storage
import cn.egg404.phone.data.bean.WarnData
import cn.egg404.phone.service.FloatWindowsService
import cn.egg404.phone.ui.page.main.alarm_switch.AlarmSwitchObj
import cn.egg404.phone.ui.page.main.alarm_switch.AlarmSwitchStore
import cn.egg404.phone.ui.page.main.warn.WarnObj
import cn.egg404.phone.ui.page.main.warn.WarnStore
import cn.egg404.phone.utils.EggUtil
import cn.egg404.phone.utils.FlashLightUtil
import cn.egg404.phone.utils.SharedPref
import cn.egg404.phone.warn.WarnAlarmManagerData.isFlashLamp
import cn.egg404.phone.warn.WarnAlarmManagerData.isMedia
import cn.egg404.phone.warn.WarnAlarmManagerData.isShock
import cn.egg404.phone.warn.data.WarnAlarmData
import java.util.*


object WarnAlarmManagerData {
    var isMedia by SharedPref("is_media", true)
    var isShock by SharedPref("is_shock", true)
    var isFlashLamp by SharedPref("is_flash_lamp", true)
}

object WarnAlarmManager {
    var currentWarn: WarnAlarmData? = null
    private var mMediaPlayer: MediaPlayer? = null
    private val list = ArrayList<WarnAData>()
    private var vibrator: Vibrator? = null
    var mediaTimer: Timer? = null
    var warnTime: Long = 0

    /***
     * 启动
     */
    fun start() {
        list.forEach {
            it.warnEvent.run()
        }
    }

    /***
     * 添加警告
     */
    fun addWarnAlarm(warn: WarnAlarmData, warnEvent: WarnEventListener) {
        list.add(WarnAData(warn, warnEvent))
    }

    /***
     * 删除警告
     */
    fun removeWarnAlarm(index: Int) {
        list.removeAt(index)
    }

    /***
     * 播放警报
     */
    fun warnAlarm(warn: WarnAlarmData) {
        var w = warn
        AlarmSwitchStore.getAlarmList().forEach {
            if (warn.name == it.name) {
                w = it
            }
        }

        if (w.enabled && EggUtil.canDrawOverlays(MyApplication.CONTEXT) && AlarmSwitchObj.isService.value && WarnMObj.pass.value != "" && (System.currentTimeMillis() - 15 * 1000) > warnTime && FloatWindowsService.num == 0) {
            this.currentWarn = w

            mMediaPlayer = if (!Storage.getAudioPath().exists()) {
                MediaPlayer.create(MyApplication.CONTEXT, R.raw.warn)
            } else {
                MediaPlayer.create(MyApplication.CONTEXT, Storage.getAudioPath().toUri())
            }

            MyApplication.CONTEXT.apply {

                warnTime = System.currentTimeMillis()
                record()
                FloatWindowsService.type = 0
                startService(Intent(this, FloatWindowsService::class.java))
                playMedia()
                startFlashLamp()
                playShock()

            }
        } else {
            this.currentWarn = null
        }


    }

    private fun record() {
        this.currentWarn?.let {
            val warnData =
                WarnData(
                    WarnStore.getWarnList().size + 1,
                    System.currentTimeMillis(),
                    it.warnContent
                )

            WarnStore.addWarn(warnData)
            WarnObj.warnList.value = WarnStore.getWarnList()
        }
    }

    private fun playMedia() {
        if (isMedia) {

            val countDownInterval = (1500 / 3).toLong()
            mediaTimer = Timer(countDownInterval) {
                mMediaPlayer?.start()
            }
            mediaTimer?.start()
        }
    }

    private fun playShock() {
        if (isShock) {
            vibrator =
                MyApplication.CONTEXT.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            val patter = longArrayOf(1000, 1000, 2000, 2050)
            vibrator?.vibrate(patter, 0)
        }
    }

    private fun startFlashLamp() {
        if (isFlashLamp) {
            try {
                FlashLightUtil.sos(4)
            } catch (e: Exception) {

            }
        }
    }

    private fun stopShock() {
        if (isShock) {
            vibrator?.cancel()
        }
    }

    private fun stopFlashLamp() {
        if (isFlashLamp) {
            FlashLightUtil.offSos()
        }
    }

    private fun stopMedia() {
        if (isMedia) {
            mMediaPlayer?.stop()
            mediaTimer?.cancel()
        }
    }

    fun stopWarn() {
        stopMedia()
        stopFlashLamp()
        stopShock()
    }

    class Timer(countDownInterval: Long, val tick: (Long) -> Unit) :
        CountDownTimer(Long.MAX_VALUE, countDownInterval) {
        override fun onFinish() {
            start()
        }

        override fun onTick(millisUntilFinished: Long) = tick(millisUntilFinished)
    }
}

data class WarnAData(val warn: WarnAlarmData, val warnEvent: WarnEventListener)