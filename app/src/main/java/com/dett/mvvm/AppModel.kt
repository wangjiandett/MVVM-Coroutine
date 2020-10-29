package com.dett.mvvm

import com.dett.dettmvvm.base.BaseViewModel
import com.dett.dettmvvm.utils.Status
import kotlinx.coroutines.CoroutineScope

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/10/28 15:43
 */
abstract class AppModel<T> : BaseViewModel<T>() {

    fun sendRequest(block: suspend CoroutineScope.() -> Unit) {
        getResponse(block, {
            mLoadState.postValue(Status(it.code, it.errMsg))
            // 如需登录，可统一处理登录跳转
        })
    }

}