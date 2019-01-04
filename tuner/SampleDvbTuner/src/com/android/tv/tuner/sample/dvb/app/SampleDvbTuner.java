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

package com.android.tv.tuner.sample.dvb.app;

import android.content.ComponentName;
import android.media.tv.TvContract;
import com.android.tv.common.BaseApplication;
import com.android.tv.common.flags.impl.DefaultCloudEpgFlags;
import com.android.tv.common.flags.impl.DefaultConcurrentDvrPlaybackFlags;
import com.android.tv.common.flags.impl.DefaultExoplayer2Flags;
import com.android.tv.common.singletons.HasSingletons;
import com.android.tv.tuner.modules.TunerSingletonsModule;
import com.android.tv.tuner.sample.dvb.singletons.SampleDvbSingletons;
import com.android.tv.tuner.sample.dvb.tvinput.SampleDvbTunerTvInputService;
import com.android.tv.tuner.tvinput.factory.TunerSessionFactory;
import com.android.tv.tuner.tvinput.factory.TunerSessionFactoryImpl;
import dagger.android.AndroidInjector;

/** The top level application for Sample DVB Tuner. */
public class SampleDvbTuner extends BaseApplication
        implements SampleDvbSingletons, HasSingletons<SampleDvbSingletons> {

    private String mEmbeddedInputId;
    private final DefaultCloudEpgFlags mCloudEpgFlags = new DefaultCloudEpgFlags();
    private final DefaultConcurrentDvrPlaybackFlags mConcurrentDvrPlaybackFlags =
            new DefaultConcurrentDvrPlaybackFlags();
    private final DefaultExoplayer2Flags mExoplayer2Flags = new DefaultExoplayer2Flags();
    private final TunerSessionFactoryImpl mTunerSessionFactory =
            new TunerSessionFactoryImpl(mExoplayer2Flags, mConcurrentDvrPlaybackFlags);

    @Override
    protected AndroidInjector<SampleDvbTuner> applicationInjector() {
        return DaggerSampleDvbTunerComponent.builder()
                .tunerSingletonsModule(new TunerSingletonsModule(this))
                .build();
    }

    @Override
    public synchronized String getEmbeddedTunerInputId() {
        if (mEmbeddedInputId == null) {
            mEmbeddedInputId =
                    TvContract.buildInputId(
                            new ComponentName(this, SampleDvbTunerTvInputService.class));
        }
        return mEmbeddedInputId;
    }

    @Override
    public DefaultCloudEpgFlags getCloudEpgFlags() {
        return mCloudEpgFlags;
    }

    @Override
    public BuildType getBuildType() {
        return BuildType.ENG;
    }

    @Override
    public DefaultConcurrentDvrPlaybackFlags getConcurrentDvrPlaybackFlags() {
        return mConcurrentDvrPlaybackFlags;
    }

    @Override
    public SampleDvbSingletons singletons() {
        return this;
    }

    @Override
    public TunerSessionFactory getTunerSessionFactory() {
        return mTunerSessionFactory;
    }
}
