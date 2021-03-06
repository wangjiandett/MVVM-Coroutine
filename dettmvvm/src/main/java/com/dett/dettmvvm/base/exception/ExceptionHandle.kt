package com.dett.dettmvvm.base.exception

import android.net.ParseException
import com.dett.dettmvvm.utils.StatusUtils
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * 统一异常处理
 *
 * @author wangjian
 * Created on 2020/11/1 14:50
 */
object ExceptionHandle {

    fun handleException(throwable: Throwable): ResponseThrowable {
        val ex: ResponseThrowable
        // 自定义异常
        if (throwable is ResponseThrowable) {
            ex = throwable
        }
        // 其他各种异常
        else if (throwable is HttpException) {
            ex = ResponseThrowable(
                StatusUtils.HTTP_ERROR,
                throwable
            )
        } else if (throwable is JsonParseException
            || throwable is JSONException
            || throwable is ParseException || throwable is MalformedJsonException
        ) {
            ex = ResponseThrowable(
                StatusUtils.PARSE_ERROR,
                throwable
            )
        } else if (throwable is ConnectException || throwable is UnknownHostException) {
            ex = ResponseThrowable(
                StatusUtils.NETWORD_ERROR,
                throwable
            )
        } else if (throwable is javax.net.ssl.SSLException) {
            ex = ResponseThrowable(
                StatusUtils.SSL_ERROR,
                throwable
            )
        } else if (throwable is java.net.SocketTimeoutException) {
            ex = ResponseThrowable(
                StatusUtils.TIMEOUT_ERROR,
                throwable
            )
        } else {
            ex = if (!throwable.message.isNullOrEmpty()) ResponseThrowable(
                1000,
                throwable.message!!,
                throwable
            )
            else ResponseThrowable(
                StatusUtils.UNKNOWN,
                throwable
            )
        }
        return ex
    }
}