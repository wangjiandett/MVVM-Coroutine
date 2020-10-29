package com.dett.mvvm

import androidx.lifecycle.MutableLiveData
import com.dett.dettmvvm.base.Message

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/10/24 15:50
 */
class DemoModel : AppModel<DemoRepository>() {

    val banners: MutableLiveData<List<BannerBean>> = MutableLiveData()

    val banners2: MutableLiveData<Message<List<BannerBean>>> = MutableLiveData()

    fun getBanner(): MutableLiveData<List<BannerBean>> {
        sendRequest {
            banners.value = mRepository.getBanners2()
        }
        return banners
    }

    fun getBanner2() {
        getBaseResponse({ mRepository.getBanners() }, banners2)
    }

}