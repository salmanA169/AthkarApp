package com.athkar.sa

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class TouchFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs:AttributeSet? = null ,
    defaultStyle :Int = 0
):FrameLayout(context,attrs,defaultStyle) , NestedScrollingParent2 {

    private val TAG: String = javaClass.simpleName

    private fun getMotionLayout() :NestedScrollingParent2{
        var currentNested :NestedScrollingParent2 ? = null
        for( i in children){
            if (i is CoordinatorLayout){
                currentNested = i
                break
            }
        }
        return currentNested!!
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return getMotionLayout().onStartNestedScroll(child, target, axes, type)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        getMotionLayout().onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        getMotionLayout().onStopNestedScroll(target,type)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        getMotionLayout().onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
       getMotionLayout().onNestedPreScroll(target, dx, dy, consumed, type)
    }
}