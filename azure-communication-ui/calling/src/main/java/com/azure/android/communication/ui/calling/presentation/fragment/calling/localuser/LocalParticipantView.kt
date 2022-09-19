// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.calling.presentation.fragment.calling.localuser

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.azure.android.communication.ui.R
import com.azure.android.communication.ui.calling.presentation.VideoViewManager
import com.azure.android.communication.ui.calling.presentation.manager.AvatarViewManager
import com.microsoft.fluentui.persona.AvatarView

internal class LocalParticipantView : ConstraintLayout {

    companion object {
        lateinit var previewView: PreviewView
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private lateinit var viewModel: LocalParticipantViewModel
    private lateinit var videoViewManager: VideoViewManager
    private lateinit var localParticipantFullCameraHolder: FrameLayout
    private lateinit var localParticipantPip: ConstraintLayout
    private lateinit var localPipWrapper: ConstraintLayout
    private lateinit var localParticipantPipCameraHolder: ConstraintLayout
    private lateinit var switchCameraButton: ImageView
    private lateinit var pipSwitchCameraButton: ConstraintLayout
    private lateinit var avatarHolder: ConstraintLayout
    private lateinit var avatar: AvatarView
    private lateinit var pipAvatar: AvatarView
    private lateinit var displayNameText: TextView
    private lateinit var micImage: ImageView
    private lateinit var dragTouchListener: DragTouchListener
    private lateinit var accessibilityManager: AccessibilityManager

    init {
        previewView = PreviewView(context)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        localParticipantFullCameraHolder =
            findViewById(R.id.azure_communication_ui_call_local_full_video_holder)
        localParticipantPip =
            findViewById(R.id.azure_communication_ui_call_local_pip)
        localPipWrapper = findViewById(R.id.azure_communication_ui_call_local_pip_wrapper)
        localParticipantPipCameraHolder =
            findViewById(R.id.azure_communication_ui_call_local_pip_video_holder)
        switchCameraButton =
            findViewById(R.id.azure_communication_ui_call_switch_camera_button)
        pipSwitchCameraButton =
            findViewById(R.id.azure_communication_ui_call_local_pip_switch_camera_button)
        avatarHolder =
            findViewById(R.id.azure_communication_ui_call_local_avatarHolder)

        avatar =
            findViewById(R.id.azure_communication_ui_call_local_avatar)
        pipAvatar =
            findViewById(R.id.azure_communication_ui_call_local_pip_avatar)

        displayNameText =
            findViewById(R.id.azure_communication_ui_call_local_display_name)
        micImage =
            findViewById(R.id.azure_communication_ui_call_local_mic_indicator)
        switchCameraButton.setOnClickListener { viewModel.switchCamera() }
        pipSwitchCameraButton.setOnClickListener { viewModel.switchCamera() }
        dragTouchListener = DragTouchListener()
    }

    fun stop() {
        localParticipantFullCameraHolder.removeAllViews()
        localParticipantPipCameraHolder.removeAllViews()
    }

    fun start(
        viewLifecycleOwner: LifecycleOwner,
        viewModel: LocalParticipantViewModel,
        videoViewManager: VideoViewManager,
        avatarViewManager: AvatarViewManager,
    ) {

        this.viewModel = viewModel
        this.videoViewManager = videoViewManager
        avatarHolder.visibility = GONE
        localParticipantPip.visibility = GONE
        localParticipantPipCameraHolder.removeAllViews()
        localParticipantFullCameraHolder.removeAllViews()
        localParticipantFullCameraHolder.addView(previewView, 0)
    }

    private fun setupAccessibility() {
        accessibilityManager =
            context?.applicationContext?.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        switchCameraButton.contentDescription =
            context.getString(R.string.azure_communication_ui_calling_button_switch_camera_accessibility_label)
    }

    private fun setLocalParticipantVideo(model: LocalParticipantViewModel.VideoModel) {
        videoViewManager.updateLocalVideoRenderer(model.videoStreamID)
        localParticipantFullCameraHolder.removeAllViews()
        localParticipantPipCameraHolder.removeAllViews()

        localParticipantPip.visibility =
            if (model.viewMode == LocalParticipantViewMode.PIP) View.VISIBLE else View.GONE

        val videoHolder = when (model.viewMode) {
            LocalParticipantViewMode.PIP -> localParticipantPipCameraHolder
            LocalParticipantViewMode.FULL_SCREEN -> localParticipantFullCameraHolder
        }

        if (model.shouldDisplayVideo) {
           // addVideoView(model.videoStreamID!!, videoHolder)
        }
    }

    private fun addVideoView(videoStreamID: String, videoHolder: ConstraintLayout) {
        videoViewManager.getLocalVideoRenderer(videoStreamID)?.let { view ->
            view.background = this.context.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.azure_communication_ui_calling_corner_radius_rectangle_4dp
                )
            }
            videoHolder.addView(view, 0)
        }
    }
}
