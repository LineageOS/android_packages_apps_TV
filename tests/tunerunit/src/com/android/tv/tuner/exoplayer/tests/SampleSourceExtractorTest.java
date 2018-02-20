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

package com.android.tv.tuner.exoplayer.tests;

import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.support.test.filters.LargeTest;
import android.test.InstrumentationTestCase;
import android.util.Pair;
import com.android.tv.tuner.exoplayer.ExoPlayerSampleExtractor;
import com.android.tv.tuner.exoplayer.buffer.BufferManager;
import com.android.tv.tuner.exoplayer.buffer.BufferManager.StorageManager;
import com.android.tv.tuner.exoplayer.buffer.SampleChunk;
import com.android.tv.tuner.testing.buffer.VerySlowSampleChunk;
import com.android.tv.tuner.tvinput.PlaybackBufferListener;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.SampleHolder;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.upstream.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

@LargeTest
public class SampleSourceExtractorTest extends InstrumentationTestCase {
    // Maximum bandwidth of 1080p channel is about 2.2MB/s. 2MB for a sample will suffice.
    private static final int SAMPLE_BUFFER_SIZE = 1024 * 1024 * 2;
    private static final int CONSUMING_SAMPLES_PERIOD = 100;
    private static final int PREPARE_POLL_DELAY_MILLIS = 100;
    private static final String TEST_TS_ASSET_PATH = "asset:///capture_stream.ts";

    public void testTrickplayDisabled() throws Throwable {
        DataSource source = new AssetDataSource(getInstrumentation().getContext());
        MockPlaybackBufferListener listener = new MockPlaybackBufferListener();
        ExoPlayerSampleExtractor extractor =
                new ExoPlayerSampleExtractor(
                        Uri.parse(TEST_TS_ASSET_PATH), source, null, listener, false);

        assertEquals("Trickplay should be disabled", listener.getLastState(), Boolean.FALSE);

        // Prepares the extractor.
        try {
            while (!extractor.prepare()) {
                Thread.sleep(PREPARE_POLL_DELAY_MILLIS);
            }
        } catch (IOException | InterruptedException e) {
            fail("Exception occurred while preparing: " + e.getMessage());
        }

        // Selects all tracks.
        List<MediaFormat> trackFormats = extractor.getTrackFormats();
        for (int i = 0; i < trackFormats.size(); ++i) {
            extractor.selectTrack(i);
        }

        // Consumes over some period.
        SampleHolder sampleHolder = new SampleHolder(SampleHolder.BUFFER_REPLACEMENT_MODE_NORMAL);
        sampleHolder.ensureSpaceForWrite(SAMPLE_BUFFER_SIZE);
        for (int i = 0; i < CONSUMING_SAMPLES_PERIOD; ++i) {
            boolean found = false;
            while (!found) {
                for (int j = 0; j < trackFormats.size(); ++j) {
                    int result = extractor.readSample(j, sampleHolder);
                    switch (result) {
                        case SampleSource.SAMPLE_READ:
                            found = true;
                            break;
                        case SampleSource.END_OF_STREAM:
                            fail("Failed to read samples");
                            break;
                        default:
                    }
                    if (found) {
                        break;
                    }
                }
                Thread.yield();
            }
        }

        extractor.release();
    }

    public void testDiskTooSlowTrickplayDisabled() throws Throwable {
        StorageManager storageManager =
                new StubStorageManager(getInstrumentation().getTargetContext());
        BufferManager bufferManager =
                new BufferManager(
                        storageManager, new VerySlowSampleChunk.VerySlowSampleChunkCreator());
        bufferManager.setMinimumSampleSizeForSpeedCheck(0);
        DataSource source = new AssetDataSource(getInstrumentation().getContext());
        MockPlaybackBufferListener listener = new MockPlaybackBufferListener();
        ExoPlayerSampleExtractor extractor =
                new ExoPlayerSampleExtractor(
                        Uri.parse(TEST_TS_ASSET_PATH), source, bufferManager, listener, false);

        assertEquals(
                "Trickplay should be enabled at the first", listener.getLastState(), Boolean.TRUE);

        // Prepares the extractor.
        try {
            while (!extractor.prepare()) {
                Thread.sleep(PREPARE_POLL_DELAY_MILLIS);
            }
        } catch (IOException | InterruptedException e) {
            fail("Exception occurred while preparing: " + e.getMessage());
        }

        // Selects all tracks.
        List<MediaFormat> trackFormats = extractor.getTrackFormats();
        for (int i = 0; i < trackFormats.size(); ++i) {
            extractor.selectTrack(i);
        }

        // Consumes until once speed check is done.
        SampleHolder sampleHolder = new SampleHolder(SampleHolder.BUFFER_REPLACEMENT_MODE_NORMAL);
        sampleHolder.ensureSpaceForWrite(SAMPLE_BUFFER_SIZE);
        while (!bufferManager.hasSpeedCheckDone()) {
            boolean found = false;
            while (!found) {
                for (int j = 0; j < trackFormats.size(); ++j) {
                    int result = extractor.readSample(j, sampleHolder);
                    switch (result) {
                        case SampleSource.SAMPLE_READ:
                            found = true;
                            break;
                        case SampleSource.END_OF_STREAM:
                            fail("Failed to read samples");
                            break;
                        default:
                    }
                    if (found) {
                        break;
                    }
                }
                Thread.yield();
            }
        }

        extractor.release();

        // Sleep for synchronization.
        SystemClock.sleep(1000);

        assertEquals(
                "Disk too slow event should be reported", listener.isReportedDiskTooSlow(), true);
    }

    private static class StubStorageManager implements StorageManager {
        private final Context mContext;

        StubStorageManager(Context context) {
            mContext = context;
        }

        @Override
        public File getBufferDir() {
            return mContext.getCacheDir();
        }

        @Override
        public boolean isPersistent() {
            return false;
        }

        @Override
        public boolean reachedStorageMax(long bufferSize, long pendingDelete) {
            return false;
        }

        @Override
        public boolean hasEnoughBuffer(long pendingDelete) {
            return true;
        }

        @Override
        public List<BufferManager.TrackFormat> readTrackInfoFiles(boolean isAudio) {
            return null;
        }

        @Override
        public ArrayList<BufferManager.PositionHolder> readIndexFile(String trackId)
                throws IOException {
            return null;
        }

        @Override
        public void writeTrackInfoFiles(List<BufferManager.TrackFormat> formatList, boolean isAudio)
                throws IOException {
            // No-op.
        }

        @Override
        public void writeIndexFile(
                String trackName, SortedMap<Long, Pair<SampleChunk, Integer>> index)
                throws IOException {
            // No-op.
        }
    }

    public class MockPlaybackBufferListener implements PlaybackBufferListener {
        private Boolean mLastState;
        private boolean mIsReportedDiskTooSlow;

        public Boolean getLastState() {
            return mLastState;
        }

        public boolean isReportedDiskTooSlow() {
            return mIsReportedDiskTooSlow;
        }

        // PlaybackBufferListener
        @Override
        public void onBufferStartTimeChanged(long startTimeMs) {
            // No-op.
        }

        @Override
        public void onBufferStateChanged(boolean available) {
            mLastState = available;
        }

        @Override
        public void onDiskTooSlow() {
            mIsReportedDiskTooSlow = true;
        }
    }
}
