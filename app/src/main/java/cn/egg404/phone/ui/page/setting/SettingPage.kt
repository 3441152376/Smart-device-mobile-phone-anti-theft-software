package cn.egg404.phone.ui.page.setting

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cn.egg404.phone.MainData
import cn.egg404.phone.data.bean.OutWardItemData
import cn.egg404.phone.data.bean.PermissionData
import cn.egg404.phone.theme.*
import cn.egg404.phone.ui.page.common.RouteName
import cn.egg404.phone.ui.widgets.*
import cn.egg404.phone.utils.BmobUtil
import cn.egg404.phone.utils.showToast
import cn.egg404.phone.warn.WarnAlarmManager
import cn.egg404.phone.warn.WarnAlarmManagerData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingPage(
    navController: NavHostController,
    viewModel: SettingPageViewModel = hiltViewModel()
) {
    var isPassDialog by remember {
        mutableStateOf(false)
    }

    if (isPassDialog) {
        PasswordDialog() {
            isPassDialog = false
        }
    }

    var isO by remember {
        mutableStateOf(false)
    }

    if (isO) {
        AgreementDialog(onDismissRequest = {
            isO = false
        }, readyOnly = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SecondPageTitleBar(title = "设置", backOnclick = {
            navController.popBackStack()
        })



        Column(
            modifier = Modifier
//                .clip(RoundedCornerShape(ContentRadius))
                .background(PhoneTheme.colors.background)
                .fillMaxSize()
                .padding(horizontal = ContentHorPadding)

        ) {
            ContentTitleBar("报警设置")

            LazyVerticalGrid(cells = GridCells.Fixed(2), content = {
                items(viewModel.viewStates.alarmList) { item ->
                    var selected by remember {
                        mutableStateOf(
                            when (item.name) {
                                "震动" -> {
                                    WarnAlarmManagerData.isShock
                                }

                                "闪光灯" -> {
                                    WarnAlarmManagerData.isFlashLamp
                                }

                                "报警音" -> {
                                    WarnAlarmManagerData.isMedia
                                }
                                else -> {
                                    false
                                }
                            }
                        )
                    }

                    AlarmSettingsItem(
                        alarmSettingItemData = item,
                        selected = selected,
                        onChangeClick = {
                            selected = it
                            when (item.name) {
                                "震动" -> {
                                    WarnAlarmManagerData.isShock = selected
                                }

                                "闪光灯" -> {
                                    WarnAlarmManagerData.isFlashLamp = selected
                                }

                                "报警音" -> {
                                    WarnAlarmManagerData.isMedia = selected
                                }

                                "设置解锁密码" -> {
                                    isPassDialog = true
                                }
                            }
                        })
                }
            })


            /*         SettingItem(
                         top = 0.dp,
                         selected = viewModel.viewStates.darkModeSelected,
                         onChangeClick = {
                             viewModel.dispatch(SettingPageAction.UpdateDarkModeSel(it))
                         },
                         settingModeData = SettingModeData("禁止关闭报警声音")
                     )*/

            SettingItem(
                iconName = "\ue68a",
                top = 16.dp,
                onclick = {
                    navController.navigate(RouteName.SELECT_AUDIO)

                },
                settingModeData = SettingModeData("自定义音频")
            )

/*

              SettingItem(
                  iconName = "\ue68a",
                  top = 16.dp,
                  selected = viewModel.viewStates.darkModeSelected,
                  onclick = {

                  },
                  settingModeData = SettingModeData("强制最大音量报警")
              )*/


            LazyColumn(modifier = Modifier.fillMaxWidth(), content = {

                item {
                    ContentTitleBar("其他")
                }

                item {
                    SettingItem(
                        iconName = "\ue611",
                        top = 16.dp,
                        selected = viewModel.viewStates.darkModeSelected,
                        onChangeClick = {
                            viewModel.dispatch(SettingPageAction.UpdateDarkModeSel(it))
                        },
                        settingModeData = SettingModeData("夜间模式")
                    )
                }

                item {
                    SettingItem(
                        iconName = "\ue608",
                        top = 16.dp,
                        onclick = {
                            isO = true
                        },
                        settingModeData = SettingModeData("查看协议")
                    )
                }


                item {
                    ContentTitleBar("权限")

                }

                item {
                    val item = viewModel.viewStates.permissionList[0]
                    PermissionItem(name = item.name, icon = item.icon, enabled = item.enabled)

                }
            })


        }
    }
}

@Composable
private fun SettingItem(
    iconName: String,
    top: Dp,
    selected: Boolean = false,
    onChangeClick: ((selected: Boolean) -> Unit)? = null,
    onclick: (() -> Unit)? = null,
    settingModeData: SettingModeData
) {
    Row(
        modifier = Modifier
            .padding(
                top = top
            )
            .clip(RoundedCornerShape(ItemRadius))
            .clickable {

                if (onChangeClick != null) {
                    onChangeClick(!selected)
                }
                if (onclick != null) {
                    onclick()
                }
            }
            .fillMaxWidth()
            .height(65.dp)
            .background(PhoneTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FontIcon(iconName = iconName, color = PhoneTheme.colors.bgTextColor, fontSize = H4)

        Text(
            text = settingModeData.name,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis, color = PhoneTheme.colors.bgTextColor
        )

        if (onChangeClick != null) {
            Switch(selected = selected)
        }
    }
}

data class SettingModeData(val name: String)