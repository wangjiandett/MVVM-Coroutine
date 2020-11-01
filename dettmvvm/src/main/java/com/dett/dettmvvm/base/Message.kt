package com.dett.dettmvvm.base

import com.dett.dettmvvm.utils.StatusUtils

/**
 * Message，用于封装回调到界面的响应结果
 *
 * @author wangjian
 * Created on 2020/10/29 9:22
 */
class Message<T> {

    /**
     * 响应code
     */
    var code = 0

    /**
     * 响应的msg
     */
    var msg = ""

    /**
     * 响应的数据
     */
    var data: T? = null

    constructor(code: Int, msg: String) {
        this.code = code
        this.msg = msg
    }

    constructor(code: Int, data: T?) {
        this.code = code
        this.data = data
    }

    companion object {
        fun <T> getMessage(code: Int, msg: String): Message<T> {
            return Message<T>(code, msg)
        }

        fun <T> getMessage(code: Int, data: T?): Message<T> {
            return Message<T>(code, data)
        }

        /**
         * 请求成功
         */
        fun <T> getSuccessMessage(data: T?): Message<T> {
            return getMessage<T>(StatusUtils.SUCCESS.code, data)
        }

        /**
         * 请求失败
         */
        fun <T> getFailMessage(msg: String): Message<T> {
            return getMessage<T>(StatusUtils.FAIL.code, msg)
        }

        /**
         * 请求完成
         */
        fun <T> getFinishMessage(): Message<T> {
            return getMessage<T>(StatusUtils.FINISH.code, "")
        }

    }


}