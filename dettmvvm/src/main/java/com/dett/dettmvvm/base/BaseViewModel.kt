package com.dett.dettmvvm.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * BaseViewModel
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
     * 调用ViewModel的  #onCleared 方法取消所有协程，即调用 Job.cancel()方法
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }


    /**
     * 自定义监听器，在界面销毁时取消请求
     * ViewModel中已经自动实现了 cancel，无需手动实现
     */
//    inner class CoroutineLifecycleListener(private val job: Job) : LifecycleObserver {
//
//        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//        fun cancelCoroutine() {
//            LogUtils.d("mLifecycleOwner: ${job.isCancelled}")
//            if (!job.isCancelled) {
//                job.cancel()
//            }
//        }
//    }
}