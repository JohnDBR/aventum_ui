package com.projects.juan.journeys.modules;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.projects.juan.journeys.activities.SignupActivity;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_CONDITION = "Some condition";
    private static final String TAG = "SmsBroadcastReceiver";
    private SmsCode smsCode;

    public SmsBroadcastReceiver(){
        this.smsCode = new SmsCode();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String smsSender = "";
            String smsBody = "";
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsBody += smsMessage.getMessageBody();
            }

            if (smsBody.startsWith(SMS_CONDITION)) {
                Log.d(TAG, "Sms with condition detected");
                Toast.makeText(context, "BroadcastReceiver caught conditional SMS: " + smsBody, Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, "SMS detected: From " + smsSender + " With text " + smsBody);
            smsCode.setSmsCode(smsBody.split(" ")[2]);
        }
    }

    public SmsCode getSmsCode(){
        return this.smsCode;
    }
}


