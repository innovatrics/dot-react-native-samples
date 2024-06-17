package com.dotreactnativesamples

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager
import kotlinx.coroutines.GlobalScope

class DotSdkReactPackage : ReactPackage {

    override fun createNativeModules(context: ReactApplicationContext): List<NativeModule> = listOf(
        DotSdkReactModule(context, GlobalScope)
    )

    override fun createViewManagers(context: ReactApplicationContext): List<ViewManager<View, ReactShadowNode<*>>> = emptyList()
}
