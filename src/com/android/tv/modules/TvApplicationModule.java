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
package com.android.tv.modules;

import com.android.tv.MainActivity;
import com.android.tv.TvApplication;
import com.android.tv.common.dagger.ApplicationModule;
import com.android.tv.onboarding.OnboardingActivity;
import dagger.Module;

/** Dagger module for {@link TvApplication}. */
@Module(
        includes = {
            ApplicationModule.class,
            TvSingletonsModule.class,
            MainActivity.Module.class,
            OnboardingActivity.Module.class
        })
public class TvApplicationModule {}
