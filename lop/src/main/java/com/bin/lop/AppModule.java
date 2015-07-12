package com.bin.lop;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jabin on 7/9/15.
 */
@Module
public class AppModule {
    private App app;

    public AppModule(App application) {
        this.app = application;
    }


    @Provides
    @Singleton
    Application providesApplication() {
        return this.app;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return this.app;
    }

//
//    @Provides
//    @Singleton
//    BusProvider getProvidesBusProvider() {
//        return new BusProvider();
//    }
//
//    @Provides
//    @Singleton
//    @Named("Any")
//    Bus providesAnyBus(BusProvider provider) {
//        return provider.getAnyBus();
//    }
//
//    @Provides
//    @Singleton
//    @Named("Main")
//    Bus providesMainBus(BusProvider provider) {
//        return provider.getMainBus();
//    }


}
