package cn.egg404.phone.ui.page.main.alarm_switch

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cn.egg404.phone.MainData
import cn.egg404.phone.service.FloatWindowsService
import cn.egg404.phone.theme.ContentHorPadding
import cn.egg404.phone.theme.ContentRadius
import cn.egg404.phone.theme.PhoneTheme
import cn.egg404.phone.ui.page.common.RouteName
import cn.egg404.phone.ui.widgets.*
import cn.egg404.phone.warn.WarnMObj

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlarmSwitch(
    navController: NavHostController,
    viewModel: AlarmSwitchViewModel = hiltViewModel()
) {
    AlarmSwitchObj.viewModel = viewModel


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MainTitleBar(settingOnclick = {
            navController.navigate(RouteName.SETTING)
        })

        val alarmItemDats = viewModel.viewStates.alarmItemDats

        var isPassDialog by remember {
            mutableStateOf(false)
        }

        var promptDialog by remember {
            mutableStateOf(false)
        }


        if (isPassDialog) {
            PasswordDialog() {
                isPassDialog = false
            }
        }


        if (promptDialog) {
            PromptDialog(
                onDismissRequest = {
                    promptDialog = false
                },
                content = "是否开启报警功能？",
                confirmOnClick = {
                    if (WarnMObj.password == "") {
                        isPassDialog = true
                    }

                    AlarmSwitchObj.isService.value = true
                    AlarmSwitchStore.isService = true

                }) {
            }
        }

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = ContentRadius, topEnd = ContentRadius))
                .fillMaxSize()
                .background(PhoneTheme.colors.background)
                .padding(horizontal = ContentHorPadding)
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {

                    }, verticalAlignment = Alignment.CenterVertically
            ) {
                ContentTitleBar("报警开关", modifier = Modifier.weight(1f))
            }

            if (WarnMObj.password == "" && !MainData.browseOnly) {
                isPassDialog = true
            }

            LazyVerticalGrid(cells = GridCells.Fixed(2), content = {
                items(alarmItemDats) { alarmItemData ->
                    SwitchListItem(
                        alarmItemData = alarmItemData,
                        index = alarmItemData.index,
                        selected = alarmItemData.enabled,
                        onChangeClick = {
                            if (MainData.browseOnly) {
                                MainData.isO = true
                                return@SwitchListItem
                            }

                            if (it) {
                                FloatWindowsService.num = 5
                            }

                            AlarmSwitchObj.justNowAlarm = alarmItemData
                            alarmItemData.enabled = it
                            viewModel.dispatch(AlarmSwitchViewAction.UpdateAlarmList(alarmItemDats))
                        })

                }
            }, modifier = Modifier.padding(bottom = 120.dp))
        }
    }
}