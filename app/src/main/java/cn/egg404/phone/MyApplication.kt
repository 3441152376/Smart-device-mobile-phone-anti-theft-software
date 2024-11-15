package cn.egg404.phone

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import cn.bmob.v3.Bmob
import com.tencent.tauth.Tencent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var CONTEXT: Context
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = this
        Tencent.setIsPermissionGranted(true);
        Bmob.initialize(this, "f4a6c72fb41f8f6b8146723ee85eb5ac")

    }
}