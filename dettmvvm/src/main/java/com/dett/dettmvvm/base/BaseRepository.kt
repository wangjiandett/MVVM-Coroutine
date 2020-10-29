package com.dett.dettmvvm.base

import com.dett.dettmvvm.base.exception.ResponseThrowable

open class BaseRepository {

    suspend fun <T> sendRequest(block: suspend () -> IBaseResponse<T>): T {
        val response = block() // 获取请求结果，如有异常，上抛
        if (response.isSuccess()) {
            return response.data()
        }
        throw ResponseThrowable(response)
    }

}