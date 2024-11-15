package cn.egg404.phone.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import cn.egg404.phone.MyApplication
import cn.egg404.phone.theme.PhoneTheme
import cn.egg404.phone.ui.page.main.alarm_switch.AlarmSwitchObj
import cn.egg404.phone.ui.screen.LockScreen
import cn.egg404.phone.warn.WarnAlarmManager
import cn.egg404.phone.warn.WarnMObj
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep


/**
 * 悬浮取色
 */
class FloatWindowsService : Service() {
    private val winlay: WindowManager.LayoutParams = WindowManager.LayoutParams()
    private val lifecycleOwner = MyLifecycleOwner()
    private var mWindowManager: WindowManager? = null
    private var composeView: ComposeView? = null
    private var params: WindowManager.LayoutParams? = null

    companion object {
        var type = 0
        var num by mutableStateOf(0)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
//        AlarmSwitchObj.isService.value = false

        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            winlay.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            winlay.type = WindowManager.LayoutParams.TYPE_PHONE
        }

        winlay.format = PixelFormat.RGBA_8888
        winlay.width = 500
        winlay.height = 500
        winlay.x = 100
        winlay.y = 100


        val layoutFlag: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )


        when (type) {
            0 -> {
                showOverlay()
            }

            1 -> {
                showCountDownOverlay()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }


    private fun showCountDownOverlay() {
        composeView = countDownView()
        val viewModelStore = ViewModelStore()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
        composeView?.let {
            ViewTreeLifecycleOwner.set(it, lifecycleOwner)
            ViewTreeViewModelStoreOwner.set(it) { viewModelStore }
            ViewTreeSavedStateRegistryOwner.set(it, lifecycleOwner)
            mWindowManager?.addView(composeView, params)
        }
    }


    private fun showOverlay() {
        composeView = uiView()
        val viewModelStore = ViewModelStore()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
        composeView?.let {
            ViewTreeLifecycleOwner.set(it, lifecycleOwner)
            ViewTreeViewModelStoreOwner.set(it) { viewModelStore }
            ViewTreeSavedStateRegistryOwner.set(it, lifecycleOwner)
            mWindowManager?.addView(composeView, params)
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
    private fun countDownView(): ComposeView {
        val composeView = ComposeView(MyApplication.CONTEXT)


        composeView.setContent {

            PhoneTheme {


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0XFF000000).copy(0.5f)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "请在倒计时结束前将手机平稳放好", color = PhoneTheme.colors.background, fontSize = 22.sp)
                    Text(
                        text = num.toString(),
                        fontSize = 42.sp,
                        modifier = Modifier.padding(top = 16.dp),
                        color = PhoneTheme.colors.background
                    )
                }

                LaunchedEffect(Unit, block = {
                    for (i in (5 downTo 0)) {
                        num = i
                        delay(1000)
                    }
                })

                if (num == 0) {
                    MyApplication.CONTEXT.stopService(Intent(this, FloatWindowsService::class.java))
                }
            }
        }

        return composeView
    }

    private fun uiView(): ComposeView {
        val composeView = ComposeView(MyApplication.CONTEXT)
        composeView.setContent {
            PhoneTheme() {
                Surface(
                    color = PhoneTheme.colors.surface,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .fillMaxSize()
                ) {
                    WarnAlarmManager.currentWarn?.let { LockScreen(warnAlarmData = it) }
                }
            }
        }

        return composeView
    }


    private fun channelOverlay() {
        mWindowManager?.removeView(composeView)
    }

    override fun onDestroy() {
        super.onDestroy()
        channelOverlay()
        WarnAlarmManager.currentWarn = null
        WarnAlarmManager.stopWarn()
//        AlarmSwitchObj.isService.value = true

        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}