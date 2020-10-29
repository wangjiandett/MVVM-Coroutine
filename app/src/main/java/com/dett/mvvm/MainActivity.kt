package com.dett.mvvm

import android.view.View
import androidx.lifecycle.Observer
import com.dett.dettmvvm.base.SimpleObserver
import com.dett.dettmvvm.base.observe
import com.dett.dettmvvm.base.ui.BaseActivity
import com.dett.dettmvvm.utils.GsonHelper
import com.dett.dettmvvm.utils.LogUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<DemoModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {

        mViewModel.banners.observe(this,
            Observer {
                textView.text = GsonHelper.toJson(it)
                LogUtils.d(it)
            })

        mViewModel.banners2.observe(
            this,
            object : SimpleObserver<List<BannerBean>> {

                override fun onSuccess(value: List<BannerBean>?) {
                    LogUtils.d(value)
                }

                override fun onFail(code: Int, msg: String?) {

                }

            })

        mViewModel.banners2.observe(this, {
            LogUtils.d(it)
        }, { code, msg ->
            LogUtils.d("msg:$msg")
        })
    }

    fun onClick(view: View) {
        if(view.id == R.id.tv_text){
            mViewModel.getBanner()
        }
        else if(view.id == R.id.tv_text1){
            mViewModel.getBanner2()
        }
        else if(view.id == R.id.tv_text2){
            mViewModel.getBanner2()
        }

    }

}