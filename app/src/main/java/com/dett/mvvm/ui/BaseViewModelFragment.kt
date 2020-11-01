package com.dett.mvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
abstract class BaseViewModelFragment<VM : BaseViewModel<out BaseRepository>>: Fragment() {

    lateinit var mView: View
    protected lateinit var mViewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mView = inflater.inflate(getLayoutId(), null)
        createViewModel()
        initView()
        initData()
        return mView
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