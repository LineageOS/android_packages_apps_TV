package com.android.tv.common.feature;

import android.content.Context;
import com.google.common.base.Function;

/** Feature from a Flag */
public class FlagFeature<T> implements Feature {

    private final Function<Context, T> mToFlag;
    private final Function<T, Boolean> mToBoolean;

    public static <T> FlagFeature<T> from(
            Function<Context, T> toFlag, Function<T, Boolean> toBoolean) {
        return new FlagFeature<T>(toFlag, toBoolean);
    }

    private FlagFeature(Function<Context, T> toFlag, Function<T, Boolean> toBoolean) {
        mToFlag = toFlag;
        mToBoolean = toBoolean;
    }

    @Override
    public boolean isEnabled(Context context) {
        return mToBoolean.apply(mToFlag.apply(context));
    }

    @Override
    public String toString() {
        return mToBoolean.toString();
    }
}
