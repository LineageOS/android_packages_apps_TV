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
 * limitations under the License
 */

package com.android.tv.dvr.ui.list;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.text.format.DateUtils;
import com.android.tv.R;
import com.android.tv.TvSingletons;
import com.android.tv.common.util.Clock;
import com.android.tv.dvr.DvrDataManager;
import com.android.tv.dvr.data.RecordedProgram;
import com.android.tv.dvr.data.ScheduledRecording;
import com.android.tv.dvr.recorder.ScheduledProgramReaper;
import com.android.tv.dvr.ui.list.SchedulesHeaderRow.DateHeaderRow;
import com.android.tv.util.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** An adapter for DVR history. */
@TargetApi(VERSION_CODES.N)
@SuppressWarnings("AndroidApiChecker") // TODO(b/32513850) remove when error prone is updated
class DvrHistoryRowAdapter extends ArrayObjectAdapter {
    // TODO: handle row added/removed/updated

    private static final long ONE_DAY_MS = TimeUnit.DAYS.toMillis(1);
    private static final int MAX_HISTORY_DAYS = ScheduledProgramReaper.DAYS;

    private final Context mContext;
    private final Clock mClock;
    private final DvrDataManager mDvrDataManager;
    private final List<String> mTitles = new ArrayList<>();

    public DvrHistoryRowAdapter(
            Context context, ClassPresenterSelector classPresenterSelector, Clock clock) {
        super(classPresenterSelector);
        mContext = context;
        mClock = clock;
        mDvrDataManager = TvSingletons.getSingletons(mContext).getDvrDataManager();
        mTitles.add(mContext.getString(R.string.dvr_date_today));
        mTitles.add(mContext.getString(R.string.dvr_date_yesterday));
    }

    /** Returns context. */
    protected Context getContext() {
        return mContext;
    }

    /** Starts row adapter. */
    public void start() {
        clear();
        List<ScheduledRecording> recordingList = mDvrDataManager.getFailedScheduledRecordings();
        List<RecordedProgram> recordedProgramList = mDvrDataManager.getRecordedPrograms();

        recordingList.addAll(
                recordedProgramsToScheduledRecordings(recordedProgramList, MAX_HISTORY_DAYS));
        recordingList
                .sort(ScheduledRecording.START_TIME_THEN_PRIORITY_THEN_ID_COMPARATOR.reversed());
        long deadLine = Utils.getFirstMillisecondOfDay(mClock.currentTimeMillis());
        for (int i = 0; i < recordingList.size(); ) {
            ArrayList<ScheduledRecording> section = new ArrayList<>();
            while (i < recordingList.size() && recordingList.get(i).getStartTimeMs() >= deadLine) {
                section.add(recordingList.get(i++));
            }
            if (!section.isEmpty()) {
                SchedulesHeaderRow headerRow =
                        new DateHeaderRow(
                                calculateHeaderDate(deadLine),
                                mContext.getResources()
                                        .getQuantityString(
                                                R.plurals.dvr_schedules_section_subtitle,
                                                section.size(),
                                                section.size()),
                                section.size(),
                                deadLine);
                add(headerRow);
                for (ScheduledRecording recording : section) {
                    add(new ScheduleRow(recording, headerRow));
                }
            }
            deadLine -= ONE_DAY_MS;
        }
    }

    private String calculateHeaderDate(long timeMs) {
        int titleIndex =
                (int)
                        ((Utils.getFirstMillisecondOfDay(mClock.currentTimeMillis()) - timeMs)
                                / ONE_DAY_MS);
        String headerDate;
        if (titleIndex < mTitles.size()) {
            headerDate = mTitles.get(titleIndex);
        } else {
            headerDate =
                    DateUtils.formatDateTime(
                            getContext(),
                            timeMs,
                            DateUtils.FORMAT_SHOW_WEEKDAY
                                    | DateUtils.FORMAT_SHOW_DATE
                                    | DateUtils.FORMAT_ABBREV_MONTH);
        }
        return headerDate;
    }

    private List<ScheduledRecording> recordedProgramsToScheduledRecordings(
            List<RecordedProgram> recordedPrograms, int maxDays) {
        List<ScheduledRecording> result = new ArrayList<>(recordedPrograms.size());
        long firstMillisecondToday = Utils.getFirstMillisecondOfDay(mClock.currentTimeMillis());
        for (RecordedProgram recordedProgram : recordedPrograms) {
            if (maxDays
                    < Utils.computeDateDifference(
                            recordedProgram.getStartTimeUtcMillis(),
                            firstMillisecondToday)) {
                continue;
            }
            result.add(ScheduledRecording.builder(recordedProgram).build());
        }
        return result;
    }
}
