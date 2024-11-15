package cn.egg404.phone.ui.page.pay

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobUser
import cn.egg404.phone.ui.page.common.RouteName
import cn.egg404.phone.ui.widgets.SecondPageTitleBar
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun Pay(navController: NavHostController, money: String) {
    val ctx = LocalContext.current
    val u = "https://pay.egg404.cn/pay.php?money=$money&subject=${
        when (money) {
            "0.01" -> {
                "月费会员"
            }

            "10" -> {
                "年费会员"

            }

            "20" -> {
                "永结会员"
            }
            else -> {
                "其他"
            }
        }
    }&body=别动我手机&userid=${BmobUser.getCurrentUser().objectId}"

    val state = rememberWebViewState(
        url = u
    )

    Column(modifier = Modifier.fillMaxSize()) {
        SecondPageTitleBar(title = "充值VIP", backOnclick = {
            navController.popBackStack()
        })

        WebView(state = state, modifier = Modifier.fillMaxSize(), onCreated = {
            it.settings.javaScriptEnabled = true
        })


        state.content.getCurrentUrl()?.let {
            if (it.contains("alipays")) {
//                navController.popBackStack()
                ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
            }
        }
    }
}