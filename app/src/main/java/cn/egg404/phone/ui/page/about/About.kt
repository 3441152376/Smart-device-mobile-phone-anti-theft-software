package cn.egg404.phone.ui.page.about

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cn.egg404.phone.R
import cn.egg404.phone.theme.PhoneTheme
import cn.egg404.phone.ui.widgets.SecondPageTitleBar
import cn.egg404.phone.utils.EggUtil

@Composable
fun About(navController: NavHostController) {
    val ctx = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(PhoneTheme.colors.background)
                .fillMaxSize()
        ) {
            SecondPageTitleBar(title = "设置", backOnclick = {
                navController.popBackStack()
            })

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .border(
                            BorderStroke(1.dp, PhoneTheme.colors.gray),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.logo),
                        contentDescription = null,
                        modifier = Modifier.size(45.dp)
                    )
                }

                Text(text = "泸州网络科技有限责任公司", modifier = Modifier.padding(vertical = 16.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .clickable {
                            EggUtil.goBrowser(ctx, "https://egg404.cn")
                        }
                        .background(PhoneTheme.colors.theme2)
                        .padding(16.dp)
                ) {
                    Text(text = "加入我们", color = PhoneTheme.colors.bgTextColor)
                }
            }

            Text(
                text = """
            我们是一支年轻的团队。我们的团队成员均为00后和90后，充满了朝气和创新精神；
            我们是一支专注的团队。我们坚信，优秀的团队源自客户的信任，只有专注，才能做到优秀；
            我们是一支有梦想的团队。我们来自全国各地，因为一个共同的梦想:在做一家真正优秀的互联网企业。
        """.trimIndent(),
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 26.dp),
                color = Color(0XFF6F6F6F)
            )
        }

        Text(
            text = "联系客服",
            modifier = Modifier
                .padding(bottom = 46.dp)
                .clickable {
                    EggUtil.goBrowser(ctx, "https://work.weixin.qq.com/kfid/kfcb66ef4abb3763e95")
                }
                .align(alignment = Alignment.BottomCenter),
            color = PhoneTheme.colors.theme2
        )
    }

}