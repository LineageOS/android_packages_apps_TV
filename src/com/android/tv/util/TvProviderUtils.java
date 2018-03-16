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

package com.android.tv.util;

import static java.lang.Boolean.TRUE;

import android.content.Context;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.android.tv.TvFeatures;
import com.android.tv.data.BaseProgram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** A utility class related to TvProvider. */
public final class TvProviderUtils {
    private static final String TAG = "TvProviderUtils";

    public static final String EXTRA_PROGRAM_COLUMN = BaseProgram.COLUMN_SERIES_ID;

    private static Boolean sProgramHasSeriesIdColumn;
    private static Boolean sRecordedProgramHasSeriesIdColumn;

    /**
     * Checks whether a table contains a series ID column.
     *
     * This method is different from {@link #getProgramHasSeriesIdColumn()} and
     * {@link #getRecordedProgramHasSeriesIdColumn()} because it may access to database, so it
     * should be run in worker thread.
     *
     * @return {@code true} if the corresponding table contains a series ID column; {@code false}
     * otherwise.
     */
    @WorkerThread
    public static synchronized boolean checkSeriesIdColumn(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return false;
        }
        if (Utils.isProgramsUri(uri)) {
            return checkProgramTable(context, uri);
        }
        if (Utils.isRecordedProgramsUri(uri)) {
            return checkRecordedProgramTable(context, uri);
        }
        return false;
    }

    @WorkerThread
    private static synchronized boolean checkProgramTable(Context context, Uri uri) {
        if (sProgramHasSeriesIdColumn == null) {
            sProgramHasSeriesIdColumn = getExistingColumns(context, uri)
                    .contains(EXTRA_PROGRAM_COLUMN);
        }
        return sProgramHasSeriesIdColumn;
    }

    @WorkerThread
    private static synchronized boolean checkRecordedProgramTable(Context context, Uri uri) {
        if (sRecordedProgramHasSeriesIdColumn == null) {
            sRecordedProgramHasSeriesIdColumn = getExistingColumns(context, uri)
                    .contains(EXTRA_PROGRAM_COLUMN);
        }
        return sRecordedProgramHasSeriesIdColumn;
    }

    public static synchronized boolean getProgramHasSeriesIdColumn() {
        return TRUE.equals(sProgramHasSeriesIdColumn);
    }

    public static synchronized boolean getRecordedProgramHasSeriesIdColumn() {
        return TRUE.equals(sRecordedProgramHasSeriesIdColumn);
    }

    public static String[] addExtraColumnsToProjection(String[] projection) {
        List<String> projectionList = new ArrayList<>(Arrays.asList(projection));
        if (!projectionList.contains(EXTRA_PROGRAM_COLUMN)) {
            projectionList.add(EXTRA_PROGRAM_COLUMN);
        }
        projection = projectionList.toArray(projection);
        return projection;
    }

    /**
     * Gets column names of a table
     *
     * @param uri the corresponding URI of the table
     */
    private static Set<String> getExistingColumns(Context context, Uri uri) {
        Bundle result =
                context
                        .getContentResolver()
                        .call(uri, TvContract.METHOD_GET_COLUMNS, uri.toString(), null);
        if (result != null) {
            String[] columns = result.getStringArray(TvContract.EXTRA_EXISTING_COLUMN_NAMES);
            if (columns != null) {
                return new HashSet<>(Arrays.asList(columns));
            }
        }
        Log.e(TAG, "Query existing column names from " + uri + " returned null");
        return Collections.emptySet();
    }

    private TvProviderUtils() {}
}
