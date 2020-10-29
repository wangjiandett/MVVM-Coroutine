package com.dett.dettmvvm.utils

object StatusUtils {

    /**
     * 加载中
     */
    var LOADING = Status(1, "加载中")

    /**
     * 加载完成
     */
    var FINISH = Status(2, "加载完成")

    /**
     * 加载失败
     */
    var FAIL = Status(3, "加载失败")

    /**
     * 加载成功
     */
    var SUCCESS = Status(4, "加载成功")



    //=============================================

    /**
     * 未知错误
     */
    var UNKNOWN = Status(1000, "未知错误")

    /**
     * 解析错误
     */
    var PARSE_ERROR = Status(1001, "解析错误")

    /**
     * 网络错误
     */
    var NETWORD_ERROR = Status(1002, "网络错误")

    /**
     * 协议出错
     */
    var HTTP_ERROR = Status(1003, "协议出错")

    /**
     * 证书出错
     */
    var  SSL_ERROR = Status(1004, "证书出错")

    /**
     * 空出错
     */
    var NULL_ERROR = Status(1005, "数据为空")

    /**
     * 连接超时
     */
    var TIMEOUT_ERROR = Status(1006, "连接超时")


}

class Status(var code: Int, var msg: String)

