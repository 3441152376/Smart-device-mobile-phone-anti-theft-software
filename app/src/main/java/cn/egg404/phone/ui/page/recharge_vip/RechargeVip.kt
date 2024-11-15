package cn.egg404.phone.ui.page.recharge_vip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FetchUserInfoListener
import cn.egg404.phone.data.bean.PhoneUser
import cn.egg404.phone.theme.ContentHorPadding
import cn.egg404.phone.theme.H4
import cn.egg404.phone.theme.PhoneTheme
import cn.egg404.phone.ui.widgets.*
import cn.egg404.phone.utils.BmobUtil
import cn.egg404.phone.utils.PayUtil
import cn.egg404.phone.utils.showToast
import com.google.accompanist.flowlayout.FlowRow


@Composable
fun RechargeVip(navController: NavHostController) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PhoneTheme.colors.background)
    ) {
        SecondPageTitleBar(title = "充值VIP", backOnclick = {
            navController.popBackStack()
        })
        var cardKeyState by remember {
            mutableStateOf(false)
        }


        var loadState by remember {
            mutableStateOf(false)
        }

        if (loadState) {
            Loading(loadState = loadState)
        }


        if (cardKeyState) {
            CardKeyDialog(onDismissRequest = {
                cardKeyState = false
            }, loadSuccess = {
                loadState = false
                showToast("充值成功")
                BmobUtil.fetchUser()
            }, loadError = {
                loadState = false
                showToast("充值失败")
            }, showLoad = {
                loadState = true
            })
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ContentHorPadding)
        ) {
            ContentTitleBar("会员充值")
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(PhoneTheme.colors.theme2)
                    .clickable {
                        cardKeyState = true
                    }
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                FontIcon(
                    iconName = "",
                    color = PhoneTheme.colors.bgTextColor,
                    fontSize = H4,
                    modifier = Modifier.padding(start = 6.dp)
                )

                Text(
                    text = "卡密充值",
                    color = PhoneTheme.colors.bgTextColor,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }

            val success: () -> Unit = {
                showToast("支付成功")
                BmobUser.fetchUserInfo(object : FetchUserInfoListener<PhoneUser>() {
                    override fun done(user: PhoneUser, e: BmobException?) {
                        if (e == null) {
                            val myUser = BmobUser.getCurrentUser(PhoneUser::class.java)
                            println("数据是$myUser")
                        } else {
                            println("更新缓存失败")
                        }
                    }
                })
            }

            val error: () -> Unit = {
                showToast("支付失败")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                VipItem(name = "一月", money = "2.55", onClick = {
                    PayUtil.payV2("2.55", success = success, error = error)
                })

                VipItem(name = "一年", money = "6.66", onClick = {
                    PayUtil.payV2("6.66", success = success, error = error)
                })

                VipItem(name = "永久", money = "8.88", onClick = {
                    PayUtil.payV2("8.88", success = success, error = error)
                })
            }

            ContentTitleBar("会员功能")

            FlowRow(modifier = Modifier.fillMaxWidth()) {
                MobItem(name = "自定义音频")

                MobItem(name = "应用守护", false)
                MobItem(name = "蓝牙模式", false)
                MobItem(name = "断网模式", false)
                MobItem(name = "自定义灵敏度", false)
            }
        }
    }
}

@Composable
private fun MobItem(name: String, isDevelop: Boolean = true) {
    Column(
        modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(100))
            .clickable {
                if (!isDevelop) {
                    showToast("此功能正在开发")
                }
            }
            .background(if (isDevelop) PhoneTheme.colors.theme2 else PhoneTheme.colors.gray)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            color = if (isDevelop) PhoneTheme.colors.bgTextColor else PhoneTheme.colors.theme2
        )
    }
}

@Composable
private fun VipItem(name: String, money: String, onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .width(120.dp)
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(PhoneTheme.colors.surface)
            .padding(16.dp)
    ) {
        Text(text = name, color = PhoneTheme.colors.bgTextColor)
        Text(
            text = money,
            color = PhoneTheme.colors.bgTextColor,
            modifier = Modifier.padding(top = 6.dp)
        )
    }

}