package com.android.tv.testing.robo;

import android.media.tv.TvContract;
import com.android.tv.testing.FakeTvProvider;
import com.android.tv.testing.TestSingletonApp;
import com.android.tv.testing.testdata.TestData;
import java.util.concurrent.TimeUnit;
import org.robolectric.Robolectric;

/** Static utilities for using {@link TestSingletonApp} in roboletric tests. */
public final class RobotTestAppHelper {

    public static void loadTestData(TestSingletonApp app, TestData testData) {
        ContentProviders.register(FakeTvProvider.class, TvContract.AUTHORITY);
        app.loadTestData(testData, TimeUnit.DAYS.toMillis(1));
        Robolectric.flushBackgroundThreadScheduler();
        Robolectric.flushForegroundThreadScheduler();
    }

    private RobotTestAppHelper() {}
}
