package com.dett.mvvm

import com.dett.dettmvvm.base.BaseRepository
import com.dett.mvvm.net.ApiService
import com.dett.mvvm.net.BaseResponse

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/10/24 15:42
 */
class DemoRepository : BaseRepository() {

    suspend fun getBanners(): BaseResponse<List<BannerBean>> {
        return ApiService.getBanners()
    }

    suspend fun getBanners2(): List<BannerBean> {
        return sendRequest { ApiService.getBanners2() }
    }

}