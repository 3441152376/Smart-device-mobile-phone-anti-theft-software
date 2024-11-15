package cn.egg404.phone.ui.widgets

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cn.bmob.v3.BmobUser
import cn.egg404.phone.MainData
import cn.egg404.phone.R
import cn.egg404.phone.common.AppInfo
import cn.egg404.phone.common.qq.QQLoginListener
import cn.egg404.phone.data.UpdateData
import cn.egg404.phone.data.bean.PermissionData
import cn.egg404.phone.theme.H1
import cn.egg404.phone.theme.H4
import cn.egg404.phone.theme.H5
import cn.egg404.phone.theme.PhoneTheme
import cn.egg404.phone.utils.EggUtil
import cn.egg404.phone.utils.showToast
import cn.egg404.phone.warn.WarnMObj
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.tencent.tauth.Tencent
import okhttp3.*
import java.io.IOException


@Composable
fun CardKeyDialog(
    onDismissRequest: () -> Unit,
    loadSuccess: () -> Unit = {},
    loadError: () -> Unit = {}, showLoad: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(PhoneTheme.colors.background)
                .padding(vertical = 16.dp, horizontal = 26.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var cardKey by remember {
                mutableStateOf("")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                FontIcon(iconName = "\ue621", color = PhoneTheme.colors.textPrimary, fontSize = H4)
                Text(
                    text = "请输入卡密",
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = H4, color = PhoneTheme.colors.textPrimary
                )
            }

            OutlinedTextField(
                value = cardKey, singleLine = true,
                onValueChange = {
                    cardKey = it
                },
                modifier = Modifier.padding(top = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PhoneTheme.colors.theme,
                    cursorColor = PhoneTheme.colors.theme, textColor = PhoneTheme.colors.textPrimary
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            onDismissRequest()
                            showLoad()
                            val okHttpClient = OkHttpClient()
                            val request = Request
                                .Builder()
                                .url("https://pay.egg404.cn/cardkey.php?type=check&key=${cardKey.trim()}&userid=${BmobUser.getCurrentUser().objectId}&token=${BmobUser.getCurrentUser().sessionToken}")
                                .build()
                            okHttpClient
                                .newCall(request)
                                .enqueue(object : Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        loadError()
                                    }

                                    override fun onResponse(call: Call, response: Response) {
                                        val body = response.body?.string()
                                        if (body == "true") {
                                            loadSuccess()
                                        } else {
                                            loadError()
                                        }
                                    }
                                })

                        }
                        .background(PhoneTheme.colors.surface)
                        .padding(16.dp)
                ) {
                    Text(text = "确定", fontSize = H5, color = PhoneTheme.colors.background)
                }

                Row(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            onDismissRequest()
                        }
                        .background(PhoneTheme.colors.theme2)
                        .padding(16.dp)
                ) {
                    Text(text = "取消", fontSize = H5, color = PhoneTheme.colors.background)
                }
            }

        }
    }
}

@Composable
fun Loading(loadState: Boolean) {
    if (loadState) {
        Dialog(onDismissRequest = { }) {
            Column(
                modifier = Modifier
                    .width(120.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0X88000000)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
fun QQLoginDialog(onDismissRequest: () -> Unit) {
    val ctx = LocalContext.current
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    val mTencent = Tencent.createInstance(
                        AppInfo.appId,
                        ctx.applicationContext,
                        "cn.egg404.phone.fileprovider"
                    )

                    if (!mTencent.isSessionValid) {
                        mTencent.login(ctx as Activity, "all", QQLoginListener())
                    }
                }
                .background(PhoneTheme.colors.surface)
                .padding(vertical = 26.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FontIcon(iconName = "\ue63d", color = PhoneTheme.colors.bgTextColor, fontSize = H1)

            Text(
                text = "QQ登录",
                color = PhoneTheme.colors.bgTextColor,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}


@Composable
fun UpdateDialog(onDismissRequest: () -> Unit, updata: UpdateData) {
    val ctx = LocalContext.current
    val v = ctx.packageManager.getPackageInfo(ctx.packageName, 0).versionName

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(PhoneTheme.colors.background)
                .padding(vertical = 16.dp, horizontal = 26.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FontIcon(iconName = "\ue8ba", color = PhoneTheme.colors.textPrimary, fontSize = H4)
                Column(modifier = Modifier.padding(start = 6.dp)) {

                    Text(
                        text = "有新版本！",
                        fontSize = H4,
                        color = PhoneTheme.colors.textPrimary
                    )

                    Text(text = "$v -> ${updata.versionName}", fontSize = H5)
                }
            }

            Image(
                painter = painterResource(id = R.mipmap.update_bg),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )

            Row(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            onDismissRequest()
                        }
                        .background(PhoneTheme.colors.surface)
                        .padding(16.dp)
                ) {
                    Text(text = "下次", fontSize = H5, color = PhoneTheme.colors.background)
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            onDismissRequest()
                            EggUtil.goBrowser(ctx, updata.link)
                        }
                        .background(PhoneTheme.colors.theme2)
                        .padding(16.dp)
                ) {
                    Text(text = "更新", fontSize = H5, color = PhoneTheme.colors.background)
                }
            }
        }
    }
}

