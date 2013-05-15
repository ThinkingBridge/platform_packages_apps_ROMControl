
package com.aokp.romcontrol.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.aokp.romcontrol.R;
import com.aokp.romcontrol.R.xml;
import com.aokp.romcontrol.AOKPPreferenceFragment;

public class PowerMenu extends AOKPPreferenceFragment implements OnPreferenceChangeListener {
	private static final String TAG = "PowerMenu";
	
    //private static final String PREF_POWER_SAVER = "show_power_saver";
    //private static final String PREF_SCREENSHOT = "show_screenshot";
    //private static final String PREF_TORCH_TOGGLE = "show_torch_toggle";
    //private static final String PREF_AIRPLANE_TOGGLE = "show_airplane_toggle";
    private static final String PREF_NAVBAR_HIDE = "show_navbar_hide";
    private static final String PREF_EXPANDED_DESKTOP = "power_menu_expanded_desktop";
    private static final String PREF_SCREENSHOT = "show_screenshot";
    private static final String PREF_AIRPLANE_TOGGLE = "show_airplane_toggle";
    private static final String KEY_REBOOT = "power_menu_reboot";
    private static final String KEY_SILENT = "power_menu_silent";
    private static final String PREF_SCREENSHOT_SOUND = "pref_screenshot_sound";
    private static final String PREF_SCREENSHOT_DELAY = "pref_screenshot_delay";

