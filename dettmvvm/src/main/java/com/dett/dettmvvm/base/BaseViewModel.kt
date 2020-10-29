package com.dett.dettmvvm.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dett.dettmvvm.base.exception.ExceptionHandle
import com.dett.dettmvvm.base.exception.ResponseThrowable
import com.dett.dettmvvm.utils.SingleLiveEvent
import com.dett.dettmvvm.utils.Status
import com.dett.dettmvvm.utils.StatusUtils
import kotlinx.coroutines.*

open class BaseViewModel<T> : ViewModel() {

    /**
     * 默认加载状态回调
     */
    val mLoadState by lazy { SingleLiveEvent<Status>() }

    val mRepository: T by lazy {
        (CommonUtil.getClass<T>(this))
            .getDeclaredConstructor()
            .newInstance()
    }

    /**
     * 所有网络请求都在 viewModelScope 域中启动，当页面销毁时会自动
     * 调用ViewModel的  #onCleared 方法取消所有协程
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }


    /**
     * 不过滤请求结果
     *
     * @param block 请求体
     * @param error 失败回调，默认通过mLoadState发送加载状态
     * @param complete  完成回调（无论成功失败都会调用）默认通过mLoadState发送加载状态
     */
    fun getResponse(
        block: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit = {
            if (showLoading) mLoadState.postValue(
                Status(it.code, it.errMsg)
            )
        },
        complete: suspend CoroutineScope.() -> Unit = {
            if (showLoading) mLoadState.postValue(
                StatusUtils.FINISH
            )
        },
        showLoading: Boolean = true
    ) {
        if (showLoading) mLoadState.postValue(StatusUtils.LOADING)
        launchUI {
            handleException(
                withContext(Dispatchers.IO) { block },
                { error(it) }, // 参数error中有默认的postValue实现
                { complete() }
            )
        }
    }


    fun <T> getBaseResponse(
        block: suspend CoroutineScope.() -> IBaseResponse<T>,
        liveData: MutableLiveData<Message<T>>
    ) {
        getBaseResponse({
            block()
        }, {
            liveData.value = Message.getSuccessMessage(it)
        }, {
            // 如需登录，可统一处理登录跳转
            liveData.value = Message.getFailMessage(it.errMsg)
        }, {
            liveData.value = Message.getFinishMessage()
        }, false)// 自定义成功，失败回调函数后，不在使用默认回调
    }


    /**
     * 过滤出请求结果，其他全抛异常
     *
     * @param block 请求体
     * @param success 成功回调
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     */
    fun <T> getBaseResponse(
        block: suspend CoroutineScope.() -> IBaseResponse<T>,
        success: (T) -> Unit = {},
        error: (ResponseThrowable) -> Unit = {
            if (showLoading) mLoadState.postValue(
                Status(
                    it.code,
                    it.errMsg
                )
            )
        },
        complete: () -> Unit = { if (showLoading) mLoadState.postValue(StatusUtils.FINISH) },
        showLoading: Boolean = true
    ) {
        if (showLoading) mLoadState.postValue(StatusUtils.LOADING)
        launchUI {
            handleException(
                {
                    withContext(Dispatchers.IO) {
                        block().let {
                            if (it.isSuccess()) it.data()
                            else throw ResponseThrowable(
                                it.code(),
                                it.msg()
                            )
                        }
                    }.also { success(it) }
                },
                { error(it) },
                { complete() }
            )
        }
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