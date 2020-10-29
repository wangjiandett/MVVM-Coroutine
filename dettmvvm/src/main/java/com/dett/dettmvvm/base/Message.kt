package com.dett.dettmvvm.base

import com.dett.dettmvvm.utils.StatusUtils

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/10/29 9:22
 */
class Message<T> {

    var code = 0
    var msg = ""
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

        fun <T> getSuccessMessage(data: T?): Message<T> {
            return getMessage<T>(StatusUtils.SUCCESS.code, data)
        }

        fun <T> getFailMessage(msg: String): Message<T> {
            return getMessage<T>(StatusUtils.FAIL.code, msg)
        }

        fun <T> getFinishMessage(): Message<T> {
            return getMessage<T>(StatusUtils.FINISH.code, "")
        }

    }


}