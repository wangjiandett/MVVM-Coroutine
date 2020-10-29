package com.dett.mvvm.net

import com.dett.dettmvvm.base.IBaseResponse


open class BaseResponse<T>(
    private val errorMsg: String,
    private val errorCode: Int,
    private val data: T
): IBaseResponse<T> {

    override fun code(): Int {
        return errorCode
    }

    override fun msg(): String {
        return errorMsg
    }

    override fun data() = data

    override fun isSuccess() = errorCode == Constant.SUCCESS_CODE // 定义请求成功响应码
}