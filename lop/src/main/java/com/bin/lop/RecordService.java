package com.bin.lop;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bin.lop.component.ApplicationComponent;
import com.bin.lop.component.DaggerApplicationComponent;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by jabin on 7/5/15.
 */
@EService
public class RecordService extends Service {
    private final static String EXTRA_RESULT_CODE = "extra_result_code";
    private final static String EXTRA_DATA = "extra_data";

    @Pref
    SettingPref_ settingPref_;


    RecordHelper.Listener listener = new RecordHelper.Listener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onStop() {
            stopSelf();
        }

    };
    private boolean running = false;
    private RecordHelper recordHelper;

    public static Intent newIntent(Context context, int resultCode, Intent data) {
        Intent intent = new Intent(context, RecordService_.class);
        intent.putExtra(EXTRA_RESULT_CODE, resultCode);
        intent.putExtra(EXTRA_DATA, data);
        return intent;
    }

    @Override
    public void onDestroy() {
        recordHelper.onDestory();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Lop: " + RecordService.class.getSimpleName(), "onStartCommand");
        if (running) {
            return START_NOT_STICKY;
        }
        Log.d("Lop: " + RecordService.class.getSimpleName(), "Starting up!");
        running = true;
        int resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, 0);
        Intent data = intent.getParcelableExtra(EXTRA_DATA);
        if (resultCode == 0 || data == null) {
            throw new IllegalStateException("Result code or data missing.");
        }
        App app = (App) this.getApplication();
        ApplicationComponent component = DaggerApplicationComponent.builder().appModule(new AppModule(app)).build();
        component.inject(app);
        recordHelper = component.recordHelper();
        recordHelper.initial(resultCode, data, listener, settingPref_);
        recordHelper.showOverlayView();
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d("Lop: " + RecordService.class.getSimpleName(), "onCreate");
    }
}
