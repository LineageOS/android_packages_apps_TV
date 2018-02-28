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

package com.android.tv.common.compat;

import android.content.Context;
import android.media.tv.TvInputService.Session;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.android.tv.common.compat.internal.Commands;
import com.android.tv.common.compat.internal.Events;
import com.android.tv.common.compat.internal.Events.NotifyDevToast;
import com.android.tv.common.compat.internal.Events.SessionEvent;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * TIF Compatibility for {@link TisSessionCompat}.
 *
 * <p>Extends {@code TisSessionCompat} in a backwards compatible way.
 */
@RequiresApi(api = VERSION_CODES.LOLLIPOP)
public abstract class TisSessionCompat extends Session {
    private static final String TAG = "TisSessionCompat";

    public TisSessionCompat(Context context) {
        super(context);
    }

    @Override
    public void onAppPrivateCommand(String action, Bundle data) {

        switch (action) {
            case Constants.ACTION_GET_VERSION:
                Bundle response = new Bundle();
                response.putInt(Constants.EVENT_GET_VERSION, getTifCompatVersion());
                notifySessionEvent(Constants.EVENT_GET_VERSION, response);
                break;
            case Constants.ACTION_COMPAT_ON:
                byte[] bytes = data.getByteArray(Constants.ACTION_COMPAT_ON);
                try {
                    Commands.PrivateCommand privateCommand =
                            Commands.PrivateCommand.parseFrom(bytes);
                    onCompat(privateCommand);
                } catch (InvalidProtocolBufferException e) {
                    Log.w(TAG, "Error parsing compat data", e);
                }

                break;
            default:
                super.onAppPrivateCommand(action, data);
        }
    }

    private void onCompat(Commands.PrivateCommand privateCommand) {
        switch (privateCommand.getCommandCase()) {
            case ON_DEV_MESSAGE:
                if (privateCommand.hasOnDevMessage()) {
                    onDevMessage(privateCommand.getOnDevMessage().getMessage());
                }
                break;
            case COMMAND_NOT_SET:
                Log.w(TAG, "Command not set ");
        }
    }

    protected void onDevMessage(String message) {}

    public final int getTifCompatVersion() {
        return Constants.TIF_COMPAT_VERSION;
    }

    public void notifyDevToast(String message) {
        NotifyDevToast devMessage = NotifyDevToast.newBuilder().setMessage(message).build();
        SessionEvent sessionEvent = createSessionEvent().setNotifyDevMessage(devMessage).build();
        notifyCompat(sessionEvent);
    }

    private SessionEvent.Builder createSessionEvent() {
        return SessionEvent.newBuilder().setCompatVersion(getTifCompatVersion());
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
        notifySessionEvent(Constants.EVENT_COMPAT_NOTIFY, response);
    }
}
