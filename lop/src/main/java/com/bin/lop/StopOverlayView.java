package com.bin.lop;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import static android.graphics.PixelFormat.TRANSLUCENT;
import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;

/**
 * Created by jabin on 7/12/15.
 */
public class StopOverlayView extends FrameLayout implements View.OnClickListener {

    static SettingPref_ settingPref;
    StopListener listener;
    TextView tvStop;
    int animationWidth;

    public StopOverlayView(Context context, StopListener listener) {
        super(context);
        this.listener = listener;
        inflate(context, R.layout.stop_view, this);
        int width = context.getResources().getDimensionPixelOffset(R.dimen.overlay_width);
        animationWidth = width;
        tvStop = (TextView) findViewById(R.id.Stop);
        tvStop.setOnClickListener(this);
        tvStop.setWidth(width);
        if (settingPref.stopVisable().get()) {
            tvStop.setText(context.getResources().getString(R.string.lop_stop));
        }
    }

    public static View inflate(Context context, int resource, ViewGroup root) {
        LayoutInflater factory = LayoutInflater.from(context);
        return factory.inflate(resource, root);
    }

    static StopOverlayView create(Context context, StopListener listener, SettingPref_ settingPref_) {
        settingPref = settingPref_;
        return new StopOverlayView(context, listener);
    }


    static WindowManager.LayoutParams createLayoutParams(Context context) {
        Resources res = context.getResources();
        int width = res.getDimensionPixelSize(R.dimen.overlay_width);
        int height = res.getDimensionPixelSize(R.dimen.overlay_height);
        // TODO Remove explicit "M" comparison when M is released.
        if (Build.VERSION.SDK_INT > LOLLIPOP_MR1 || "M".equals(Build.VERSION.RELEASE)) {
            height = res.getDimensionPixelSize(R.dimen.overlay_height_m);
        }

        final WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(width, height, TYPE_SYSTEM_ERROR, FLAG_NOT_FOCUSABLE
                        | FLAG_NOT_TOUCH_MODAL
                        | FLAG_LAYOUT_NO_LIMITS
                        | FLAG_LAYOUT_INSET_DECOR
                        | FLAG_LAYOUT_IN_SCREEN, TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        return params;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setTranslationX(animationWidth);
        animate().translationX(0)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator());
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Stop) {
            listener.stop();
        }
    }

    interface StopListener {
        void stop();
    }
}
