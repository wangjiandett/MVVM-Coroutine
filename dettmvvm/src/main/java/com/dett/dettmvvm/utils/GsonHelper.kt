package com.dett.dettmvvm.utils

import android.text.TextUtils
import com.google.gson.GsonBuilder
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

/**
 * Json工具类
 *
 * @author wangjian
 * Created on 2020/10/28 14:36
 */
object GsonHelper {

    //定义并配置gson
    val gson = GsonBuilder() //建造者模式设置不同的配置
        .serializeNulls() //序列化为null对象
        // .setLenient()// 设置GSON的非严格模式
        .setDateFormat("yyyy-MM-dd HH:mm:ss") //设置日期的格式
        .disableHtmlEscaping() //防止对html标签解析乱码 忽略对特殊字符的转换
        .create()

    /**
     * 对解析数据的形式进行转换
     *
     * @param obj 解析的对象
     * @return 转化结果为json字符串
     */
    fun toJson(obj: Any?): String {
        return if (obj == null) {
            ""
        } else try {
            gson.toJson(obj)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 解析为一个具体的对象
     *
     * @param json 要解析的字符串
     * @param obj  要解析的对象
     * @param <T>  将json字符串解析成obj类型的对象
     * @return
    </T> */
    fun <T> toObj(json: String?, obj: Class<T>?): T? {
        //如果为null直接返回为null
        return if (obj == null || TextUtils.isEmpty(json)) {
            null
        } else try {
            gson.fromJson(json, obj)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * @return 不区分类型 传什么解析什么
     */
    fun <T> toObj(jsonStr: String?, type: Type?): T {
        return gson.fromJson(jsonStr, type)
    }

    /**
     * 将Json数组解析成相应的映射对象列表
     * 解决类型擦除的问题
     */
    fun <T> toList(jsonStr: String?, clz: Class<T>): List<T> {
        var list: List<T> = gson.fromJson(
            jsonStr,
            PType(clz)
        )
        if (list == null) list = ArrayList()
        return list
    }

    fun <T> toMap(
        jsonStr: String?,
        clz: Class<T>
    ): Map<String?, T> {
        var map: Map<String?, T> =
            gson.fromJson(
                jsonStr,
                PType(clz)
            )
        return map
    }

    private class PType constructor(private val type: Type) :
        ParameterizedType {
        override fun getActualTypeArguments(): Array<Type> {
            return arrayOf(type)
        }

        override fun getRawType(): Type {
            return ArrayList::class.java
        }

        override fun getOwnerType(): Type? {
            return null
        }

    }
}