    //CheckBoxPreference mShowPowerSaver;
    //CheckBoxPreference mShowScreenShot;
    //CheckBoxPreference mShowTorchToggle;
    //CheckBoxPreference mShowAirplaneToggle;
    CheckBoxPreference mShowNavBarHide;
    CheckBoxPreference mShowScreenShot;
    CheckBoxPreference mShowAirplaneToggle;
    CheckBoxPreference mRebootPref;
    CheckBoxPreference mSilentPref;
    CheckBoxPreference mScreenshotSound;
    ListPreference mExpandedDesktopPref;
    ListPreference mScreenshotDelay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs_powermenu);
        /*
        mShowPowerSaver = (CheckBoxPreference) findPreference(PREF_POWER_SAVER);
        int powerSaverVal = 0;
        try {
            powerSaverVal = Settings.Secure.getInt(getActivity()
                    .getContentResolver(), Settings.Secure.POWER_SAVER_MODE);
        } catch (SettingNotFoundException e) {
            mShowPowerSaver.setEnabled(false);
            mShowPowerSaver
                    .setSummary("You need to enable power saver before you can see it in the power menu.");
        }
        mShowPowerSaver.setChecked(powerSaverVal != 0);

        mShowTorchToggle = (CheckBoxPreference) findPreference(PREF_TORCH_TOGGLE);
        mShowTorchToggle.setChecked(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.POWER_DIALOG_SHOW_TORCH_TOGGLE,
                0) == 1);

        mShowScreenShot = (CheckBoxPreference) findPreference(PREF_SCREENSHOT);
        mShowScreenShot.setChecked(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.POWER_DIALOG_SHOW_SCREENSHOT,
                0) == 1);

        mShowAirplaneToggle = (CheckBoxPreference) findPreference(PREF_AIRPLANE_TOGGLE);
        mShowAirplaneToggle.setChecked(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.POWER_DIALOG_SHOW_AIRPLANE_TOGGLE,
                1) == 1);
        */
        mShowNavBarHide = (CheckBoxPreference) findPreference(PREF_NAVBAR_HIDE);
        mShowNavBarHide.setChecked(Settings.System.getBoolean(getActivity()
                .getContentResolver(), Settings.System.POWER_DIALOG_SHOW_NAVBAR_HIDE,
                false));
        
        PreferenceScreen prefSet = getPreferenceScreen();
        
        mExpandedDesktopPref = (ListPreference) prefSet.findPreference(PREF_EXPANDED_DESKTOP);
        mExpandedDesktopPref.setOnPreferenceChangeListener(this);
        int expandedDesktopValue = Settings.System.getInt(getContentResolver(),
                        Settings.System.EXPANDED_DESKTOP_STYLE, 0);
        mExpandedDesktopPref.setValue(String.valueOf(expandedDesktopValue));
        mExpandedDesktopPref.setSummary(mExpandedDesktopPref.getEntries()[expandedDesktopValue]);
                
        mShowScreenShot = (CheckBoxPreference) findPreference(PREF_SCREENSHOT);
        mShowScreenShot.setChecked(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.POWER_MENU_SCREENSHOT_ENABLED,
                0) == 1);

        mShowAirplaneToggle = (CheckBoxPreference) findPreference(PREF_AIRPLANE_TOGGLE);
        mShowAirplaneToggle.setChecked(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.POWER_MENU_AIRPLANE_ENABLED,
                1) == 1);
        
        mRebootPref = (CheckBoxPreference) findPreference(KEY_REBOOT);
        mRebootPref.setChecked(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.POWER_MENU_REBOOT_ENABLED,
                1) == 1);
                
        mSilentPref = (CheckBoxPreference) findPreference(KEY_SILENT);
        mSilentPref.setChecked(Settings.System.getInt(getActivity()
        		.getContentResolver(), Settings.System.POWER_MENU_SILENT_ENABLED,
        		1) == 1);
        
        mScreenshotSound = (CheckBoxPreference) findPreference(PREF_SCREENSHOT_SOUND);
        mScreenshotSound.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.SCREENSHOT_SOUND, 1) == 1));

        mScreenshotDelay = (ListPreference) findPreference(PREF_SCREENSHOT_DELAY);
        int screenshotDelay = Settings.System.getInt(getContentResolver(),
                Settings.System.SCREENSHOT_DELAY, 1000);
        mScreenshotDelay.setValue(String.valueOf(screenshotDelay));
        mScreenshotDelay.setOnPreferenceChangeListener(this);
        
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            /*
        if (preference == mShowScreenShot) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWER_DIALOG_SHOW_SCREENSHOT,
                    ((CheckBoxPreference)preference).isChecked() ? 1 : 0);
            return true;

        } else if (preference == mShowPowerSaver) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWER_DIALOG_SHOW_POWER_SAVER,
                    ((CheckBoxPreference)preference).isChecked() ? 1 : 0);
            return true;
        } else if (preference == mShowTorchToggle) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWER_DIALOG_SHOW_TORCH_TOGGLE,
                    ((CheckBoxPreference)preference).isChecked() ? 1 : 0);
            return true;

        } else if (preference == mShowAirplaneToggle) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWER_DIALOG_SHOW_AIRPLANE_TOGGLE,
                    ((CheckBoxPreference)preference).isChecked() ? 1 : 0);
            return true;
        } else */
        if (preference == mShowNavBarHide) {
            Settings.System.putBoolean(getActivity().getContentResolver(),
                    Settings.System.POWER_DIALOG_SHOW_NAVBAR_HIDE,
                    ((CheckBoxPreference)preference).isChecked());
            return true;
        } else if (preference == mShowScreenShot) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWER_MENU_SCREENSHOT_ENABLED,
                    ((CheckBoxPreference)preference).isChecked() ? 1 : 0);
            return true;
        } else if (preference == mShowAirplaneToggle) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWER_MENU_AIRPLANE_ENABLED,
                    ((CheckBoxPreference)preference).isChecked() ? 1 : 0);
            return true;
        } else if (preference == mRebootPref) {
        	Settings.System.putInt(getActivity().getContentResolver(),
        			Settings.System.POWER_MENU_REBOOT_ENABLED,
        			((CheckBoxPreference)preference).isChecked() ? 1 : 0);
        	return true;
        } else if (preference == mSilentPref) {
        	Settings.System.putInt(getActivity().getContentResolver(),
        			Settings.System.POWER_MENU_SILENT_ENABLED,
        			((CheckBoxPreference)preference).isChecked() ? 1 : 0);
        	return true;
        } else if (preference == mScreenshotSound) {
        	boolean value = mScreenshotSound.isChecked();
        	Settings.System.putInt(getContentResolver(),
        			Settings.System.SCREENSHOT_SOUND, value ? 1 : 0);
        	return true;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
    
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mExpandedDesktopPref) {
            int expandedDesktopValue = Integer.valueOf((String) newValue);
            int index = mExpandedDesktopPref.findIndexOfValue((String) newValue);
            if (expandedDesktopValue == 0) {
                Settings.System.putInt(getContentResolver(),
                        Settings.System.POWER_MENU_EXPANDED_DESKTOP_ENABLED, 0);
                // Disable expanded desktop if enabled
                Settings.System.putInt(getContentResolver(),
                        Settings.System.EXPANDED_DESKTOP_STATE, 0);
            } else {
                Settings.System.putInt(getContentResolver(),
                        Settings.System.POWER_MENU_EXPANDED_DESKTOP_ENABLED, 1);
            }
            Settings.System.putInt(getContentResolver(),
                    Settings.System.EXPANDED_DESKTOP_STYLE, expandedDesktopValue);
            mExpandedDesktopPref.setSummary(mExpandedDesktopPref.getEntries()[index]);
            return true;
        }
        return false;
    }
