package com.wordpress.zackleaman.materialtablayout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zack on 8/9/2016.
 */
public class StartMyServiceAtBootReceiver extends BroadcastReceiver {
    private ArrayList<String> alarmList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
//            Intent serviceIntent = new Intent("com.wordpress.zackleaman.materialtablayout.NotifyService");
//            context.startService(serviceIntent);
            loadArray(alarmList,context,"alarmList");
            for(int i = 0; i < alarmList.size(); i++){
                String[] entry = alarmList.get(i).split("/n");
                String category = entry[1];
                String from = entry[2];
                String msg = entry[3];
                String subCategory = entry[4];
                String notifID = entry[5];
                String notifString = entry[6];
                String time = entry[7];
                String fullMessage = "/n" + category + "/n" + from + "/n" +
                        msg + "/n" + subCategory + "/n" + notifID + "/n" + notifString;

                Intent alertIntent = new Intent(context,AlertReceiver.class);
                alertIntent.putExtra("msg",fullMessage);
                alertIntent.putExtra("msgCategory",category);
                alertIntent.putExtra("msgTitle", category + subCategory);
                alertIntent.putExtra("msgText",msg);
                alertIntent.putExtra("msgAlert","New " + category + " Notification");
                //alertIntent.putExtra("msgPos",pos);
                alertIntent.putExtra("notifyID",notifID);

                if(!notifString.equals("Alarm Off")){
                    try {
                        int notifyID = Integer.parseInt(notifID);
                        long timeLong = Long.parseLong(time);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,notifyID,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager)
                                context.getSystemService(Context.ALARM_SERVICE);

                        String[] type = notifString.split(" ");
                        if(type[1].equals("Daily")){
//                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeLong,
//                                    AlarmManager.INTERVAL_DAY, pendingIntent);
                            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                alarmManager.set(AlarmManager.RTC_WAKEUP, timeLong + AlarmManager.INTERVAL_DAY, pendingIntent);
                            } else {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeLong + AlarmManager.INTERVAL_DAY, pendingIntent);
                            }


                        }else if(type[1].equals("Weekly")){
//                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeLong,
//                                    AlarmManager.INTERVAL_DAY*7, pendingIntent);

                            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                alarmManager.set(AlarmManager.RTC_WAKEUP, timeLong + AlarmManager.INTERVAL_DAY*7, pendingIntent);
                            } else {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeLong + AlarmManager.INTERVAL_DAY*7, pendingIntent);
                            }

                        }



                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

        }
    }


    private static void loadArray(List<String> sKey, Context mContext, String arrayName){
        SharedPreferences mSharedPreference1= PreferenceManager.getDefaultSharedPreferences(mContext);
        sKey.clear();
        int size=mSharedPreference1.getInt("SMS_"+arrayName+"_size",0);

        for(int i=0;i<size;i++){
            sKey.add(mSharedPreference1.getString("SMS_"+arrayName+i,null));
        }

    }
}
