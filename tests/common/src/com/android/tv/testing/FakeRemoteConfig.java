/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.android.tv.testing;

import android.text.TextUtils;
import com.android.tv.common.config.api.RemoteConfig;
import java.util.HashMap;
import java.util.Map;

/** Fake {@link RemoteConfig} suitable for testing. */
public class FakeRemoteConfig implements RemoteConfig {
    public final Map<String, String> values = new HashMap();

    @Override
    public void fetch(OnRemoteConfigUpdatedListener listener) {}

    @Override
    public String getString(String key) {
        return values.get(key);
    }

    @Override
    public boolean getBoolean(String key) {
        String value = values.get(key);
        return TextUtils.isEmpty(value) ? false : Boolean.valueOf(key);
    }

    @Override
    public long getLong(String key) {
        String value = values.get(key);
        return TextUtils.isEmpty(value) ? 0 : Long.valueOf(key);
    }
}
