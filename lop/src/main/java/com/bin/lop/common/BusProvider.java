package com.bin.lop.common;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Inject;

/**
 * Created by jabin on 7/12/15.
 */
public class BusProvider {

    Bus anyBus = new Bus(ThreadEnforcer.ANY);
    Bus mainBus = new Bus(ThreadEnforcer.MAIN);

    @Inject
    BusProvider() {
    }

    public Bus getAnyBus() {
        return anyBus;
    }

    public Bus getMainBus() {
        return mainBus;
    }
}
