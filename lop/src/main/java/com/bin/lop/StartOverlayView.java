package com.bin.lop;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import static android.graphics.PixelFormat.TRANSLUCENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;

/**
 * Created by jabin on 7/12/15.
 */
public class StartOverlayView extends FrameLayout implements View.OnClickListener {
    private static final int COUNTDOWN_DELAY = 1000;
    private static final int NON_COUNTDOWN_DELAY = 500;
    static SettingPref_ settingPref;
    StartListener listener;
    boolean showCountDown = true;
    TextView tvCountDown;
    int countdown = 2;

    public StartOverlayView(Context context, StartListener listener) {
        super(context);
        this.listener = listener;
        inflate(context, R.layout.overview, this);
        tvCountDown = (TextView) findViewById(R.id.tvCountDown);
        tvCountDown.setOnClickListener(this);
        showCountDown = settingPref.countDownTime().get();
    }

    public static View inflate(Context context, int resource, ViewGroup root) {
        LayoutInflater factory = LayoutInflater.from(context);
        return factory.inflate(resource, root);
    }

    static StartOverlayView create(Context context, StartListener listener, SettingPref_ settingPref_) {
        settingPref = settingPref_;
        return new StartOverlayView(context, listener);
    }

    static WindowManager.LayoutParams createLayoutParams(Context context) {

        final WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(MATCH_PARENT, WRAP_CONTENT, TYPE_SYSTEM_ERROR, FLAG_NOT_FOCUSABLE
                        | FLAG_NOT_TOUCH_MODAL
                        | FLAG_LAYOUT_NO_LIMITS
                        | FLAG_LAYOUT_INSET_DECOR
                        | FLAG_LAYOUT_IN_SCREEN, TRANSLUCENT);
        params.gravity = Gravity.CENTER;
        return params;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvCountDown) {
            if (tvCountDown.getBackground() != null) {
                tvCountDown.setTextSize(64);
                if (showCountDown) {
                    tvCountDown.setText(countdown + "...");
                }
                tvCountDown.setBackground(null);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (showCountDown) {
                            showCountDown();
                        } else {
                            countdownComplete();
                        }
                    }
                }, showCountDown ? COUNTDOWN_DELAY : NON_COUNTDOWN_DELAY);
            } else {
                listener.cancel();
            }
        }
    }

    private void showCountDown() {
        countdown(countdown, countdown); // array resource must not be empty
    }

    private void countdownComplete() {
        tvCountDown.animate()
                .alpha(0)
                .setDuration(COUNTDOWN_DELAY)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        startRecord();
                    }
                });
    }


    private void countdown(final int countdown, final int index) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index > 1 && index <= countdown) {
                    tvCountDown.setText(index - 1 + "...");
                    countdown(countdown, index - 1);
                } else {
                    countdownComplete();
                }
            }
        }, COUNTDOWN_DELAY);
    }


    private void startRecord() {
        listener.start();
    }

    interface StartListener {
        void start();

        void cancel();
    }
}
