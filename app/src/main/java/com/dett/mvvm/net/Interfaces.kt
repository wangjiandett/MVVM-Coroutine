package com.dett.mvvm.net

import com.dett.mvvm.demo.bean.BannerBean
import retrofit2.http.GET

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/10/24 15:33
 */
interface Interfaces {

    @GET("banner/json")
    suspend fun getBanners(): BaseResponse<List<BannerBean>>

    @GET("banner/json")
    suspend fun getBanners2(): BaseResponse<List<BannerBean>>
}