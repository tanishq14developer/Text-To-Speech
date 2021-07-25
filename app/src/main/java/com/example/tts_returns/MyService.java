package com.example.tts_returns;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;

public class MyService extends JobIntentService {
    String name;
    private TextToSpeech mySpeakTextToSpeech = null;
    private boolean isSafeToDestroy = false;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, MyService.class, 1, intent);
    }
    public MyService() {
    }


    @Override
    protected void onHandleWork(@NonNull  Intent intent) {
        name = intent.getStringExtra("name");
        name = intent.getStringExtra("message");

        mySpeakTextToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {

                mySpeakTextToSpeech.speak(name, TextToSpeech.QUEUE_ADD, null, null);
                while (mySpeakTextToSpeech.isSpeaking()) {

                }
                isSafeToDestroy = true;
            }
        });
    }




    @Override
    public void onDestroy() {
        if (isSafeToDestroy) {
            if (mySpeakTextToSpeech != null) {
                mySpeakTextToSpeech.shutdown();
            }
            super.onDestroy();
        }
    }
}
