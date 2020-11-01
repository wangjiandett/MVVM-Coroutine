package com.dett.dettmvvm.base

import com.dett.dettmvvm.base.exception.ExceptionHandle
import com.dett.dettmvvm.base.exception.ResponseThrowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * BaseRepository，用于封装协程请求和回调
 *
 * @author wangjian
 * Created on 2020/10/24 15:50
 */
open class BaseRepository {

    //
    // ===========以下协程的响应结果需要响应是IBaseResponse实例==========
    //

    /**
     * 获取请求结果，并支持自定义异常处理
     *
     * @param block 请求协程
     * @param error 异常函数回调
     */
    suspend fun <T> getBaseResponseMessageWithError(
        block: suspend CoroutineScope.() -> IBaseResponse<T>?,
        error: (ResponseThrowable) -> Message<T>?
    ): Message<T>? {
        return getBaseResponseMessage(
            block,
            { Message.getSuccessMessage(it) },
            { error(it) })
    }


    /**
     * 获取请求结果
     *
     * @param block 请求协程
     * @param success 请求成功函数回调，默认实现返回Message.getSuccessMessage(it)
     * @param error 异常函数回调，默认实现返回Message.getFailMessage(it.errMsg)
     * @param complete 请求完成函数回调，默认实现返回Message.getFinishMessage()
     */
    suspend fun <T> getBaseResponseMessage(
        block: suspend CoroutineScope.() -> IBaseResponse<T>?,
        success: (T) -> Message<T>? = { Message.getSuccessMessage(it) },
        error: (ResponseThrowable) -> Message<T>? = { Message.getFailMessage(it.errMsg) },
        complete: () -> Message<T>? = { Message.getFinishMessage() }
    ): Message<T>? {
        var message: Message<T>? = null
        getBaseResponse({
            block()
        }, {
            message = success(it)
        }, {
            message = error(it)
        }, {
            complete() // 此处不可在对message赋值，否则会覆盖success或error中的回调值，因为其实在try中的finally里面执行的
        })

        return message
    }


    /**
     * 过滤出请求结果，其他全抛异常
     *
     * @param block 请求体
     * @param success 成功回调
     * @param error 失败回调
     * @param complete  完成回调（在finally中执行，无论成功失败都会调用）
     */
    suspend fun <T> getBaseResponse(
        block: suspend CoroutineScope.() -> IBaseResponse<T>?,
        success: (T) -> Unit = {},
        error: (ResponseThrowable) -> Unit = {},
        complete: () -> Unit = {}
    ) {
        handleException(
            {
                withContext(Dispatchers.IO) {
                    block()?.let {
                        if (it.isSuccess()) it.data()
                        else throw ResponseThrowable(it.code(), it.msg())
                    }
                }.also {
                    if (it != null) {
                        success(it)
                    }
                }
            },
            {
                error(it)
            },
            { complete() }
        )
    }

    //
    // ===========以下协程的响应结果可以是任意实例==========
    //


    /**
     * 获取请求结果，并支持自定义异常处理
     *
     * @param block 请求协程
     * @param error 异常函数回调
     */
    suspend fun <T> getResponseWithError(
        block: suspend CoroutineScope.() -> T,
        error: (ResponseThrowable) -> Message<T>?
    ): Message<T>? {
        return getResponseMessage(
            block,
            { Message.getSuccessMessage(it) },
            { error(it) })
    }

    /**
     * 获取请求结果，没有经过处理的最原始的数据
     *
     * @param block 请求协程
     * @param success 请求成功函数回调，默认实现返回Message.getSuccessMessage(it)
     * @param error 异常函数回调，默认实现返回Message.getFailMessage(it.errMsg)
     * @param complete 请求完成函数回调，默认实现返回Message.getFinishMessage()
     */
    suspend fun <T> getResponseMessage(
        block: suspend CoroutineScope.() -> T,
        success: (T) -> Message<T>? = { Message.getSuccessMessage(it) },
        error: (ResponseThrowable) -> Message<T>? = { Message.getFailMessage(it.errMsg) },
        complete: () -> Message<T>? = { Message.getFinishMessage() }
    ): Message<T>? {
        var message: Message<T>? = null
        getResponse(block, {
            message = success(it)
        }, {
            message = error(it)
        }, {
            complete() // 此处不可在对message赋值，否则会覆盖success或error中的回调值，因为其实在try中的finally里面执行的
        })

        return message
    }

    /**
     * 获取请求结果，没有经过处理的最原始的数据
     *
     * @param block 请求协程
     * @param success 请求成功函数回调
     * @param error 异常函数回调，默认空实现
     * @param complete 请求完成函数回调，默认空实现
     */
    suspend fun <T> getResponse(
        block: suspend CoroutineScope.() -> T,
        success: suspend CoroutineScope.(T) -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit = {},
        complete: suspend CoroutineScope.() -> Unit = {}
    ) {
        handleException(
            {
                withContext(Dispatchers.IO) {
                    block().let {
                        it ?: throw ResponseThrowable(-1, "getResponse() response is null")
                    }
                }.also { success(it) }
            },
            { error(it) },
            { complete() }
        )
    }


    /**
     * 异常统一处理
     */
    private suspend fun handleException(
        block: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                block()
            } catch (e: Throwable) {
                error(ExceptionHandle.handleException(e))
            } finally {
                complete()
            }
        }
    }


}