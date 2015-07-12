package com.bin.lop;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.PreferenceChange;
import org.androidannotations.annotations.PreferenceScreen;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by jabin on 7/5/15.
 */
@PreferenceScreen(R.xml.preference)
@EFragment
public class SettingFragment extends PreferenceFragment {
    @Pref
    SettingPref_ settingPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getPreferenceManager().setSharedPreferencesName("SettingPref");
    }

    @PreferenceChange(R.string.lop_setting_show_stop_title_key)
    void showStopTitle() {
        boolean result = !settingPref.stopVisable().get();
        Log.d("setting", "showStopTitle " + result);
        settingPref.edit().stopVisable().put(result).apply();
    }

    @PreferenceChange(R.string.lop_setting_show_notification_key)
    void showNotification() {
        boolean result = !settingPref.recordNotification().get();
        Log.d("setting", "showNotification " + result);
        settingPref.edit().recordNotification().put(result).apply();
    }


    @PreferenceChange(R.string.lop_setting_countdown_key)
    void showCountdown() {
        boolean result = !settingPref.countDownTime().get();
        Log.d("setting", "countdown " + result);
        settingPref.edit().countDownTime().put(result).apply();
    }
}
