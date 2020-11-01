# MVVM-Coroutine

最近看到很多人在学习kotlin的时候，都有提及到`协程`，于是也就在网上跟着学习了一波。但是学过之后在什么用到呢？很多大牛开始用这个代替Rxjava或者线程来写MVVM框架了。更多关于个方面的原理问题，这里就不讨论了。以下就是我参考了网上很多大佬写的例子加入了自己的一些想法，模仿的一个MVVM框架分享给大家。希望能给要学习MVVM的小伙伴做个参考。

**使用retrofit+okhttp+协程的mvvm请求框架**

**特点：**
1. 使用自定义的MutableLiveData的Observer，可以像使用回调一样方便的获取到返回值和异常
同时又不会造成内存泄露
2. 自定义异常处理
3. 支持任意响应数据格式，拓展方便

[项目地址https://github.com/wangjiandett/MVVM-Coroutine](https://github.com/wangjiandett/MVVM-Coroutine)

## 下面贴一些项目中的demo使用方法代码，让大家有个大概的了解，更多使用方法请参考源码和代码注释
### 1. model层代码，从网络中获取数据，如需获取返回本地数据可自行在`DemoRepository `类中添加修改

```
object ApiService {

    private val mInterfaces by lazy { RetrofitClient.getInstance().create(Interfaces::class.java) }

    suspend fun getBanners() = mInterfaces.getBanners()

    suspend fun getBanners2() = mInterfaces.getBanners2()

}
```

```
class DemoRepository : BaseRepository() {

    suspend fun getBanners(): BaseResponse<List<BannerBean>>? {
        return ApiService.getBanners()
    }

    suspend fun getBanners2(): BaseResponse<List<BannerBean>> {
        return ApiService.getBanners2()
    }

}
```
### 2. viewModel层的代码，通过从`DemoRepository`中获取数据，之后设置到对应的`MutableLiveData`中

```
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
```
### 3. view层的代码，通过创建viewModel实例，调用其中的对应方法获取数据，并监听数据变化和请求状态，支持3种数据回调方式

```
class MainViewModelActivity : BaseViewModelActivity<DemoModel>() {

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
```
到此就是一个简单的使用demo，欢迎大家留言共同学习讨论


感谢以下参考项目：
[https://github.com/AleynP/MVVMLin](https://github.com/AleynP/MVVMLin)