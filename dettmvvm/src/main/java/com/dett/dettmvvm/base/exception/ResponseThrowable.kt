package com.dett.dettmvvm.base.exception

import com.dett.dettmvvm.base.IBaseResponse
import com.dett.dettmvvm.utils.Status

class ResponseThrowable : Exception {
    var code: Int
    var errMsg: String

    constructor(code: Int, errMsg: String) {
        this.code = code
        this.errMsg = errMsg
    }

    constructor(status: Status, e: Throwable? = null) : super(e) {
        code = status.code
        errMsg = status.msg
    }

    constructor(code: Int, msg: String, e: Throwable? = null) : super(e) {
        this.code = code
        this.errMsg = msg
    }

    constructor(base: IBaseResponse<*>, e: Throwable? = null) : super(e) {
        this.code = base.code()
        this.errMsg = base.msg()
    }


}

