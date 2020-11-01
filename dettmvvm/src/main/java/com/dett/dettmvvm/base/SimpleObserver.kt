package com.dett.dettmvvm.base

import androidx.lifecycle.Observer
import com.dett.dettmvvm.utils.StatusUtils

/**
 * MutableLiveData回调函数，处理请求成功，失败，完成回调
 *
 * @author wangjian
 * Created on 2020/10/29 9:04
 */
interface SimpleObserver<T> : Observer<Message<T>> {

    override fun onChanged(msg: Message<T>) {
        when (msg.code) {
            StatusUtils.SUCCESS.code -> {
                onSuccess(msg.data)
            }
            StatusUtils.FAIL.code -> {
                onFail(msg.code, msg.msg)
            }
            StatusUtils.FINISH.code -> {
                onFinish()
            }
        }
    }

    fun onSuccess(value: T?)

    fun onFail(code: Int, msg: String?)

    fun onFinish(){}

}


