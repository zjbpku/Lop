package com.bin.lop.component;

import com.bin.lop.ActivityModule;
import com.bin.lop.MainActivity;

import dagger.Component;

/**
 * Created by jabin on 7/9/15.
 */
@Component(
        dependencies = ApplicationComponent.class,
        modules = ActivityModule.class
)
@ActivityScope
public interface ActivityMainComponent extends ApplicationComponent {
    void injectActivity(MainActivity activity);
}
