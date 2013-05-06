
package com.aokp.romcontrol.fragments;

import net.margaritov.preference.colorpicker.ColorPickerPreference;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.util.Log;

import com.aokp.romcontrol.R;
import com.aokp.romcontrol.AOKPPreferenceFragment;
import com.aokp.romcontrol.util.Helpers;

public class StatusBarSignal extends AOKPPreferenceFragment implements
        OnPreferenceChangeListener {
	
	private static final String STATUS_BAR_TRAFFIC = "status_bar_traffic";
	private static final String STATUS_BAR_TRAFFIC_COLOR = "status_bar_traffic_color";

    ListPreference mDbmStyletyle;
    ListPreference mWifiStyle;
    ColorPickerPreference mColorPicker;
    ColorPickerPreference mWifiColorPicker;
    CheckBoxPreference mHideSignal;
    CheckBoxPreference mAltSignal;
    CheckBoxPreference mStatusBarTraffic;
    ColorPickerPreference mTrafficColorPicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs_statusbar_signal);

        int defaultColor;
        int intColor;
        String hexColor;

        mDbmStyletyle = (ListPreference) findPreference("signal_style");
        mDbmStyletyle.setOnPreferenceChangeListener(this);
        mDbmStyletyle.setValue(Integer.toString(Settings.System.getInt(mContentRes,
                Settings.System.STATUSBAR_SIGNAL_TEXT, 0)));

        mColorPicker = (ColorPickerPreference) findPreference("signal_color");
        mColorPicker.setOnPreferenceChangeListener(this);
        defaultColor = getResources().getColor(
                com.android.internal.R.color.holo_blue_light);
        intColor = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_SIGNAL_TEXT_COLOR, defaultColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mColorPicker.setSummary(hexColor);
        mColorPicker.setNewPreviewColor(intColor);

        mWifiStyle = (ListPreference) findPreference("wifi_signal_style");
        mWifiStyle.setOnPreferenceChangeListener(this);
        mWifiStyle.setValue(Integer.toString(Settings.System.getInt(mContentRes,
                Settings.System.STATUSBAR_WIFI_SIGNAL_TEXT, 0)));

        mWifiColorPicker = (ColorPickerPreference) findPreference("wifi_signal_color");
        mWifiColorPicker.setOnPreferenceChangeListener(this);
        defaultColor = getResources().getColor(
                com.android.internal.R.color.holo_blue_light);
        intColor = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_WIFI_SIGNAL_TEXT_COLOR, defaultColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mWifiColorPicker.setSummary(hexColor);
        mWifiColorPicker.setNewPreviewColor(intColor);

        mHideSignal = (CheckBoxPreference) findPreference("hide_signal");
        mHideSignal.setChecked(Settings.System.getBoolean(mContentRes,
                Settings.System.STATUSBAR_HIDE_SIGNAL_BARS, false));

        mAltSignal = (CheckBoxPreference) findPreference("alt_signal");
        mAltSignal.setChecked(Settings.System.getBoolean(mContentRes,
                Settings.System.STATUSBAR_SIGNAL_CLUSTER_ALT,false));
        
        mStatusBarTraffic = (CheckBoxPreference) findPreference(STATUS_BAR_TRAFFIC);
        mStatusBarTraffic.setChecked((Settings.System.getInt(mContentRes,
                Settings.System.STATUS_BAR_TRAFFIC, 1) == 1));

        mTrafficColorPicker = (ColorPickerPreference) findPreference("status_bar_traffic_color");
        mTrafficColorPicker.setOnPreferenceChangeListener(this);
        defaultColor = getResources().getColor(
                com.android.internal.R.color.holo_blue_light);
        intColor = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_TRAFFIC_COLOR, defaultColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mTrafficColorPicker.setSummary(hexColor);
        mTrafficColorPicker.setNewPreviewColor(intColor);

        if (Integer.parseInt(mDbmStyletyle.getValue()) == 0) {
            mColorPicker.setEnabled(false);
        }

        if (Integer.parseInt(mWifiStyle.getValue()) == 0) {
            mWifiColorPicker.setEnabled(false);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        if (preference == mHideSignal) {
            Settings.System.putBoolean(mContentRes,
                    Settings.System.STATUSBAR_HIDE_SIGNAL_BARS, mHideSignal.isChecked());
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mAltSignal) {
            Settings.System.putBoolean(mContentRes,
                    Settings.System.STATUSBAR_SIGNAL_CLUSTER_ALT,mAltSignal.isChecked());
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mStatusBarTraffic) {
        	Settings.System.putInt(mContentRes,
        			Settings.System.STATUS_BAR_TRAFFIC,
        			mStatusBarTraffic.isChecked() ? 1 : 0);
        	Helpers.restartSystemUI();
        	return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mDbmStyletyle) {

            int val = Integer.parseInt((String) newValue);
            Settings.System.putInt(mContentRes,
                    Settings.System.STATUSBAR_SIGNAL_TEXT, val);
            mColorPicker.setEnabled(val == 0 ? false : true);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mColorPicker) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            preference.setSummary(hex);

            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mContentRes,
                    Settings.System.STATUSBAR_SIGNAL_TEXT_COLOR, intHex);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mWifiStyle) {

            int val = Integer.parseInt((String) newValue);
            Settings.System.putInt(mContentRes,
                    Settings.System.STATUSBAR_WIFI_SIGNAL_TEXT, val);
            mWifiColorPicker.setEnabled(val == 0 ? false : true);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mWifiColorPicker) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            preference.setSummary(hex);

            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mContentRes,
                    Settings.System.STATUSBAR_WIFI_SIGNAL_TEXT_COLOR, intHex);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mTrafficColorPicker) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            preference.setSummary(hex);

            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mContentRes,
                    Settings.System.STATUS_BAR_TRAFFIC_COLOR, intHex);
            Helpers.restartSystemUI();
            return true;
        }
        return false;
    }
}
