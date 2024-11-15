package cn.egg404.phone.ui.page.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cn.egg404.phone.theme.PhoneTheme
import cn.egg404.phone.ui.page.about.About
import cn.egg404.phone.ui.page.main.alarm_switch.AlarmSwitch
import cn.egg404.phone.ui.page.main.me.Me
import cn.egg404.phone.ui.page.main.warn.Alarm
import cn.egg404.phone.ui.page.pay.Pay
import cn.egg404.phone.ui.page.recharge_vip.RechargeVip
import cn.egg404.phone.ui.page.select_audio.SelectAudio
import cn.egg404.phone.ui.page.setting.SettingPage
import cn.egg404.phone.ui.widgets.AppSnackBar
import cn.egg404.phone.ui.widgets.BottomNavBarView
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppScaffold() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scaffold = rememberScaffoldState()
    Scaffold(bottomBar = {
        when (currentDestination?.route) {
            RouteName.ALARM -> BottomNavBarView(navCtrl = navController)
            RouteName.ALARM_SWITCH -> BottomNavBarView(navCtrl = navController)
            RouteName.ME -> BottomNavBarView(navCtrl = navController)
        }
    }, content = {
        NavHost(
            navController = navController,
            startDestination = RouteName.ALARM, modifier = Modifier.background(
                PhoneTheme.colors.surface
            )

        ) {
            composable(route = RouteName.ALARM) {
                Alarm(navController = navController)
            }

            composable(route = RouteName.ALARM_SWITCH) {
                AlarmSwitch(navController = navController)
            }

            composable(route = RouteName.ME) {
                Me(navController = navController)
            }

            composable(route = RouteName.SETTING) {
                SettingPage(navController = navController)
            }

            composable(route = RouteName.SELECT_AUDIO) {
                SelectAudio(navController = navController)
            }

            composable(route = RouteName.RECHARGE_VIP) {
                RechargeVip(navController = navController)
            }

            composable(route = "${RouteName.PAY}/{money}") { back ->

                Pay(
                    navController = navController,
                    money = back.arguments?.getString("money") ?: "0"
                )
            }

            composable(route = RouteName.ABOUT) {
                About(navController = navController)
            }
        }


    }, snackbarHost = {
        SnackbarHost(hostState = scaffold.snackbarHostState) { data ->
            AppSnackBar(data = data)
        }
    })
}