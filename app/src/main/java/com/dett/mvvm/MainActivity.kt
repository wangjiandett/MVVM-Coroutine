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

        // 监听过滤掉BaseResponse的请求

        //========================== 第1种监听方式==========================
        mViewModel.banners.observe(this,
            Observer {
                textView.text = GsonHelper.toJson(it)
                LogUtils.d(it.data)
            })

        // ========================== 第2种监听方式==========================
        mViewModel.banners.observe(this, {
            textView.text = GsonHelper.toJson(it)
            LogUtils.d(it)
        }, { code, msg ->
            LogUtils.d("msg:$msg")
        })

        // ========================== 第3种监听方式==========================
        mViewModel.banners.observe(this,
            object : SimpleObserver<List<BannerBean>> {
                override fun onSuccess(value: List<BannerBean>?) {
                    textView.text = GsonHelper.toJson(value)
                    LogUtils.d(value)
                }

                override fun onFail(code: Int, msg: String?) {
                }
            })

        // 监听未过滤掉BaseResponse的请求
        // 监听方式同上
        mViewModel.banners2.observe(this, {
            textView.text = GsonHelper.toJson(it)
            LogUtils.d(it)
        }, { code, msg ->
            LogUtils.d("code:$code, msg:$msg")
        })
    }

    fun onClick(view: View) {
        if (view.id == R.id.tv_text) {
            mViewModel.getBanner()
        } else if (view.id == R.id.tv_text1) {
            mViewModel.getBanner2()
        }
    }

}