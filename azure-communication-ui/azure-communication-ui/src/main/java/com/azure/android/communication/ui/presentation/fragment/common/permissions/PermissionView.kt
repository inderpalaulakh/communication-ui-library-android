// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.presentation.fragment.common.permissions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.azure.android.communication.ui.R
import com.azure.android.communication.ui.redux.state.PermissionStatus
import com.microsoft.fluentui.drawer.DrawerDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


internal class PermissionView(
    private val viewModel: PermissionViewModel,
    context: Context,
) : ConstraintLayout(context) {

    private lateinit var drawer: DrawerDialog
    private var allowPermissionsButton: Button
    private var settingsButton: Button

    private var allowPermissions: ConstraintLayout
    private var settingsChange: ConstraintLayout


    init {
        inflate(context, R.layout.azure_communication_ui_permissions_view, this)
        allowPermissionsButton = findViewById(R.id.azure_communication_ui_allow_permissions_allow_button)
        settingsButton= findViewById(R.id.azure_communication_ui_allow_permissions_button)

        allowPermissions = findViewById(R.id.azure_communication_ui_request_allow_permissions_layout)
        settingsChange = findViewById(R.id.azure_communication_ui_request_settings_change)
        allowPermissions.visibility = GONE
        settingsChange.visibility = GONE
        allowPermissionsButton.background = ContextCompat.getDrawable(
            context,
            R.drawable.azure_communication_ui_corner_radius_rectangle_4dp_primary_background
        )
        settingsButton.background = ContextCompat.getDrawable(
            context,
            R.drawable.azure_communication_ui_corner_radius_rectangle_4dp_primary_background
        )
        allowPermissionsButton.setOnClickListener {
            viewModel.requestAudioPermissions()
        }
        settingsButton.setOnClickListener {

            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri = Uri.fromParts("package", context!!.packageName , null)
            intent.data = uri
            context.startActivity(intent)


        }
    }

    fun start(viewLifecycleOwner: LifecycleOwner) {
        initializeAudioDeviceDrawer()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.audioPermissionMutableStateFlow.collect {
                if (it == PermissionStatus.NOT_ASKED) {
                    allowPermissions.visibility = VISIBLE
                    settingsChange.visibility = GONE

                }

                if (it == PermissionStatus.DENIED) {
                    allowPermissions.visibility = GONE
                    settingsChange.visibility = VISIBLE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.displayPermissionsDialogMutableStateFlow.collect {

                if (viewModel.audioPermissionMutableStateFlow.value == PermissionStatus.NOT_ASKED) {
                    allowPermissions.visibility = VISIBLE
                    settingsChange.visibility = GONE

                }

                if (viewModel.audioPermissionMutableStateFlow.value == PermissionStatus.DENIED) {
                    allowPermissions.visibility = GONE
                    settingsChange.visibility = VISIBLE
                }

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
            drawer.hide()
        }
    }
}
