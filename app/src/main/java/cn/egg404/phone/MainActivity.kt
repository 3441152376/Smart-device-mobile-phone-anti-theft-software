package cn.egg404.phone

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import cn.egg404.phone.common.qq.QQLoginListener
import cn.egg404.phone.data.UpdateData
import cn.egg404.phone.data.bean.PermissionData
import cn.egg404.phone.service.AlarmService
import cn.egg404.phone.service.FloatWindowsService
import cn.egg404.phone.theme.PhoneTheme
import cn.egg404.phone.ui.page.common.AppScaffold
import cn.egg404.phone.ui.page.main.alarm_switch.AlarmSwitchObj
import cn.egg404.phone.ui.page.setting.SettingPageStore
import cn.egg404.phone.ui.widgets.AgreementDialog
import cn.egg404.phone.ui.widgets.PermissionDialog
import cn.egg404.phone.ui.widgets.QQLoginDialog
import cn.egg404.phone.ui.widgets.UpdateDialog
import cn.egg404.phone.utils.EggUtil
import cn.egg404.phone.warn.WarnMObj
import com.alibaba.fastjson.JSON
import com.tencent.connect.common.Constants
import com.tencent.tauth.Tencent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.CONTEXT = this

        val intent = Intent(this, AlarmService::class.java)
        startService(intent)


        // 首先判断获取到的list是否为空 if (packageInfoList == null)
        // 首先判断获取到的list是否为空 if (packageInfoList == null)

/*
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            try {
                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            } catch (e: java.lang.Exception) {
                Toast.makeText(this@MainActivity, "无法开启允许查看使用情况的应用界面", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }*/

        setContent {
            MainData.isDark.value = SettingPageStore.isDarkMode
            if (AlarmSwitchObj.isService.value && WarnMObj.pass.value != "" && FloatWindowsService.num == 5) {
                if (AlarmSwitchObj.justNowAlarm != null) {
                    if (AlarmSwitchObj.justNowAlarm!!.name == "移动") {
                        FloatWindowsService.type = 1
                        MyApplication.CONTEXT.startService(
                            Intent(
                                MyApplication.CONTEXT,
                                FloatWindowsService::class.java
                            )
                        )
                    } else {
                        FloatWindowsService.num = 0
                    }
                }
            }

            var isS by remember {
                mutableStateOf(EggUtil.canDrawOverlays(MyApplication.CONTEXT))
            }

            PhoneTheme(MainData.isDark.value) {
                val ctx = LocalContext.current
                var nextUpdate by remember {
                    mutableStateOf(false)
                }

                var updateData by remember {
                    mutableStateOf(UpdateData(versionName = "", link = ""))
                }

                var isUpdate by remember {
                    mutableStateOf(false)
                }

                val scope = rememberCoroutineScope()

                scope.launch(Dispatchers.IO) {
                    try {
                        val okHttpClient = OkHttpClient()
                        val request = Request.Builder()
                            .url("https://phone.egg404.cn/json/update.json")
                            .build()


                        val result = okHttpClient.newCall(request).execute()

                        val json = JSON.parseObject(result.body?.string() ?: "")
                        val v = ctx.packageManager.getPackageInfo(packageName, 0).versionCode
                        if (json.getInteger("version_code") > v) {
                            isUpdate = true
                            updateData = UpdateData(
                                versionName = json.getString("version_name"),
                                link = json.getString("link")
                            )
                        }
                    } catch (e: Exception) {

                    }
                }


                var qqLoginDialog by remember {
                    mutableStateOf(false)
                }

                AnimatedVisibility(visible = qqLoginDialog) {
                    QQLoginDialog(onDismissRequest = {
                        qqLoginDialog = false
                    })
                }

                if (isUpdate && !nextUpdate) {
                    UpdateDialog(
                        onDismissRequest = {
                            isUpdate = false
                            nextUpdate = true
                        },
                        updata = updateData
                    )
                }

                if (MainData.isO) {
                    AgreementDialog(onDismissRequest = {
                        MainData.isO = false
                    })
                }


                if (!isS && !MainData.isO && !MainData.browseOnly) {
                    PermissionDialog(
                        onDismissRequest = {
                            isS = true
                        },
                        permissionData = listOf(PermissionData("悬浮窗", "\uE60B", false)),
                        content = "温馨提示：默认解锁密码是：1234，请牢记！", onClick = {
                            EggUtil.requestSettingCanDrawOverlays(this)
                        }
                    )
                }

                AppScaffold()

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, QQLoginListener())
        }
    }

}