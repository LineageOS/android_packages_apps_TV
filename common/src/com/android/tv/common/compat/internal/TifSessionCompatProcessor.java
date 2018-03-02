/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package com.android.tv.common.compat.internal;

import android.os.Bundle;
import android.util.Log;
import com.android.tv.common.compat.api.SessionCompatCommands;
import com.android.tv.common.compat.api.SessionCompatEvents;
import com.android.tv.common.compat.api.SessionEventNotifier;
import com.android.tv.common.compat.internal.Events.NotifyDevToast;
import com.android.tv.common.compat.internal.Events.SessionEvent;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Sends {@link SessionCompatEvents} to the TV App via {@link SessionEventNotifier} and receives
 * Commands from TV App forwarding them to {@link SessionCompatCommands}
 */
public final class TifSessionCompatProcessor implements SessionCompatEvents {

    private static final String TAG = "TifSessionCompatProcessor";

    private final SessionEventNotifier mSessionEventNotifier;
    private final SessionCompatCommands mSessionOnCompat;

    public TifSessionCompatProcessor(
            SessionEventNotifier sessionEventNotifier, SessionCompatCommands sessionOnCompat) {
        mSessionEventNotifier = sessionEventNotifier;
        mSessionOnCompat = sessionOnCompat;
    }

    public boolean handleAppPrivateCommand(String action, Bundle data) {

        switch (action) {
            case Constants.ACTION_GET_VERSION:
                Bundle response = new Bundle();
                response.putInt(Constants.EVENT_GET_VERSION, Constants.TIF_COMPAT_VERSION);
                mSessionEventNotifier.notifySessionEvent(Constants.EVENT_GET_VERSION, response);
                return true;
            case Constants.ACTION_COMPAT_ON:
                byte[] bytes = data.getByteArray(Constants.ACTION_COMPAT_ON);
                try {
                    Commands.PrivateCommand privateCommand =
                            Commands.PrivateCommand.parseFrom(bytes);
                    onCompat(privateCommand);
                } catch (InvalidProtocolBufferException e) {
                    Log.w(TAG, "Error parsing compat data", e);
                }

                return true;
            default:
                return false;
        }
    }

    private void onCompat(Commands.PrivateCommand privateCommand) {
        switch (privateCommand.getCommandCase()) {
            case ON_DEV_MESSAGE:
                if (privateCommand.hasOnDevMessage()) {
                    mSessionOnCompat.onDevMessage(privateCommand.getOnDevMessage().getMessage());
                }
                break;
            case COMMAND_NOT_SET:
                Log.w(TAG, "Command not set ");
        }
    }

    @Override
    public void notifyDevToast(String message) {
        NotifyDevToast devMessage = NotifyDevToast.newBuilder().setMessage(message).build();
        SessionEvent sessionEvent = createSessionEvent().setNotifyDevMessage(devMessage).build();
        notifyCompat(sessionEvent);
    }

    private SessionEvent.Builder createSessionEvent() {
        return SessionEvent.newBuilder().setCompatVersion(Constants.TIF_COMPAT_VERSION);
    }

    private void notifyCompat(Events.SessionEvent sessionEvent) {
        Bundle response = new Bundle();
        try {
            byte[] bytes = sessionEvent.toByteArray();
            response.putByteArray(Constants.EVENT_COMPAT_NOTIFY, bytes);
        } catch (Exception e) {
            Log.w(
                    TAG,
                    "Failed to send sessionEvent version "
                            + sessionEvent.getCompatVersion()
                            + " event "
                            + sessionEvent.getEventCase(),
                    e);
            response.putString(Constants.EVENT_COMPAT_NOTIFY_ERROR, e.getMessage());
        }
        mSessionEventNotifier.notifySessionEvent(Constants.EVENT_COMPAT_NOTIFY, response);
    }
}
