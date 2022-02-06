// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.presentation.fragment.calling.participant.grid.screenshare

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

internal class ScreenShareScrollLayout :
    FrameLayout,
    GestureListenerEvents {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
public  var minWidth: Int = 0
    private val gestureListener = GestureListener(context, this)

    private lateinit var rendererView: View

    fun onTouch(motionEvent:MotionEvent) {
        gestureListener.onTouchEvent(motionEvent)
    }

    fun setRendererView(view: View) {
        rendererView = view
    }

    override fun onSingleClick() {

    }

    override fun onDoubleClick(motionEvent: MotionEvent) {

    }

    override fun initTransformation() {
    }

    var lastScale = 0f

    override fun updateTransformation() {
        val x = gestureListener.translationX
        val y = gestureListener.translationY
        val scale = gestureListener.scale
        Log.d("catchup", " x $x y $y scale $scale");
        val delta = 100

        if(lastScale != scale) {
            lastScale = scale
            if (scale> 1) {
                val lp = rendererView.layoutParams
                val width =  lp.width + delta
                lp.height = (width/1.7777777777).toInt()
                lp.width = width
                rendererView.layoutParams = lp
                this.invalidate()
                this.refreshDrawableState()
            } else {
                val lp = rendererView.layoutParams
                if(lp.width > minWidth -delta) {
                    val width =  lp.width - delta
                    lp.height = (width/1.7777777777).toInt()
                    lp.width = width
                    rendererView.layoutParams = lp
                    this.invalidate()
                    this.refreshDrawableState()
                }

            }
        }


    }
}
