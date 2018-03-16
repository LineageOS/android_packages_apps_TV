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
import com.android.tv.common.compat.api.SessionCompatCommands;
import com.android.tv.common.compat.api.SessionCompatEvents;
import com.android.tv.common.compat.api.SessionEventNotifier;
import com.android.tv.common.compat.internal.TifSessionCompatProcessor;

/**
 * TIF Compatibility for {@link TisSessionCompat}.
 *
 * <p>Extends {@code TisSessionCompat} in a backwards compatible way.
 */
@RequiresApi(api = VERSION_CODES.LOLLIPOP)
public abstract class TisSessionCompat extends Session
        implements SessionEventNotifier, SessionCompatCommands, SessionCompatEvents {
    private static final String TAG = "TisSessionCompat";

    private final TifSessionCompatProcessor mTifCompatProcessor;

    public TisSessionCompat(Context context) {
        super(context);
        mTifCompatProcessor = new TifSessionCompatProcessor(this, this);
    }

    @Override
    public void onAppPrivateCommand(String action, Bundle data) {
        if (!mTifCompatProcessor.handleAppPrivateCommand(action, data)) {
            super.onAppPrivateCommand(action, data);
        }
    }

    @Override
    public void onDevMessage(String message) {}

    @Override
    public void notifyDevToast(String message) {
        mTifCompatProcessor.notifyDevToast(message);
    }
}
