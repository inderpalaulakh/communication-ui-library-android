// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.presentation.fragment.common.permissions

import android.content.Context
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.azure.android.communication.ui.R
import com.microsoft.fluentui.drawer.DrawerDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class PermissionView(
    private val viewModel: PermissionViewModel,
    context: Context,
) : ConstraintLayout(context) {

    private lateinit var drawer: DrawerDialog
    private var allowPermissionsButton: Button


    init {
        inflate(context, R.layout.azure_communication_ui_permissions_view, this)
        allowPermissionsButton = findViewById(R.id.azure_communication_ui_allow_permissions_button)
        allowPermissionsButton.background = ContextCompat.getDrawable(
            context,
            R.drawable.azure_communication_ui_corner_radius_rectangle_4dp_primary_background
        )
        allowPermissionsButton.setOnClickListener {
            viewModel.requestAudioPermissions()
        }
    }

    fun start(viewLifecycleOwner: LifecycleOwner) {
        initializeAudioDeviceDrawer()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.displayPermissionsDialogMutableStateFlow.collect {
                if (it) {
                    drawer.show()
                } else {
                    drawer.hide()
                }
            }
        }
    }

    private fun initializeAudioDeviceDrawer() {
        drawer = DrawerDialog(context, DrawerDialog.BehaviorType.BOTTOM)
        drawer.setContentView(this)
        drawer.setOnDismissListener {
            viewModel.displayPermissionsDialogMutableStateFlow.value = false
        }
    }
}
