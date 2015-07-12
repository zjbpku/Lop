package com.bin.lop.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.bin.lop.R;

import javax.inject.Inject;

import static android.content.Intent.ACTION_VIEW;

/**
 * Created by jabin on 7/12/15.
 */
public class NfsManager {
    private static final int NOTIFICATION_ID = 20150712;
    @Inject
    BitmapHelper mBitmapHelper;
    private Context mContext;
    private NotificationManager notificationManager;

    @Inject
    NfsManager(Context context) {
        mContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private Notification.Builder getBaseBuilder() {
        Notification.Builder builder = new Notification.Builder(mContext) //
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.logo)
                .setColor(mContext.getResources().getColor(R.color.primary_material_light));
        return builder;
    }

    public void ShowRecoredViewNotification(Uri uri, Bitmap bitmap, String filenName) {
        Intent viewIntent = new Intent(ACTION_VIEW, uri);
        PendingIntent pendingViewIntent = PendingIntent.getActivity(mContext, 0, viewIntent, 0);

        Notification.Builder builder = getBaseBuilder();
        String title = mContext.getResources().getString(R.string.lop_ntf_title_record_finish);
        builder.setContentTitle(title);
        builder.setContentText(filenName);
        builder.setContentIntent(pendingViewIntent);
        if (bitmap != null) {
            builder.setLargeIcon(mBitmapHelper.createSquareBitmap(bitmap)) //
                    .setStyle(new Notification.BigPictureStyle() //
                            .setBigContentTitle(title)
                            .setSummaryText(filenName)
                            .bigPicture(bitmap));
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
