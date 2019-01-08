package com.android.tv.tuner.tvinput.factory;

import android.content.Context;
import android.media.tv.TvInputService.Session;
import com.android.tv.tuner.source.TsDataSourceManagerFactory;
import com.android.tv.tuner.tvinput.TunerSession;
import com.android.tv.tuner.tvinput.TunerSessionExoV2;
import com.android.tv.tuner.tvinput.datamanager.ChannelDataManager;
import com.android.tv.common.flags.ConcurrentDvrPlaybackFlags;
import com.android.tv.common.flags.Exoplayer2Flags;

/** Creates a {@link TunerSessionFactory}. */
public class TunerSessionFactoryImpl implements TunerSessionFactory {
    private final Exoplayer2Flags mExoplayer2Flags;
    private final ConcurrentDvrPlaybackFlags mConcurrentDvrPlaybackFlags;
    private final TsDataSourceManagerFactory mTsDataSourceManagerFactory;

    public TunerSessionFactoryImpl(
            Exoplayer2Flags exoplayer2Flags,
            ConcurrentDvrPlaybackFlags concurrentDvrPlaybackFlags,
            TsDataSourceManagerFactory tsDataSourceManagerFactory) {
        mExoplayer2Flags = exoplayer2Flags;
        mConcurrentDvrPlaybackFlags = concurrentDvrPlaybackFlags;
        mTsDataSourceManagerFactory = tsDataSourceManagerFactory;
    }

    @Override
    public Session create(
            Context context,
            ChannelDataManager channelDataManager,
            SessionReleasedCallback releasedCallback) {
        return mExoplayer2Flags.enabled()
                ? new TunerSessionExoV2(
                        context,
                        channelDataManager,
                        releasedCallback,
                        mConcurrentDvrPlaybackFlags,
                        mTsDataSourceManagerFactory)
                : new TunerSession(
                        context,
                        channelDataManager,
                        releasedCallback,
                        mConcurrentDvrPlaybackFlags,
                        mTsDataSourceManagerFactory);
    }
}
