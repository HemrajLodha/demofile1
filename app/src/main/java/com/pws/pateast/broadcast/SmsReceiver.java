package com.pws.pateast.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Config;

import com.pws.pateast.Constants;
import com.pws.pateast.listener.SmsListener;

/**
 * Created by pws on 8/16/2017.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.
            if(sender.equalsIgnoreCase("AD-RINGCV")||sender.equalsIgnoreCase("HP-PLVSSMS")){
                String messageBody = smsMessage.getMessageBody();
                //Pass on the text to our listener.
                if(mListener!= null)
                    mListener.messageReceived(getVerificationCode(messageBody));
            }

        }

    }
    private String getVerificationCode(String message) {
        String code = null;
        int index = message.indexOf(Constants.OTP_DELIMITER);

        if (index != -1) {
            int start = index + 2;
            int length = 4;
            code = message.substring(start, start + length);
            return code;
        }

        return code;
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
