package cn.egg404.phone.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import cn.egg404.phone.MainActivity
import cn.egg404.phone.R
import cn.egg404.phone.ui.page.main.alarm_switch.AlarmSwitchStore
import cn.egg404.phone.warn.WarnAlarmManager
import cn.egg404.phone.warn.WarnEventListener
import cn.egg404.phone.warn.event.*


class AlarmService() : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        createNotificationChannel()
        AlarmSwitchStore.getAlarmList().forEach {
            WarnAlarmManager.addWarnAlarm(it,
                object : WarnEventListener {
                    override fun run() {
                        when (it.name) {


                            "抬起" -> {
                                WakeUpEvent().event(this@AlarmService, it)
                            }

                            "移动" -> {
                                MoveEvent().event(this@AlarmService, it)
                            }

                            "拔充电器" -> {
                                USBOffEvent().event(this@AlarmService, it)
                            }

                            "拔耳机" -> {
                                EarphoneEvent().event(this@AlarmService, it)
                            }

                            "飞行模式" -> {
                                FlyEvent().event(this@AlarmService, it)
                            }

                            "口袋模式" -> {
                                PocketDetectorEvent().event(this@AlarmService, it)
                            }

                            "断网报警" -> {
                                
                            }

                            "蓝牙报警" -> {

                            }

                            "应用监听报警" -> {

                            }
                        }
                    }
                })
        }
//        val wakeData = WarnAlarmData("抬起报警", "你犯法了你知道吗，小老弟", "", "5642", true,"","你犯法了你知道吗，小老弟")


        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        WarnAlarmManager.start()
        return super.onStartCommand(intent, flags, startId)
    }


    private fun createNotificationChannel() {
        var notificationManager: NotificationManager? = null

        val screenNotificationId = "SCREEN_NOTIFICATION"
        val builder: Notification.Builder =
            Notification.Builder(this.applicationContext)
        val nfIntent = Intent(this, MainActivity::class.java)
        var pendingFlags = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingFlags = PendingIntent.FLAG_IMMUTABLE
        }

        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, pendingFlags))
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.logo
                )
            )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText("正在监听报警")
            .setWhen(System.currentTimeMillis())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(screenNotificationId)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(
                screenNotificationId,
                "notification_name",
                NotificationManager.IMPORTANCE_LOW
            )

            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = builder.build()
        notification.defaults = Notification.DEFAULT_SOUND
        startForeground(110, notification)
    }
}