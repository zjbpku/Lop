package com.bin.lop.component;

import com.bin.lop.App;
import com.bin.lop.AppModule;
import com.bin.lop.RecordHelper;
import com.bin.lop.common.OSUtils;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jabin on 7/9/15.
 */
@Component(
        modules = {AppModule.class}
)
@Singleton
public interface ApplicationComponent {
    App inject(App app);

    OSUtils getOSUtils();

    RecordHelper recordHelper();
}
