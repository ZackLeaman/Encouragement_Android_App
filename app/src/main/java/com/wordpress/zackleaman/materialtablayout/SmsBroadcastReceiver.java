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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Zack on 6/20/2016.
 * This class is for receiving sms messages through the broadcast receiver and displaying them in
 * the inbox tab.
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    @Override
    public void onReceive(Context context, Intent intent){
        // get the extras bundle from the intent
        Bundle intentExtras = intent.getExtras();
        SmsMessage smsMessage;

        if(intentExtras != null){
            // if the intent extras is not null get the SMS_BUNDLE and put it in an Object array
            //      SMS_BUNDLE = pdus
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";

            // if sms object array not null then get the sms message string
            if (sms != null) {
                for (Object sm : sms) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = intentExtras.getString("format");
                        smsMessage = SmsMessage.createFromPdu((byte[]) sm, format);
                    } else {
                        smsMessage = SmsMessage.createFromPdu((byte[]) sm);
                    }

                    // From the SmsMessage get the message body, address, and timestampMillis
                    String smsBody = smsMessage.getMessageBody();
                    String address = smsMessage.getOriginatingAddress();
                    long timeMillis = smsMessage.getTimestampMillis();

                    // Create a date and format it using the millis given
                    Date date = new Date(timeMillis);
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                    String dateText = format.format(date);

                    // If has a contact name from address
                    if (getContactName(context, address) != null) {
                        smsMessageStr += getContactName(context, address) + " on " + dateText +
                                "/n" + smsBody;
                    // If does not have a contact name from address
                    } else {
                        smsMessageStr += address + " on " + dateText + "/n" + smsBody;
                    }

                }
            }

            // Get the single instance of NewMessages and update its list because new SMS message
            NewMessages inst = NewMessages.instance();
            if(inst != null) {
                inst.updateList(smsMessageStr);
            }
        }
    }

    /**
     * This is used to get the contact name from a phone number address in the users contact list
     * @param context Context current of application
     * @param phoneNumber String phone number address that contact is under
     * @return contactName String that is the contact name that the phone number is under
     */
    public String getContactName(Context context, String phoneNumber) {
        // Make a uri from the phone number string given
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));

        // Make a content resolver from the context given to make a cursor
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(uri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        // If cursor is null then return null because problem making Cursor
        if (cursor == null) {
            return null;
        }

        // Get contactName but if not there then return null
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        // close the cursor
        if (!cursor.isClosed()) {
            cursor.close();
        }

        return contactName;

    }


}
