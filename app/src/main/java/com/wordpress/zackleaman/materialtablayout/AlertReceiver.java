package com.wordpress.zackleaman.materialtablayout;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by Zack on 8/1/2016.
 * This class extends the broadcast receiver and handles when alarm alert received
 */
public class AlertReceiver extends BroadcastReceiver {
    private ArrayList<String> encouragementList = new ArrayList<>();
    private ArrayList<String> alarmList = new ArrayList<>();
    private ArrayList<String> notificationEncouragementList = new ArrayList<>();

    /**
     * When Alarm goes off this method is called
     * @param context context of app
     * @param intent intent from which this was called from
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        // load the notificationEncouragementList to make sure if cant find the first encouragement
        //      then go to the next one that is actually in encouragement list
        loadArray(notificationEncouragementList,context,"notificationEncouragementList");

        String msgFull = intent.getStringExtra("msg");
        String msgTitle = intent.getStringExtra("msgTitle");
        String msgAlarmString = "Alarm Off";
        String alarmType = "Off";
        String category = "";
        boolean foundMessage = false;
        String encouragementType = loadString("encouragementType",context);

        // if the entry is a encouragement reminder then send reminder notification and create new
        //      alarm
        if(intent.getStringExtra("msgCategory").equals("Reminder")){
            createNotification(context,
                    msgFull,
                    intent.getStringExtra("msgCategory"),
                    msgTitle,
                    intent.getStringExtra("msgText"),
                    intent.getStringExtra("msgAlert"),
                    intent.getStringExtra("msgPos"),
                    intent.getIntExtra("notifyID", 999));

            setDailyAlarm("Weekly",context,intent,"Have you encouraged someone this week?" +
                    "/nnone/n4/nAlarm Weekly");

        }

        // if this entry alarmed is an encouragement then notification list needs to be updated
        if(intent.getStringExtra("msgCategory").equals("Encouragement")) {
            saveString("needUpdateNotificationEncouragement", context, "true");
        }

        try {
            // handle if entry has been moved
            loadArray(encouragementList, context, "encouragementList");
            try{
                if(intent.getStringExtra("encouragementList") != null) {
                    encouragementList = intent.getStringArrayListExtra("encouragementList");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            if (!encouragementList.isEmpty()) {
                // break the current message into its main parts
                String[] msg = intent.getStringExtra("msg").split("/n");
                String msgCat = msg[1];
                String msgFrom = msg[2];
                String msgText = msg[3];
                String msgSubCat = msg[4];
                String msgNotifID = msg[5];
                String msgNotifString = msg[6];
                // create a msg string to compare without sub cat because it can change
                String msgCompare = msgCat + msgFrom + msgText + msgNotifID;

                // check the encouragement list to make sure user has not deleted entry in between
                //      alarm time
                for (int i = 0; i < encouragementList.size(); i++) {
                    String[] entry = encouragementList.get(i).split("/n");
                    String entryCat = entry[1];
                    String entryFrom = entry[2];
                    String entryText = entry[3];
                    String entrySubCat = entry[4];
                    String entryNotifID = entry[5];
                    String entryNotifString = entry[6];
                    // create an entry string to compare without sub cat because it can change
                    String entryCompare = entryCat + entryFrom + entryText + entryNotifID;

                    // if message is found in encouragement list then it is good to display it
                    if (msgCompare.equals(entryCompare)) {
                        foundMessage = true;
                        // check that the notification string is not 'none'
                        if(!entryNotifString.equals("none")) {
                            // set alarm to either daily or weekly if not none
                            String[] type = entryNotifString.split(" ");
                            alarmType = type[1];
                        }else{
                            // set alarm to off if none
                            alarmType = "Off";
                        }
                        category = msgCat;
                        msgAlarmString = entryNotifString;
                        if (!msgSubCat.equals(entrySubCat)) {
                            msgSubCat = entrySubCat;

                            // create the full message string and message title to show on alarm notification
                            msgFull = "/n" + entryCat + "/n" + entryFrom + "/n" + entryText + "/n" +
                                    entrySubCat + "/n" + entryNotifID + "/n" + entryNotifString;
                            msgTitle = entryCat + " - " + entrySubCat;

                            // exit out of for loop because found the message
                            i = encouragementList.size();
                        }


                    }
                }

                // If NotifEncouragement not found go to the next until found
                if(!foundMessage){
                    if(msgCat.equals("Encouragement")){
                        int remove = -1;
                        if(!notificationEncouragementList.isEmpty() && !encouragementList.isEmpty()) {
                            for (int x = 0; x < notificationEncouragementList.size(); x++) {
                                String[] notifMsg = notificationEncouragementList.get(x).split("/n");
                                String notifMsgCat = notifMsg[1];
                                String notifMsgFrom = notifMsg[2];
                                String notifMsgText = notifMsg[3];
                                String notifMsgSubCat = notifMsg[4];
                                String notifMsgNotifID = notifMsg[5];
                                String notifMsgNotifString = notifMsg[6];
                                String notifMsgCompare = notifMsgCat + notifMsgFrom + notifMsgText + notifMsgNotifID;

                                for (int i = 0; i < encouragementList.size(); i++) {
                                    String[] entry = encouragementList.get(i).split("/n");
                                    String entryCat = entry[1];
                                    String entryFrom = entry[2];
                                    String entryText = entry[3];
                                    String entrySubCat = entry[4];
                                    String entryNotifID = entry[5];
                                    String entryNotifString = entry[6];
                                    String entryCompare = entryCat + entryFrom + entryText + entryNotifID;

                                    if (notifMsgCompare.equals(entryCompare)) {
                                        foundMessage = true;
                                        if (!entryNotifString.equals("none")) {
                                            String[] type = entryNotifString.split(" ");
                                            alarmType = type[1];
                                        } else {
                                            alarmType = "Off";
                                        }
                                        category = msgCat;
                                        msgAlarmString = entryNotifString;
                                        if (!msgSubCat.equals(entrySubCat)) {
                                            msgSubCat = entrySubCat;
                                            msgFull = "/n" + entryCat + "/n" + entryFrom + "/n" + entryText + "/n" +
                                                    entrySubCat + "/n" + entryNotifID + "/n" + entryNotifString;
                                            msgTitle = entryCat + " - " + entrySubCat;

                                        }
                                        i = encouragementList.size();
                                        x = notificationEncouragementList.size();

                                    }

                                }

                                remove++;
                            }

                            for(int r = remove; r >= 0; r--){
                                notificationEncouragementList.remove(r);
                            }

                            saveArray(notificationEncouragementList,context,"notificationEncouragementList");



                        }
                        // If still not found then grab the first encouragement from list
                        if(!foundMessage && !encouragementList.isEmpty()){
                            for (int i = 0; i < encouragementList.size(); i++) {
                                String[] entry = encouragementList.get(i).split("/n");
                                String entryCat = entry[1];
                                String entrySubCat = entry[4];

                                if(entryCat.equals("Encouragement")){
                                    foundMessage = true;
                                    msgSubCat = entrySubCat;
                                    msgFull = encouragementList.get(i);
                                    msgTitle = entryCat;
                                    i = encouragementList.size();
                                }
                            }

                        }


                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Either Message still part of encouragementList or we need to stop notification
        if(foundMessage) {

            // reset the alarm
            if(category.equals("Encouragement")){
                if(!encouragementType.equals("Off")){

                    createNotification(context,
                            msgFull,
                            intent.getStringExtra("msgCategory"),
                            msgTitle,
                            intent.getStringExtra("msgText"),
                            intent.getStringExtra("msgAlert"),
                            intent.getStringExtra("msgPos"),
                            intent.getIntExtra("notifyID", 999));

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                    SharedPreferences.Editor mEdit1 = sp.edit();
                    mEdit1.remove("homeEncouragement");
                    mEdit1.putString("homeEncouragement",msgFull);
                    mEdit1.commit();

                    if(!notificationEncouragementList.isEmpty()) {
                        notificationEncouragementList.remove(0);
                    }
                    if(notificationEncouragementList.isEmpty()) {
                        // || MainActivity.isFirstTimeOpening || notificationEncouragementList == null
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
                }
            }else {
                if (!alarmType.equals("Off")) {
                    setDailyAlarm(alarmType, context, intent, msgFull);
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

    /**
     * This method builds the notification and then sends it to the user
     * @param context Context from which to build intent
     * @param msg String full notification message
     * @param msgCategory String message category
     * @param msgTitle String message title
     * @param msgText String message text to display on the notification
     * @param msgAlert String message alert that can be used to reset alarm
     * @param msgPos String message position in encouragement list
     * @param notifyID int unique notification id
     */
    public void createNotification(Context context, String msg, String msgCategory,
                                   String msgTitle,String msgText, String msgAlert,
                                   String msgPos, int notifyID){

        // Create new intent to go to the current encouragement sent through notification
        Intent intent = new Intent(context, CurrentEncouragement.class);

        // If message is a reminder then go to main activity instead of current
        if(msgCategory.equals("Reminder")) {
            intent = new Intent(context, MainActivity.class);
        }

        // Add intent flags for single top
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP|
                Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        // put all data to be sent to the new intent activity
        intent.putExtra("msg",msg);
        intent.putExtra("msgCategory", msgCategory);
        intent.putExtra("msgTitle",msgTitle);
        intent.putExtra("msgText",msgText);
        intent.putExtra("msgAlert",msgAlert);
        intent.putExtra("msgPos",msgPos);
        if(msgCategory.equals("Encouragement")) {
            intent.putExtra("homeEncouragement", msg);
        }

        // make a unique set action to ensure activity has new data
        intent.setAction("com.wordpress.zackleaman.materialtablayout.intent.action.ACTION_NAME" + notifyID);

        // Build notification and send it through notification manager
        PendingIntent notificIntent = PendingIntent.getActivity(context, notifyID,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);


        Notification.BigTextStyle style = new Notification.BigTextStyle();
        style.setBigContentTitle(msgTitle);
        style.bigText(msgText);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(msgTitle);
        builder.setContentText(msgText);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker(msgAlert);


        builder.setStyle(style);

        builder.setContentIntent(notificIntent);


        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notifyID,builder.build());

    }

    /**
     * This sets the alarm for the particular entry or for all encouragements
     * @param notificationType string that represents type of notification
     * @param context Context to send the alarm from
     * @param intent Intent to send the alarm from
     * @param msg string message to send as the notification text
     */
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

            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);
            }

        }else if(notificationType.equals("Weekly")){

            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY*7, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY*7, pendingIntent);
            }
        }

    }

    /**
     * Stops the notification if needed
     * @param notifID int unique notification id used
     * @param context Context to stop the notification
     */
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