@Composable
fun AgreementDialog(onDismissRequest: () -> Unit, readyOnly: Boolean = false) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = false)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(PhoneTheme.colors.background)
                .padding(vertical = 16.dp, horizontal = 26.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var textVis by remember {
                mutableStateOf(true)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FontIcon(iconName = "\ue8ba", color = PhoneTheme.colors.textPrimary, fontSize = H4)
                Text(
                    text = "用户/隐私协议",
                    fontSize = H4,
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .weight(1f), color = PhoneTheme.colors.textPrimary
                )

                if (readyOnly) {
                    FontIcon(
                        iconName = "\ue620",
                        color = PhoneTheme.colors.textPrimary,
                        fontSize = H4,
                        modifier = Modifier
                            .clickable {
                                onDismissRequest()
                            }
                            .padding(end = 6.dp)
                    )
                }

                AnimatedVisibility(visible = !textVis) {
                    FontIcon(
                        iconName = "\ue610",
                        color = PhoneTheme.colors.textPrimary,
                        fontSize = H4,
                        modifier = Modifier.clickable {
                            textVis = true
                        })
                }

            }

            val text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = PhoneTheme.colors.textPrimary)) {
                    append(
                        "尊敬的用户，您好!感谢您对别动我手机的信赖!我们依据最新的监管要求更新了".trimIndent()
                    )
                }

                pushStringAnnotation("user_xy", "https://egg404.cn/news/?type=detail&id=2")
                withStyle(style = SpanStyle(color = PhoneTheme.colors.theme)) {
                    append("《用户服务协议》")
                }
                pop()

                withStyle(style = SpanStyle(color = PhoneTheme.colors.textPrimary)) {
                    append("和")
                }

                pushStringAnnotation("privacy_xy", "https://egg404.cn/xy.html")
                withStyle(style = SpanStyle(color = PhoneTheme.colors.theme)) {
                    append("《隐私政策》")
                }
                pop()
                withStyle(style = SpanStyle(color = PhoneTheme.colors.textPrimary)) {
                    append(
                        """，请务必审慎阅读所有条款,尤其是:
1、我们对您的个人信息的收集/保存/使用/对外提供/保护等规则，以及您的用户权利等条款;
2、与您约定免除或限制责任的条款:3、与您约定法律适用和管辖的条款:4、其他以颜色或粗体进行标识的重要条款。
您点击“同意”即表示您已阅读完毕并同意以上协议的全部内容。
                    """.trimIndent()
                    )
                }

            }


            var webUrl by remember {
                mutableStateOf("")
            }
            val webState = rememberWebViewState(webUrl)

            AnimatedVisibility(visible = textVis) {
                ClickableText(text = text, onClick = { offset ->
                    text.getStringAnnotations(
                        tag = "user_xy", start = offset,
                        end = offset
                    )
                        .firstOrNull()?.let { annotation ->
                            webUrl = annotation.item
                            textVis = false
                        }

                    text.getStringAnnotations(
                        tag = "privacy_xy", start = offset,
                        end = offset
                    )
                        .firstOrNull()?.let { annotation ->
                            webUrl = annotation.item
                            textVis = false
                        }
                }, modifier = Modifier.height(220.dp))
            }

            AnimatedVisibility(visible = !textVis) {
                WebView(
                    webState,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .height(220.dp)
                )
            }


            if (!readyOnly) {

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                onDismissRequest()
                                MainData.browseOnly = true
                            }
                            .background(PhoneTheme.colors.surface)
                            .padding(16.dp)
                    ) {
                        Text(text = "仅浏览", fontSize = H5, color = PhoneTheme.colors.bgTextColor)
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                onDismissRequest()
                                MainData.isOnce = false
                                MainData.browseOnly = false
                            }
                            .background(PhoneTheme.colors.theme2)
                            .padding(16.dp)
                    ) {
                        Text(text = "同意了", fontSize = H5, color = PhoneTheme.colors.bgTextColor)
                    }
                }
            }


        }
    }
}

