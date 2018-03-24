package com.android.tv.features;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.android.tv.common.feature.Feature;
import com.google.android.tv.partner.support.PartnerCustomizations;

/** Features backed by {@link PartnerCustomizations}. */
@SuppressWarnings("AndroidApiChecker") // TODO(b/32513850) remove when error prone is updated
public final class PartnerFeatures {

    public static final Feature TVPROVIDER_ALLOWS_SYSTEM_INSERTS_TO_PROGRAM_TABLE =
            new PartnerFeature(
                    PartnerCustomizations.TVPROVIDER_ALLOWS_SYSTEM_INSERTS_TO_PROGRAM_TABLE);

    public static final Feature TURN_OFF_EMBEDDED_TUNER =
            new PartnerFeature(PartnerCustomizations.TURN_OFF_EMBEDDED_TUNER);

    public static final Feature TVPROVIDER_ALLOWS_COLUMN_CREATION =
            new PartnerFeature(PartnerCustomizations.TVPROVIDER_ALLOWS_COLUMN_CREATION);

    private static class PartnerFeature implements Feature {

        private final String property;

        public PartnerFeature(String property) {
            this.property = property;
        }

        @Override
        public boolean isEnabled(Context context) {
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                PartnerCustomizations partnerCustomizations = new PartnerCustomizations(context);
                return partnerCustomizations.getBooleanResource(context, property).orElse(false);
            }
            return false;
        }
    }

    private PartnerFeatures() {}
}
