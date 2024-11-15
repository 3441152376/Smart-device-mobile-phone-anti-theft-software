package cn.egg404.phone.common

import cn.egg404.phone.MyApplication
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object Storage {
    const val AUDIO_NAME = "audio.mp3"


    private var root: File? = null

    init {
        root = File(MyApplication.CONTEXT.filesDir, AUDIO_NAME)
    }

    fun getAudioPath(): File {
        return File(MyApplication.CONTEXT.filesDir, AUDIO_NAME)
    }

    fun putAudioFile(f: File) {
        val ins = f.inputStream()
        val out = FileOutputStream(getAudioPath())
        var len = 0
        val byteArray = ByteArray(524)
        while ((ins.read(byteArray).apply { len = this }) != -1) {
            out.write(byteArray, 0, len)
        }

        out.flush()
        ins.close()
    }

    fun putAudioFile(ins: InputStream) {
        val out = FileOutputStream(getAudioPath())
        var len = 0
        val byteArray = ByteArray(524)
        while ((ins.read(byteArray).apply { len = this }) != -1) {
            out.write(byteArray, 0, len)
        }

        out.flush()
        ins.close()
    }

    fun deleteAudioFile(): Boolean {
        return getAudioPath().delete()
    }
}