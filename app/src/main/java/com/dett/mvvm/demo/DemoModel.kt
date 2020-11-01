package com.dett.mvvm.demo

import androidx.lifecycle.MutableLiveData
import com.dett.dettmvvm.base.Message
import com.dett.mvvm.net.BaseResponse

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/10/24 15:50
 */
class DemoModel : AppModel<DemoRepository>() {

    val banners: MutableLiveData<Message<List<BannerBean>>> = MutableLiveData()

    /**
     * 返回过滤掉BaseResponse之后的data数据
     */
    fun getBanner(): MutableLiveData<Message<List<BannerBean>>> {
        sendBaseResponseRequest({mRepository.getBanners2()}, {
            banners.value = it
        })
        return banners
    }




    val banners2: MutableLiveData<Message<BaseResponse<List<BannerBean>>?>> = MutableLiveData()

    /**
     * 返回没有经过过滤的响应数据
     */
    fun getBanner2() {
        sendRequest({mRepository.getBanners()}, {
            banners2.value = it
        })
    }

}