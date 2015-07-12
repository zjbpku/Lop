package com.bin.lop.common;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * Created by jabin on 7/12/15.
 */
public class ToastHelper {

    Toast toast = null;

    @Inject
    ToastHelper() {
    }

    public void show(Context mContext, String text, int time) {
        if (toast == null) {
            toast = Toast.makeText(mContext, text, time);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public void show(Context mContext, int resId, int time) {
        String text = mContext.getString(resId);
        if (toast == null) {
            toast = Toast.makeText(mContext, text, time);
        } else {
            toast.setText(text);
        }
        toast.show();
    }
}
