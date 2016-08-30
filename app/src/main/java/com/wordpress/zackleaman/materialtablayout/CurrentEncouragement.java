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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by Zack on 7/28/2016.
 */
public class CurrentEncouragement extends AppCompatActivity implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener{
    private Activity activity;
    private TextView tvEncouragementMessage,tvEncouragementFrom;
    private ArrayList<String> selectedEncouragement, selectedBackground, encouragementList, categoriesList, categoriesListPrayer, categoriesListScripture;
    private RelativeLayout entryRelativeLayout;
    private ImageButton btnAriedLandscape, btnBrickwall, btnCity, btnMountains1, btnMountains2,
                        btnRoof, btnSea1, btnSea2, btnSea3, btnSky1, btnSky2, btnSunset, btnTarp, btnWheatfield,
                        btnCity2, btnCrumbledPaper,btnForest,btnHutWall,btnSea4,btnShore,btnTrain,btnTropic2,btnWood;
    private Button btnClose, btnCyclePhotos;
    private HorizontalScrollView hsv;
    private String fullEntry;
    private String category,from,message,subCategory, notifID, notifInfoString;
    private int mainCategory;

    private TextView tvNotification;
    private Button btnInfoNotification;
    private ScrollView svNotificationDetails;
    private LinearLayout linearLayoutNotification;
    private Button btnNotificationFreq, btnDayWeekNotification, btnSaveNotification;
    private TimePicker timePickerNotification;
    private RelativeLayout relativeLayoutNotification;
    private enum Notification{DAILY,WEEKLY,NONE}
    private enum NotificationDay{SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY}
    private Notification mNotification;
    private NotificationDay mNotificationDay;
    private boolean isInfoActive, isNotifFreqActive, isNotifDayActive;
    private int hour, minute;

