package cn.egg404.phone.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import cn.egg404.phone.MyApplication
import com.alipay.sdk.app.PayTask
import kotlin.concurrent.thread

object PayUtil {
    private const val APPID = "2021003121611848"

    private const val RSA_PRIVATE = ""
    private const val SDK_PAY_FLAG = 1
    private const val SDK_AUTH_FLAG = 2


    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String?, String?>)

                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo: String = payResult.result // 同步返回需要验证的信息
                    val resultStatus: String = payResult.resultStatus
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showToast("支付成功")
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToast("支付失败")
                    }
                }
                /*  SDK_AUTH_FLAG -> {
                      val authResult = AuthResult(msg.obj as Map<String?, String?>, true)
                      val resultStatus: String = authResult.resultStatus

                      // 判断resultStatus 为“9000”且result_code
                      // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                      if (TextUtils.equals(
                              resultStatus,
                              "9000"
                          ) && TextUtils.equals(authResult.resultCode, "200")
                      ) {
                          // 获取alipay_open_id，调支付时作为参数extern_token 的value
                          // 传入，则支付账户为该授权账户
                          showToast("支付成功")

                      } else {
                          // 其他状态值则为授权失败
                          showToast("支付失败")

                      }
                  }*/
                else -> {}
            }
        }
    }


    fun payV2(money: String, success: () -> Unit, error: () -> Unit) {
        if (TextUtils.isEmpty(APPID)) {
            showToast("错误: 需要配置 PayDemoActivity 中的 APPID 和 RSA_PRIVATE")
            return
        }

        val rsa2 = true
        val params =
            OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2, money)
        val orderParam = OrderInfoUtil2_0.buildOrderParam(params)

        OrderInfoUtil2_0.getSign(
            params
        ) { sign ->
            val orderInfo = "$orderParam&$sign"

            val payRunnable = Runnable {
                val alipay = PayTask(MyApplication.CONTEXT as Activity)
                val result: Map<String, String> = alipay.payV2(orderInfo, true)
                /*Log.i("msp", result.toString())
                        val msg = Message()
                        msg.what = SDK_PAY_FLAG
                        msg.obj = result
                        mHandler.sendMessage(msg)*/

                val payResult = PayResult(result as Map<String?, String?>)

                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                val resultInfo: String = payResult.result // 同步返回需要验证的信息
                val resultStatus: String = payResult.resultStatus
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    success()
                } else {
                    error()
                }


            }

            // 必须异步调用
            val payThread = Thread(payRunnable)
            payThread.start()
        }


    }
}