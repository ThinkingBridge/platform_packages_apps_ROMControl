/*
 * Copyright (C) 2013 Slimroms
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

package com.aokp.romcontrol.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.aokp.romcontrol.AOKPPreferenceFragment;
import com.aokp.romcontrol.R;
import com.aokp.romcontrol.widgets.SeekBarPreference;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class PieStyleSettings extends AOKPPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String TAG = "PieStyleSettings";
    private static final String PREF_PIE_BACKGROUND_COLOR = "pie_background_color";
    private static final String PREF_PIE_SNAP_COLOR = "pie_snap_color";
    private static final String PREF_PIE_TEXT_COLOR = "pie_text_color";
    private static final String PREF_PIE_BACKGROUND_ALPHA = "pie_background_alpha";
    private static final String PREF_PIE_CONTROL_SIZE = "pie_control_size";

    private static final float PIE_CONTROL_SIZE_MIN = 0.9f;
    private static final float PIE_CONTROL_SIZE_MAX = 1.5f;

    private boolean mCheckPreferences;

    Resources mSystemUiResources;

    ColorPickerPreference mPieBackgroundColor;
    ColorPickerPreference mPieSnapColor;
    ColorPickerPreference mPieTextColor;
    SeekBarPreference mPieBackgroundAlpha;
    SeekBarPreference mPieControlSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshSettings();
    }

    private PreferenceScreen refreshSettings() {
        mCheckPreferences = false;
        PreferenceScreen prefs = getPreferenceScreen();
        if (prefs != null) {
            prefs.removeAll();
        }

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pie_style);

        prefs = getPreferenceScreen();

        PackageManager pm = mContext.getPackageManager();

        if (pm != null) {
            try {
                mSystemUiResources = pm.getResourcesForApplication("com.android.systemui");
            } catch (Exception e) {
                mSystemUiResources = null;
                Log.e("PIEStyle:", "can't access systemui resources",e);
            }
        }

        String hexColor;
        int intColor;

        mPieBackgroundColor = (ColorPickerPreference) findPreference(PREF_PIE_BACKGROUND_COLOR);
        mPieBackgroundColor.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.PIE_BACKGROUND_COLOR, -2);
        if (intColor == -2) {
            intColor = mSystemUiResources.getColor(
                    mSystemUiResources.getIdentifier("pie_overlay_color", "color", "com.android.systemui"));
            mPieBackgroundColor.setSummary(getResources().getString(R.string.color_default));
        } else {
            hexColor = String.format("#%08x", (0xffffffff & intColor));
            mPieBackgroundColor.setSummary(hexColor);
        }
        mPieBackgroundColor.setNewPreviewColor(intColor);

        mPieSnapColor = (ColorPickerPreference) findPreference(PREF_PIE_SNAP_COLOR);
        mPieSnapColor.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.PIE_SNAP_COLOR, -2);
        if (intColor == -2) {
            intColor = mSystemUiResources.getColor(
                    mSystemUiResources.getIdentifier("pie_snap_color", "color", "com.android.systemui"));
            mPieSnapColor.setSummary(getResources().getString(R.string.color_default));
        } else {
            hexColor = String.format("#%08x", (0xffffffff & intColor));
            mPieSnapColor.setSummary(hexColor);
        }
        mPieSnapColor.setNewPreviewColor(intColor);

        mPieTextColor = (ColorPickerPreference) findPreference(PREF_PIE_TEXT_COLOR);
        mPieTextColor.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.PIE_TEXT_COLOR, -2);
        if (intColor == -2) {
            intColor = mSystemUiResources.getColor(
                    mSystemUiResources.getIdentifier("pie_text_color", "color", "com.android.systemui"));
            mPieTextColor.setSummary(getResources().getString(R.string.color_default));
        } else {
            hexColor = String.format("#%08x", (0xffffffff & intColor));
            mPieTextColor.setSummary(hexColor);
        }
        mPieTextColor.setNewPreviewColor(intColor);

        float defaultAlpha;
        try{
            defaultAlpha = Settings.System.getFloat(getActivity()
                     .getContentResolver(), Settings.System.PIE_BACKGROUND_ALPHA);
        } catch (Exception e) {
            defaultAlpha = 0.3f;
            Settings.System.putFloat(getActivity().getContentResolver(),
                Settings.System.PIE_BACKGROUND_ALPHA, defaultAlpha);
        }
        mPieBackgroundAlpha = (SeekBarPreference) findPreference(PREF_PIE_BACKGROUND_ALPHA);
        mPieBackgroundAlpha.setProperty(String.valueOf(defaultAlpha));
        mPieBackgroundAlpha.setInitValue((int) (defaultAlpha * 100));
        mPieBackgroundAlpha.setOnPreferenceChangeListener(this);

        float controlSize;
        try{
            controlSize = Settings.System.getFloat(getActivity()
                    .getContentResolver(), Settings.System.PIE_SIZE);
        } catch (Exception e) {
            controlSize = 0.9f;
            Settings.System.putFloat(getActivity().getContentResolver(),
                Settings.System.PIE_SIZE, controlSize);
        }
        float controlSizeValue = ((controlSize - PIE_CONTROL_SIZE_MIN) /
                    ((PIE_CONTROL_SIZE_MAX - PIE_CONTROL_SIZE_MIN) / 100)) / 100;
        mPieControlSize = (SeekBarPreference) findPreference(PREF_PIE_CONTROL_SIZE);
        mPieControlSize.disablePercentageValue(true);
        mPieControlSize.setInitValue((int) (controlSizeValue * 100));
        mPieControlSize.setOnPreferenceChangeListener(this);

        setHasOptionsMenu(true);
        mCheckPreferences = true;
        return prefs;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.pie_style, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset:
                Settings.System.putInt(getActivity().getContentResolver(),
                        Settings.System.PIE_BACKGROUND_COLOR, -2);
                Settings.System.putInt(getActivity().getContentResolver(),
                        Settings.System.PIE_SNAP_COLOR, -2);
                Settings.System.putInt(getActivity().getContentResolver(),
                        Settings.System.PIE_TEXT_COLOR, -2);

                Settings.System.putFloat(getActivity().getContentResolver(),
                       Settings.System.PIE_BACKGROUND_ALPHA, 0.3f);

                refreshSettings();
                return true;
             default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (!mCheckPreferences) {
            return false;
        }
        if (preference == mPieBackgroundColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.PIE_BACKGROUND_COLOR, intHex);
            return true;
        } else if (preference == mPieTextColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.PIE_TEXT_COLOR, intHex);
            return true;
        } else if (preference == mPieSnapColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.PIE_SNAP_COLOR, intHex);
            return true;
       } else if (preference == mPieBackgroundAlpha) {
            float val = Float.parseFloat((String) newValue);
            Log.e("R", "value: " + val / 100);
            Settings.System.putFloat(getActivity().getContentResolver(),
                    Settings.System.PIE_BACKGROUND_ALPHA,
                    val / 100);
            return true;
       }else if (preference == mPieControlSize) {
            float val = Float.parseFloat((String) newValue);
            float value = (val * ((PIE_CONTROL_SIZE_MAX - PIE_CONTROL_SIZE_MIN) /
                100)) + PIE_CONTROL_SIZE_MIN;
            Settings.System.putFloat(getActivity().getContentResolver(),
                    Settings.System.PIE_SIZE,
                    value);
            return true;
       }
       return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
