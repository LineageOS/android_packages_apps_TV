package com.android.tv.modules;

import com.android.tv.TvSingletons;
import com.android.tv.data.ChannelDataManager;
import com.android.tv.data.ProgramDataManager;
import com.android.tv.util.SetupUtils;
import com.android.tv.util.TvInputManagerHelper;
import dagger.Module;
import dagger.Provides;

/**
 * Provides bindings for items provided by {@link TvSingletons}.
 *
 * <p>Use this module to inject items directly instead of using {@code TvSingletons}.
 */
@Module
public class TvSingletonsModule {
    private final TvSingletons mTvSingletons;

    public TvSingletonsModule(TvSingletons mTvSingletons) {
        this.mTvSingletons = mTvSingletons;
    }

    @Provides
    ChannelDataManager providesChannelDataManager() {
        return mTvSingletons.getChannelDataManager();
    }

    @Provides
    ProgramDataManager providesProgramDataManager() {
        return mTvSingletons.getProgramDataManager();
    }

    @Provides
    TvInputManagerHelper providesTvInputManagerHelper() {
        return mTvSingletons.getTvInputManagerHelper();
    }

    @Provides
    SetupUtils providesTvSetupUtils() {
        return mTvSingletons.getSetupUtils();
    }
}
