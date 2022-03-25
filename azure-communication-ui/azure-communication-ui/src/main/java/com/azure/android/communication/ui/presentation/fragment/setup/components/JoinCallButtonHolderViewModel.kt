// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.presentation.fragment.setup.components

import com.azure.android.communication.ui.configuration.LocalizationProvider
import com.azure.android.communication.ui.presentation.fragment.common.permissions.PermissionViewModel
import com.azure.android.communication.ui.redux.action.Action
import com.azure.android.communication.ui.redux.action.CallingAction
import com.azure.android.communication.ui.redux.action.PermissionAction
import com.azure.android.communication.ui.redux.state.CallingState
import com.azure.android.communication.ui.redux.state.PermissionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class JoinCallButtonHolderViewModel(
    private val dispatch: (Action) -> Unit,
    private val localizationProvider: LocalizationProvider,
    private val permissionViewModel: PermissionViewModel
) {

    private lateinit var joinCallButtonEnabledFlow: MutableStateFlow<Boolean>
    private var disableJoinCallButtonFlow = MutableStateFlow(false)

    private var hasAudioPermissions = false
    private var joinCallInProgress = false

    fun getJoinCallButtonEnabledFlow(): StateFlow<Boolean> = joinCallButtonEnabledFlow

    fun getDisableJoinCallButtonFlow(): StateFlow<Boolean> = disableJoinCallButtonFlow

    fun launchCallScreen() {
        if (!hasAudioPermissions) {
            joinCallInProgress = true
            dispatch(PermissionAction.AudioPermissionRequested())
        } else {
            dispatch(CallingAction.CallStartRequested())
            disableJoinCallButtonFlow.value = true
        }
    }

    fun init(audioPermissionState: PermissionStatus) {
        joinCallButtonEnabledFlow = MutableStateFlow(true)
        disableJoinCallButtonFlow.value = false
    }

    fun update(audioPermissionState: PermissionStatus, callingState: CallingState) {
        hasAudioPermissions = audioPermissionState == PermissionStatus.GRANTED

        if(joinCallInProgress) {
            joinCallInProgress = false

            if (!hasAudioPermissions) {
                permissionViewModel.showPermissionsDialog()
            } else {
                dispatch(CallingAction.CallStartRequested())
                disableJoinCallButtonFlow.value = true
            }
        }

        joinCallButtonEnabledFlow.value = true
        disableJoinCallButtonFlow.value = false
    }

    fun getLocalizationProvider(): LocalizationProvider {
        return localizationProvider
    }
}
