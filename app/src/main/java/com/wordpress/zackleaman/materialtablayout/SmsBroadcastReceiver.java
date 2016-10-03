package com.wordpress.zackleaman.materialtablayout;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Zack on 6/20/2016.
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    @Override
    public void onReceive(Context context, Intent intent){
        Bundle intentExtras = intent.getExtras();
        SmsMessage[] smsMessage = null;

        if(intentExtras != null){
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            smsMessage = new SmsMessage[sms.length];

            for(int i = 0; i < sms.length; i++){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String format = intentExtras.getString("format");
                    smsMessage[i] = SmsMessage.createFromPdu((byte[]) sms[i], format);
                } else {
                    smsMessage[i] = SmsMessage.createFromPdu((byte[]) sms[i]);
                }
                //SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage[i].getMessageBody().toString();
                String address = smsMessage[i].getOriginatingAddress();
                long timeMillis = smsMessage[i].getTimestampMillis();

                Date date = new Date(timeMillis);
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                String dateText = format.format(date);

                smsMessageStr += getContactName(context , address) + "\n" + address + " at " + dateText + "\n";
                smsMessageStr += smsBody + "\n";

            }

            //Toast.makeText(context,smsMessageStr, Toast.LENGTH_LONG).show();

            NewMessages inst = NewMessages.instance().instance();
            if(inst != null) {
                inst.updateList(smsMessageStr);
            }else{
                Log.d("inst.updateList","updateList null yoyo");
            }
        }
    }

    public String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri,
                new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }


}
