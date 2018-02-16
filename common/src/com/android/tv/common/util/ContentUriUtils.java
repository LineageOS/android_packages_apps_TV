package com.android.tv.common.util;

import android.content.ContentUris;
import android.net.Uri;
import android.util.Log;

/** Static utils for{@link android.content.ContentUris}. */
public class ContentUriUtils {
    private static final String TAG = "ContentUriUtils";

    /**
     * Converts the last path segment to a long.
     *
     * <p>This supports a common convention for content URIs where an ID is stored in the last
     * segment.
     *
     * @return the long conversion of the last segment or -1 if the path is empty or there is any
     *     error
     * @see ContentUris#parseId(Uri)
     */
    public static long safeParseId(Uri uri) {
        try {
            return ContentUris.parseId(uri);
        } catch (Exception e) {
            Log.d(TAG, "Error parsing " + uri, e);
            return -1;
        }
    }
}
