package com.dett.dettmvvm.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/10/24 15:50
 */
open class BaseViewModel<T> : ViewModel() {

    /**
     * 根据泛型自动初始化Repository实例
     */
    val mRepository: T by lazy {
        (CommonUtil.getClass<T>(this))
            .getDeclaredConstructor()
            .newInstance()
    }

    /**
     * 所有协程网络请求都在 viewModelScope 域中启动，当页面销毁时会自动
     * 调用ViewModel的  #onCleared 方法取消所有协程
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }

}