// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.presentation.fragment.calling.participant.grid.screenshare

import android.content.Context
import android.content.res.Configuration
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.azure.android.communication.calling.VideoStreamRenderer
import com.azure.android.communication.ui.R

internal class ScreenShareViewManager(
    private val context: Context,
    private val videoContainer: ConstraintLayout,
    private val getScreenShareVideoStreamRendererCallback: () -> VideoStreamRenderer?,
    private val showFloatingHeaderCallBack: () -> Unit,
) {
    companion object {
        private const val STREAM_SIZE_RETRY_DURATION: Long = 1000
    }

    private lateinit var screenShareZoomFrameLayout: ScreenShareZoomFrameLayout
    private lateinit var rendererView: View

    // zoom transformation is applied to rendererViewTransformationWrapper
    // applying transformation to renderer view does not reset on screen share
    private lateinit var rendererViewTransformationWrapper: LinearLayout
    private lateinit var rendererViewTransformationWrapper2: LinearLayout


    fun getScreenShareView(rendererView: View): ScreenShareZoomFrameLayout {
        this.rendererView = rendererView
        rendererViewTransformationWrapper2 = LinearLayout(this.context)
     //   rendererViewTransformationWrapper2.addView(rendererView)
        rendererViewTransformationWrapper = LinearLayout(this.context)
        rendererViewTransformationWrapper.addView(rendererViewTransformationWrapper2)

        screenShareZoomFrameLayout = ScreenShareZoomFrameLayout(this.context)
        screenShareZoomFrameLayout.layoutParams =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )

        screenShareZoomFrameLayout.addView(rendererViewTransformationWrapper)
        screenShareZoomFrameLayout.setFloatingHeaderCallback(showFloatingHeaderCallBack)

        screenShareZoomFrameLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    screenShareZoomFrameLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    // update view size only after child is added successfully
                    // otherwise renderer video size will be 0
                    screenShareZoomFrameLayout.postDelayed({
                      //  setScreenShareLayoutSize()
                    }, STREAM_SIZE_RETRY_DURATION)
                }
            })



        val layoutParams =
            rendererViewTransformationWrapper.layoutParams as FrameLayout.LayoutParams
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutParams.height = 584
            layoutParams.gravity = Gravity.CENTER_VERTICAL
        }
        rendererViewTransformationWrapper.layoutParams = layoutParams
        rendererViewTransformationWrapper.background = ContextCompat.getDrawable(
            context,
            R.color.azure_communication_ui_color_background_red
        )
        screenShareZoomFrameLayout.background = ContextCompat.getDrawable(
            context,
            R.color.azure_communication_ui_color_warning
        )
        screenShareZoomFrameLayout.setTView(rendererViewTransformationWrapper2, rendererView)
        screenShareZoomFrameLayout.enableZoom()

        return screenShareZoomFrameLayout
    }

    private fun setScreenShareLayoutSize() {
        val streamSize = getScreenShareVideoStreamRendererCallback()?.size
        if (streamSize == null) {
            screenShareZoomFrameLayout.postDelayed({
                setScreenShareLayoutSize()
            }, STREAM_SIZE_RETRY_DURATION)
        } else {
            screenShareZoomFrameLayout.post {
                // this logic is from Azure communication calling SDK code to find width and height of video view excluding grey screen
                val viewWidth = videoContainer.width.toFloat()
                val viewHeight = videoContainer.height.toFloat()
                val videoWidth = streamSize.width
                val videoHeight = streamSize.height

                val scaleWidth = viewWidth / videoWidth
                val scaleHeight = viewHeight / videoHeight
                val scale = scaleWidth.coerceAtMost(scaleHeight)

                val layoutParams =
                    rendererViewTransformationWrapper.layoutParams as FrameLayout.LayoutParams
                if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    layoutParams.height = (scale * videoHeight).toInt()
                    layoutParams.gravity = Gravity.CENTER_VERTICAL
                } else {
                    layoutParams.width = (scale * videoWidth).toInt()
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL
                }
                rendererViewTransformationWrapper.layoutParams = layoutParams
                rendererViewTransformationWrapper.background = ContextCompat.getDrawable(
                    context,
                R.color.azure_communication_ui_color_background_red
                )
                screenShareZoomFrameLayout.background = ContextCompat.getDrawable(
                    context,
                    R.color.azure_communication_ui_color_warning
                )
                screenShareZoomFrameLayout.setTView(rendererViewTransformationWrapper2, rendererView)
                screenShareZoomFrameLayout.enableZoom()
                rendererViewTransformationWrapper2.removeView(rendererView)
            }
        }
    }
}
