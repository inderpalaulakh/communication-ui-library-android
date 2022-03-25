// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.presentation.fragment.common.permissions

import com.azure.android.communication.ui.configuration.LocalizationProvider
import com.azure.android.communication.ui.redux.action.Action
import com.azure.android.communication.ui.redux.action.PermissionAction
import com.azure.android.communication.ui.redux.state.PermissionState
import com.azure.android.communication.ui.redux.state.PermissionStatus
import kotlinx.coroutines.flow.MutableStateFlow

internal class PermissionViewModel(
    private val dispatch: (Action) -> Unit,
    private val localizationProvider: LocalizationProvider,
) {

    val displayPermissionsDialogMutableStateFlow = MutableStateFlow(false)

    fun init(permissionState: PermissionState) {

    }

    fun showPermissionsDialog() {
        displayPermissionsDialogMutableStateFlow.value = true
    }

    fun update(permissionState: PermissionState) {
        if(permissionState.audioPermissionState == PermissionStatus.NOT_ASKED) {
            displayPermissionsDialogMutableStateFlow.value =true

        }
        if(permissionState.audioPermissionState == PermissionStatus.DENIED) {
            displayPermissionsDialogMutableStateFlow.value =false

        }
    }

    fun getLocalizationProvider(): LocalizationProvider {
        return localizationProvider
    }

    fun requestAudioPermissions() {
        dispatch(PermissionAction.AudioPermissionRequested())
    }
}
