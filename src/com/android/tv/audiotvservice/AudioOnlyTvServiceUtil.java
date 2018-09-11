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

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.MainThread;
import android.util.Log;

/** Utility methods to start and stop audio only TV Player. */
public class AudioOnlyTvServiceUtil {
    private static final String TAG = "AudioOnlyTvServiceUtil";

    @MainThread
    public static void startAudioOnlyInput(Context context, String tvInputId) {
        Log.i(TAG, "startAudioOnlyInput");
        Intent intent = new Intent(context, AudioOnlyTvService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    @MainThread
    public static void stopAudioOnlyInput() {
        Log.i(TAG, "stopForegroundService");
    }
}
