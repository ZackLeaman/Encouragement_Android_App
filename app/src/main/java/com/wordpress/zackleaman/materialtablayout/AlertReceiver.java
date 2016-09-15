package com.wordpress.zackleaman.materialtablayout;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by Zack on 8/1/2016.
 */
public class AlertReceiver extends BroadcastReceiver {
    private ArrayList<String> encouragementList = new ArrayList<>();
    private ArrayList<String> alarmList = new ArrayList<>();
    private ArrayList<String> notificationEncouragementList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlertReceiver","in On Receive");
        loadArray(notificationEncouragementList,context,"notificationEncouragementList");

        String msgFull = intent.getStringExtra("msg");
        String msgTitle = intent.getStringExtra("msgTitle");
        String msgAlarmString = "Alarm Off";
        String alarmType = "Off";
        String category = "";
        boolean foundMessage = false;
        String encouragementType = loadString("encouragementType",context);

//        Toast.makeText(context,msgFull,Toast.LENGTH_LONG).show();

        if(intent.getStringExtra("msgCategory").equals("Encouragement")) {
            saveString("needUpdateNotificationEncouragement", context, "true");
        }
        try {
            // handle if entry has been moved
            loadArray(encouragementList, context, "encouragementList");
            if (!encouragementList.isEmpty()) {
                String[] msg = intent.getStringExtra("msg").split("/n");
                String msgCat = msg[1];
                String msgFrom = msg[2];
                String msgText = msg[3];
                String msgSubCat = msg[4];
                String msgNotifID = msg[5];
                String msgNotifString = msg[6];
                String msgCompare = msgCat + msgFrom + msgText + msgNotifID;

                for (int i = 0; i < encouragementList.size(); i++) {
                    String[] entry = encouragementList.get(i).split("/n");
                    String entryCat = entry[1];
                    String entryFrom = entry[2];
                    String entryText = entry[3];
                    String entrySubCat = entry[4];
                    String entryNotifID = entry[5];
                    String entryNotifString = entry[6];
                    String entryCompare = entryCat + entryFrom + entryText + entryNotifID;

                    if (msgCompare.equals(entryCompare)) {
                        foundMessage = true;
                        String[] type = entryNotifString.split(" ");
                        alarmType = type[1];
                        category = entryCat;
                        msgAlarmString = entryNotifString;
                        if (!msgSubCat.equals(entrySubCat)) {
                            msgSubCat = entrySubCat;
                            msgFull = "/n" + entryCat + "/n" + entryFrom + "/n" + entryText + "/n" +
                                    entrySubCat + "/n" + entryNotifID + "/n" + entryNotifString;
                            msgTitle = entryCat + " - " + entrySubCat;

                            i = encouragementList.size();
                        }
                    }
                }
            }else{
//                Toast.makeText(context,"encouragementList empty",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Either Message still part of encouragementList or we need to stop notification
        if(foundMessage) {
//            Toast.makeText(context,"in foundMessage",Toast.LENGTH_LONG).show();
//            Toast.makeText(context,category,Toast.LENGTH_LONG).show();
//            Toast.makeText(context,encouragementType,Toast.LENGTH_LONG).show();


            // reset the alarm
            if(category.equals("Encouragement")){
                if(!encouragementType.equals("Off")){
//                    Toast.makeText(context,"New Alarm Set",Toast.LENGTH_LONG).show();
                    createNotification(context,
                            msgFull,
                            intent.getStringExtra("msgCategory"),
                            msgTitle,
                            intent.getStringExtra("msgText"),
                            intent.getStringExtra("msgAlert"),
                            intent.getStringExtra("msgPos"),
                            intent.getIntExtra("notifyID", 999));

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor mEdit1 = sp.edit();
                    mEdit1.remove("homeEncouragement");
                    mEdit1.putString("homeEncouragement",msgFull);
                    mEdit1.commit();


                    notificationEncouragementList.remove(0);
                    if(notificationEncouragementList.isEmpty() || MainActivity.isFirstTimeOpening || notificationEncouragementList == null) {
                        Log.d("notificationEnc", "is empty");
                        Random r = new Random();
                        ArrayList<String> orderedList = new ArrayList<>();
                        for (int i = 0; i < encouragementList.size(); i++) {
                            try {
                                String[] entry = encouragementList.get(i).split("/n");
                                String entry_category = entry[1];

                                if (entry_category.equals("Encouragement")) {
                                    orderedList.add(encouragementList.get(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        int olSize = orderedList.size();
                        int i1;
                        while (olSize > 0) {
                            if (olSize > 1) {
                                i1 = r.nextInt(olSize);
                                notificationEncouragementList.add(0, orderedList.get(i1));
                                orderedList.remove(i1);
                            } else {
                                notificationEncouragementList.add(0, orderedList.get(0));
                                orderedList.remove(0);
                            }
                            olSize--;

                        }
                    }
                    setDailyAlarm(encouragementType,context,intent,notificationEncouragementList.get(0));
                    saveArray(notificationEncouragementList,context,"notificationEncouragementList");
                    Log.d("notifEncouragementList",notificationEncouragementList.toString());
                }
            }else {
                if (!alarmType.equals("Off")) {
                    setDailyAlarm(alarmType, context, intent, msgFull);
//                    Toast.makeText(context, "New Alarm Set", Toast.LENGTH_LONG).show();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    boolean wantsPrayerAndScripture = sp.getBoolean("wantsPrayerAndScripture",true);
                    if(wantsPrayerAndScripture) {
                        createNotification(context,
                                msgFull,
                                intent.getStringExtra("msgCategory"),
                                msgTitle,
                                intent.getStringExtra("msgText"),
                                intent.getStringExtra("msgAlert"),
                                intent.getStringExtra("msgPos"),
                                intent.getIntExtra("notifyID", 999));
                    }
                }
            }


            try {
                loadArray(alarmList, context, "alarmList");
                String[] mesEntry = msgFull.split("/n");
                String notIDMes = mesEntry[5];
                for (int j = 0; j < alarmList.size(); j++) {
                    String[] entry = alarmList.get(j).split("/n");
                    String notIDEnt = entry[5];
                    if(notIDMes.equals(notIDEnt)){
                        String newMessage = msgFull + "/n" + System.currentTimeMillis();
                        alarmList.set(j,newMessage);
                    }
                }
                saveArray(alarmList,context,"alarmList");
            }catch (Exception e){
                e.printStackTrace();
            }



        }else{
            // may not need the stop notification cause the alarm is being set manually
            stopNotification(intent.getIntExtra("notifyID", 999),context);

            try {
                loadArray(alarmList, context, "alarmList");
                String[] mesEntry = msgFull.split("/n");
                String notIDMes = mesEntry[5];
                for (int j = 0; j < alarmList.size(); j++) {
                    String[] entry = alarmList.get(j).split("/n");
                    String notIDEnt = entry[5];
                    if(notIDMes.equals(notIDEnt)){
                        alarmList.remove(j);
                    }
                }
                saveArray(alarmList,context,"alarmList");
            }catch (Exception e){
                e.printStackTrace();
            }
        }



    }

    public void createNotification(Context context, String msg, String msgCategory, String msgTitle,String msgText, String msgAlert, String msgPos, int notifyID){


        Intent intent = new Intent(context, CurrentEncouragement.class);

        //intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.removeExtra("msg");
        intent.removeExtra("msgCategory");
        intent.removeExtra("msgTitle");
        intent.removeExtra("msgText");
        intent.removeExtra("msgAlert");
        intent.removeExtra("msgPos");

        intent.putExtra("msg",msg);
        intent.putExtra("msgCategory", msgCategory);
        intent.putExtra("msgTitle",msgTitle);
        intent.putExtra("msgText",msgText);
        intent.putExtra("msgAlert",msgAlert);
        intent.putExtra("msgPos",msgPos);

        intent.setAction("com.wordpress.zackleaman.materialtablayout.intent.action.ACTION_NAME" + notifyID);

        PendingIntent notificIntent = PendingIntent.getActivity(context, notifyID,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setBigContentTitle(msgTitle);
        style.bigText(msgText);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(msgTitle);
        builder.setContentText(msgText);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker(msgAlert);


        builder.setStyle(style);

        builder.setContentIntent(notificIntent);


        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        builder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notifyID,builder.build());

    }


    public void setDailyAlarm(String notificationType, Context context, Intent intent, String msg){
        String category = "";
        String nameAddressDate = "";
        String message = "";
        String subCategory = "";
        int notifyID = 0;
        try{
            String[] entry = msg.split("/n");
            category = entry[1];
            nameAddressDate = entry[2];
            message = entry[3];
            subCategory = entry[4];
            notifyID = Integer.parseInt(entry[5]);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(subCategory.equals("none")){
            subCategory = "";
        }else{
            subCategory = " - " + subCategory;
        }

        PendingIntent oldPendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), notifyID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager oldAlarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        oldAlarmManager.cancel(oldPendingIntent);
        oldPendingIntent.cancel();

//        Calendar calendar = Calendar.getInstance();
//        if(notificationType.equals("Weekly")) {
//            calendar.set(Calendar.DAY_OF_WEEK, day);
//        }
//
//        calendar.set(Calendar.HOUR_OF_DAY,hour);
//        calendar.set(Calendar.MINUTE,minute);
//        calendar.set(Calendar.SECOND,0);


        Intent alertIntent = new Intent(context,AlertReceiver.class);
        alertIntent.putExtra("msg",msg);
        alertIntent.putExtra("msgCategory",category);
        alertIntent.putExtra("msgTitle", category + subCategory);
        alertIntent.putExtra("msgText",message);
        alertIntent.putExtra("msgAlert","New " + category + " Notification");
        alertIntent.putExtra("msgPos",0);
        alertIntent.putExtra("notifyID",notifyID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),notifyID,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        try {
            loadArray(alarmList, context, "alarmList");
            boolean newEntry = true;
            String[] mesEntry = msg.split("/n");
            String notIDMes = mesEntry[5];
            for (int j = 0; j < alarmList.size(); j++) {
                String[] entry = alarmList.get(j).split("/n");
                String notIDEnt = entry[5];
                if(notIDMes.equals(notIDEnt)){
                    String newMessage = msg + "/n" + Long.toString(System.currentTimeMillis());
                    alarmList.set(j,newMessage);
                    newEntry = false;
                }
            }
            if(newEntry){
                String newMessage = msg + "/n" + Long.toString(System.currentTimeMillis());
                alarmList.add(newMessage);
            }
            saveArray(alarmList,context,"alarmList");
        }catch (Exception e){
            e.printStackTrace();
        }

        AlarmManager alarmManager = (AlarmManager)
                context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        if(notificationType.equals("Daily")) {
//            alarmManager.set(AlarmManager.RTC_WAKEUP,
//                    calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);

            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);
            }

        }else if(notificationType.equals("Weekly")){
//            alarmManager.set(AlarmManager.RTC_WAKEUP,
//                    calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY*7, pendingIntent);
            //either set or setWindow and use the alarmList to determine the last time it fired


            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY*7, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY*7, pendingIntent);
            }
        }

    }

    public void stopNotification(int notifID,Context context){
        Intent intentstop = new Intent(context, AlertReceiver.class);
        PendingIntent senderstop = PendingIntent.getBroadcast(context,
                notifID, intentstop, 0);
        AlarmManager alarmManagerstop = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        alarmManagerstop.cancel(senderstop);
    }

    private boolean saveString(String stringName,Context mContext, String sKey){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        SharedPreferences.Editor mEdit1=sp.edit();
        mEdit1.remove("SMS_" + stringName);
        mEdit1.putString("SMS_" + stringName,sKey);
        return mEdit1.commit();
    }

    private String loadString(String stringName, Context mContext){
        String sKey = "";
        SharedPreferences mSharedPreference1=PreferenceManager.getDefaultSharedPreferences(mContext);
        sKey = mSharedPreference1.getString("SMS_" + stringName,"");
        return sKey;
    }


    private boolean saveArray(List<String> sKey, Context context, String arrayName){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEdit1=sp.edit();
        mEdit1.putInt("SMS_"+arrayName+"_size",sKey.size());

        for(int i=0;i<sKey.size();i++){
            mEdit1.remove("SMS_"+arrayName+i);
            mEdit1.putString("SMS_"+arrayName+i,sKey.get(i));
        }

        return mEdit1.commit();
    }

    private static void loadArray(List<String> sKey, Context mContext, String arrayName){
        SharedPreferences mSharedPreference1=PreferenceManager.getDefaultSharedPreferences(mContext);
        sKey.clear();
        int size=mSharedPreference1.getInt("SMS_"+arrayName+"_size",0);

        for(int i=0;i<size;i++){
            sKey.add(mSharedPreference1.getString("SMS_"+arrayName+i,null));
        }

    }
}
