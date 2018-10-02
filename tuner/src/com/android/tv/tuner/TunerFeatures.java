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

package com.android.tv.tuner;

import static com.android.tv.common.feature.EngOnlyFeature.ENG_ONLY_FEATURE;
import static com.android.tv.common.feature.FeatureUtils.OFF;
import static com.android.tv.common.feature.FeatureUtils.and;
import static com.android.tv.common.feature.FeatureUtils.aospFeature;
import static com.android.tv.common.feature.FeatureUtils.or;

import com.android.tv.common.config.RemoteConfigFeature;
import com.android.tv.common.feature.CommonFeatures;
import com.android.tv.common.feature.Feature;
import com.android.tv.common.feature.Model;
import com.android.tv.common.feature.PropertyFeature;
import com.android.tv.common.util.LocationUtils;
import java.util.Locale;

/**
 * List of {@link Feature} for Tuner.
 *
 * <p>Remove the {@code Feature} once it is launched.
 */
public class TunerFeatures extends CommonFeatures {

    /** Use network tuner if it is available and there is no other tuner types. */
    public static final Feature NETWORK_TUNER =
            or(
                    ENG_ONLY_FEATURE,
                    aospFeature(
                            context ->
                                    Locale.US
                                            .getCountry()
                                            .equalsIgnoreCase(
                                                    LocationUtils.getCurrentCountry(context))));

    /**
     * USE_SW_CODEC_FOR_SD
     *
     * <p>Prefer software based codec for SD channels.
     */
    public static final Feature USE_SW_CODEC_FOR_SD =
            PropertyFeature.create(
                    "use_sw_codec_for_sd",
                    false
                    );

    /** Enable Dvb parsers and listeners. */
    public static final Feature ENABLE_FILE_DVB = OFF;

    /**
     * Use ExoPlayer V2 only.
     *
     * <p>Turn on with <code>adb shell setprop <em>exoplayer.v2.only</em> <em>true</em></code>
     */
    public static final Feature EXO_PLAYER_V2_ONLY =
            and(ENG_ONLY_FEATURE, PropertyFeature.create("exoplayer.v2.only", false));

    private TunerFeatures() {}
}
