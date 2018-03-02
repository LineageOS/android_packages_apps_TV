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
import android.media.tv.TvView;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import com.android.tv.common.compat.internal.Commands;
import com.android.tv.common.compat.internal.Commands.OnDevMessage;
import com.android.tv.common.compat.internal.Commands.PrivateCommand;
import com.android.tv.common.compat.internal.Events;
import com.android.tv.common.compat.internal.Events.NotifyDevToast;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * TIF Compatibility for {@link TvView}.
 *
 * <p>Extends {@code TvView} in a backwards compatible way.
 */
@RequiresApi(api = VERSION_CODES.LOLLIPOP)
public class TvViewCompat extends TvView {
    private static final String TAG = "TvViewCompat";

    private TvInputCallbackCompat mTvInputCallbackCompat;

    public TvViewCompat(Context context) {
        this(context, null);
    }

    public TvViewCompat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvViewCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCallback(TvInputCallbackCompat callback) {
        mTvInputCallbackCompat = callback;
        super.setCallback(mTvInputCallbackCompat);
    }

    /** @deprecated call {@link #setCallback(TvInputCallbackCompat)} instead. */
    @Deprecated
    @Override
    public void setCallback(TvInputCallback callback) {
        throw new UnsupportedOperationException("Call setCallback(TvInputCallbackCompat) instead");
    }

    public void devMessage(String message) {
        OnDevMessage devMessage = Commands.OnDevMessage.newBuilder().setMessage(message).build();
        Commands.PrivateCommand privateCommand =
                createPrivateCommandCommand().setOnDevMessage(devMessage).build();
        sendCompatCommand(privateCommand);
    }

    @NonNull
    public PrivateCommand.Builder createPrivateCommandCommand() {
        PrivateCommand.Builder builder =
                Commands.PrivateCommand.newBuilder().setCompatVersion(Constants.TIF_COMPAT_VERSION);
        return builder;
    }

    private final void sendCompatCommand(Commands.PrivateCommand privateCommand) {
        try {
            Bundle data = new Bundle();
            data.putByteArray(Constants.ACTION_COMPAT_ON, privateCommand.toByteArray());
            sendAppPrivateCommand(Constants.ACTION_COMPAT_ON, data);
        } catch (Exception e) {
            Log.w(TAG, "Error sending compat action " + privateCommand.getCommandCase(), e);
        }
    }

    /**
     * TIF Compatibility for {@link TvInputCallback}.
     *
     * <p>Extends {@code TvInputCallback} in a backwards compatible way.
     */
    public static class TvInputCallbackCompat extends TvInputCallback {
        private final ArrayMap<String, Integer> inputCompatVersionMap = new ArrayMap<>();

        @Override
        public void onEvent(String inputId, String eventType, Bundle eventArgs) {
            switch (eventType) {
                case Constants.EVENT_GET_VERSION:
                    int version = eventArgs.getInt(Constants.EVENT_GET_VERSION, 0);
                    inputCompatVersionMap.put(inputId, version);
                    break;
                case Constants.EVENT_COMPAT_NOTIFY:
                    byte[] protoBytes = eventArgs.getByteArray(Constants.EVENT_COMPAT_NOTIFY);
                    if (protoBytes != null && protoBytes.length > 0) {
                        try {
                            Events.SessionEvent sessionEvent =
                                    Events.SessionEvent.parseFrom(protoBytes);
                            handle(inputId, sessionEvent);

                        } catch (InvalidProtocolBufferException e) {
                            Log.w(TAG, "Error parsing in compat notify for  " + inputId);
                        }

                    } else {
                        String errorMessage =
                                eventArgs.getString(Constants.EVENT_COMPAT_NOTIFY_ERROR);
                        Log.w(TAG, "Error sent in compat notify  " + errorMessage);
                    }
                    break;
                default:
                    super.onEvent(inputId, eventType, eventArgs);
            }
        }

        public int getTifCompatVersionForInput(String inputId) {
            return inputCompatVersionMap.containsKey(inputId)
                    ? inputCompatVersionMap.get(inputId)
                    : 0;
        }

        public void onDevToast(String inputId, String message) {}

        private void handle(String inputId, Events.SessionEvent sessionEvent) {
            switch (sessionEvent.getEventCase()) {
                case NOTIFY_DEV_MESSAGE:
                    handle(inputId, sessionEvent.getNotifyDevMessage());
                    break;
                case EVENT_NOT_SET:
                    Log.w(TAG, "Error event not set compat notify  ");
            }
        }

        private void handle(String inputId, NotifyDevToast devToast) {
            onDevToast(inputId, devToast.getMessage());
        }
    }
}
