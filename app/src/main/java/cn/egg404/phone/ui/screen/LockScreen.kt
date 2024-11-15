package cn.egg404.phone.ui.screen

import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import cn.egg404.phone.MyApplication
import cn.egg404.phone.service.FloatWindowsService
import cn.egg404.phone.theme.H3
import cn.egg404.phone.theme.H4
import cn.egg404.phone.theme.PhoneTheme
import cn.egg404.phone.theme.TextStartPadding
import cn.egg404.phone.ui.page.main.alarm_switch.AlarmSwitchObj
import cn.egg404.phone.ui.page.main.alarm_switch.AlarmSwitchStore
import cn.egg404.phone.ui.page.main.alarm_switch.AlarmSwitchViewAction
import cn.egg404.phone.ui.widgets.FontIcon
import cn.egg404.phone.ui.widgets.PromptDialog
import cn.egg404.phone.ui.widgets.PromptDialog2
import cn.egg404.phone.utils.showLongToast
import cn.egg404.phone.utils.showToast
import cn.egg404.phone.warn.WarnAlarmManager
import cn.egg404.phone.warn.WarnMObj
import cn.egg404.phone.warn.data.WarnAlarmData

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LockScreen(viewModel: LockScreenViewModel = hiltViewModel(), warnAlarmData: WarnAlarmData) {


    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(PhoneTheme.colors.background)
                .padding(26.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 26.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FontIcon(
                        iconName = "\ue630",
                        color = PhoneTheme.colors.error,
                        fontSize = H3
                    )
                    Column {
                        Text(
                            text = "请解锁",
                            modifier = Modifier.padding(start = TextStartPadding),
                            fontSize = H4,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = warnAlarmData.warnContent,
                            color = PhoneTheme.colors.textSecondary,
                            modifier = Modifier.padding(start = TextStartPadding, top = 6.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in (0..3)) {
                    RoundBackground(i, viewModel)
                }
            }

            Column(
                modifier = Modifier
                    .padding(top = 66.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (i in (7..9)) {
                        NumberContainer(num = i.toString()) {
                            viewModel.dispatch(LockScreenAction.UpdatePassword(i.toString()))
                        }
                    }

                    NumberContainer(num = "all") {
                        viewModel.dispatch(LockScreenAction.ClearAllPassword)
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (i in (4..6)) {
                        NumberContainer(num = i.toString()) {
                            viewModel.dispatch(LockScreenAction.UpdatePassword(i.toString()))
                        }
                    }

                    NumberContainer(num = "X", color = Color(0XFFFFBFD8)) {
                        viewModel.dispatch(LockScreenAction.ClearPassword)
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (i in (1..3)) {
                        NumberContainer(num = i.toString()) {
                            viewModel.dispatch(LockScreenAction.UpdatePassword(i.toString()))
                        }
                    }


                    NumberContainer(num = "OK", color = PhoneTheme.colors.theme2) {
                        if (viewModel.viewStates.password == WarnMObj.password) {
                            LockScreenObj.keepWarn.value = true
//                            stopService()
                        } else {
                            showToast("密码错误")
                        }
                    }

                }

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (i in (1..4)) {
                        if (i != 2) {
                            NumberContainer(num = "", color = PhoneTheme.colors.background)
                        } else {
                            NumberContainer(num = "0") {
                                viewModel.dispatch(LockScreenAction.UpdatePassword("0"))
                            }
                        }
                    }
                }
            }
        }


        AnimatedVisibility(visible = LockScreenObj.keepWarn.value, enter = fadeIn()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0X55000000))
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PromptDialog2(
                    onDismissRequest = {
                        LockScreenObj.keepWarn.value = false
                        stopService()
                    },
                    content = "密码正确，是否继续检测报警？",
                    confirmOnClick = {
                        showLongToast(text = "15秒后开启守护")
                    },
                    cancelOnClick = {
                        warnAlarmData.enabled = false
                        AlarmSwitchStore.updateAlarm(warnAlarmData)
                        AlarmSwitchObj.viewModel?.dispatch(
                            AlarmSwitchViewAction.UpdateAlarmList(
                                AlarmSwitchStore.getAlarmList()
                            )
                        )
                        WarnAlarmManager.warnTime = 0
                    }, confirmText = "继续报警", cancelText = "取消报警"
                )
            }
        }


    }
}

private fun stopService() {

    MyApplication.CONTEXT.stopService(
        Intent(
            MyApplication.CONTEXT,
            FloatWindowsService::class.java
        )
    )
}

@Composable
private fun RoundBackground(index: Int, viewModel: LockScreenViewModel) {
    Row(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(100))
            .background(PhoneTheme.colors.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        var t = ""
        if (viewModel.viewStates.password.length > index) {
            t = viewModel.viewStates.password[index].toString()
        }

        Text(text = t, color = PhoneTheme.colors.background)
    }
}

@Composable
private fun NumberContainer(
    num: String,
    color: Color = Color(0XFFEDF5F7),
    onclick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clickable {
                onclick()
            }
            .size(70.dp)
            .clip(RoundedCornerShape(100))
            .background(color),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (num == "all") {
            FontIcon(iconName = "\ue631", color = PhoneTheme.colors.error, fontSize = H3)
        } else {
            Text(text = num)
        }
    }
}