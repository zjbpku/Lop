package com.bin.lop;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.display.VirtualDisplay;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.WindowManager;

import com.bin.lop.common.NfsManager;
import com.bin.lop.common.OSUtils;
import com.bin.lop.common.ToastHelper;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION;
import static android.media.MediaRecorder.OutputFormat.MPEG_4;
import static android.media.MediaRecorder.VideoEncoder.H264;
import static android.media.MediaRecorder.VideoSource.SURFACE;
import static android.os.Environment.DIRECTORY_MOVIES;

/**
 * Created by jabin on 7/12/15.
 */

public class RecordHelper {

    private final DateFormat fileFormat = new SimpleDateFormat("'lop_'yyyy-MM-dd-HH-mm-ss'.mp4'", Locale.US);
    private final Handler mainThread = new Handler(Looper.getMainLooper());
    @Inject
    OSUtils mOUtils;
    @Inject
    ToastHelper mToastHelper;
    @Inject
    NfsManager nfsManager;
    SettingPref_ settingPref;
    private WindowManager windowManager;
    private Context mContext;
    private Listener listener;
    private StartOverlayView startOverlayView;
    private StopOverlayView stopOverlayView;
    private MediaProjectionManager projectionManager;
    private File outputDir;
    private int width;
    private int height;
    private int density;
    private int resultCode;
    private Intent data;
    private MediaRecorder recorder;
    private String outputFilePath;
    private MediaProjection projection;
    private VirtualDisplay display;
    private boolean running = false;

    @Inject
    RecordHelper(Context context) {
        mContext = context;
    }

    void initial(int resultCode, Intent data, Listener listener, SettingPref_ settingPref) {
        this.resultCode = resultCode;
        this.data = data;
        this.listener = listener;
        this.settingPref = settingPref;
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        projectionManager = (MediaProjectionManager) mContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        createFileDir();
    }

    private void createFileDir() {
        File picturesDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES);
        outputDir = new File(picturesDir, "Lop");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
    }

    public void showOverlayView() {
        StartOverlayView.StartListener listener = new StartOverlayView.StartListener() {
            @Override
            public void start() {
                startRecord();
            }

            @Override
            public void cancel() {
                stopRecord();
            }
        };
        startOverlayView = StartOverlayView.create(mContext, listener, settingPref);

        windowManager.addView(startOverlayView, StartOverlayView.createLayoutParams(mContext));
    }

    private void startRecord() {
        if (startOverlayView != null) {
            windowManager.removeView(startOverlayView);
            startOverlayView = null;
            StopOverlayView.StopListener stopListener = new StopOverlayView.StopListener() {
                @Override
                public void stop() {
                    stopRecord();
                }
            };
            stopOverlayView = StopOverlayView.create(mContext, stopListener, settingPref);
            windowManager.addView(stopOverlayView, StopOverlayView.createLayoutParams(mContext));
        }
        //start
        initialDisplay();
        initRecorder();
        projection = projectionManager.getMediaProjection(resultCode, data);

        Surface surface = recorder.getSurface();
        display = projection.createVirtualDisplay(mContext.getString(R.string.app_name), this.width, this.height,
                this.density, VIRTUAL_DISPLAY_FLAG_PRESENTATION, surface, null, null);
        recorder.start();
        running = true;
        listener.onStart();
    }

    private void removeOverlayView() {
        if (startOverlayView != null) {
            windowManager.removeView(startOverlayView);
            startOverlayView = null;
        }
        if (stopOverlayView != null) {
            windowManager.removeView(stopOverlayView);
            stopOverlayView = null;
        }
    }

    private void stopRecord() {
        if (!running) {
            return;
        }
        //stop
        running = false;
        removeOverlayView();
        projection.stop();
        recorder.stop();
        recorder.release();
        display.release();
        listener.onStop();
        scanFile();
    }

    private void scanFile() {
        MediaScannerConnection.scanFile(mContext, new String[]{outputFilePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, final Uri uri) {
                        mainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                boolean isShowLargebitmap = false;
                                if (settingPref.recordNotification().get()) {
                                    if (isShowLargebitmap) {
                                    } else {
                                        nfsManager.ShowRecoredViewNotification(uri, null, new File(outputFilePath).getName());
                                    }
                                }
                            }
                        });
                    }
                });
    }

    void initialDisplay() {
        DisplayMetrics displayMetrics = mOUtils.getDisplayMetrics();
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        int displayDensity = displayMetrics.densityDpi;

        Configuration configuration = mContext.getResources().getConfiguration();
        boolean isLandscape = configuration.orientation == ORIENTATION_LANDSCAPE;

        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        int cameraWidth = camcorderProfile != null ? camcorderProfile.videoFrameWidth : -1;
        int cameraHeight = camcorderProfile != null ? camcorderProfile.videoFrameHeight : -1;

        int sizePercentage = 100; //setting

        displayWidth = displayWidth * sizePercentage / 100;
        displayHeight = displayHeight * sizePercentage / 100;

        if (cameraWidth == -1 && cameraHeight == -1) {
            this.width = displayWidth;
            this.height = displayHeight;
            this.density = displayDensity;
            return;
        }

        int frameWidth = isLandscape ? cameraWidth : cameraHeight;
        int frameHeight = isLandscape ? cameraHeight : cameraWidth;
        if (frameWidth >= displayWidth && frameHeight >= displayHeight) {
            // Frame can hold the entire display. Use exact values.
            this.width = displayWidth;
            this.height = displayHeight;
            this.density = displayDensity;
            return;
        }

        // Calculate new width or height to preserve aspect ratio.
        if (isLandscape) {
            frameWidth = displayWidth * frameHeight / displayHeight;
        } else {
            frameHeight = displayHeight * frameWidth / displayWidth;
        }
        this.width = frameWidth;
        this.height = frameHeight;
        this.density = displayDensity;
    }

    private void initRecorder() {
        recorder = new MediaRecorder();
        recorder.setVideoSource(SURFACE);
        recorder.setOutputFormat(MPEG_4);
        recorder.setVideoFrameRate(30);
        recorder.setVideoEncoder(H264);
        recorder.setVideoSize(this.width, this.height);
        recorder.setVideoEncodingBitRate(8 * 1000 * 1000);

        String outputName = fileFormat.format(new Date());
        outputFilePath = new File(outputDir, outputName).getAbsolutePath();
        recorder.setOutputFile(outputFilePath);
        try {
            recorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException("Unable to prepare MediaRecorder.", e);
        }
    }


    public void onDestory() {
        if (running) {
            stopRecord();
        }
    }

    interface Listener {
        /**
         * Invoked immediately prior to the start of recording.
         */
        void onStart();

        /**
         * Invoked immediately after the end of recording.
         */
        void onStop();
    }

}
