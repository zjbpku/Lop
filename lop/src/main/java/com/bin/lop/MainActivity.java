package com.bin.lop;

import android.content.Intent;
import android.graphics.Color;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bin.lop.common.OSUtils;
import com.bin.lop.common.ToastHelper;
import com.bin.lop.component.ActivityMainComponent;
import com.bin.lop.component.DaggerActivityMainComponent;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1212;
    @ViewById
    Toolbar toolbar;
    //    @ViewById
//    RecyclerView recyclerView;
    ActivityMainComponent mainComponent;
    OSUtils mOSUtils;
    @Inject
    ToastHelper toastHelper;
    @ViewById
    TextView tvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInect();
    }

    private void initInect() {
        App app = (App) getApplication();
        mainComponent = DaggerActivityMainComponent.builder()
                .applicationComponent(app.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build();
        mainComponent.injectActivity(this);
        mOSUtils = app.getAppComponent().getOSUtils();
    }

    @AfterViews
    void afterViews() {
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        tvHistory.setText(R.string.lop_history);
        tvHistory.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/Lop\n\n");
        tvHistory.append(getString(R.string.lop_empty_result_hint));
    }

    @OptionsItem
    void actionSettings() {
        MobclickAgent.onEvent(this, "Setting");
        SettingActivity_.intent(this).start();
    }

    @Click
    public void btnStart() {
        MobclickAgent.onEvent(this, "Start Record");
        MediaProjectionManager manager =
                (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        Intent intent = manager.createScreenCaptureIntent();
        startActivityForResult(intent, REQUEST_CODE);
    }

    @OnActivityResult(REQUEST_CODE)
    void onResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent intent = RecordService_.newIntent(this, resultCode, data);
            startService(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

}
