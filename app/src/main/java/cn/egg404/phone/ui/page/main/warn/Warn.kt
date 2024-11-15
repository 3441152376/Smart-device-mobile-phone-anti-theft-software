package cn.egg404.phone.ui.page.main.warn

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cn.egg404.phone.R
import cn.egg404.phone.theme.ContentHorPadding
import cn.egg404.phone.theme.ContentRadius
import cn.egg404.phone.theme.H3
import cn.egg404.phone.theme.PhoneTheme
import cn.egg404.phone.ui.page.common.RouteName
import cn.egg404.phone.ui.widgets.FontIcon
import cn.egg404.phone.ui.widgets.MainTitleBar
import cn.egg404.phone.ui.widgets.TimeLine
import cn.egg404.phone.utils.RelativeDateHandler
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Composable
fun Alarm(navController: NavHostController, viewModel: WarnViewModel = hiltViewModel()) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MainTitleBar(settingOnclick = {
            navController.navigate(RouteName.SETTING)
        })

        WarnObj.warnList.value = WarnStore.getWarnList()

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = ContentRadius, topEnd = ContentRadius))
                .fillMaxSize()
                .background(PhoneTheme.colors.background)
                .padding(start = ContentHorPadding, end = ContentHorPadding, top = 36.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    WarnObj.warnList.value = ArrayList()
                    viewModel.dispatch(WarnViewAction.UpdateWarnList(ArrayList()))
                }) {
                Text(text = "", modifier = Modifier.weight(1f))
                FontIcon(
                    iconName = "\ue631",
                    color = PhoneTheme.colors.error,
                    fontSize = H3, modifier = Modifier.padding(end = 16.dp)
                )
            }

            if (WarnObj.warnList.value.size != 0) {
                LazyColumn(content = {
                    items(WarnObj.warnList.value.reversed()) { item ->
                        TimeLine(
                            RelativeDateHandler.format(Date(item.timestamp)),
                            item.content,
                            item.index, WarnObj.warnList.value.size
                        )
                    }
                }, modifier = Modifier.padding(bottom = 120.dp))
            } else {
                EmptyList()
            }
        }
    }
}

@Composable
fun EmptyList() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.mipmap.empty),
            contentDescription = "空空如也",
            modifier = Modifier

                .size(120.dp)
        )
        Text(
            text = "您的手机很安全，没人稀罕，哦不，没人敢碰！",
            modifier = Modifier.padding(bottom = 240.dp, top = 8.dp),
            color = PhoneTheme.colors.textSecondary
        )
    }
}

@SuppressLint("SimpleDateFormat")
private fun transToString(time: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
    return sdf.format(time)
}