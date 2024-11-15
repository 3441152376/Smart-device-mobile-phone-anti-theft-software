package cn.egg404.phone.ui.page.select_audio

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cn.bmob.v3.BmobUser
import cn.egg404.phone.MyApplication
import cn.egg404.phone.common.Storage
import cn.egg404.phone.theme.ContentHorPadding
import cn.egg404.phone.theme.PhoneTheme
import cn.egg404.phone.ui.page.common.RouteName
import cn.egg404.phone.ui.widgets.AudioItem
import cn.egg404.phone.ui.widgets.SecondPageTitleBar
import cn.egg404.phone.utils.BmobUtil
import cn.egg404.phone.utils.FileUtil
import cn.egg404.phone.utils.showToast
import kotlinx.coroutines.launch

@Composable
fun SelectAudio(
    navController: NavHostController,
    viewModel: SelectAudioViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
            if (it == null) return@rememberLauncherForActivityResult
//            val path = FileUtil.getPath(ctx, it)
            val type = MyApplication.CONTEXT.contentResolver.getType(it)
            type?.let { typeN ->
                if (typeN.contains("audio")) {
                    MyApplication.CONTEXT.contentResolver.openInputStream(it)
                        ?.let { it1 -> Storage.putAudioFile(it1) }
                    showToast("已选择")

                } else {
                    showToast("请选择音频文件")
                }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PhoneTheme.colors.background)
    ) {
        SecondPageTitleBar(title = "音频选择", backOnclick = {
            navController.popBackStack()
        })

        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ContentHorPadding)
        ) {
            AudioItem(text = "本地选择", onclick = {
                if (!BmobUser.isLogin()) {
                    showToast("请您先登录")
                    BmobUtil.login(ctx as Activity)
                    return@AudioItem
                }

                BmobUtil.fetchUser(success = {
                    if (it.expiration_time != null) {
                        it.expiration_time.let { et ->
                            if (System.currentTimeMillis() < et.toLong() * 1000) {
                                launcher.launch(arrayOf("audio/*"))
                            } else {
                                scope.launch {
                                    showToast("请充值VIP")
                                    navController.navigate(RouteName.RECHARGE_VIP)
                                }
                            }
                        }
                    } else {
                        showToast("请充值VIP")
                        navController.navigate(RouteName.RECHARGE_VIP)
                    }

                }, error = {
                    scope.launch {
                        showToast("失败，请重试。")
                    }
                })
            }, bgColor = PhoneTheme.colors.surface)

            AudioItem(text = "恢复默认", onclick = {
                if (Storage.deleteAudioFile()) {
                    showToast("已恢复")
                } else {
                    showToast("恢复失败，请重试")
                }
            }, bgColor = PhoneTheme.colors.surface)

            /*viewModel.viewStates.audioL.forEach { item ->
                AudioItem(text = item.name, onclick = {

                }, selected = item.selected)

            }*/
        }
    }
}