package com.bin.lop.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by jabin on 7/5/15.
 */
@Singleton
public class OSUtils {

    Context mContext;

    @Inject
    OSUtils(Context context) {
        this.mContext = context;
    }


    public String getAppChannel() {
        try {
            PackageManager pm = mContext.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getRealMetrics(displayMetrics);
        return displayMetrics;
    }
}