    private ArrayList<String> alarmList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onNewIntent(getIntent());

    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setContentView(R.layout.activity_custom_encouragement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ToolbarConfigurer(CurrentEncouragement.this, toolbar, true);
        fullEntry = "";
        category = "";
        from = "";
        message = "";
        subCategory = "";
        notifID = "";
        notifInfoString = "";
        selectedEncouragement = new ArrayList<>();
        encouragementList = new ArrayList<>();
        categoriesList = new ArrayList<>();
        categoriesListPrayer = new ArrayList<>();
        categoriesListScripture = new ArrayList<>();
        tvEncouragementMessage = (TextView)findViewById(R.id.tvEncouragementMessage);
        tvEncouragementFrom = (TextView)findViewById(R.id.tvEncouragementFrom);
        btnAriedLandscape = (ImageButton)findViewById(R.id.btn_ariedlandscape_small);
        btnBrickwall  = (ImageButton)findViewById(R.id.btn_brickwall_small);
        btnCity  = (ImageButton)findViewById(R.id.btn_city_small);
        btnMountains1  = (ImageButton)findViewById(R.id.btn_mountains1_small);
        btnMountains2 = (ImageButton)findViewById(R.id.btn_mountains2_small);
        btnRoof  = (ImageButton)findViewById(R.id.btn_roof_small);
        btnSea1  = (ImageButton)findViewById(R.id.btn_sea1_small);
        btnSea2  = (ImageButton)findViewById(R.id.btn_sea2_small);
        btnSea3  = (ImageButton)findViewById(R.id.btn_sea3_small);
        btnSky1  = (ImageButton)findViewById(R.id.btn_sky1_small);
        btnSky2  = (ImageButton)findViewById(R.id.btn_sky2_small);
        btnSunset  = (ImageButton)findViewById(R.id.btn_sunset_small);
        btnTarp  = (ImageButton)findViewById(R.id.btn_tarp_small);
        btnWheatfield = (ImageButton)findViewById(R.id.btn_wheatfield_small);
        //                        btnCity2, btnCrumbledPaper,btnForest,btnHutWall,btnSea4,btnShore,btnTrain,btnTropic2,btnWood;
        btnCity2 = (ImageButton)findViewById(R.id.btn_city_2);
        btnCrumbledPaper = (ImageButton)findViewById(R.id.btn_crumbled_paper);
        btnForest = (ImageButton)findViewById(R.id.btn_forest);
        btnHutWall = (ImageButton)findViewById(R.id.btn_hut_wall);
        btnSea4 = (ImageButton)findViewById(R.id.btn_sea4);
        btnShore = (ImageButton)findViewById(R.id.btn_shore);
        btnTrain = (ImageButton)findViewById(R.id.btn_train);
        btnTropic2 = (ImageButton)findViewById(R.id.btn_tropic2);
        btnWood = (ImageButton)findViewById(R.id.btn_wood);

        btnClose = (Button)findViewById(R.id.btnClose);
        btnCyclePhotos = (Button)findViewById(R.id.btn_cycle_pictures);
        btnAriedLandscape.setOnClickListener(this);
        btnBrickwall.setOnClickListener(this);
        btnCity.setOnClickListener(this);
        btnMountains1.setOnClickListener(this);
        btnMountains2.setOnClickListener(this);
        btnRoof.setOnClickListener(this);
        btnSea1.setOnClickListener(this);
        btnSea2.setOnClickListener(this);
        btnSea3.setOnClickListener(this);
        btnSky1.setOnClickListener(this);
        btnSky2.setOnClickListener(this);
        btnSunset.setOnClickListener(this);
        btnTarp.setOnClickListener(this);
        btnWheatfield.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnCyclePhotos.setOnClickListener(this);
        btnCity2.setOnClickListener(this);
        btnCrumbledPaper.setOnClickListener(this);
        btnForest.setOnClickListener(this);
        btnHutWall.setOnClickListener(this);
        btnSea4.setOnClickListener(this);
        btnShore.setOnClickListener(this);
        btnTrain.setOnClickListener(this);
        btnTropic2.setOnClickListener(this);
        btnWood.setOnClickListener(this);

        hsv = (HorizontalScrollView)findViewById(R.id.hsv);
        hsv.setVisibility(View.INVISIBLE);
        btnClose.setVisibility(View.GONE);

        //Notification setup
        tvNotification = (TextView)this.findViewById(R.id.tvNotification);
        btnInfoNotification = (Button)this.findViewById(R.id.btnInfoNotification);
        //svNotificationDetails = (ScrollView)this.findViewById(R.id.svNotificationDetails);
        linearLayoutNotification = (LinearLayout)this.findViewById(R.id.linearLayoutNotification);
        btnNotificationFreq = (Button)this.findViewById(R.id.btnNotificationFreq);
        btnDayWeekNotification = (Button)this.findViewById(R.id.btnDayWeekNotification);
        btnSaveNotification = (Button)this.findViewById(R.id.btnSaveNotification);
        timePickerNotification = (TimePicker)this.findViewById(R.id.timePickerNotification);
        relativeLayoutNotification = (RelativeLayout)this.findViewById(R.id.relativeLayoutNotification);
        btnInfoNotification.setOnClickListener(CurrentEncouragement.this);
        btnNotificationFreq.setOnClickListener(CurrentEncouragement.this);
        btnDayWeekNotification.setOnClickListener(CurrentEncouragement.this);
        btnSaveNotification.setOnClickListener(CurrentEncouragement.this);
        mNotification = Notification.NONE;
        mNotificationDay = NotificationDay.SUNDAY;

        loadArray(categoriesList, getApplicationContext(), "categoriesList");
        loadArray(categoriesListPrayer,getApplicationContext(),"categoriesListPrayer");
        loadArray(categoriesListScripture,getApplicationContext(),"categoriesListScripture");
        loadArray(encouragementList, getApplicationContext(), "encouragementList");
        loadArray(selectedEncouragement,getApplicationContext(),"selectedEncouragement");



        entryRelativeLayout = (RelativeLayout)findViewById(R.id.entryRelativeLayout);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int picChoice = -1;
        try{
            picChoice = Integer.parseInt(sp.getString("backgroundSelection","-1"));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(picChoice == 0){
            btnAriedLandscape.setAlpha(0.2f);
        }else if(picChoice == 1){
            btnBrickwall.setAlpha(0.2f);
        }else if(picChoice == 2){
            btnCity.setAlpha(0.2f);
        }else if(picChoice == 3){
            btnMountains1.setAlpha(0.2f);
        }else if(picChoice == 4){
            btnMountains2.setAlpha(0.2f);
        }else if(picChoice == 5){
            btnRoof.setAlpha(0.2f);
        }else if(picChoice == 6){
            btnSea1.setAlpha(0.2f);
        }else if(picChoice == 7){
            btnSea2.setAlpha(0.2f);
        }else if(picChoice == 8){
            btnSea3.setAlpha(0.2f);
        }else if(picChoice == 9){
            btnSky1.setAlpha(0.2f);
        }else if(picChoice == 10){
            btnSky2.setAlpha(0.2f);
        }else if(picChoice == 11){
            btnSunset.setAlpha(0.2f);
        }else if(picChoice == 12){
            btnTarp.setAlpha(0.2f);
        }else if(picChoice == 13){
            btnWheatfield.setAlpha(0.2f);
        }else if(picChoice == 14){
            btnCity2.setAlpha(0.2f);
        }else if(picChoice == 15){
            btnCrumbledPaper.setAlpha(0.2f);
        }else if(picChoice == 16){
            btnForest.setAlpha(0.2f);
        }else if(picChoice == 17){
            btnHutWall.setAlpha(0.2f);
        }else if(picChoice == 18){
            btnSea4.setAlpha(0.2f);
        }else if(picChoice == 19){
            btnShore.setAlpha(0.2f);
        }else if(picChoice == 20){
            btnTrain.setAlpha(0.2f);
        }else if(picChoice == 21){
            btnTropic2.setAlpha(0.2f);
        }else if(picChoice == 22){
            btnWood.setAlpha(0.2f);
        }
        if(picChoice == -1){
            Random r = new Random();
            picChoice = r.nextInt(14);
            btnCyclePhotos.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent3));
        }
        if(picChoice == 0){
            entryRelativeLayout.setBackgroundResource(R.drawable.ariedlandscape);
        }else if(picChoice == 1){
            entryRelativeLayout.setBackgroundResource(R.drawable.brickwall);
        }else if(picChoice == 2){
            entryRelativeLayout.setBackgroundResource(R.drawable.city);
        }else if(picChoice == 3){
            entryRelativeLayout.setBackgroundResource(R.drawable.mountains1);
        }else if(picChoice == 4){
            entryRelativeLayout.setBackgroundResource(R.drawable.mountains2);
        }else if(picChoice == 5){
            entryRelativeLayout.setBackgroundResource(R.drawable.roof);
        }else if(picChoice == 6){
            entryRelativeLayout.setBackgroundResource(R.drawable.sea1);
        }else if(picChoice == 7){
            entryRelativeLayout.setBackgroundResource(R.drawable.sea2);
        }else if(picChoice == 8){
            entryRelativeLayout.setBackgroundResource(R.drawable.sea3);
        }else if(picChoice == 9){
            entryRelativeLayout.setBackgroundResource(R.drawable.sky1);
        }else if(picChoice == 10){
            entryRelativeLayout.setBackgroundResource(R.drawable.sky2);
        }else if(picChoice == 11){
            entryRelativeLayout.setBackgroundResource(R.drawable.sunset);
        }else if(picChoice == 12){
            entryRelativeLayout.setBackgroundResource(R.drawable.tarp);
        }else if(picChoice == 13){
            entryRelativeLayout.setBackgroundResource(R.drawable.wheatfield);
        }else if(picChoice == 14){
            entryRelativeLayout.setBackgroundResource(R.drawable.city_2);
        }else if(picChoice == 15){
            entryRelativeLayout.setBackgroundResource(R.drawable.crumbled_paper);
        }else if(picChoice == 16){
            entryRelativeLayout.setBackgroundResource(R.drawable.forest);
        }else if(picChoice == 17){
            entryRelativeLayout.setBackgroundResource(R.drawable.hut_wall);
        }else if(picChoice == 18){
            entryRelativeLayout.setBackgroundResource(R.drawable.sea_4);
        }else if(picChoice == 19){
            entryRelativeLayout.setBackgroundResource(R.drawable.shore);
        }else if(picChoice == 20){
            entryRelativeLayout.setBackgroundResource(R.drawable.train);
        }else if(picChoice == 21){
            entryRelativeLayout.setBackgroundResource(R.drawable.tropic_2);
        }else if(picChoice == 22){
            entryRelativeLayout.setBackgroundResource(R.drawable.wood);
        }








//        selectedBackground = new ArrayList<>();
//        loadArray(selectedBackground,getApplicationContext(),"selectedBackground");
//        int picChoice;
//        if(!selectedBackground.isEmpty()) {
//            picChoice = Integer.parseInt(selectedBackground.get(0));
//        }else{
//            picChoice = 0;
//        }
//
//        if(picChoice == 1){
//            btnAriedLandscape.setAlpha(0.2f);
//        }else if(picChoice == 2){
//            btnBrickwall.setAlpha(0.2f);
//        }else if(picChoice == 3){
//            btnCity.setAlpha(0.2f);
//        }else if(picChoice == 4){
//            btnMountains1.setAlpha(0.2f);
//        }else if(picChoice == 5){
//            btnMountains2.setAlpha(0.2f);
//        }else if(picChoice == 6){
//            btnRoof.setAlpha(0.2f);
//        }else if(picChoice == 7){
//            btnSea1.setAlpha(0.2f);
//        }else if(picChoice == 8){
//            btnSea2.setAlpha(0.2f);
//        }else if(picChoice == 9){
//            btnSea3.setAlpha(0.2f);
//        }else if(picChoice == 10){
//            btnSky1.setAlpha(0.2f);
//        }else if(picChoice == 11){
//            btnSky2.setAlpha(0.2f);
//        }else if(picChoice == 12){
//            btnSunset.setAlpha(0.2f);
//        }else if(picChoice == 13){
//            btnTarp.setAlpha(0.2f);
//        }else if(picChoice == 14){
//            btnWheatfield.setAlpha(0.2f);
//        }
//
//        if(picChoice == 0){
//            Random r = new Random();
//            picChoice = r.nextInt(14) + 1;
//            btnCyclePhotos.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent3));
//        }
//
//        if(picChoice == 1){
//            entryRelativeLayout.setBackgroundResource(R.drawable.ariedlandscape);
//        }else if(picChoice == 2){
//            entryRelativeLayout.setBackgroundResource(R.drawable.brickwall);
//        }else if(picChoice == 3){
//            entryRelativeLayout.setBackgroundResource(R.drawable.city);
//        }else if(picChoice == 4){
//            entryRelativeLayout.setBackgroundResource(R.drawable.mountains1);
//        }else if(picChoice == 5){
//            entryRelativeLayout.setBackgroundResource(R.drawable.mountains2);
//        }else if(picChoice == 6){
//            entryRelativeLayout.setBackgroundResource(R.drawable.roof);
//        }else if(picChoice == 7){
//            entryRelativeLayout.setBackgroundResource(R.drawable.sea1);
//        }else if(picChoice == 8){
//            entryRelativeLayout.setBackgroundResource(R.drawable.sea2);
//        }else if(picChoice == 9){
//            entryRelativeLayout.setBackgroundResource(R.drawable.sea3);
//        }else if(picChoice == 10){
//            entryRelativeLayout.setBackgroundResource(R.drawable.sky1);
//        }else if(picChoice == 11){
//            entryRelativeLayout.setBackgroundResource(R.drawable.sky2);
//        }else if(picChoice == 12){
//            entryRelativeLayout.setBackgroundResource(R.drawable.sunset);
//        }else if(picChoice == 13){
//            entryRelativeLayout.setBackgroundResource(R.drawable.tarp);
//        }else if(picChoice == 14){
//            entryRelativeLayout.setBackgroundResource(R.drawable.wheatfield);
//        }


//        createNotification(context,
//                intent.getStringExtra("msg"),
//                intent.getStringExtra("msgCategory"),
//                intent.getStringExtra("msgTitle"),
//                intent.getStringExtra("msgText"),
//                intent.getStringExtra("msgAlert"),
//                intent.getStringExtra("msgPos"),
//                intent.getIntExtra("notifyID",999));
        // TODO end of new Intent
        Bundle extras = intent.getExtras();
        if(extras != null){
            if(extras.containsKey("msg")){
                ///TODO SAVE MESSAGE
                try{
                    String msg = extras.getString("msg");
                    fullEntry = msg;
                    String[] entry = msg.split("/n");
                    category = entry[1];
                    from = entry[2];
                    message = entry[3];
                    subCategory = entry[4];
                    notifID = entry[5];
                    notifInfoString = entry[6];
                    selectedEncouragement.clear();
                    selectedEncouragement.add(0,msg);
                    saveArray(selectedEncouragement,"selectedEncouragement");
                    tvEncouragementMessage.setText(message);
                    tvEncouragementFrom.setText(from);
                    tvNotification.setText(notifInfoString);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        if(!selectedEncouragement.isEmpty()) {
            try{
                String[] entry = selectedEncouragement.get(0).split("/n");
                fullEntry = selectedEncouragement.get(0);
                category = entry[1];
                from = entry[2];
                message = entry[3];
                subCategory = entry[4];
                notifID = entry[5];
                notifInfoString = entry[6];


                if(category.equals("Encouragement")){
                    mainCategory = 1;
                }else if(category.equals("Prayer")) {
                    mainCategory = 2;
                }else if(category.equals("Scripture")) {
                    mainCategory = 3;
                }

                if(!category.equals("Encouragement")) {
                    //Alarm Weekly on Monday 8:39 PM ::format::
                    if (notifInfoString.equals("new")) {
                        tvNotification.setText("Alarm Off");
                        String smsMessageStr = "/n" + category + "/n" + from
                                + "/n" + message + "/n" + subCategory + "/n"
                                + notifID + "/n" + "Alarm Off";
                        for (int j = 0; j < encouragementList.size(); j++) {
                            if (selectedEncouragement.get(0).equals(encouragementList.get(j))) {
                                encouragementList.set(j, smsMessageStr);
                                selectedEncouragement.set(0, smsMessageStr);
                                j = encouragementList.size();
                            }
                        }
                        saveArray(selectedEncouragement, "selectedEncouragement");
                        saveArray(encouragementList, "encouragementList");
                        mNotification = Notification.NONE;
                        btnNotificationFreq.setText("No Notification");
                        mNotificationDay = NotificationDay.SUNDAY;
                        btnDayWeekNotification.setText("Sunday");
                        btnInfoNotification.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                        btnNotificationFreq.setVisibility(View.VISIBLE);
                        btnSaveNotification.setVisibility(View.VISIBLE);
                        timePickerNotification.setVisibility(View.GONE);
                        btnDayWeekNotification.setVisibility(View.GONE);
                        isInfoActive = true;
                        isNotifFreqActive = false;
                        isNotifDayActive = false;
                        notifInfoString = "Alarm Off";

                    } else {
                        tvNotification.setText(notifInfoString);
                        btnInfoNotification.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        //relativeLayoutNotification.setVisibility(View.GONE);
                        //svNotificationDetails.setVisibility(View.GONE);
                        linearLayoutNotification.setVisibility(View.GONE);
                        isInfoActive = false;
                        isNotifFreqActive = false;
                        isNotifDayActive = false;
                        try {
                            String[] notifString = notifInfoString.split(" ");
                            String type = notifString[1];
                            if (type.equals("Daily")) {
                                String time = notifString[3];
                                String amOrPm = notifString[4];
                                String[] splitTime = time.split(":");
                                String hourString = splitTime[0];
                                String minuteString = splitTime[1];
                                hour = Integer.parseInt(hourString);
                                minute = Integer.parseInt(minuteString);
                                if (amOrPm.equals("PM")) {
                                    hour = hour + 12;
                                }
                                if (Build.VERSION.SDK_INT >= 23) {
                                    timePickerNotification.setHour(hour);
                                    timePickerNotification.setMinute(minute);
                                } else {
                                    timePickerNotification.setCurrentHour(hour);
                                    timePickerNotification.setCurrentMinute(minute);
                                }
                                btnNotificationFreq.setText("Daily Notification");
                                mNotification = Notification.DAILY;
                                mNotificationDay = NotificationDay.SUNDAY;
                                btnDayWeekNotification.setText("Sunday");
                            } else if (type.equals("Weekly")) {
                                String day = notifString[3];
                                String time = notifString[4];
                                String amOrPm = notifString[5];
                                String[] splitTime = time.split(":");
                                String hourString = splitTime[0];
                                String minuteString = splitTime[1];
                                hour = Integer.parseInt(hourString);
                                minute = Integer.parseInt(minuteString);
                                if (amOrPm.equals("PM")) {
                                    hour = hour + 12;
                                }
                                if (Build.VERSION.SDK_INT >= 23) {
                                    timePickerNotification.setHour(hour);
                                    timePickerNotification.setMinute(minute);
                                } else {
                                    timePickerNotification.setCurrentHour(hour);
                                    timePickerNotification.setCurrentMinute(minute);
                                }
                                btnNotificationFreq.setText("Weekly Notification");
                                mNotification = Notification.WEEKLY;
                                if (day.equals("Sunday")) {
                                    mNotificationDay = NotificationDay.SUNDAY;
                                } else if (day.equals("Monday")) {
                                    mNotificationDay = NotificationDay.MONDAY;
                                } else if (day.equals("Tuesday")) {
                                    mNotificationDay = NotificationDay.TUESDAY;
                                } else if (day.equals("Wednesday")) {
                                    mNotificationDay = NotificationDay.WEDNESDAY;
                                } else if (day.equals("Thursday")) {
                                    mNotificationDay = NotificationDay.THURSDAY;
                                } else if (day.equals("Friday")) {
                                    mNotificationDay = NotificationDay.FRIDAY;
                                } else if (day.equals("Saturday")) {
                                    mNotificationDay = NotificationDay.SATURDAY;
                                }
                                btnDayWeekNotification.setText(day);
                            } else if (type.equals("Off")) {
                                btnNotificationFreq.setText("No Notification");
                                mNotification = Notification.NONE;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    tvNotification.setText("");
                    tvNotification.setVisibility(View.GONE);
                    btnInfoNotification.setVisibility(View.GONE);
                    //relativeLayoutNotification.setVisibility(View.GONE);
                    //svNotificationDetails.setVisibility(View.GONE);
                    linearLayoutNotification.setVisibility(View.GONE);
                }

                tvEncouragementMessage.setText(message);
                tvEncouragementFrom.setText(from);

            }catch (Exception e){
                e.printStackTrace();
            }
        }



    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void ToolbarConfigurer(Activity activity, Toolbar toolbar, boolean displayHomeAsUpEnabled) {
        toolbar.setTitle((this.activity = activity).getTitle());
        if (!displayHomeAsUpEnabled) return;
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(this);
    }


//    tvNotification = (TextView)findViewById(R.id.tvNotification);
//    btnInfoNotification = (Button)findViewById(R.id.btnInfoNotification);
//    svNotificationDetails = (ScrollView)findViewById(R.id.svNotificationDetails);
//    btnNotificationFreq = (Button)findViewById(R.id.btnNotificationFreq);
//    btnDayWeekNotification = (Button)findViewById(R.id.btnDayWeekNotification);
//    btnSaveNotification = (Button)findViewById(R.id.btnSaveNotification);
//    timePickerNotification = (TimePicker)findViewById(R.id.timePickerNotification);
//    relativeLayoutNotification = (RelativeLayout)findViewById(R.id.relativeLayoutNotification);


    @Override
    public void onClick(View v) {
        //NavUtils.navigateUpFromSameTask(activity);
        switch (v.getId()){
            case R.id.btnInfoNotification:
                isInfoActive = !isInfoActive;
                if(isInfoActive){
                    btnInfoNotification.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                    //relativeLayoutNotification.setVisibility(View.VISIBLE);
                    //svNotificationDetails.setVisibility(View.VISIBLE);
                    linearLayoutNotification.setVisibility(View.VISIBLE);
                    btnNotificationFreq.setVisibility(View.VISIBLE);
                    btnSaveNotification.setVisibility(View.VISIBLE);
                    switch (mNotification){
                        case DAILY:
                            btnDayWeekNotification.setVisibility(View.GONE);
                            timePickerNotification.setVisibility(View.VISIBLE);
                            break;
                        case WEEKLY:
                            btnDayWeekNotification.setVisibility(View.VISIBLE);
                            timePickerNotification.setVisibility(View.VISIBLE);
                            break;
                        case NONE:
                            btnDayWeekNotification.setVisibility(View.GONE);
                            timePickerNotification.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }

                    if (Build.VERSION.SDK_INT >= 23 ) {
                        timePickerNotification.setHour(hour);
                        timePickerNotification.setMinute(minute);
                    }else {
                        timePickerNotification.setCurrentHour(hour);
                        timePickerNotification.setCurrentMinute(minute);
                    }


                }else{
                    //svNotificationDetails.setVisibility(View.GONE);
                    //relativeLayoutNotification.setVisibility(View.GONE);
                    linearLayoutNotification.setVisibility(View.GONE);
                    btnInfoNotification.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                }
                break;
            case R.id.btnNotificationFreq:
                isNotifFreqActive = true;
                isNotifDayActive = false;
                showPopUp(v);
                break;
            case R.id.btnDayWeekNotification:
                isNotifFreqActive = false;
                isNotifDayActive = true;
                showPopUp(v);
                break;
            case R.id.btnSaveNotification:
                //TODO notification save
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
                int notID = 1;
                try {
                    notID = Integer.parseInt(notifID);
                }catch (Exception e){
                    e.printStackTrace();
                }
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
                        notifInfoString = "Alarm Daily at " +
                                amOrPmHour + ":" + stringMinute + " " + amOrPm;
                        tvNotification.setText(notifInfoString);
                        break;
                    case WEEKLY:
                        type = "Weekly";
                        notifInfoString = "Alarm Weekly on " + day + " " +
                                amOrPmHour + ":" + stringMinute + " " + amOrPm;
                        tvNotification.setText(notifInfoString);
                        break;
                    case NONE:
                        type = "None";
                        stopNotification(notID);
                        notifInfoString = "Alarm Off";
                        tvNotification.setText(notifInfoString);
                        break;
                }

                String pos = "0";
                for(int i = 0; i < encouragementList.size(); i++){
                    if(selectedEncouragement.get(0).equals(encouragementList.get(i))){
                        encouragementList.set(i,"/n" + category + "/n" + from + "/n"
                                                + message + "/n" + subCategory + "/n"
                                                + notifID + "/n" + notifInfoString);
                        selectedEncouragement.set(0,"/n" + category + "/n" + from + "/n"
                                                + message + "/n" + subCategory + "/n"
                                                + notifID + "/n" + notifInfoString);
                        pos = Integer.toString(i);
                        i = encouragementList.size();
                    }
                }
                saveArray(encouragementList,"encouragementList");
                saveArray(selectedEncouragement,"selectedEncouragement");

                if(!type.equals("None")) {
                    setDailyAlarm(type, dayNum, hour, minute, notID, selectedEncouragement.get(0), pos);
                }




                btnInfoNotification.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                isInfoActive = false;
                isNotifFreqActive = false;
                isNotifDayActive = false;
                //relativeLayoutNotification.setVisibility(View.GONE);
                //svNotificationDetails.setVisibility(View.GONE);
                linearLayoutNotification.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),
                        "Notification Settings Saved",
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_cycle_pictures:
                resetButtonTints();
                btnCyclePhotos.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent3));
                saveString("backgroundSelection","-1");

//                selectedBackground.clear();
//                selectedBackground.add(0,"0");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_ariedlandscape_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.ariedlandscape);
                resetButtonTints();
                btnAriedLandscape.setAlpha(0.2f);
                saveString("backgroundSelection","0");

//                selectedBackground.clear();
//                selectedBackground.add(0,"1");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_brickwall_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.brickwall);
                resetButtonTints();
                btnBrickwall.setAlpha(0.2f);
                saveString("backgroundSelection","1");

//                selectedBackground.clear();
//                selectedBackground.add(0,"2");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_city_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.city);
                resetButtonTints();
                btnCity.setAlpha(0.2f);
                saveString("backgroundSelection","2");

//                selectedBackground.clear();
//                selectedBackground.add(0,"3");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_mountains1_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.mountains1);
                resetButtonTints();
                btnMountains1.setAlpha(0.2f);
                saveString("backgroundSelection","3");

//                selectedBackground.clear();
//                selectedBackground.add(0,"4");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_mountains2_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.mountains2);
                resetButtonTints();
                btnMountains2.setAlpha(0.2f);
                saveString("backgroundSelection","4");

//                selectedBackground.clear();
//                selectedBackground.add(0,"5");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_roof_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.roof);
                resetButtonTints();
                btnRoof.setAlpha(0.2f);
                saveString("backgroundSelection","5");

//                selectedBackground.clear();
//                selectedBackground.add(0,"6");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_sea1_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.sea1);
                resetButtonTints();
                btnSea1.setAlpha(0.2f);
                saveString("backgroundSelection","6");

//                selectedBackground.clear();
//                selectedBackground.add(0,"7");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_sea2_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.sea2);
                resetButtonTints();
                btnSea2.setAlpha(0.2f);
                saveString("backgroundSelection","7");

//                selectedBackground.clear();
//                selectedBackground.add(0,"8");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_sea3_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.sea3);
                resetButtonTints();
                btnSea3.setAlpha(0.2f);
                saveString("backgroundSelection","8");

//                selectedBackground.clear();
//                selectedBackground.add(0,"9");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_sky1_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.sky1);
                resetButtonTints();
                btnSky1.setAlpha(0.2f);
                saveString("backgroundSelection","9");

//                selectedBackground.clear();
//                selectedBackground.add(0,"10");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_sky2_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.sky2);
                resetButtonTints();
                btnSky2.setAlpha(0.2f);
                saveString("backgroundSelection","10");

//                selectedBackground.clear();
//                selectedBackground.add(0,"11");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_sunset_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.sunset);
                resetButtonTints();
                btnSunset.setAlpha(0.2f);
                saveString("backgroundSelection","11");

//                selectedBackground.clear();
//                selectedBackground.add(0,"12");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_tarp_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.tarp);
                resetButtonTints();
                btnTarp.setAlpha(0.2f);
                saveString("backgroundSelection","12");

//                selectedBackground.clear();
//                selectedBackground.add(0,"13");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            case R.id.btn_wheatfield_small:
                entryRelativeLayout.setBackgroundResource(R.drawable.wheatfield);
                resetButtonTints();
                btnWheatfield.setAlpha(0.2f);
                saveString("backgroundSelection","13");

//                selectedBackground.clear();
//                selectedBackground.add(0,"14");
//                saveArray(selectedBackground,"selectedBackground");
                break;
            //TODO add the new picture buttons
            case R.id.btn_city_2:
                entryRelativeLayout.setBackgroundResource(R.drawable.city_2);
                resetButtonTints();
                btnCity2.setAlpha(0.2f);
                saveString("backgroundSelection","14");
                break;
            case R.id.btn_crumbled_paper:
                entryRelativeLayout.setBackgroundResource(R.drawable.crumbled_paper);
                resetButtonTints();
                btnCrumbledPaper.setAlpha(0.2f);
                saveString("backgroundSelection","15");
                break;
            case R.id.btn_forest:
                entryRelativeLayout.setBackgroundResource(R.drawable.forest);
                resetButtonTints();
                btnForest.setAlpha(0.2f);
                saveString("backgroundSelection","16");
                break;
            case R.id.btn_hut_wall:
                entryRelativeLayout.setBackgroundResource(R.drawable.hut_wall);
                resetButtonTints();
                btnHutWall.setAlpha(0.2f);
                saveString("backgroundSelection","17");
                break;
            case R.id.btn_sea4:
                entryRelativeLayout.setBackgroundResource(R.drawable.sea_4);
                resetButtonTints();
                btnSea4.setAlpha(0.2f);
                saveString("backgroundSelection","18");
                break;
            case R.id.btn_shore:
                entryRelativeLayout.setBackgroundResource(R.drawable.shore);
                resetButtonTints();
                btnShore.setAlpha(0.2f);
                saveString("backgroundSelection","19");
                break;
            case R.id.btn_train:
                entryRelativeLayout.setBackgroundResource(R.drawable.train);
                resetButtonTints();
                btnTrain.setAlpha(0.2f);
                saveString("backgroundSelection","20");
                break;
            case R.id.btn_tropic2:
                entryRelativeLayout.setBackgroundResource(R.drawable.tropic_2);
                resetButtonTints();
                btnTropic2.setAlpha(0.2f);
                saveString("backgroundSelection","21");
                break;
            case R.id.btn_wood:
                entryRelativeLayout.setBackgroundResource(R.drawable.wood);
                resetButtonTints();
                btnWood.setAlpha(0.2f);
                saveString("backgroundSelection","22");
                break;
            case R.id.btnClose:
                hsv.setVisibility(View.INVISIBLE);
                btnClose.setVisibility(View.GONE);
                break;
            default:
                //NavUtils.navigateUpFromSameTask(activity);
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
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

    private boolean saveString(String stringName, String sKey){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1=sp.edit();
        mEdit1.remove(stringName);
        mEdit1.putString(stringName,sKey);
        return mEdit1.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.entry_menu, menu);
        if(mainCategory == 1){
            menu.getItem(0).setVisible(false);
        }
        try {
            if(mainCategory == 2) {
                for (int i = 0; i < categoriesListPrayer.size(); i++) {
                    String[] catList = categoriesListPrayer.get(i).split("/n");
                    //String[] selectedCategory = tvTitle.getText().toString().split(" - ");
//                int catSize = categoriesList.size() -1;
//                for(int j = 0; j < catSize; j++){
//                    item.getSubMenu().removeItem(j);
//                }
//                if(!catList[1].equals(category)) {
                    CharSequence c_arr = catList[1];
                    menu.getItem(0).getSubMenu().addSubMenu(0, i, 0, c_arr);
                    //getMenu().getItem(0)
//                }
                }
            }else if(mainCategory == 3) {
                for (int i = 0; i < categoriesListScripture.size(); i++) {
                    String[] catList = categoriesListScripture.get(i).split("/n");
                    //String[] selectedCategory = tvTitle.getText().toString().split(" - ");
//                int catSize = categoriesList.size() -1;
//                for(int j = 0; j < catSize; j++){
//                    item.getSubMenu().removeItem(j);
//                }
//                if(!catList[1].equals(category)) {
                    CharSequence c_arr = catList[1];
                    menu.getItem(0).getSubMenu().addSubMenu(0, i, 0, c_arr);
                    //getMenu().getItem(0)
//                }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.moveEntry:

                return true;
            case R.id.choosePhotoEntry:
                //Toast.makeText(getApplicationContext(),"Background",Toast.LENGTH_LONG).show();
                hsv.setVisibility(View.VISIBLE);
                btnClose.setVisibility(View.VISIBLE);
                return true;
            case R.id.deleteEntry:
//                Toast.makeText(getApplicationContext(),"Entry Deleted",Toast.LENGTH_LONG).show();
//                deleteEntry();
//                NavUtils.navigateUpFromSameTask(activity);

                DialogPopup dialog = new DialogPopup();
                Bundle args = new Bundle();
                args.putString(DialogPopup.DIALOG_TYPE, DialogPopup.DELETE_RECORD);
                args.putInt("entryPos", -1);
//                args.putInt("FragmentID",this.getId());
                args.putString("msg", fullEntry);
                args.putString("type","Review");
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "delete-record");
                return true;
            default:
                String title = item.getTitle().toString();
                try{

                    String smsMessageStr = "/n" + category + "/n" + from + "/n" + message + "/n" + title + "/n" + notifID + "/n" + notifInfoString;
                    if(!subCategory.equals(title)) {
                        for (int j = 0; j < encouragementList.size(); j++) {
                            if (selectedEncouragement.get(0).equals(encouragementList.get(j))) {
                                encouragementList.set(j,smsMessageStr);
                                selectedEncouragement.set(0,smsMessageStr);
                                j = encouragementList.size();
                            }
                        }
                        saveArray(selectedEncouragement, "selectedEncouragement");
                        saveArray(encouragementList, "encouragementList");
                        category = title;

//                        Toast.makeText(getApplicationContext(),encouragementList.get(0),Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(),selectedEncouragement.get(0),Toast.LENGTH_LONG).show();


                        Toast.makeText(getApplicationContext(), "Entry moved to " + title + " category", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Entry already in " + title + " category", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                return super.onOptionsItemSelected(item);


        }



        //return super.onOptionsItemSelected(item);
    }

    public void resetButtonTints(){
        btnCyclePhotos.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent2));
        btnAriedLandscape.setAlpha(1.0f);
        btnBrickwall.setAlpha(1.0f);
        btnCity.setAlpha(1.0f);
        btnMountains1.setAlpha(1.0f);
        btnMountains2.setAlpha(1.0f);
        btnRoof.setAlpha(1.0f);
        btnSea1.setAlpha(1.0f);
        btnSea2.setAlpha(1.0f);
        btnSea3.setAlpha(1.0f);
        btnSky1.setAlpha(1.0f);
        btnSky2.setAlpha(1.0f);
        btnSunset.setAlpha(1.0f);
        btnTarp.setAlpha(1.0f);
        btnWheatfield.setAlpha(1.0f);
        btnCity2.setAlpha(1.0f);
        btnCrumbledPaper.setAlpha(1.0f);
        btnForest.setAlpha(1.0f);
        btnHutWall.setAlpha(1.0f);
        btnSea4.setAlpha(1.0f);
        btnShore.setAlpha(1.0f);
        btnTrain.setAlpha(1.0f);
        btnTropic2.setAlpha(1.0f);
        btnWood.setAlpha(1.0f);

    }


    public void deleteEntry(){
        for(int i = 0; i < encouragementList.size(); i++){
            if(selectedEncouragement.get(0).equals(encouragementList.get(i))){

                //This is here to delete the entry from notification list
                ArrayList<String> notificationEncouragementList = new ArrayList<>();
                loadArray(notificationEncouragementList,this,"notificationEncouragementList");
                for(int j = 0; j < notificationEncouragementList.size(); j++) {
                    if (notificationEncouragementList.get(j).equals(selectedEncouragement.get(0))) {
                        notificationEncouragementList.remove(j);
                        saveArray(notificationEncouragementList, "notificationEncouragementList");
                    }
                }

                encouragementList.remove(i);
                saveArray(encouragementList,"encouragementList");
            }
        }
        if(category.equals("Prayer") || category.equals("Scripture")){
            try {
                int notID = Integer.parseInt(notifID);
                stopNotification(notID);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    public void showPopUp(View v){
        PopupMenu popupMenu = new PopupMenu(this,v,1,0,R.style.PopupMenu);

        popupMenu.setOnMenuItemClickListener(CurrentEncouragement.this);
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

//        inflater.inflate(R.menu.entry_menu, popupMenu.getMenu());
//        try {
//            for(int i = 0; i < categoriesList.size(); i++){
//                String[] catList = categoriesList.get(i).split("/n");
//                //String[] selectedCategory = tvTitle.getText().toString().split(" - ");
//
//                if(!catList[1].equals(category)) {
//                    CharSequence c_arr = catList[1];
//                    popupMenu.getMenu().getItem(0).getSubMenu().addSubMenu(0, i, 0, c_arr);
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }



        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.popup_dailyNotification:
                if(isNotifFreqActive) {
                    mNotification = Notification.DAILY;
                    btnNotificationFreq.setText("Daily Notification");
                    btnDayWeekNotification.setVisibility(View.GONE);
                    timePickerNotification.setVisibility(View.VISIBLE);
                    isNotifFreqActive = false;
                }
                return true;
            case R.id.popup_weeklyNotification:
                if(isNotifFreqActive) {
                    mNotification = Notification.WEEKLY;
                    btnNotificationFreq.setText("Weekly Notification");
                    btnDayWeekNotification.setVisibility(View.VISIBLE);
                    timePickerNotification.setVisibility(View.VISIBLE);
                    isNotifFreqActive = false;
                }
                return true;
            case R.id.popup_noNotification:
                if(isNotifFreqActive) {
                    mNotification = Notification.NONE;
                    btnNotificationFreq.setText("No Notification");
                    btnDayWeekNotification.setVisibility(View.GONE);
                    timePickerNotification.setVisibility(View.GONE);
                    isNotifFreqActive = false;
                }

                return true;
            case R.id.popup_sunday:
                if(isNotifDayActive) {
                    btnDayWeekNotification.setText("Sunday");
                    mNotificationDay = NotificationDay.SUNDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_monday:
                if(isNotifDayActive) {
                    btnDayWeekNotification.setText("Monday");
                    mNotificationDay = NotificationDay.MONDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_tuesday:
                if(isNotifDayActive) {
                    btnDayWeekNotification.setText("Tuesday");
                    mNotificationDay = NotificationDay.TUESDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_wednesday:
                if(isNotifDayActive) {
                    btnDayWeekNotification.setText("Wednesday");
                    mNotificationDay = NotificationDay.WEDNESDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_thursday:
                if(isNotifDayActive) {
                    btnDayWeekNotification.setText("Thursday");
                    mNotificationDay = NotificationDay.THURSDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_friday:
                if(isNotifDayActive) {
                    btnDayWeekNotification.setText("Friday");
                    mNotificationDay = NotificationDay.FRIDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_saturday:
                if(isNotifDayActive) {
                    btnDayWeekNotification.setText("Saturday");
                    mNotificationDay = NotificationDay.SATURDAY;
                    isNotifDayActive = false;
                }
                return true;
            default:
                return false;
//            default:
//                String title = item.getTitle().toString();
//                try{
//
//                    String smsMessageStr = "/n" + category + "/n" + from + "/n" + message + "/n" + title;
//                    Toast.makeText(getApplicationContext(), "Entry Moved to " + title, Toast.LENGTH_SHORT).show();
//
//                    for (int j = 0; j < encouragementList.size(); j++) {
//                        if (encouragementList.get(j).equals(selectedEncouragement.get(0))) {
//                            encouragementList.set(j, smsMessageStr);
//                            saveArray(encouragementList, "encouragementList");
//                            j = encouragementList.size();
//                        }
//                    }
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                return false;
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

//            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
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
            }

//            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }

    public void stopNotification(int notifID){
        Intent intentstop = new Intent(this, AlertReceiver.class);
        PendingIntent senderstop = PendingIntent.getBroadcast(this,
                notifID, intentstop, 0);
        AlarmManager alarmManagerstop = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);

        alarmManagerstop.cancel(senderstop);
    }



}