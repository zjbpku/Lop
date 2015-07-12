package com.bin.lop;

import android.app.Activity;

import dagger.Module;

/**
 * Created by jabin on 7/9/15.
 */
@Module
public class ActivityModule {
    private MainActivity activity;
    private SettingActivity settingActivity;

    public ActivityModule(Activity activity) {
        if (activity.getClass() == MainActivity_.class) {
            this.activity = (MainActivity) activity;
        } else if (activity.getClass() == SettingActivity_.class) {
            this.settingActivity = (SettingActivity) activity;
        }
    }
}
