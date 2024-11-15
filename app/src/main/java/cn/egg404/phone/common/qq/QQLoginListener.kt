package cn.egg404.phone.common.qq

import cn.bmob.v3.BmobUser
import cn.egg404.phone.utils.BmobUtil
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError
import org.json.JSONObject

class QQLoginListener : IUiListener {
    override fun onComplete(p0: Any?) {
        p0 as JSONObject
        val token = p0.getString("access_token")
        val expires = p0.getString("expires_in")
        val openId = p0.getString("openid")
        BmobUtil.thirdSingupLogin(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, token, expires, openId)

    }

    override fun onError(p0: UiError?) {
        println("失败")

    }

    override fun onCancel() {
        println("关闭")
    }

    override fun onWarning(p0: Int) {
        println("警告")
    }
}