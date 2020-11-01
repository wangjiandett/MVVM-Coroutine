package com.dett.mvvm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dett.dettmvvm.base.BaseRepository
import com.dett.dettmvvm.base.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * 简单封装获取BaseViewModel实例
 *
 * @author wangjian
 * Created on 2020/10/28 14:36
 */
abstract class BaseViewModelActivity<VM : BaseViewModel<out BaseRepository>>: AppCompatActivity() {

    protected lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        createViewModel()
        initView()
        initData()
    }

    abstract fun getLayoutId(): Int

    open fun initView(){}
    open fun initData(){}

    private fun createViewModel() {
        val type = this.javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            mViewModel = ViewModelProvider(this,
                ViewModelProvider.NewInstanceFactory()
            ).get(tClass) as VM
        }
    }
}