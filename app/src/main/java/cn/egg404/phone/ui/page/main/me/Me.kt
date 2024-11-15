package cn.egg404.phone.ui.page.main.me

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cn.bmob.v3.BmobUser
import cn.egg404.phone.common.AppInfo
import cn.egg404.phone.common.qq.QQLoginListener
import cn.egg404.phone.data.bean.PhoneUser
import cn.egg404.phone.theme.*
import cn.egg404.phone.ui.page.common.RouteName
import cn.egg404.phone.ui.widgets.CardItem
import cn.egg404.phone.ui.widgets.FontIcon
import cn.egg404.phone.ui.widgets.Loading
import cn.egg404.phone.ui.widgets.MainTitleBar
import cn.egg404.phone.utils.BmobUtil
import cn.egg404.phone.utils.EggUtil
import cn.egg404.phone.utils.showToast
import com.tencent.tauth.Tencent
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Me(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        MainTitleBar(settingOnclick = {
            navController.navigate(RouteName.SETTING)
        })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = ContentRadius, topEnd = ContentRadius))
                .background(PhoneTheme.colors.background)
                .padding(horizontal = ContentHorPadding)
        ) {

            if (MeObj.loginState.value) {
                Logged(navController)
            } else {
                NotLogged(navController)
            }
        }

    }

}

@SuppressLint("SimpleDateFormat")
@Composable
private fun Logged(navController: NavHostController) {
    val phoneUser = BmobUser.getCurrentUser(PhoneUser::class.java)

    var currTime = "未充值"

    if (phoneUser.expiration_time != null) {
        val data = Date(phoneUser.expiration_time.toLong() * 1000)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        currTime = sdf.format(data) + "到期"
    }


    val ctx = LocalContext.current

    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(PhoneTheme.colors.surface)
            .fillMaxWidth()
            .padding(vertical = 26.dp, horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FontIcon(iconName = "\ue773", color = PhoneTheme.colors.bgTextColor, fontSize = H3)
            Text(
                text = "VIP",
                modifier = Modifier.padding(start = 8.dp),
                color = PhoneTheme.colors.bgTextColor
            )
        }

        Text(
            text = currTime,
            color = PhoneTheme.colors.bgTextColor, modifier = Modifier.padding(top = 6.dp)
        )
    }

    CardItem(text = "关于", icon = "\ueb77", onclick = {
        navController.navigate(RouteName.ABOUT)
    })
    CardItem(text = "充值VIP", icon = "\ue773", onclick = {
        navController.navigate(RouteName.RECHARGE_VIP)
    })

    CardItem(text = "退出登录", icon = "\ueb77", onclick = {
        BmobUser.logOut()
        MeObj.loginState.value = false
        showToast("退出登录成功")
    })

    CardItem(text = "联系客服", icon = "\ue602", bgColor = PhoneTheme.colors.theme2, onclick = {
        EggUtil.goBrowser(ctx, "https://work.weixin.qq.com/kfid/kfcb66ef4abb3763e95")

    })
}


@Composable
private fun NotLogged(navController: NavHostController) {
    val ctx = LocalContext.current

    Loading(loadState = MeObj.loginLoadState.value)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                BmobUtil.login(ctx as Activity)
                MeObj.loginLoadState.value = true
            }
            .background(PhoneTheme.colors.surface)
            .padding(vertical = 26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FontIcon(iconName = "\ue63d", color = PhoneTheme.colors.bgTextColor, fontSize = H2)
        Text(
            text = "QQ登录",
            modifier = Modifier.padding(top = 8.dp),
            color = PhoneTheme.colors.bgTextColor,
            fontSize = H5
        )
    }
/*
    MeItem(name = "充值VIP", icon = "\ue773", onClick = {
    })*/
}

@Composable
private fun MeItem(name: String, icon: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(8.dp))
            .background(PhoneTheme.colors.theme2)
            .padding(vertical = 16.dp)
    ) {
        FontIcon(
            iconName = icon,
            color = PhoneTheme.colors.bgTextColor,
            fontSize = H4,
            modifier = Modifier.padding(start = 16.dp)
        )

        Text(
            text = name,
            modifier = Modifier.padding(start = 8.dp),
            color = PhoneTheme.colors.bgTextColor
        )
    }
}