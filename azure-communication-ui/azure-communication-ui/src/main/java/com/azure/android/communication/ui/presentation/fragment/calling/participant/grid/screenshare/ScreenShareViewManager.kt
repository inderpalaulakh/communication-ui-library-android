// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.presentation.fragment.calling.participant.grid.screenshare

import android.content.Context
import android.content.res.Configuration
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import com.azure.android.communication.calling.RendererListener
import com.azure.android.communication.calling.VideoStreamRenderer

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

    // zoom transformation is applied to rendererViewTransformationWrapper
    // applying transformation to renderer view does not reset on screen share
    private lateinit var rendererViewTransformationWrapper: LinearLayout

    fun getScreenShareView(rendererView: View): View {

        val linearLayout = FrameLayout(context)

        linearLayout.addView(rendererView)

        val scrollView = ScrollView(context)
        scrollView.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        scrollView.addView(linearLayout)

        val horizontalScrollView = HorizontalScrollView(context)
        horizontalScrollView.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        horizontalScrollView.addView(scrollView)

        videoContainer.addView(horizontalScrollView, 0)

        val lp = rendererView.layoutParams
        lp.height = 1080*3//962//1080
        lp.width = 1920*3//1710//1920
        rendererView.layoutParams = lp

        return horizontalScrollView
    }

    private fun rendererListener() = object : RendererListener {
        override fun onFirstFrameRendered() {
            setScreenShareLayoutSize()
            getScreenShareVideoStreamRendererCallback()?.removeRendererListener(this)
        }

        override fun onRendererFailedToStart() {
        }
    }

    private fun setScreenShareLayoutSize() {
        val streamSize = getScreenShareVideoStreamRendererCallback()?.size
        if (streamSize == null) {
            videoContainer.postDelayed({
                setScreenShareLayoutSize()
            }, STREAM_SIZE_RETRY_DURATION)
        } else {
            videoContainer.post {
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
                screenShareZoomFrameLayout.enableZoom()
            }
        }
    }
}
