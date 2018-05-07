/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.android.tv.dvr.provider;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import com.android.tv.common.concurrent.NamedThreadFactory;
import com.android.tv.dvr.data.ScheduledRecording;
import com.android.tv.dvr.data.SeriesRecording;
import com.android.tv.dvr.provider.DvrContract.Schedules;
import com.android.tv.dvr.provider.DvrContract.SeriesRecordings;
import com.android.tv.util.MainThreadExecutor;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/** {@link AsyncTask} that defaults to executing on its own single threaded Executor Service. */
public abstract class AsyncDvrDbTask<ParamsT, ResultT> {
    private static final NamedThreadFactory THREAD_FACTORY =
        new NamedThreadFactory(AsyncDvrDbTask.class.getSimpleName());
    private static final ListeningExecutorService DB_EXECUTOR =
        MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor(THREAD_FACTORY));

    private static DvrDatabaseHelper sDbHelper;
    private ListenableFuture<ResultT> mFuture;

    private static synchronized DvrDatabaseHelper initializeDbHelper(Context context) {
        if (sDbHelper == null) {
            sDbHelper = new DvrDatabaseHelper(context.getApplicationContext());
        }
        return sDbHelper;
    }

    final Context mContext;

    private AsyncDvrDbTask(Context context) {
        mContext = context;
    }

    /** Execute the task on the {@link #DB_EXECUTOR} thread and return Future*/
    @SafeVarargs
    public final ListenableFuture<ResultT> executeOnDbThread(
        FutureCallback<ResultT> callback, ParamsT... params) {
            mFuture = DB_EXECUTOR.submit(() -> doInBackground(params));
            Futures.addCallback(mFuture, callback, MainThreadExecutor.getInstance());
            return mFuture;
    }

    protected final ResultT doInBackground(ParamsT... params) {
        initializeDbHelper(mContext);
        return doInDvrBackground(params);
    }

    /** Executes in the background after {@link #initializeDbHelper(Context)} */
    @Nullable
    protected abstract ResultT doInDvrBackground(ParamsT... params);

    public final boolean isCancelled() {
        return mFuture.isCancelled();
    }

    /** Inserts schedules. */
    public static class AsyncAddScheduleTask
            extends AsyncDvrDbTask<ScheduledRecording, Void> {
        public AsyncAddScheduleTask(Context context) {
            super(context);
        }

        @Override
        protected final Void doInDvrBackground(ScheduledRecording... params) {
            sDbHelper.insertSchedules(params);
            return null;
        }
    }

    /** Update schedules. */
    public static class AsyncUpdateScheduleTask
            extends AsyncDvrDbTask<ScheduledRecording, Void> {
        public AsyncUpdateScheduleTask(Context context) {
            super(context);
        }

        @Override
        protected final Void doInDvrBackground(ScheduledRecording... params) {
            sDbHelper.updateSchedules(params);
            return null;
        }
    }

    /** Delete schedules. */
    public static class AsyncDeleteScheduleTask
            extends AsyncDvrDbTask<ScheduledRecording, Void> {
        public AsyncDeleteScheduleTask(Context context) {
            super(context);
        }

        @Override
        protected final Void doInDvrBackground(ScheduledRecording... params) {
            sDbHelper.deleteSchedules(params);
            return null;
        }
    }

    /** Returns all {@link ScheduledRecording}s. */
    public static class AsyncDvrQueryScheduleTask
            extends AsyncDvrDbTask<Void, List<ScheduledRecording>> {
        public AsyncDvrQueryScheduleTask(Context context) {
            super(context);
        }

        @Override
        @Nullable
        protected final List<ScheduledRecording> doInDvrBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            List<ScheduledRecording> scheduledRecordings = new ArrayList<>();
            try (Cursor c = sDbHelper.query(Schedules.TABLE_NAME, ScheduledRecording.PROJECTION)) {
                while (c.moveToNext() && !isCancelled()) {
                    scheduledRecordings.add(ScheduledRecording.fromCursor(c));
                }
            }
            return scheduledRecordings;
        }
    }

    /** Inserts series recordings. */
    public static class AsyncAddSeriesRecordingTask
            extends AsyncDvrDbTask<SeriesRecording, Void> {
        public AsyncAddSeriesRecordingTask(Context context) {
            super(context);
        }

        @Override
        protected final Void doInDvrBackground(SeriesRecording... params) {
            sDbHelper.insertSeriesRecordings(params);
            return null;
        }
    }

    /** Update series recordings. */
    public static class AsyncUpdateSeriesRecordingTask
            extends AsyncDvrDbTask<SeriesRecording, Void> {
        public AsyncUpdateSeriesRecordingTask(Context context) {
            super(context);
        }

        @Override
        protected final Void doInDvrBackground(SeriesRecording... params) {
            sDbHelper.updateSeriesRecordings(params);
            return null;
        }
    }

    /** Delete series recordings. */
    public static class AsyncDeleteSeriesRecordingTask
            extends AsyncDvrDbTask<SeriesRecording, Void> {
        public AsyncDeleteSeriesRecordingTask(Context context) {
            super(context);
        }

        @Override
        protected final Void doInDvrBackground(SeriesRecording... params) {
            sDbHelper.deleteSeriesRecordings(params);
            return null;
        }
    }

    /** Returns all {@link SeriesRecording}s. */
    public static class AsyncDvrQuerySeriesRecordingTask
            extends AsyncDvrDbTask<Void, List<SeriesRecording>> {
        private static final String TAG = "DvrQuerySeriesRecording";

        public AsyncDvrQuerySeriesRecordingTask(Context context) {
            super(context);
        }

        @Override
        @Nullable
        protected final List<SeriesRecording> doInDvrBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            List<SeriesRecording> scheduledRecordings = new ArrayList<>();
            try (Cursor c =
                    sDbHelper.query(SeriesRecordings.TABLE_NAME, SeriesRecording.PROJECTION)) {
                while (c.moveToNext() && !isCancelled()) {
                    scheduledRecordings.add(SeriesRecording.fromCursor(c));
                }
            } catch (Exception e) {
                Log.w(TAG, "Can't query dvr series recording data", e);
            }
            return scheduledRecordings;
        }
    }
}
