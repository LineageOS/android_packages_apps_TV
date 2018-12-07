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
package com.android.tv.common.flags;

/** Flags allowing concurrent DVR playback */
public interface ConcurrentDvrPlaybackFlags {

    /**
     * Whether or not this feature is compiled into this build.
     *
     * <p>If the macro which generated this code does not have condtional_compilation_enabled as
     * true, then this always returns true.
     *
     * <p>If the macro which generated this code does have conditional_compilation_enabled as true,
     * this will return true or false depending on the value of the corresponding
     * config_feature_flag controlling this feature. See go/phenotype-compile-time-features.
     */
    boolean compiled();

    /** Enable playback of DVR playback druing recording */
    boolean enabled();

    /** Enable tuner using recording data for playback in onTune */
    boolean onTuneUsesRecording();
}
