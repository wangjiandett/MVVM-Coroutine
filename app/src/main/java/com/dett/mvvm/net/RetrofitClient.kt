package com.dett.mvvm.net

import com.dett.dettmvvm.utils.LogUtils
import com.dett.mvvm.net.Constant.BASE_URL
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object {
        private lateinit var retrofit: Retrofit
        fun getInstance() =
            SingletonHolder.INSTANCE
    }

    private object SingletonHolder {
        val INSTANCE = RetrofitClient()
    }

    init {
        retrofit = Retrofit.Builder()
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {

        // 1 print log
        val loggingInterceptor =
            HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    try {
                        // 防止出现%号，解析异常
                        val text = URLDecoder.decode(
                            message.replace(
                                "%(?![0-9a-fA-F]{2})".toRegex(),
                                "%25"
                            ), "UTF-8"
                        )
                        LogUtils.d(text)
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                        LogUtils.d(message)
                    }
                }
            })

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(20L, TimeUnit.SECONDS)
            .writeTimeout(20L, TimeUnit.SECONDS)
            .readTimeout(20L, TimeUnit.SECONDS)
            .addNetworkInterceptor(loggingInterceptor)
            .connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
            .build()
    }

    fun <T> create(service: Class<T>): T =
        retrofit.create(service)
}