@Composable
fun PromptDialog2(
    onDismissRequest: () -> Unit,
    content: String,
    confirmOnClick: () -> Unit,
    cancelOnClick: () -> Unit, confirmText: String, cancelText: String
) {

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(PhoneTheme.colors.background)
            .padding(vertical = 16.dp, horizontal = 26.dp)
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FontIcon(iconName = "\ue8ba", color = PhoneTheme.colors.textPrimary, fontSize = H4)
            Text(
                text = "温馨提示",
                fontSize = H4,
                modifier = Modifier.padding(start = 6.dp)
            )
        }

        Text(
            text = content,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            fontSize = H5
        )

        Row(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        cancelOnClick()
                        onDismissRequest()
                    }
                    .background(PhoneTheme.colors.surface)
                    .padding(16.dp)
            ) {
                Text(text = cancelText, fontSize = H5, color = PhoneTheme.colors.bgTextColor)
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        confirmOnClick()
                        onDismissRequest()
                    }
                    .background(PhoneTheme.colors.theme2)
                    .padding(16.dp)
            ) {
                Text(text = confirmText, fontSize = H5, color = PhoneTheme.colors.bgTextColor)
            }
        }

    }

}


@Composable
fun PromptDialog(
    onDismissRequest: () -> Unit,
    content: String,
    confirmOnClick: () -> Unit,
    cancelOnClick: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(PhoneTheme.colors.background)
                .padding(vertical = 16.dp, horizontal = 26.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FontIcon(iconName = "\ue8ba", color = PhoneTheme.colors.textPrimary, fontSize = H4)
                Text(
                    text = "温馨提示",
                    fontSize = H4,
                    modifier = Modifier.padding(start = 6.dp), color = PhoneTheme.colors.textPrimary
                )
            }

            Text(
                text = content,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                fontSize = H5, color = PhoneTheme.colors.textPrimary
            )

            Row(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            cancelOnClick()

                            onDismissRequest()
                        }
                        .background(PhoneTheme.colors.surface)
                        .padding(16.dp)
                ) {
                    Text(text = "取消", fontSize = H5, color = PhoneTheme.colors.background)
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            confirmOnClick()

                            onDismissRequest()
                        }
                        .background(PhoneTheme.colors.theme2)
                        .padding(16.dp)
                ) {
                    Text(text = "确定", fontSize = H5, color = PhoneTheme.colors.background)
                }
            }

        }
    }
}


@Composable
fun PermissionDialog(
    onDismissRequest: () -> Unit,
    permissionData: List<PermissionData>,
    onClick: () -> Unit, content: String
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(PhoneTheme.colors.background)
                .padding(vertical = 16.dp, horizontal = 26.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "请授予以下权限",
                fontSize = H4,
                modifier = Modifier.padding(top = 16.dp),
                color = PhoneTheme.colors.background
            )

            Row(modifier = Modifier.padding(top = 16.dp)) {
                permissionData.forEach {
                    PermissionItem(name = it.name, icon = it.icon)
                }
            }

            /*        Text(
                        text = content,
                        modifier = Modifier.padding(top = 16.dp),
                        color = PhoneTheme.colors.error
                    )*/

            Row(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        onClick()
                        onDismissRequest()
                    }
                    .background(PhoneTheme.colors.surface)
                    .padding(16.dp)
            ) {
                Text(text = "确定", fontSize = H5, color = PhoneTheme.colors.bgTextColor)
            }
        }
    }
}

@Composable
fun PasswordDialog(onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(PhoneTheme.colors.background)
                .padding(vertical = 16.dp, horizontal = 26.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var password by remember {
                mutableStateOf("")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                FontIcon(iconName = "\ue62b", color = PhoneTheme.colors.textPrimary, fontSize = H4)
                Text(
                    text = "设置解锁密码",
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = H4, color = PhoneTheme.colors.textPrimary
                )
            }

            OutlinedTextField(
                value = password,
                onValueChange = {
                    try {
                        if ("0123456789".contains(it[it.length - 1])) {
                            if (it.length <= 4) {
                                password = it
                            }
                        }
                    } catch (e: Exception) {
                        password = ""
                    }

                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(top = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PhoneTheme.colors.theme,
                    cursorColor = PhoneTheme.colors.theme, textColor = PhoneTheme.colors.textPrimary
                )
            )

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        println(password.length)
                        if (password.length < 4) {
                            showToast("请输入四位数密码")
                            return@clickable
                        }
                        WarnMObj.password = password
                        WarnMObj.pass.value = password
                        onDismissRequest()
                    }
                    .background(PhoneTheme.colors.surface)
                    .padding(16.dp)
            ) {
                Text(text = "确定", fontSize = H5, color = PhoneTheme.colors.background)
            }
        }
    }
}

@Composable
fun SampleAlertDialog(
    title: String,
    content: String,
    cancelText: String = "取消",
    confirmText: String = "继续",
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = title)
        },
        text = {
            Text(text = content)
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirmClick.invoke()
                onDismiss.invoke()
            }) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss.invoke() }) {
                Text(text = cancelText)
            }
        },
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    )
}