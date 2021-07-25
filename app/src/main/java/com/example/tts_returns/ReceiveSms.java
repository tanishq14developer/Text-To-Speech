package com.example.tts_returns;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeechService;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Locale;

public class ReceiveSms extends BroadcastReceiver {
    TextToSpeech t1;

    @Override
    public void onReceive(Context context, Intent intent)  {

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage[] smsMessages = null;
            String smsStr = null;
            String msgbody;
            if (bundle !=null) {
                try {

                    Object[] pdus = (Object[]) bundle.get("pdus");
                    smsMessages = new SmsMessage[pdus.length];
                    for (int i = 0; i < smsMessages.length; i++) {
                        smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msgbody = smsMessages[i].getMessageBody();
                        Toast.makeText(context, "from: " + smsStr + ",Body" + msgbody, Toast.LENGTH_SHORT).show();
                        Intent intentdata = new Intent("otp");
                        intentdata.putExtra("message", msgbody);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intentdata);
                        MyService.enqueueWork(context, intentdata);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



}

