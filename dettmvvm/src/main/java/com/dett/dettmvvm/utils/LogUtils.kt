package com.dett.dettmvvm.utils

import android.util.Log
import java.util.*

/**
 * 日志管理文件
 *
 * Created by：wangjian on 2017/12/22 14:40
 */
object LogUtils {
    private const val TAG = "LogUtils"
    private val mLogLocale = Locale.CHINA

    val isLoggable: Boolean
        get() = true

    fun d(msg: Any?) {
        d(TAG, GsonHelper.toJson(msg))
    }

    fun d(tag: String?, msg: String) {
        val log = getLog(msg)
        if (isLoggable) Log.d(tag, log)
    }

    private fun getLog(msg: String): String {
        return fileLineMethod + msg
    }

    private val fileLineMethod: String
        get() {
            val traceElements =
                Exception().stackTrace
            var method = ""
            if (traceElements.size > 4) {
                val traceElement = traceElements[4]
                method = "[" +  //
                        traceElement.fileName +  //
                        " | " +  //
                        traceElement.lineNumber +  //
                        " | " +  //
                        traceElement.methodName +  //
                        "] "
            }
            return method
        }
}