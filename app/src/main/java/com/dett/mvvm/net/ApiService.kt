package com.dett.mvvm.net

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/10/24 15:32
 */
object ApiService {

    private val mInterfaces by lazy { RetrofitClient.getInstance().create(Interfaces::class.java) }

    suspend fun getBanners() = mInterfaces.getBanners()

    suspend fun getBanners2() = mInterfaces.getBanners2()













}