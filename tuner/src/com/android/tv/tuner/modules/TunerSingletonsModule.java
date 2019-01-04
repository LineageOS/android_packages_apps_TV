package com.android.tv.tuner.modules;

import com.android.tv.tuner.singletons.TunerSingletons;
import com.android.tv.tuner.tvinput.factory.TunerSessionFactory;
import dagger.Module;
import dagger.Provides;

/**
 * Provides bindings for items provided by {@link TunerSingletons}.
 *
 * <p>Use this module to inject items directly instead of using {@code TunerSingletons}.
 */
@Module
public class TunerSingletonsModule {
    private final TunerSingletons mTunerSingletons;

    public TunerSingletonsModule(TunerSingletons tunerSingletons) {
        this.mTunerSingletons = tunerSingletons;
    }

    @Provides
    public TunerSessionFactory providesTunerSessionFactory() {
        return mTunerSingletons.getTunerSessionFactory();
    }
}
