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

package com.android.tv.tuner;

import android.content.Context;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.util.Pair;
import com.android.tv.common.customization.CustomizationManager;
import com.android.tv.common.feature.Model;
import com.android.tv.tuner.api.ITunerHal;


/** TunerHal factory that creates all built in tuner types. */
public final class BuiltInTunerHalFactory {
    private static final String TAG = "BuiltInTunerHalFactory";
    private static final boolean DEBUG = false;

    private static Integer sBuiltInTunerType;

    @ITunerHal.BuiltInTunerType
    private static int getBuiltInTunerType(Context context) {
        if (sBuiltInTunerType == null) {
            sBuiltInTunerType = 0;
            if (CustomizationManager.hasLinuxDvbBuiltInTuner(context)
                    && DvbTunerHal.getNumberOfDevices(context) > 0) {
                sBuiltInTunerType = ITunerHal.BUILT_IN_TUNER_TYPE_LINUX_DVB;
            }
        }
        return sBuiltInTunerType;
    }

    /**
     * Creates a TunerHal instance.
     *
     * @param context context for creating the TunerHal instance
     * @return the TunerHal instance
     */
    @WorkerThread
    public static synchronized ITunerHal createInstance(Context context) {
        ITunerHal tunerHal = null;
        if (DvbTunerHal.getNumberOfDevices(context) > 0) {
            if (DEBUG) Log.d(TAG, "Use DvbTunerHal");
            tunerHal = new DvbTunerHal(context);
        }
        return tunerHal != null && tunerHal.openFirstAvailable() ? tunerHal : null;
    }

    /**
     * Returns if tuner input service would use built-in tuners instead of USB tuners or network
     * tuners.
     */
    public static boolean useBuiltInTuner(Context context) {
        return getBuiltInTunerType(context) != 0;
    }

    /** Gets the number of tuner devices currently present. */
    @WorkerThread
    public static Pair<Integer, Integer> getTunerTypeAndCount(Context context) {
        if (useBuiltInTuner(context)) {
            if (getBuiltInTunerType(context) == ITunerHal.BUILT_IN_TUNER_TYPE_LINUX_DVB) {
                return new Pair<>(
                        ITunerHal.TUNER_TYPE_BUILT_IN, DvbTunerHal.getNumberOfDevices(context));
            }
        } else {
            int usbTunerCount = DvbTunerHal.getNumberOfDevices(context);
            if (usbTunerCount > 0) {
                return new Pair<>(ITunerHal.TUNER_TYPE_USB, usbTunerCount);
            }
        }
        return new Pair<>(null, 0);
    }
}
