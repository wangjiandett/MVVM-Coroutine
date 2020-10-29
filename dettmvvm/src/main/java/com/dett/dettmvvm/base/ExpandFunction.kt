package com.dett.dettmvvm.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/10/29 10:47
 */
fun <T> MutableLiveData<Message<T>>.observe(
    owner: LifecycleOwner,
    onSuccess: (T?) -> Unit,
    onFail: (Int, String?) -> Unit
) {
    this.observe(owner, object : SimpleObserver<T> {

        override fun onSuccess(value: T?) {
            onSuccess(value)
        }

        override fun onFail(code: Int, msg: String?) {
            onFail(code, msg)
        }
    })
}

fun <T> MutableLiveData<Message<T>>.observe(
    owner: LifecycleOwner,
    simpleObserver: SimpleObserver<T>
) {
    this.observe(owner, simpleObserver)
}