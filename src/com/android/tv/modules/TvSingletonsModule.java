package com.android.tv.modules;

import com.android.tv.TvSingletons;
import com.android.tv.data.ChannelDataManager;
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
}
