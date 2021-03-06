/*
Copyright 2016-2017 Pieter Pareit

This file is part of SwiFTP.

SwiFTP is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SwiFTP is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.hipad.swiftp.locale;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.twofortyfouram.locale.sdk.client.ui.activity.AbstractPluginActivity;

import com.hipad.swiftp.FsSettings;
import com.hipad.swiftp.R;

/**
 * Created by ppareit on 29/04/16.
 */
public class EditActivity extends AbstractPluginActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(FsSettings.getTheme());
        super.onCreate(savedInstanceState);

        setContentView(R.layout.locale_edit_layout);

        CharSequence callingApplicationLabel = null;
        try {
            callingApplicationLabel =
                    getPackageManager().getApplicationLabel(
                            getPackageManager().getApplicationInfo(getCallingPackage(),
                                    0));
        } catch (final PackageManager.NameNotFoundException e) {
        }
        if (null != callingApplicationLabel) {
            setTitle(callingApplicationLabel);
        }

        getActionBar().setSubtitle("Swiftp");

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isBundleValid(@NonNull Bundle bundle) {
        return SettingsBundleHelper.isBundleValid(bundle);
    }

    @Override
    public void onPostCreateWithPreviousResult(@NonNull Bundle previousBundle,
                                               @NonNull String previousBlurb) {
        if (!isBundleValid(previousBundle)) {
            previousBundle = SettingsBundleHelper.generateBundle(this, false);
        }
        boolean running = previousBundle.getBoolean(SettingsBundleHelper.BUNDLE_BOOLEAN_RUNNING);
        RadioButton radioButton =
                (RadioButton) findViewById(running ? R.id.radio_server_running :
                                R.id.radio_server_stopped);
        radioButton.setChecked(true);
    }

    @Nullable
    @Override
    public Bundle getResultBundle() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_server_state_group);
        int checkedId = radioGroup.getCheckedRadioButtonId();
        boolean running = (checkedId == R.id.radio_server_running);

        return SettingsBundleHelper.generateBundle(this, running);
    }

    @NonNull
    @Override
    public String getResultBlurb(@NonNull Bundle bundle) {
        boolean running = SettingsBundleHelper.getBundleRunningState(bundle);
        return running ? "Running" : "Stopped";
    }
}
