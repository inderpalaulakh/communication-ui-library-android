// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.error

import com.azure.android.communication.ui.configuration.events.CallCompositeErrorCode

internal class CallCompositeError(
    var callCompositeErrorCode: CallCompositeErrorCode,
    var cause: Throwable,
)
