package com.codezlab.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.content.res.ResourcesCompat;

public class Utils {
    public static String TAG = "TOKEN";
    public static String CHANNEL_ID = "Messages";
    public static int NOTIFICATION_ID = 100;

    public static void showNotification(Context context, String title, String msg){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = ((BitmapDrawable)(ResourcesCompat.getDrawable(context.getResources(),R.drawable.icon,null))).getBitmap();
        Notification newMSG;
        long[] vibrate = {0, 200,500, 200};
        VibrationEffect vibrationEffect = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrationEffect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
        }
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            newMSG = new Notification.Builder(context)
                    .setLargeIcon(largeIcon)
                    .setChannelId(CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.icon)
                    .build();
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(vibrationEffect);
            }
            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"Messages",NotificationManager.IMPORTANCE_HIGH));
        }else {
            newMSG = new Notification.Builder(context)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.icon)
                    .setVibrate(vibrate)
                    .build();
        }
        manager.notify(NOTIFICATION_ID,newMSG);
    }
}
