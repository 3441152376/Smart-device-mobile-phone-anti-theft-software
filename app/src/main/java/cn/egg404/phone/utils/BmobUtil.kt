package cn.egg404.phone.utils

import android.app.Activity
import android.content.Context
import cn.bmob.v3.BmobUser
import cn.bmob.v3.BmobUser.BmobThirdUserAuth
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FetchUserInfoListener
import cn.bmob.v3.listener.LogInListener
import cn.egg404.phone.common.AppInfo
import cn.egg404.phone.common.qq.QQLoginListener
import cn.egg404.phone.data.bean.PhoneUser
import cn.egg404.phone.ui.page.main.me.MeObj
import com.tencent.tauth.Tencent
import org.json.JSONObject


object BmobUtil {


    fun login(context: Activity) {
        val mTencent = Tencent.createInstance(
            AppInfo.appId,
            context.applicationContext,
            "cn.egg404.phone.fileprovider"
        )

        if (!mTencent.isSessionValid) {
            mTencent.login(context, "all", QQLoginListener())
        }
    }

    fun fetchUser(
        success: (phoneUser: PhoneUser) -> Unit = {},
        error: (e: BmobException) -> Unit = {}
    ) {
        if (BmobUser.isLogin()) {

            BmobUser.fetchUserInfo(object : FetchUserInfoListener<PhoneUser>() {
                override fun done(user: PhoneUser, e: BmobException?) {
                    if (e == null) {
                        success(BmobUser.getCurrentUser(PhoneUser::class.java))
                    } else {
                        error(e)
                    }
                }
            })
        }
    }


    /**
     * 第三方平台一键注册或登录
     * @param snsType
     * @param accessToken
     * @param expiresIn
     * @param userId
     */
    fun thirdSingupLogin(
        snsType: String,
        accessToken: String,
        expiresIn: String,
        userId: String
    ) {
        val authInfo = BmobThirdUserAuth(snsType, accessToken, expiresIn, userId)
        BmobUser.loginWithAuthData(authInfo, object : LogInListener<JSONObject>() {
            override fun done(user: JSONObject, e: BmobException?) {
                if (e == null) {
                    MeObj.loginState.value = true
                    MeObj.loginLoadState.value = false
                    fetchUser()
                    showToast("成功")
                } else {
                    showToast("失败")
                }
            }
        })
    }
}