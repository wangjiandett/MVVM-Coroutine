package com.dett.dettmvvm.base.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dett.dettmvvm.base.BaseRepository
import com.dett.dettmvvm.base.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/10/28 14:36
 */
abstract class BaseActivity<VM : BaseViewModel<out BaseRepository>>: AppCompatActivity() {

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

            dealError()
        }
    }

    protected fun dealError(){
        mViewModel.mLoadState.observe(this, Observer {
            Toast.makeText(this, it.msg, Toast.LENGTH_LONG).show()
        })
    }

}