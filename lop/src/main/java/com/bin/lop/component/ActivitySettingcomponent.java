package com.bin.lop.component;

import com.bin.lop.ActivityModule;
import com.bin.lop.SettingActivity;

import dagger.Component;

/**
 * Created by jabin on 7/10/15.
 */
@Component(dependencies = ApplicationComponent.class,modules = ActivityModule.class)
@ActivityScope
public interface ActivitySettingcomponent extends ApplicationComponent {
    void injectActivity(SettingActivity activity);
}
