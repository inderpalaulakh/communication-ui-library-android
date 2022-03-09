// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.participant.remote;

import com.azure.android.communication.common.CommunicationIdentifier;

public interface RemoteParticipantJoinedHandler {
    void Joined(CommunicationIdentifier communicationIdentifier, RemoteParticipantManager remoteParticipantManager);
    void isMuted(CommunicationIdentifier communicationIdentifier)
    void removed(CommunicationIdentifier communicationIdentifier)
}
