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

package com.android.tv.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.tv.ChannelChanger;
import com.android.tv.R;
import com.android.tv.TvFeatures;

/**
 * Accessibility controls for TvView.
 *
 * <p>Adds channel up and down controls.
 */
public class A11yTvControlsView extends LinearLayout implements AccessibilityStateChangeListener {

    private final ChannelChanger mChannelChanger;
    private TextView mChannelUp;
    private TextView mChannelDown;
    private TextView mShortcuts;
    private TextView mMenu;
    private boolean shortcutsHidden = false;

    public A11yTvControlsView(@NonNull Context context) {
        this(context, null);
    }

    public A11yTvControlsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public A11yTvControlsView(
            @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public A11yTvControlsView(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mChannelChanger = (ChannelChanger) context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        AccessibilityManager a11yManager =
                getContext().getSystemService(AccessibilityManager.class);
        boolean a11yEnabled = a11yManager.isEnabled();
        setVisibility(
                TvFeatures.A11Y_CHANNEL_CHANGE_UI.isEnabled(getContext()) && a11yEnabled
                        ? VISIBLE
                        : GONE);
        mShortcuts = (TextView) findViewById(R.id.a11y_shortcuts);
        mShortcuts.setOnClickListener(v -> toggleShortcuts());

        mChannelUp = (TextView) findViewById(R.id.a11y_channel_up);
        mChannelUp.setOnClickListener(v -> mChannelChanger.channelUp());

        mChannelDown = (TextView) findViewById(R.id.a11y_channel_down);
        mChannelDown.setOnClickListener(v -> mChannelChanger.channelDown());

        mMenu = (TextView) findViewById(R.id.a11y_menu);
        // Without a click handler the dpad center just goes to the
        // activity which will open the menu

        a11yManager.addAccessibilityStateChangeListener(this);
    }

    private void toggleShortcuts() {
        String text;
        int visibility;
        if (shortcutsHidden) {
            shortcutsHidden = false;
            text = "Hide shortcuts";
            visibility = VISIBLE;
        } else {
            shortcutsHidden = true;
            text = "Show shortcuts";
            visibility = INVISIBLE;
        }
        mShortcuts.setText(text);
        mChannelUp.setVisibility(visibility);
        mChannelDown.setVisibility(visibility);
        mMenu.setVisibility(visibility);
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        setVisibility(
                TvFeatures.A11Y_CHANNEL_CHANGE_UI.isEnabled(getContext()) && enabled
                        ? VISIBLE
                        : GONE);
    }
}
