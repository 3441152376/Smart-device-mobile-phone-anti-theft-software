package cn.egg404.phone.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.Toast

object EggUtil {

    fun goWeiXinKef(context: Context, ticket: String) {
        val uri = "weixin://dl/business/?ticket=$ticket"
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
    }

    fun goBrowser(context: Context, url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    fun canDrawOverlays(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) true else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            Settings.canDrawOverlays(context)
        } else {
            if (Settings.canDrawOverlays(context)) return true
            try {
                val mgr = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    ?: return false
                //getSystemService might return null
                val viewToAdd = View(context)
                val params = WindowManager.LayoutParams(
                    0,
                    0,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT
                )
                viewToAdd.layoutParams = params
                mgr.addView(viewToAdd, params)
                mgr.removeView(viewToAdd)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            false
        }
    }

    fun requestSettingCanDrawOverlays(context: Activity) {
        Toast.makeText(context, "请打开显示悬浮窗开关!", Toast.LENGTH_LONG).show()
        val sdkInt = Build.VERSION.SDK_INT
        when {
            sdkInt >= Build.VERSION_CODES.O -> { //8.0以上
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                context.startActivityForResult(intent, 0Xff)
            }
            sdkInt >= Build.VERSION_CODES.M -> { //6.0-8.0
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                intent.data = Uri.parse("package:${context.packageName}")
                context.startActivityForResult(intent, 0Xff)
            }
            else -> { //4.4-6.0一下
                //无需处理了
            }
        }
    }

}