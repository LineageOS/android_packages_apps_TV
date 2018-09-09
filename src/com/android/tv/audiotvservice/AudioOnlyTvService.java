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
 * limitations under the License.
 */
package com.android.tv.audiotvservice;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Foreground service for audio-only TV inputs.
 */
public class AudioOnlyTvService extends Service {
    // TODO(b/110969180): implement this service.
    private static final String TAG = "AudioOnlyTvService";
    private static final int NOTIFICATION_ID = 1;

    private static AudioOnlyTvService sInstance;

    @MainThread
    public static void startForegroundService(Context context) {
        Log.i(TAG,
                "startForegroundService, sInstance " + (sInstance == null ? "null" : "not null"));
        if (sInstance == null) {
            Intent intent = new Intent(context, AudioOnlyTvService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        } else {
            sInstance.startForeground(NOTIFICATION_ID, new Notification());
        }
    }
    @MainThread
    public static void stopForegroundService() {
        Log.i(TAG, "stopForegroundService");
        sInstance.stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand. flags = " + flags + ", startId = " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
    }
}
