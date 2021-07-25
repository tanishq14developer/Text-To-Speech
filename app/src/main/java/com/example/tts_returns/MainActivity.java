package com.example.tts_returns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String getText;
    String messageSMS;
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS)
        != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},1000);
        }

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status !=TextToSpeech.ERROR){
                    int result = t1.setLanguage(Locale.getDefault());

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS", "Language not Supported ");
                    }
                    else {
                    }
                }else {
                    Log.e("TTS","Initilization failed");
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Permission granted!",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Permission not granted!",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equalsIgnoreCase("otp")){
                messageSMS = intent.getStringExtra("message");
                Log.e(MainActivity.class.getSimpleName(), "onReceive: "+messageSMS );
                Log.e("Tag","Print the msg"+ messageSMS);
                Log.e(MainActivity.class.getSimpleName(), "onClick: "+getText+ "" );


            }
        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,new IntentFilter("otp"));
        super.onPause();
    }

    @Override
    protected void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,new IntentFilter("otp"));
        super.onStart();
    }
    @Override
    protected void onDestroy() {


        if (t1 != null) {
            Log.i("TTS", "speech on destroy");
        }
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}