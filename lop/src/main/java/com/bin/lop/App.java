package com.bin.lop;

import android.app.Application;

import com.bin.lop.component.ApplicationComponent;
import com.bin.lop.component.DaggerApplicationComponent;

/**
 * Created by jabin on 7/5/15.
 */
public class App extends Application {

    private ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.appComponent = DaggerApplicationComponent.builder().appModule(new AppModule(this)).build();
        this.appComponent.inject(this);
    }

    public ApplicationComponent getAppComponent() {
        return this.appComponent;
    }


}
