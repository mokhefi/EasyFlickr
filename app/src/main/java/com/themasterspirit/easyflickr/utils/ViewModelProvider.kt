package com.themasterspirit.easyflickr.utils

//inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
//        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
//        crossinline provider: () -> VM) = lazy(mode) {
//    ViewModelProviders.of(this, object : ViewModelProvider.Factory {
//        override fun <T1 : ViewModel> create(aClass: Class<T1>) = provider() as T1
//    }).get(VM::class.java)
//}
//
//inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
//        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
//        crossinline provider: () -> VM) = lazy(mode) {
//    ViewModelProviders.of(this, object : ViewModelProvider.Factory {
//        override fun <T1 : ViewModel> create(aClass: Class<T1>) = provider() as T1
//    }).get(VM::class.java)
//}