package com.android.tv.tuner.tvinput.factory;

import android.content.Context;
import android.media.tv.TvInputService.Session;
import com.android.tv.tuner.features.TunerFeatures;
import com.android.tv.tuner.tvinput.TunerSession;
import com.android.tv.tuner.tvinput.TunerSessionExoV2;
import com.android.tv.tuner.tvinput.datamanager.ChannelDataManager;

/** Creates a {@link TunerSessionFactory}. */
public class TunerSessionFactoryImpl implements TunerSessionFactory {

    @Override
    public Session create(
            Context context,
            ChannelDataManager channelDataManager,
            SessionReleasedCallback releasedCallback) {
        return TunerFeatures.EXO_PLAYER_V2_ONLY.isEnabled(context)
                ? new TunerSessionExoV2(context, channelDataManager, releasedCallback)
                : new TunerSession(context, channelDataManager, releasedCallback);
    }
}
