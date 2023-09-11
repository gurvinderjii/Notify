package com.codezlab.notify;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MainActivity extends AppCompatActivity {
    String[] permissions = {"android.permission.POST_NOTIFICATIONS"};
    private boolean isNotificationPermissionGranted = false;
    Button btn;
    RelativeLayout main;
    TextView title, message;
    public static String token;

    private Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        main = findViewById(R.id.main);
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Failed to get token", Toast.LENGTH_SHORT).show();
                                    Log.d("TOKEN","Error to get token");
                                    return;
                                }
                                Log.d("TOKEN",task.getResult());
                                token = task.getResult();
                            }
                        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions,80);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()){
                    title.setError("Set title");
                    return;
                }
                if (message.getText().toString().isEmpty()){
                    message.setError("Enter message");
                    return;
                }
                Utils.showNotification(MainActivity.this,title.getText().toString(),message.getText().toString());
                title.setText("");
                message.setText("");
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyborad(v);
                title.clearFocus();
                message.clearFocus();
            }
        });
        utils = new Utils(MainActivity.this);
    }

    public void hideKeyborad(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 80){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                isNotificationPermissionGranted = true;
                Log.d("Permission: ","Accessed");
                if(utils.isFirstLaunch()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showNotification(MainActivity.this,"Welcome to notify","This app is designed by Gurvinder Singh.");
                        }
                    },2000);
                    utils.setIsFirstTimeLaunch(false);
                }

            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissions,80);
                    Log.d("Permission: ","Denied");
                }
            }
        }
    }
    public static class MyFirebaseMessaging extends FirebaseMessagingService {
        @Override
        public void onNewToken(@NonNull String token) {
            super.onNewToken(token);
            Log.d("Token","Refreshed token: "+ token);
        }
        @Override
        public void onMessageReceived(@NonNull RemoteMessage message) {
            super.onMessageReceived(message);
            Utils.showNotification(this,message.getNotification().getTitle(),message.getNotification().getBody());
        }
    }
}