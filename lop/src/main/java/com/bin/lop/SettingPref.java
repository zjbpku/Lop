package com.bin.lop;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by jabin on 7/12/15.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface SettingPref {

    @DefaultBoolean(true)
    boolean countDownTime();

    @DefaultBoolean(false)
    boolean stopVisable();

    @DefaultInt(100)
    int videoSize();

    @DefaultBoolean(true)
    boolean recordNotification();
}
