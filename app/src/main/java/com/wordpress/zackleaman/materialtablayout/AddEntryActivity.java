package com.wordpress.zackleaman.materialtablayout;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEntryActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    private Activity activity;
    private String category = "";
    private String nameAddressDate = "Me on ";
    private String message = "";
    private String subCategory = "";
    private String notifID = "";
    private String notifAlarmString = "";
    private int notID = 0;


    private TextView tvCategoryTitle;
    private EditText etMessage;
    private LinearLayout linearLayoutNotification;
    private Button btnNotificationFreq, btnNotificationDay;
    private TimePicker timePickerNotification;
    private Button btnCreate, btnCancel;
    private enum Notification{DAILY,WEEKLY,NONE}
    private enum NotificationDay{SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY}
    private Notification mNotification = Notification.NONE;
    private NotificationDay mNotificationDay = NotificationDay.SUNDAY;
    private boolean isNotifFreqActive, isNotifDayActive;
    private int hour, minute;
    private ArrayList<String> alarmList = new ArrayList<>();
    private ArrayList<String> encouragementList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ToolbarConfigurer(AddEntryActivity.this, toolbar, true);

        loadArray(encouragementList, getApplicationContext(), "encouragementList");
        loadArray(alarmList, getApplicationContext(), "alarmList");

        tvCategoryTitle = (TextView)findViewById(R.id.tvCategoryTitle);
        etMessage = (EditText)findViewById(R.id.etMessage);
        linearLayoutNotification = (LinearLayout)findViewById(R.id.linearLayoutNotification);
        btnNotificationFreq = (Button)findViewById(R.id.btnNotificationFreq);
        btnNotificationDay = (Button)findViewById(R.id.btnNotificationDay);
        timePickerNotification = (TimePicker)findViewById(R.id.timePickerNotification);
        btnCreate = (Button)findViewById(R.id.btnCreate);
        btnCancel = (Button)findViewById(R.id.btnCancel);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if (extras.containsKey("category_createEntry")) {
                category = getIntent().getStringExtra("category_createEntry");
            }
            if (extras.containsKey("subCategory_createEntry")) {
                subCategory = getIntent().getStringExtra("subCategory_createEntry");
            }

            String displayedCatTitle = category;
            if(!subCategory.isEmpty()) {
                displayedCatTitle = category + " - " + subCategory;
            }
            tvCategoryTitle.setText(displayedCatTitle);
        }

        btnNotificationFreq.setText("No Notification");
        btnNotificationDay.setText("Sunday");
        btnNotificationDay.setVisibility(View.GONE);
        timePickerNotification.setVisibility(View.GONE);
        if(category.equals("Encouragement")) {
            linearLayoutNotification.setVisibility(View.GONE);
        }

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String dateText = format.format(date);
        nameAddressDate = nameAddressDate + dateText;

        notID = findNextNotifID();
        notifID = Integer.toString(notID);

        btnNotificationFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNotifFreqActive = true;
                isNotifDayActive = false;
                showPopUp(view);
            }
        });
        btnNotificationDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNotifFreqActive = false;
                isNotifDayActive = true;
                showPopUp(view);
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavUtils.navigateUpFromSameTask(activity);
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etMessage.getText().toString().isEmpty()){
                    message = etMessage.getText().toString();
                    if (Build.VERSION.SDK_INT >= 23 ) {
                        hour = timePickerNotification.getHour();
                        minute = timePickerNotification.getMinute();
                    }else {
                        hour = timePickerNotification.getCurrentHour();
                        minute = timePickerNotification.getCurrentMinute();
                    }
                    String type = "Daily";
                    String amOrPmHour;
                    String amOrPm;
                    String stringMinute;
                    int dayNum = 1;
                    String day = "";
                    String pos = "0";

                    if(hour >= 12 && hour <= 23){
                        amOrPm = "PM";
                        if(hour != 12) {
                            amOrPmHour = Integer.toString(hour - 12);
                        }else{
                            amOrPmHour = Integer.toString(hour);
                        }
                    }else{
                        amOrPm = "AM";
                        if(hour == 0) {
                            amOrPmHour = Integer.toString(hour + 12);
                        }else{
                            amOrPmHour = Integer.toString(hour);
                        }
                    }
                    if(minute < 10){
                        stringMinute = "0"+Integer.toString(minute);
                    }else{
                        stringMinute = Integer.toString(minute);
                    }
                    switch (mNotificationDay){
                        case SUNDAY:
                            dayNum = 1;
                            day = "Sunday";
                            break;
                        case MONDAY:
                            dayNum = 2;
                            day = "Monday";
                            break;
                        case TUESDAY:
                            dayNum = 3;
                            day = "Tuesday";
                            break;
                        case WEDNESDAY:
                            dayNum = 4;
                            day = "Wednesday";
                            break;
                        case THURSDAY:
                            dayNum = 5;
                            day = "Thursday";
                            break;
                        case FRIDAY:
                            dayNum = 6;
                            day = "Friday";
                            break;
                        case SATURDAY:
                            dayNum = 7;
                            day = "Saturday";
                            break;
                    }
                    switch (mNotification){
                        case DAILY:
                            type = "Daily";
                            notifAlarmString = "Alarm Daily at " +
                                    amOrPmHour + ":" + stringMinute + " " + amOrPm;
                            break;
                        case WEEKLY:
                            type = "Weekly";
                            notifAlarmString = "Alarm Weekly on " + day + " " +
                                    amOrPmHour + ":" + stringMinute + " " + amOrPm;
                            break;
                        case NONE:
                            type = "None";
                            notifAlarmString = "Alarm Off";
                            break;
                    }

                    String fullEntry = "/n" + category + "/n" + nameAddressDate + "/n"
                            + message + "/n" + subCategory + "/n" + notifID + "/n" +
                            notifAlarmString;
                    encouragementList.add(0,fullEntry);
                    saveArray(encouragementList,"encouragementList");


                    if(!type.equals("None")){
                        setDailyAlarm(type,dayNum,hour,minute,notID,fullEntry,pos);
                    }

                    Toast.makeText(getApplicationContext(),"Entry added to " + tvCategoryTitle.getText().toString(),Toast.LENGTH_LONG).show();
                    NavUtils.navigateUpFromSameTask(activity);
                }else{
                    Toast.makeText(getApplicationContext(),"Please enter a message to create", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if(extras != null) {
            if (extras.containsKey("category_createEntry")) {
                category = intent.getStringExtra("category_createEntry");
            }
            if (extras.containsKey("subCategory_createEntry")) {
                subCategory = intent.getStringExtra("subCategory_createEntry");
            }

            String displayedCatTitle = category;
            if(!subCategory.isEmpty()) {
                displayedCatTitle = category + " - " + subCategory;
            }
            tvCategoryTitle.setText(displayedCatTitle);
        }
    }

    public void ToolbarConfigurer(Activity activity, Toolbar toolbar, boolean displayHomeAsUpEnabled) {
        toolbar.setTitle((this.activity = activity).getTitle());
        if (!displayHomeAsUpEnabled) return;
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //NavUtils.navigateUpFromSameTask(activity);
        switch (v.getId()) {
            default:
                //NavUtils.navigateUpFromSameTask(activity);
                Intent intent = new Intent(this,PermissionsActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,PermissionsActivity.class);
        startActivity(intent);
        finish();
    }

    private int findNextNotifID(){
        int notifID = 200;
        for(int i = 0; i < encouragementList.size(); i++){
            try{
                String[] entry = encouragementList.get(i).split("/n");
                if(!entry[5].equals("none")){
                    int num = Integer.parseInt(entry[5]);
                    if(num >= notifID){
                        notifID = num + 1;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return notifID;
    }

    public void showPopUp(View v){
        PopupMenu popupMenu = new PopupMenu(this,v,1,0,R.style.PopupMenu);

        popupMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = popupMenu.getMenuInflater();

        inflater.inflate(R.menu.popup_home_menu, popupMenu.getMenu());

        if(isNotifFreqActive){
            for(int i = 3; i <= 9; i++) {
                popupMenu.getMenu().getItem(i).setVisible(false);
            }
        }
        if(isNotifDayActive){
            for(int i = 0; i <= 2; i++) {
                popupMenu.getMenu().getItem(i).setVisible(false);
            }
        }



        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.popup_dailyNotification:
                if(isNotifFreqActive) {
                    mNotification = Notification.DAILY;
                    btnNotificationFreq.setText("Daily Notification");
                    btnNotificationDay.setVisibility(View.GONE);
                    timePickerNotification.setVisibility(View.VISIBLE);
                    isNotifFreqActive = false;
                }
                return true;
            case R.id.popup_weeklyNotification:
                if(isNotifFreqActive) {
                    mNotification = Notification.WEEKLY;
                    btnNotificationFreq.setText("Weekly Notification");
                    btnNotificationDay.setVisibility(View.VISIBLE);
                    timePickerNotification.setVisibility(View.VISIBLE);
                    isNotifFreqActive = false;
                }
                return true;
            case R.id.popup_noNotification:
                if(isNotifFreqActive) {
                    mNotification = Notification.NONE;
                    btnNotificationFreq.setText("No Notification");
                    btnNotificationDay.setVisibility(View.GONE);
                    timePickerNotification.setVisibility(View.GONE);
                    isNotifFreqActive = false;
                }

                return true;
            case R.id.popup_sunday:
                if(isNotifDayActive) {
                    btnNotificationDay.setText("Sunday");
                    mNotificationDay = NotificationDay.SUNDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_monday:
                if(isNotifDayActive) {
                    btnNotificationDay.setText("Monday");
                    mNotificationDay = NotificationDay.MONDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_tuesday:
                if(isNotifDayActive) {
                    btnNotificationDay.setText("Tuesday");
                    mNotificationDay = NotificationDay.TUESDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_wednesday:
                if(isNotifDayActive) {
                    btnNotificationDay.setText("Wednesday");
                    mNotificationDay = NotificationDay.WEDNESDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_thursday:
                if(isNotifDayActive) {
                    btnNotificationDay.setText("Thursday");
                    mNotificationDay = NotificationDay.THURSDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_friday:
                if(isNotifDayActive) {
                    btnNotificationDay.setText("Friday");
                    mNotificationDay = NotificationDay.FRIDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_saturday:
                if(isNotifDayActive) {
                    btnNotificationDay.setText("Saturday");
                    mNotificationDay = NotificationDay.SATURDAY;
                    isNotifDayActive = false;
                }
                return true;
            default:
                return false;
        }
    }

    public void setDailyAlarm(String notificationType, int day, int hour, int minute, int notifyID, String msg, String pos){
        String category = "";
        String nameAddressDate = "";
        String message = "";
        String subCategory = "";
        try{
            String[] entry = msg.split("/n");
            category = entry[1];
            nameAddressDate = entry[2];
            message = entry[3];
            subCategory = entry[4];
        }catch (Exception e){
            e.printStackTrace();
        }

        if(subCategory.equals("none")){
            subCategory = "";
        }else{
            subCategory = " - " + subCategory;
        }


        Calendar calendar = Calendar.getInstance();
        if(notificationType.equals("Weekly")) {
            calendar.set(Calendar.DAY_OF_WEEK, day);
        }

        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);


        Intent alertIntent = new Intent(this,AlertReceiver.class);
        alertIntent.putExtra("msg",msg);
        alertIntent.putExtra("msgCategory",category);
        alertIntent.putExtra("msgTitle", category + subCategory);
        alertIntent.putExtra("msgText",message);
        alertIntent.putExtra("msgAlert","New " + category + " Notification");
        alertIntent.putExtra("msgPos",pos);
        alertIntent.putExtra("notifyID",notifyID);
        alertIntent.putStringArrayListExtra("encouragementList",encouragementList);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),notifyID,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        try {
            loadArray(alarmList, getApplicationContext(), "alarmList");
            boolean newEntry = true;
            String[] mesEntry = msg.split("/n");
            String notIDMes = mesEntry[5];
            for (int j = 0; j < alarmList.size(); j++) {
                String[] entry = alarmList.get(j).split("/n");
                String notIDEnt = entry[5];
                if(notIDMes.equals(notIDEnt)){
                    String newMessage = msg + "/n" + Long.toString(calendar.getTimeInMillis());
                    alarmList.set(j,newMessage);
                    newEntry = false;
                }
            }
            if(newEntry){
                String newMessage = msg + "/n" + Long.toString(calendar.getTimeInMillis());
                alarmList.add(newMessage);
            }
            saveArray(alarmList,"alarmList");
        }catch (Exception e){
            e.printStackTrace();
        }

        AlarmManager alarmManager = (AlarmManager)
                this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        if(notificationType.equals("Daily")) {
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                    AlarmManager.INTERVAL_DAY, pendingIntent);

            // if calendar time has already passed then add a day to the time
            if(calendar.getTimeInMillis() < System.currentTimeMillis()){
                calendar.setTimeInMillis(calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY);
                //Toast.makeText(this,"Time Added",Toast.LENGTH_LONG).show();
            }

            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

        }else if(notificationType.equals("Weekly")){
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                    AlarmManager.INTERVAL_DAY*7, pendingIntent);

            // if calendar time has already passed then add a week to the time
            if(calendar.getTimeInMillis() < System.currentTimeMillis()){
                calendar.setTimeInMillis(calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY*7);
//                Toast.makeText(getContext(),"Time Added",Toast.LENGTH_LONG).show();
            }

            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }        }

    }

    private boolean saveArray(List<String> sKey, String arrayName){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor mEdit1=sp.edit();
        mEdit1.putInt("SMS_"+arrayName+"_size",sKey.size());

        for(int i=0;i<sKey.size();i++){
            mEdit1.remove("SMS_"+arrayName+i);
            mEdit1.putString("SMS_"+arrayName+i,sKey.get(i));
        }

        return mEdit1.commit();
    }

    private static void loadArray(List<String>sKey, Context mContext, String arrayName){
        SharedPreferences mSharedPreference1=PreferenceManager.getDefaultSharedPreferences(mContext);
        sKey.clear();
        int size=mSharedPreference1.getInt("SMS_"+arrayName+"_size",0);

        for(int i=0;i<size;i++){
            sKey.add(mSharedPreference1.getString("SMS_"+arrayName+i,null));
        }

    }
}
