package com.dett.dettmvvm.base

/**
 * IBaseResponse
 *
 * @author wangjian
 * Created on 2020/10/24 15:50
 */
interface IBaseResponse<T> {
    /**
     * 请求响应码
     */
    fun code(): Int

    /**
     * 请求提示msg
     */
    fun msg(): String

    /**
     * 请求响应数据
     */
    fun data(): T

    /**
     * 是否请求成功
     */
    fun isSuccess(): Boolean
}