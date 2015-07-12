package com.bin.lop;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bin.lop.component.ActivitySettingcomponent;
import com.bin.lop.component.DaggerActivitySettingcomponent;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


/**
 * Created by jabin on 7/5/15.
 */
@EActivity(R.layout.activity_setting)
public class SettingActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    ActivitySettingcomponent settingComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
    }

    private void inject() {
        App app = (App) getApplication();
        settingComponent = DaggerActivitySettingcomponent.builder()
                .applicationComponent(app.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build();
        settingComponent.injectActivity(this);
    }


    @AfterViews
    void afterViews() {
        toolbar.setTitle(R.string.action_settings);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        setToolbarListener();
        getFragmentManager().beginTransaction().replace(R.id.fragemnt_container, SettingFragment_.builder().build()).commit();
    }


    private void setToolbarListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });
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
