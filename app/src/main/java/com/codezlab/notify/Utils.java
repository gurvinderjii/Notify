package com.codezlab.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
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
    private static final String PREF_NAME = "MyAppPreference";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstLaunch";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public Utils(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void setIsFirstTimeLaunch(boolean isFirstLaunch){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH,isFirstLaunch);
        editor.apply();
    }

    public boolean isFirstLaunch(){
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH,true);
    }

    public static void showNotification(Context context, String title, String msg){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = ((BitmapDrawable)(ResourcesCompat.getDrawable(context.getResources(),R.drawable.icon,null))).getBitmap();
        Notification newMSG;
        long[] vibrate = {100,400,200,400};
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            newMSG = new Notification.Builder(context)
                    .setLargeIcon(largeIcon)
                    .setChannelId(CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.icon)
                    .build();
            vibrator.vibrate(vibrate,-1);
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
