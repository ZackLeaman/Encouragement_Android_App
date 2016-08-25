package com.wordpress.zackleaman.materialtablayout;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, AdapterView.OnItemClickListener{

    private ArrayList<String> encouragementList,starterEncouragements,categoriesList,selectedEncouragement;
    private ArrayList<String> categoriesListPrayer;
    private ArrayList<String> categoriesListScripture;
    private Button btnFiveSecondAlarm, btnTenSecondAlarm;
    private boolean wantsStarterEncouragement = true, addedStarterEncouragements;

    private int positionEncouragement;
    private RelativeLayout relativeLayoutEncouragementEntry;
    private TextView tvEncouragementMessage, tvEncouragementFrom;
    private TextView tvEncouragement;
    private Button btnInfoEncouragement, btnNotificationFreqEncouragement, btnDayWeekEncouragement,btnSaveEncouragement;
    private TimePicker timePickerEncouragement;
    private boolean isEncouragementInfoActive, isEncouragementNotiFreqActive, isEncouragementDayActive;
    private boolean needUpdateNotificationEncouragement = false;
    private Button btnEncouragementEntry;
    private enum EncouragementNotification{DAILY,WEEKLY,NONE}
    private enum EncouragementDay{SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY}
    private EncouragementNotification mEncouragementNotification;
    private EncouragementDay mEncouragementDay;
    private ArrayList<String> notificationEncouragementList;

    private int positionPrayer;
    private RelativeLayout relativeLayoutPrayerEntry;
    private TextView tvPrayerMessage, tvPrayerFrom;
    private TextView tvPrayer;
    private Button btnInfoPrayer, btnNotificationFreqPrayer, btnDayWeekPrayer,btnSavePrayer;
    private TimePicker timePickerPrayer;
    private boolean isPrayerInfoActive, isPrayerNotiFreqActive, isPrayerDayActive;
    private boolean needUpdateNotificationPrayer = false;
    private enum PrayerNotification{DAILY,WEEKLY,NONE}
    private enum PrayerDay{SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY}
    private PrayerNotification mPrayerNotification;
    private PrayerDay mPrayerDay;
    private ArrayList<String> notificationPrayerList;

    private int positionScripture;
    private RelativeLayout relativeLayoutScriptureEntry;
    private TextView tvScriptureMessage, tvScriptureFrom;
    private TextView tvScripture;
    private Button btnInfoScripture, btnNotificationFreqScripture, btnDayWeekScripture,btnSaveScripture;
    private TimePicker timePickerScripture;
    private boolean isScriptureInfoActive, isScriptureNotiFreqActive, isScriptureDayActive;
    private boolean needUpdateNotificationScripture = false;
    private enum ScriptureNotification{DAILY,WEEKLY,NONE}
    private enum ScriptureDay{SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY}
    private ScriptureNotification mScriptureNotification;
    private ScriptureDay mScriptureDay;
    private ArrayList<String> notificationScriptureList;

    private boolean isEncouragementDropDownActive, isPrayerDropDownActive, isScriptureDropDownActive;
    private ListView listViewPrayer, listViewScripture;
    private Button btnEncouragementDropdown, btnPrayerDropdown, btnScriptureDropdown;
    private ArrayList<Integer> intPrayerList, intScriptureList;

    private ArrayList<String> alarmList = new ArrayList<>();
    private String encouragementAlarmString = "Alarm Off";
    private TextView tvEncouragementAlarmString;

    private Switch switchPrayerAndScripture;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        encouragementList = new ArrayList<>();
        starterEncouragements = new ArrayList<>();
        categoriesList = new ArrayList<>();
        selectedEncouragement = new ArrayList<>();
        categoriesListPrayer = new ArrayList<>();
        categoriesListScripture = new ArrayList<>();
        notificationEncouragementList = new ArrayList<>();
        notificationPrayerList = new ArrayList<>();
        notificationScriptureList = new ArrayList<>();
        intPrayerList = new ArrayList<>();
        intScriptureList = new ArrayList<>();
        positionEncouragement = 0;
        positionPrayer = 0;
        positionScripture = 0;
        isEncouragementDropDownActive = true;
        isPrayerDropDownActive = false;
        isScriptureDropDownActive = false;
        addedStarterEncouragements = false;
        loadArray(notificationEncouragementList,getActivity().getApplicationContext(),"notificationEncouragementList");
        loadArray(notificationPrayerList,getActivity().getApplicationContext(),"notificationPrayerList");
        loadArray(notificationScriptureList,getActivity().getApplicationContext(),"notificationScriptureList");
        loadArray(encouragementList,getActivity().getApplicationContext(),"encouragementList");



        starterEncouragements.clear();
        for(int i = 0; i < 30; i++){
            //TODO set up 30 starter encouragements
            starterEncouragements.add("/nEncouragement/n" + "NameAddressDate" + "/n" +
                    "Starter Encouragement " + i + "/nnone" + "/n3"  + "/nAlarm Off");
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        wantsStarterEncouragement = sp.getBoolean("wantsStarterEncouragement", true);
        //String wSE = loadString("wantsStarterEncouragements",getContext());
//        if(!wSE.isEmpty()){
//            try{
//                wantsStarterEncouragement = Boolean.parseBoolean(wSE);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
        String aSE = loadString("addedStarterEncouragements",getContext());
        if(!aSE.isEmpty()){
            try{
                addedStarterEncouragements = Boolean.parseBoolean(aSE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //TODO setup settings menu to be able to either wantStarterEncouragement or not
        if(wantsStarterEncouragement){
            if(!addedStarterEncouragements){
                for(int i = 0; i < starterEncouragements.size(); i++){
                    encouragementList.add(i,starterEncouragements.get(i));
                }
//                if(!encouragementList.isEmpty()){
//                    for(int z = 0; z < encouragementList.size(); z++){
//                        if(encouragementList.get(z).equals("/nEncouragement/n/nNo Entries in Encouragement/nnone/n3/nAlarm Off")){
//                            encouragementList.remove(z);
//                            saveArray(encouragementList,"encouragementList");
//                            z = encouragementList.size();
//                        }
//                    }
//                }
//                if(!notificationEncouragementList.isEmpty()){
//                    for(int z = 0; z < notificationEncouragementList.size(); z++){
//                        if(notificationEncouragementList.get(z).equals("/nEncouragement/n/nNo Entries in Encouragement/nnone/n3/nAlarm Off")){
//                            notificationEncouragementList.remove(z);
//                            saveArray(notificationEncouragementList,"notificationEncouragementList");
//                            z = notificationEncouragementList.size();
//                        }
//                    }
//                }
                saveArray(encouragementList,"encouragementList");
                addedStarterEncouragements = true;
                saveString("addedStarterEncouragements",Boolean.toString(addedStarterEncouragements));
            }
        }else{
            if(addedStarterEncouragements){
                ArrayList<Integer> index = new ArrayList<>();
                for(int i = 0; i < encouragementList.size(); i++){
                    for(int j = 0; j < starterEncouragements.size(); j++) {
                        if (encouragementList.get(i).equals(starterEncouragements.get(j))) {
                            index.add(i);
                        }
                    }
                }

                if(!index.isEmpty()) {
                    for (int l = index.size(); l > 0; l--) {
                        int pos = l - 1;
                        encouragementList.remove(index.get(pos).intValue());
                    }
                }

                //            if(encouragementList.isEmpty()){
    //                encouragementList.add(0,"/nEncouragement/n/nNo Entries in Encouragement/nnone/n3/nAlarm Off");
    //                saveArray(encouragementList,"encouragementList");
    //            }
    //            index.clear();
    //            for(int k = 0; k < notificationEncouragementList.size(); k++){
    //                for(int r = 0; r < starterEncouragements.size(); r++){
    //                    if(notificationEncouragementList.get(k).equals(starterEncouragements.get(r))){
    //                        index.add(r);
    //                    }
    //                }
    //            }
    //            for(int x = index.size(); x >= 1; x--){
    //                int pos = x -1;
    //                notificationEncouragementList.remove(index.get(pos).intValue());
    //            }
    //
    //            saveArray(notificationEncouragementList,"notificationEncouragementList");
                saveArray(encouragementList,"encouragementList");
                addedStarterEncouragements = false;
                saveString("addedStarterEncouragements",Boolean.toString(addedStarterEncouragements));
            }
        }

        //categoriesList.clear();
        loadArray(categoriesList,getContext().getApplicationContext(),"categoriesList");
        if(categoriesList == null || categoriesList.isEmpty()) {
            categoriesList.add("/nEncouragement/n");
            categoriesList.add("/nPrayer/n");
            categoriesList.add("/nScripture/n");
            saveArray(categoriesList,"categoriesList");
        }
        loadArray(categoriesListPrayer,getContext().getApplicationContext(),"categoriesListPrayer");
        if(categoriesListPrayer == null || categoriesListPrayer.isEmpty()){
            categoriesListPrayer.add("/nChurch");
            categoriesListPrayer.add("/nFamily");
            categoriesListPrayer.add("/nFriends");
            categoriesListPrayer.add("/nMissions");
            categoriesListPrayer.add("/nWork");
            categoriesListPrayer.add("/nOther");
            saveArray(categoriesListPrayer,"categoriesListPrayer");
        }
        loadArray(categoriesListScripture,getContext().getApplicationContext(),"categoriesListScripture");
        if(categoriesListScripture == null || categoriesListScripture.isEmpty()){
            categoriesListScripture.add("/nHope");
            categoriesListScripture.add("/nLove");
            categoriesListScripture.add("/nFriendship");
            categoriesListScripture.add("/nForgiveness");
            categoriesListScripture.add("/nStrength");
            categoriesListScripture.add("/nOther");
            saveArray(categoriesListScripture,"categoriesListScripture");
        }

//        btnFiveSecondAlarm = (Button)getView().findViewById(R.id.btnFiveSecondAlarm);
//        btnFiveSecondAlarm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setAlarm1(view);
//            }
//        });
//
//        btnTenSecondAlarm = (Button)getView().findViewById(R.id.btnTenSecondAlarm);
//        btnTenSecondAlarm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setAlarm2(view);
//            }
//        });




        /// THIS IS THE START OF ENCOURAGEMENT NOTIFICATION INITIALIZATION

        tvEncouragementMessage = (TextView)getView().findViewById(R.id.tvEncouragementMessage);
        tvEncouragementFrom = (TextView)getView().findViewById(R.id.tvEncouragementFrom);
//        int size = encouragementList.size()-1;
//        if(size > 0) {
//            try {
//                Random r = new Random();
//                int i1 = r.nextInt(size);
//                String[] entry = encouragementList.get(i1).split("/n");
//                position = i1;
//                String category = entry[1];
//                String nameAddressDate = entry[2];
//                String message = entry[3];
//
//                tvEncouragementMessage.setText(message);
//                tvEncouragementFrom.setText(nameAddressDate);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }else if(size == 0){
//            try {
//                String[] entry = encouragementList.get(0).split("/n");
//                position = 0;
//                String category = entry[1];
//                String nameAddressDate = entry[2];
//                String message = entry[3];
//
//                tvEncouragementMessage.setText(message);
//                tvEncouragementFrom.setText(nameAddressDate);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }

        // Sets up the Encouragement notification array list that is random
//        String updateEnc = loadString("needUpdateNotificationEncouragement",getContext());
//        if(!updateEnc.isEmpty()){
//            try{
//                needUpdateNotificationEncouragement = Boolean.parseBoolean(updateEnc);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        if(needUpdateNotificationEncouragement){
//            notificationEncouragementList.remove(0);
//            needUpdateNotificationEncouragement = false;
//            saveString("needUpdateNotificationEncouragement","false");
//            saveArray(notificationEncouragementList,"notificationEncouragementList");
//        }

        ArrayList<String> encouragements = new ArrayList<>();
        try{
            if(!encouragementList.isEmpty()){
                for(int w = 0; w < encouragementList.size(); w++){
                    String[] entry = encouragementList.get(w).split("/n");
                    if(entry[1].equals("Encouragement")){
                        encouragements.add(encouragementList.get(w));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(notificationEncouragementList.isEmpty() && encouragements.isEmpty()){
            notificationEncouragementList.add(0,"/nEncouragement/n /nNo Entries in Encouragement/nnone/n3/nAlarm Off");
            saveArray(notificationEncouragementList,"notificationEncouragementList");
        }
        if(notificationEncouragementList.size() == 1 && !encouragements.isEmpty()){
            if(notificationEncouragementList.get(0).equals("/nEncouragement/n /nNo Entries in Encouragement/nnone/n3/nAlarm Off")){
                notificationEncouragementList.remove(0);
                saveArray(notificationEncouragementList, "notificationEncouragementList");
            }
        }
        if(notificationEncouragementList.isEmpty() || MainActivity.isFirstTimeOpening || notificationEncouragementList == null){
            Log.d("notificationEnc", "is empty");
            Random r = new Random();
            ArrayList<String> orderedList = new ArrayList<>();
            for(int i = 0; i < encouragementList.size(); i++){
                try{
                    String[] entry = encouragementList.get(i).split("/n");
                    String category = entry[1];

                    if(category.equals("Encouragement")){
                        orderedList.add(encouragementList.get(i));
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            int olSize = orderedList.size();
            int i1;
            while(olSize > 0){
                if(olSize > 1) {
                    i1 = r.nextInt(olSize);
                    notificationEncouragementList.add(0,orderedList.get(i1));
                    orderedList.remove(i1);
                }else{
                    notificationEncouragementList.add(0,orderedList.get(0));
                    orderedList.remove(0);
                }
                olSize--;

            }

            saveArray(notificationEncouragementList,"notificationEncouragementList");
            try{
                for(int j = 0; j < encouragementList.size(); j++){
                    if(notificationEncouragementList.get(0).equals(encouragementList.get(j))){
                        positionEncouragement = j;
                    }
                }

                String[] entry = notificationEncouragementList.get(0).split("/n");
                String nameAddressDate = entry[2];
                String message = entry[3];

                tvEncouragementMessage.setText(message);
                tvEncouragementFrom.setText(nameAddressDate);
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            try{
                if(!wantsStarterEncouragement) {
                    if(!notificationEncouragementList.isEmpty() && !starterEncouragements.isEmpty()) {
                        ArrayList<Integer> index = new ArrayList<>();
                        for (int k = 0; k < notificationEncouragementList.size(); k++) {
                            for (int r = 0; r < starterEncouragements.size(); r++) {
                                if (notificationEncouragementList.get(k).equals(starterEncouragements.get(r))) {
                                    index.add(k);
                                }
                            }
                        }

                        for (int x = index.size(); x > 0; x--) {
                            int pos = x - 1;
                            notificationEncouragementList.remove(index.get(pos).intValue());
                        }

                        saveArray(notificationEncouragementList, "notificationEncouragementList");
                    }

                    if(notificationEncouragementList.isEmpty() && encouragements.isEmpty()){
                        notificationEncouragementList.add(0,"/nEncouragement/n /nNo Entries in Encouragement/nnone/n3/nAlarm Off");
                        saveArray(notificationEncouragementList,"notificationEncouragementList");
                    }

                }

                if(!encouragementList.isEmpty()) {
                    for (int j = 0; j < encouragementList.size(); j++) {
                        if (notificationEncouragementList.get(0).equals(encouragementList.get(j))) {
                            positionEncouragement = j;
                        }
                    }
                }

                String[] entry = notificationEncouragementList.get(0).split("/n");
                String nameAddressDate = entry[2];
                String message = entry[3];

                tvEncouragementMessage.setText(message);
                tvEncouragementFrom.setText(nameAddressDate);

            }catch (Exception e){
                e.printStackTrace();
            }


        }

        relativeLayoutEncouragementEntry = (RelativeLayout)getView().findViewById(R.id.relativeLayoutEncouragementEntry);
        relativeLayoutEncouragementEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!notificationEncouragementList.get(0).equals("/nEncouragement/n /nNo Entries in Encouragement/nnone/n3/nAlarm Off")){
                    selectedEncouragement.clear();
                    selectedEncouragement.add(0,notificationEncouragementList.get(0));
                    saveArray(selectedEncouragement,"selectedEncouragement");
                    Intent intent = new Intent(getContext().getApplicationContext(),CurrentEncouragement.class);
                    startActivity(intent);
                }
            }
        });

        btnEncouragementEntry = (Button)getView().findViewById(R.id.btnEncouragementEntry);
        if(!notificationEncouragementList.isEmpty()) {
            if (!notificationEncouragementList.get(0).equals("/nEncouragement/n /nNo Entries in Encouragement/nnone/n3/nAlarm Off")) {
                btnEncouragementEntry.setVisibility(View.VISIBLE);
                Log.d("NotifIf",notificationEncouragementList.get(0));
            } else {
                btnEncouragementEntry.setVisibility(View.INVISIBLE);
            }
        }else{
            btnEncouragementEntry.setVisibility(View.INVISIBLE);
        }

        btnEncouragementEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!notificationEncouragementList.get(0).equals("/nEncouragement/n /nNo Entries in Encouragement/nnone/n3/nAlarm Off")){
                    selectedEncouragement.clear();
                    selectedEncouragement.add(0,notificationEncouragementList.get(0));
                    saveArray(selectedEncouragement,"selectedEncouragement");
                    Intent intent = new Intent(getContext().getApplicationContext(),CurrentEncouragement.class);
                    startActivity(intent);
                }
            }
        });




        tvEncouragement = (TextView)getView().findViewById(R.id.tvEncouragement);
        btnInfoEncouragement = (Button)getView().findViewById(R.id.btnInfoEncouragement);
        btnNotificationFreqEncouragement = (Button)getView().findViewById(R.id.btnNotificationFreqEncouragement);
        btnDayWeekEncouragement = (Button)getView().findViewById(R.id.btnDayWeekEncouragement);
        timePickerEncouragement = (TimePicker)getView().findViewById(R.id.timePickerEncouragement);
        btnSaveEncouragement = (Button)getView().findViewById(R.id.btnSaveEncouragement);


        mEncouragementNotification = EncouragementNotification.NONE;
        btnNotificationFreqEncouragement.setText("No Notification");
        mEncouragementDay = EncouragementDay.SUNDAY;
        btnDayWeekEncouragement.setText("Sunday");
        String encDay = loadString("EncouragementDay",getActivity().getApplicationContext());
        String encNotFreq = loadString("EncouragementNotiFreq",getActivity().getApplicationContext());

        if(encNotFreq.equals("Daily")){
            tvEncouragement.setText("Daily Encouragement");
            mEncouragementNotification = EncouragementNotification.DAILY;
            btnNotificationFreqEncouragement.setText("Daily Notification");
        }else if(encNotFreq.equals("Weekly")){
            tvEncouragement.setText("Weekly Encouragement");
            mEncouragementNotification = EncouragementNotification.WEEKLY;
            btnNotificationFreqEncouragement.setText("Weekly Notification");
        }else if(encNotFreq.equals("None")){
            tvEncouragement.setText("Encouragement");
            mEncouragementNotification = EncouragementNotification.NONE;
            btnNotificationFreqEncouragement.setText("No Notification");
        }

        if(encDay.equals("Sunday")){
            mEncouragementDay = EncouragementDay.SUNDAY;
            btnDayWeekEncouragement.setText(encDay);
        }else if(encDay.equals("Monday")){
            mEncouragementDay = EncouragementDay.MONDAY;
            btnDayWeekEncouragement.setText(encDay);
        }else if(encDay.equals("Tuesday")){
            mEncouragementDay = EncouragementDay.TUESDAY;
            btnDayWeekEncouragement.setText(encDay);
        }else if(encDay.equals("Wednesday")){
            mEncouragementDay = EncouragementDay.WEDNESDAY;
            btnDayWeekEncouragement.setText(encDay);
        }else if(encDay.equals("Thursday")){
            mEncouragementDay = EncouragementDay.THURSDAY;
            btnDayWeekEncouragement.setText(encDay);
        }else if(encDay.equals("Friday")){
            mEncouragementDay = EncouragementDay.FRIDAY;
            btnDayWeekEncouragement.setText(encDay);
        }else if(encDay.equals("Saturday")){
            mEncouragementDay = EncouragementDay.SATURDAY;
            btnDayWeekEncouragement.setText(encDay);
        }


        isEncouragementInfoActive = false;
        isEncouragementNotiFreqActive = false;
        isEncouragementDayActive = false;
        btnNotificationFreqEncouragement.setVisibility(View.GONE);
        btnDayWeekEncouragement.setVisibility(View.GONE);
        timePickerEncouragement.setVisibility(View.GONE);
        btnSaveEncouragement.setVisibility(View.GONE);
        btnInfoEncouragement.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        btnInfoEncouragement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEncouragementInfoActive = !isEncouragementInfoActive;
                if(isEncouragementInfoActive){
                    btnInfoEncouragement.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    tvEncouragementAlarmString.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    btnNotificationFreqEncouragement.setVisibility(View.VISIBLE);
                    btnSaveEncouragement.setVisibility(View.VISIBLE);
                    switch (mEncouragementNotification){
                        case DAILY:
                            btnDayWeekEncouragement.setVisibility(View.GONE);
                            timePickerEncouragement.setVisibility(View.VISIBLE);
                            break;
                        case WEEKLY:
                            btnDayWeekEncouragement.setVisibility(View.VISIBLE);
                            timePickerEncouragement.setVisibility(View.VISIBLE);
                            break;
                        case NONE:
                            btnDayWeekEncouragement.setVisibility(View.GONE);
                            timePickerEncouragement.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }

                    if (Build.VERSION.SDK_INT >= 23 ) {
                        String hour = loadString("EncouragementTimeHour",getContext());
                        String minute = loadString("EncouragementTimeMinute",getContext());
                        if(!hour.isEmpty()){
                            timePickerEncouragement.setHour(Integer.parseInt(hour));
                        }
                        if(!minute.isEmpty()){
                            timePickerEncouragement.setMinute(Integer.parseInt(minute));
                        }
                    }else {
                        String hour = loadString("EncouragementTimeHour",getContext());
                        String minute = loadString("EncouragementTimeMinute",getContext());
                        if(!hour.isEmpty()){
                            timePickerEncouragement.setCurrentHour(Integer.parseInt(hour));
                        }
                        if(!minute.isEmpty()){
                            timePickerEncouragement.setCurrentMinute(Integer.parseInt(minute));
                        }
                    }


                }else{
                    btnNotificationFreqEncouragement.setVisibility(View.GONE);
                    btnDayWeekEncouragement.setVisibility(View.GONE);
                    timePickerEncouragement.setVisibility(View.GONE);
                    btnSaveEncouragement.setVisibility(View.GONE);
                    btnInfoEncouragement.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    tvEncouragementAlarmString.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                }
            }
        });
        btnNotificationFreqEncouragement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEncouragementNotiFreqActive = true;
                isEncouragementDayActive = false;
                isPrayerNotiFreqActive = false;
                isPrayerDayActive = false;
                isScriptureNotiFreqActive = false;
                isScriptureDayActive = false;
                showPopUp(view);
            }
        });
        btnDayWeekEncouragement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEncouragementNotiFreqActive = false;
                isEncouragementDayActive = true;
                isPrayerNotiFreqActive = false;
                isPrayerDayActive = false;
                isScriptureNotiFreqActive = false;
                isScriptureDayActive = false;
                showPopUp(view);
            }
        });
        tvEncouragementAlarmString = (TextView)getView().findViewById(R.id.tvEncouragementAlarmString);
        encouragementAlarmString = loadString("encouragementAlarmString",getContext());
        if(encouragementAlarmString.isEmpty()){
            encouragementAlarmString = "Alarm Off";
        }
        tvEncouragementAlarmString.setText(encouragementAlarmString);
        tvEncouragementAlarmString.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        btnSaveEncouragement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = 0;
                int hour = 1;
                int minute = 1;
                String amOrPmHour;
                String amOrPm;
                String stringMinute;
                String dayString = "";



                if (Build.VERSION.SDK_INT >= 23 ) {
                    saveString("EncouragementTimeHour",Integer.toString(timePickerEncouragement.getHour()));
                    saveString("EncouragementTimeMinute",Integer.toString(timePickerEncouragement.getMinute()));
                    hour = timePickerEncouragement.getHour();
                    minute = timePickerEncouragement.getMinute();
                }else {
                    saveString("EncouragementTimeHour",Integer.toString(timePickerEncouragement.getCurrentHour()));
                    saveString("EncouragementTimeMinute",Integer.toString(timePickerEncouragement.getCurrentMinute()));
                    hour = timePickerEncouragement.getCurrentHour();
                    minute = timePickerEncouragement.getCurrentMinute();
                }
                switch (mEncouragementDay){
                    case SUNDAY:
                        saveString("EncouragementDay","Sunday");
                        dayString = "Sunday";
                        day = 1;
                        break;
                    case MONDAY:
                        saveString("EncouragementDay","Monday");
                        dayString = "Monday";
                        day = 2;
                        break;
                    case TUESDAY:
                        saveString("EncouragementDay","Tuesday");
                        dayString = "Tuesday";
                        day = 3;
                        break;
                    case WEDNESDAY:
                        saveString("EncouragementDay","Wednesday");
                        dayString = "Wednesday";
                        day = 4;
                        break;
                    case THURSDAY:
                        saveString("EncouragementDay","Thursday");
                        dayString = "Thursday";
                        day = 5;
                        break;
                    case FRIDAY:
                        saveString("EncouragementDay","Friday");
                        dayString = "Friday";
                        day = 6;
                        break;
                    case SATURDAY:
                        saveString("EncouragementDay","Saturday");
                        dayString = "Saturday";
                        day = 7;
                        break;
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

                //Toast.makeText(getContext(),notificationEncouragementList.get(0),Toast.LENGTH_LONG).show();

                switch (mEncouragementNotification){
                    case DAILY:
                        saveString("EncouragementNotiFreq","Daily");
                        setDailyAlarm("Daily",day,hour,minute,3,notificationEncouragementList.get(0),Integer.toString(positionEncouragement));
                        encouragementAlarmString = "Alarm Daily at " +
                                amOrPmHour + ":" + stringMinute + " " + amOrPm;
                        saveString("encouragementAlarmString", encouragementAlarmString);
                        saveString("encouragementType","Daily");
                        break;
                    case WEEKLY:
                        saveString("EncouragementNotiFreq","Weekly");
                        setDailyAlarm("Weekly",day,hour,minute,3,notificationEncouragementList.get(0),Integer.toString(positionEncouragement));
                        encouragementAlarmString = "Alarm Weekly on " + dayString + " " +
                                amOrPmHour + ":" + stringMinute + " " + amOrPm;
                        saveString("encouragementAlarmString", encouragementAlarmString);
                        saveString("encouragementType","Weekly");
                        break;
                    case NONE:
                        saveString("EncouragementNotiFreq","None");
                        saveString("encouragementType","Off");
                        stopNotification(3);
                        break;
                }



                btnInfoEncouragement.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                tvEncouragementAlarmString.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                tvEncouragementAlarmString.setText(encouragementAlarmString);
                isEncouragementInfoActive = false;
                isEncouragementNotiFreqActive = false;
                isEncouragementDayActive = false;
                btnDayWeekEncouragement.setVisibility(View.GONE);
                btnNotificationFreqEncouragement.setVisibility(View.GONE);
                btnSaveEncouragement.setVisibility(View.GONE);
                timePickerEncouragement.setVisibility(View.GONE);

                Toast.makeText(getActivity().getApplicationContext(),
                        "Encouragement Notification Settings Saved",
                        Toast.LENGTH_LONG).show();

            }
        });

        listViewPrayer = (ListView)getView().findViewById(R.id.listViewPrayer);
        listViewScripture = (ListView)getView().findViewById(R.id.listViewScripture);
        listViewPrayer.setOnItemClickListener(this);
        listViewScripture.setOnItemClickListener(this);
        btnPrayerDropdown = (Button)getView().findViewById(R.id.btnPrayerDropdown);
        btnScriptureDropdown = (Button)getView().findViewById(R.id.btnScriptureDropdown);
        btnEncouragementDropdown = (Button)getView().findViewById(R.id.btnEncouragementDropdown);
        btnEncouragementDropdown.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        btnPrayerDropdown.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        btnScriptureDropdown.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        btnEncouragementDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEncouragementDropDownActive = !isEncouragementDropDownActive;
                if(isEncouragementDropDownActive){
                    relativeLayoutEncouragementEntry.setVisibility(View.VISIBLE);
                    //btnEncouragementDropdown.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    btnEncouragementDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_up_float);
                    isPrayerDropDownActive = false;
                    isScriptureDropDownActive = false;
                    btnPrayerDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                    btnScriptureDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                    listViewPrayer.setVisibility(View.GONE);
                    listViewScripture.setVisibility(View.GONE);
                }else{
                    relativeLayoutEncouragementEntry.setVisibility(View.GONE);
                    //btnEncouragementDropdown.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    btnEncouragementDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                }
            }
        });
        btnPrayerDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPrayerDropDownActive = !isPrayerDropDownActive;
                if(isPrayerDropDownActive){
                    listViewPrayer.setVisibility(View.VISIBLE);
                    //btnPrayerDropdown.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    ArrayList<String> alarmNotifications = new ArrayList<>();
                    try {
                        intPrayerList.clear();
                        for(int i = 0; i < encouragementList.size(); i++){
                            String[] entry = encouragementList.get(i).split("/n");
                            if(entry[1].equals("Prayer") && !entry[6].equals("Alarm Off")){
                                alarmNotifications.add(encouragementList.get(i));
                                intPrayerList.add(i);
                            }
                        }
                        listViewPrayer.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, alarmNotifications));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    btnPrayerDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_up_float);
                    isEncouragementDropDownActive = false;
                    isScriptureDropDownActive = false;
                    btnEncouragementDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                    btnScriptureDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                    relativeLayoutEncouragementEntry.setVisibility(View.GONE);
                    listViewScripture.setVisibility(View.GONE);

                }else{
                    listViewPrayer.setVisibility(View.GONE);
                    //btnPrayerDropdown.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    btnPrayerDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                }
            }
        });
        btnScriptureDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isScriptureDropDownActive = !isScriptureDropDownActive;
                if(isScriptureDropDownActive){
                    listViewScripture.setVisibility(View.VISIBLE);
                    //btnScriptureDropdown.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    ArrayList<String> alarmNotifications = new ArrayList<>();
                    try {
                        intScriptureList.clear();
                        for(int i = 0; i < encouragementList.size(); i++){
                            String[] entry = encouragementList.get(i).split("/n");
                            if(entry[1].equals("Scripture") && !entry[6].equals("Alarm Off")){
                                alarmNotifications.add(encouragementList.get(i));
                                intScriptureList.add(i);
                            }
                        }
                        listViewScripture.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, alarmNotifications));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    btnScriptureDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_up_float);
                    isEncouragementDropDownActive = false;
                    isPrayerDropDownActive = false;
                    btnEncouragementDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                    btnPrayerDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                    relativeLayoutEncouragementEntry.setVisibility(View.GONE);
                    listViewPrayer.setVisibility(View.GONE);
                }else{
                    listViewScripture.setVisibility(View.GONE);
                    //btnScriptureDropdown.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    btnScriptureDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                }
            }
        });


        // This is switch that will appear if Prayer and Scripture Categories are turned off
        switchPrayerAndScripture = (Switch)getView().findViewById(R.id.switchPrayerAndScripture);
        boolean wantsPrayerAndScripture = sp.getBoolean("wantsPrayerAndScripture",true);
        if(wantsPrayerAndScripture){
            switchPrayerAndScripture.setChecked(wantsPrayerAndScripture);
            switchPrayerAndScripture.setVisibility(View.GONE);
        }else{
            RelativeLayout relativeLayoutPrayer, relativeLayoutScripture;
            ListView listViewPrayer, listViewScripture;
            relativeLayoutPrayer = (RelativeLayout)getView().findViewById(R.id.relativeLayoutPrayer);
            relativeLayoutScripture = (RelativeLayout)getView().findViewById(R.id.relativeLayoutScripture);
            listViewPrayer = (ListView)getView().findViewById(R.id.listViewPrayer);
            listViewScripture = (ListView)getView().findViewById(R.id.listViewScripture);

            relativeLayoutPrayer.setVisibility(View.GONE);
            relativeLayoutScripture.setVisibility(View.GONE);
            listViewPrayer.setVisibility(View.GONE);
            listViewScripture.setVisibility(View.GONE);
            btnPrayerDropdown.setVisibility(View.GONE);
            btnScriptureDropdown.setVisibility(View.GONE);

            switchPrayerAndScripture.setVisibility(View.VISIBLE);
            switchPrayerAndScripture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                        sp.edit().putBoolean("wantsPrayerAndScripture",b).commit();
                        Intent intent = new Intent(getContext(),MainActivity.class);
                        getActivity().finish();
                        startActivity(intent);
                    }
                }
            });
        }

//        /// THIS IS THE START OF PRAYER NOTIFICATION INITIALIZATION
//
//        tvPrayerMessage = (TextView)getView().findViewById(R.id.tvPrayerMessage);
//        tvPrayerFrom = (TextView)getView().findViewById(R.id.tvPrayerFrom);
//
//        String updatePra = loadString("needUpdateNotificationPrayer",getContext());
//        if(!updatePra.isEmpty()){
//            try{
//                needUpdateNotificationPrayer = Boolean.parseBoolean(updateEnc);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        if(needUpdateNotificationPrayer){
//            notificationPrayerList.remove(0);
//            needUpdateNotificationPrayer = false;
//            saveString("needUpdateNotificationPrayer","false");
//            saveArray(notificationPrayerList,"notificationPrayerList");
//        }
//        if(notificationPrayerList.isEmpty() || MainActivity.isFirstTimeOpening || notificationPrayerList == null){
//            Random r = new Random();
//            ArrayList<String> orderedList = new ArrayList<>();
//            for(int i = 0; i < encouragementList.size(); i++){
//                try{
//                    String[] entry = encouragementList.get(i).split("/n");
//                    String category = entry[1];
//
//                    if(category.equals("Prayer")){
//                        orderedList.add(encouragementList.get(i));
//                    }
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            int olSize = orderedList.size();
//            int i1;
//            while(olSize > 0){
//                if(olSize > 1) {
//                    i1 = r.nextInt(olSize);
//                    notificationPrayerList.add(0,orderedList.get(i1));
//                    orderedList.remove(i1);
//                }else{
//                    notificationPrayerList.add(0,orderedList.get(0));
//                    orderedList.remove(0);
//                }
//                olSize--;
//
//            }
//            saveArray(notificationPrayerList,"notificationPrayerList");
//            try{
//                for(int j = 0; j < encouragementList.size(); j++){
//                    if(notificationPrayerList.get(0).equals(encouragementList.get(j))){
//                        positionPrayer = j;
//                    }
//                }
//
//                String[] entry = notificationPrayerList.get(0).split("/n");
//                String nameAddressDate = entry[2];
//                String message = entry[3];
//
//                tvPrayerMessage.setText(message);
//                tvPrayerFrom.setText(nameAddressDate);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }else{
//            try{
//                for(int j = 0; j < encouragementList.size(); j++){
//                    if(notificationPrayerList.get(0).equals(encouragementList.get(j))){
//                        positionPrayer = j;
//                    }
//                }
//
//                String[] entry = notificationPrayerList.get(0).split("/n");
//                String nameAddressDate = entry[2];
//                String message = entry[3];
//
//                tvPrayerMessage.setText(message);
//                tvPrayerFrom.setText(nameAddressDate);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//
//        relativeLayoutPrayerEntry = (RelativeLayout)getView().findViewById(R.id.relativeLayoutPrayerEntry);
//        relativeLayoutPrayerEntry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectedEncouragement.add(0,encouragementList.get(positionPrayer));
//                saveArray(selectedEncouragement,"selectedEncouragement");
//                Intent intent = new Intent(getContext().getApplicationContext(),CurrentEncouragement.class);
//                startActivity(intent);
//            }
//        });
//
//
//
//
//        tvPrayer = (TextView)getView().findViewById(R.id.tvPrayer);
//        btnInfoPrayer = (Button)getView().findViewById(R.id.btnInfoPrayer);
//        btnNotificationFreqPrayer = (Button)getView().findViewById(R.id.btnNotificationFreqPrayer);
//        btnDayWeekPrayer = (Button)getView().findViewById(R.id.btnDayWeekPrayer);
//        timePickerPrayer = (TimePicker)getView().findViewById(R.id.timePickerPrayer);
//        btnSavePrayer = (Button)getView().findViewById(R.id.btnSavePrayer);
//
//        //TODO NEED to work for Prayer and Scripture as well
//        mPrayerNotification = PrayerNotification.DAILY;
//        btnNotificationFreqPrayer.setText("Daily Notification");
//        mPrayerDay = PrayerDay.SUNDAY;
//        btnDayWeekPrayer.setText("Sunday");
//        String prayDay = loadString("PrayerDay",getActivity().getApplicationContext());
//        String prayNotFreq = loadString("PrayerNotiFreq",getActivity().getApplicationContext());
//
//        if(prayNotFreq.equals("Daily")){
//            tvPrayer.setText("Daily Prayer");
//            mPrayerNotification = PrayerNotification.DAILY;
//            btnNotificationFreqPrayer.setText("Daily Notification");
//        }else if(prayNotFreq.equals("Weekly")){
//            tvPrayer.setText("Weekly Prayer");
//            mPrayerNotification = PrayerNotification.WEEKLY;
//            btnNotificationFreqPrayer.setText("Weekly Notification");
//        }else if(prayNotFreq.equals("None")){
//            tvPrayer.setText("Prayer");
//            mPrayerNotification = PrayerNotification.NONE;
//            btnNotificationFreqPrayer.setText("No Notification");
//        }
//
//        if(prayDay.equals("Sunday")){
//            mPrayerDay = PrayerDay.SUNDAY;
//            btnDayWeekPrayer.setText(prayDay);
//        }else if(prayDay.equals("Monday")){
//            mPrayerDay = PrayerDay.MONDAY;
//            btnDayWeekPrayer.setText(prayDay);
//        }else if(prayDay.equals("Tuesday")){
//            mPrayerDay = PrayerDay.TUESDAY;
//            btnDayWeekPrayer.setText(prayDay);
//        }else if(prayDay.equals("Wednesday")){
//            mPrayerDay = PrayerDay.WEDNESDAY;
//            btnDayWeekPrayer.setText(prayDay);
//        }else if(prayDay.equals("Thursday")){
//            mPrayerDay = PrayerDay.THURSDAY;
//            btnDayWeekPrayer.setText(prayDay);
//        }else if(prayDay.equals("Friday")){
//            mPrayerDay = PrayerDay.FRIDAY;
//            btnDayWeekPrayer.setText(prayDay);
//        }else if(prayDay.equals("Saturday")){
//            mPrayerDay = PrayerDay.SATURDAY;
//            btnDayWeekPrayer.setText(prayDay);
//        }
//
//
//        isPrayerInfoActive = false;
//        isPrayerNotiFreqActive = false;
//        isPrayerDayActive = false;
//        btnNotificationFreqPrayer.setVisibility(View.GONE);
//        btnDayWeekPrayer.setVisibility(View.GONE);
//        timePickerPrayer.setVisibility(View.GONE);
//        btnSavePrayer.setVisibility(View.GONE);
//        btnInfoPrayer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//        btnInfoPrayer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isPrayerInfoActive = !isPrayerInfoActive;
//                if(isPrayerInfoActive){
//                    btnInfoPrayer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
//                    btnNotificationFreqPrayer.setVisibility(View.VISIBLE);
//                    btnSavePrayer.setVisibility(View.VISIBLE);
//                    switch (mPrayerNotification){
//                        case DAILY:
//                            btnDayWeekPrayer.setVisibility(View.GONE);
//                            timePickerPrayer.setVisibility(View.VISIBLE);
//                            break;
//                        case WEEKLY:
//                            btnDayWeekPrayer.setVisibility(View.VISIBLE);
//                            timePickerPrayer.setVisibility(View.VISIBLE);
//                            break;
//                        case NONE:
//                            btnDayWeekPrayer.setVisibility(View.GONE);
//                            timePickerPrayer.setVisibility(View.GONE);
//                            break;
//                        default:
//                            break;
//                    }
//
//                    if (Build.VERSION.SDK_INT >= 23 ) {
//                        String hour = loadString("PrayerTimeHour",getContext());
//                        String minute = loadString("PrayerTimeMinute",getContext());
//                        if(!hour.isEmpty()){
//                            timePickerPrayer.setHour(Integer.parseInt(hour));
//                        }
//                        if(!minute.isEmpty()){
//                            timePickerPrayer.setMinute(Integer.parseInt(minute));
//                        }
//                    }else {
//                        String hour = loadString("PrayerTimeHour",getContext());
//                        String minute = loadString("PrayerTimeMinute",getContext());
//                        if(!hour.isEmpty()){
//                            timePickerPrayer.setCurrentHour(Integer.parseInt(hour));
//                        }
//                        if(!minute.isEmpty()){
//                            timePickerPrayer.setCurrentMinute(Integer.parseInt(minute));
//                        }
//                    }
//
//
//                }else{
//                    btnNotificationFreqPrayer.setVisibility(View.GONE);
//                    btnDayWeekPrayer.setVisibility(View.GONE);
//                    timePickerPrayer.setVisibility(View.GONE);
//                    btnSavePrayer.setVisibility(View.GONE);
//                    btnInfoPrayer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//                }
//            }
//        });
//        btnNotificationFreqPrayer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isPrayerNotiFreqActive = true;
//                isPrayerDayActive = false;
//                isEncouragementDayActive = false;
//                isEncouragementNotiFreqActive = false;
//                isScriptureDayActive = false;
//                isScriptureNotiFreqActive = false;
//                showPopUp(view);
//            }
//        });
//        btnDayWeekPrayer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isPrayerNotiFreqActive = false;
//                isPrayerDayActive = true;
//                isEncouragementDayActive = false;
//                isEncouragementNotiFreqActive = false;
//                isScriptureDayActive = false;
//                isScriptureNotiFreqActive = false;
//                showPopUp(view);
//            }
//        });
//        btnSavePrayer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int day = 0;
//                int hour = 1;
//                int minute = 1;
//
//                if (Build.VERSION.SDK_INT >= 23 ) {
//                    saveString("PrayerTimeHour",Integer.toString(timePickerPrayer.getHour()));
//                    saveString("PrayerTimeMinute",Integer.toString(timePickerPrayer.getMinute()));
//                    hour = timePickerPrayer.getHour();
//                    minute = timePickerPrayer.getMinute();
//                }else {
//                    saveString("PrayerTimeHour",Integer.toString(timePickerPrayer.getCurrentHour()));
//                    saveString("PrayerTimeMinute",Integer.toString(timePickerPrayer.getCurrentMinute()));
//                    hour = timePickerPrayer.getCurrentHour();
//                    minute = timePickerPrayer.getCurrentMinute();
//                }
//                switch (mPrayerDay){
//                    case SUNDAY:
//                        saveString("PrayerDay","Sunday");
//                        day = 1;
//                        break;
//                    case MONDAY:
//                        saveString("PrayerDay","Monday");
//                        day = 2;
//                        break;
//                    case TUESDAY:
//                        saveString("PrayerDay","Tuesday");
//                        day = 3;
//                        break;
//                    case WEDNESDAY:
//                        saveString("PrayerDay","Wednesday");
//                        day = 4;
//                        break;
//                    case THURSDAY:
//                        saveString("PrayerDay","Thursday");
//                        day = 5;
//                        break;
//                    case FRIDAY:
//                        saveString("PrayerDay","Friday");
//                        day = 6;
//                        break;
//                    case SATURDAY:
//                        saveString("PrayerDay","Saturday");
//                        day = 7;
//                        break;
//                }
//                switch (mPrayerNotification){
//                    case DAILY:
//                        saveString("PrayerNotiFreq","Daily");
//                        setDailyAlarm("Daily",day,hour,minute,4,encouragementList.get(positionPrayer),Integer.toString(positionPrayer));
//                        break;
//                    case WEEKLY:
//                        saveString("PrayerNotiFreq","Weekly");
//                        setDailyAlarm("Weekly",day,hour,minute,4,encouragementList.get(positionPrayer),Integer.toString(positionPrayer));
//                        break;
//                    case NONE:
//                        saveString("PrayerNotiFreq","None");
//                        stopNotification(4);
//                        break;
//                }
//
//
//
//                btnInfoPrayer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//                isPrayerInfoActive = false;
//                isPrayerNotiFreqActive = false;
//                isPrayerDayActive = false;
//                btnDayWeekPrayer.setVisibility(View.GONE);
//                btnNotificationFreqPrayer.setVisibility(View.GONE);
//                btnSavePrayer.setVisibility(View.GONE);
//                timePickerPrayer.setVisibility(View.GONE);
//
//                Toast.makeText(getActivity().getApplicationContext(),
//                        "Prayer Notification Settings Saved",
//                        Toast.LENGTH_LONG).show();
//
//            }
//        });
//
//
//        /// THIS IS THE START OF SCRIPTURE NOTIFICATION INITIALIZATION
//
//        tvScriptureMessage = (TextView)getView().findViewById(R.id.tvScriptureMessage);
//        tvScriptureFrom = (TextView)getView().findViewById(R.id.tvScriptureFrom);
//
//        String updateScr = loadString("needUpdateNotificationScripture",getContext());
//        if(!updateScr.isEmpty()){
//            try{
//                needUpdateNotificationScripture = Boolean.parseBoolean(updateScr);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        if(needUpdateNotificationScripture){
//            notificationScriptureList.remove(0);
//            needUpdateNotificationScripture = false;
//            saveString("needUpdateNotificationScripture","false");
//            saveArray(notificationScriptureList,"notificationScriptureList");
//        }
//        if(notificationScriptureList.isEmpty() || MainActivity.isFirstTimeOpening || notificationScriptureList == null){
//            Random r = new Random();
//            ArrayList<String> orderedList = new ArrayList<>();
//            for(int i = 0; i < encouragementList.size(); i++){
//                try{
//                    String[] entry = encouragementList.get(i).split("/n");
//                    String category = entry[1];
//
//                    if(category.equals("Scripture")){
//                        orderedList.add(encouragementList.get(i));
//                    }
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            int olSize = orderedList.size();
//            int i1;
//            while(olSize > 0){
//                if(olSize > 1) {
//                    i1 = r.nextInt(olSize);
//                    notificationScriptureList.add(0,orderedList.get(i1));
//                    orderedList.remove(i1);
//                }else{
//                    notificationScriptureList.add(0,orderedList.get(0));
//                    orderedList.remove(0);
//                }
//                olSize--;
//
//            }
//            saveArray(notificationScriptureList,"notificationScriptureList");
//            try{
//                for(int j = 0; j < encouragementList.size(); j++){
//                    if(notificationScriptureList.get(0).equals(encouragementList.get(j))){
//                        positionScripture = j;
//                    }
//                }
//
//                String[] entry = notificationScriptureList.get(0).split("/n");
//                String nameAddressDate = entry[2];
//                String message = entry[3];
//
//                tvScriptureMessage.setText(message);
//                tvScriptureFrom.setText(nameAddressDate);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }else{
//            try{
//                for(int j = 0; j < encouragementList.size(); j++){
//                    if(notificationScriptureList.get(0).equals(encouragementList.get(j))){
//                        positionScripture = j;
//                    }
//                }
//
//                String[] entry = notificationScriptureList.get(0).split("/n");
//                String nameAddressDate = entry[2];
//                String message = entry[3];
//
//                tvScriptureMessage.setText(message);
//                tvScriptureFrom.setText(nameAddressDate);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//
//        relativeLayoutScriptureEntry = (RelativeLayout)getView().findViewById(R.id.relativeLayoutScriptureEntry);
//        relativeLayoutScriptureEntry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectedEncouragement.add(0,encouragementList.get(positionScripture));
//                saveArray(selectedEncouragement,"selectedEncouragement");
//                Intent intent = new Intent(getContext().getApplicationContext(),CurrentEncouragement.class);
//                startActivity(intent);
//            }
//        });
//
//
//
//
//        tvScripture = (TextView)getView().findViewById(R.id.tvScripture);
//        btnInfoScripture = (Button)getView().findViewById(R.id.btnInfoScripture);
//        btnNotificationFreqScripture = (Button)getView().findViewById(R.id.btnNotificationFreqScripture);
//        btnDayWeekScripture = (Button)getView().findViewById(R.id.btnDayWeekScripture);
//        timePickerScripture = (TimePicker)getView().findViewById(R.id.timePickerScripture);
//        btnSaveScripture = (Button)getView().findViewById(R.id.btnSaveScripture);
//
//        //TODO NEED to work for Prayer and Scripture as well
//        mScriptureNotification = ScriptureNotification.DAILY;
//        btnNotificationFreqScripture.setText("Daily Notification");
//        mScriptureDay = ScriptureDay.SUNDAY;
//        btnDayWeekScripture.setText("Sunday");
//        String scrDay = loadString("ScriptureDay",getActivity().getApplicationContext());
//        String scrNotFreq = loadString("ScriptureNotiFreq",getActivity().getApplicationContext());
//
//        if(scrNotFreq.equals("Daily")){
//            tvScripture.setText("Daily Scripture");
//            mScriptureNotification = ScriptureNotification.DAILY;
//            btnNotificationFreqScripture.setText("Daily Notification");
//        }else if(scrNotFreq.equals("Weekly")){
//            tvScripture.setText("Weekly Scripture");
//            mScriptureNotification = ScriptureNotification.WEEKLY;
//            btnNotificationFreqScripture.setText("Weekly Notification");
//        }else if(scrNotFreq.equals("None")){
//            tvScripture.setText("Scripture");
//            mScriptureNotification = ScriptureNotification.NONE;
//            btnNotificationFreqScripture.setText("No Notification");
//        }
//
//        if(scrDay.equals("Sunday")){
//            mScriptureDay = ScriptureDay.SUNDAY;
//            btnDayWeekScripture.setText(scrDay);
//        }else if(scrDay.equals("Monday")){
//            mScriptureDay = ScriptureDay.MONDAY;
//            btnDayWeekScripture.setText(scrDay);
//        }else if(scrDay.equals("Tuesday")){
//            mScriptureDay = ScriptureDay.TUESDAY;
//            btnDayWeekScripture.setText(scrDay);
//        }else if(scrDay.equals("Wednesday")){
//            mScriptureDay = ScriptureDay.WEDNESDAY;
//            btnDayWeekScripture.setText(scrDay);
//        }else if(scrDay.equals("Thursday")){
//            mScriptureDay = ScriptureDay.THURSDAY;
//            btnDayWeekScripture.setText(scrDay);
//        }else if(scrDay.equals("Friday")){
//            mScriptureDay = ScriptureDay.FRIDAY;
//            btnDayWeekScripture.setText(scrDay);
//        }else if(scrDay.equals("Saturday")){
//            mScriptureDay = ScriptureDay.SATURDAY;
//            btnDayWeekScripture.setText(scrDay);
//        }
//
//
//        isScriptureInfoActive = false;
//        isScriptureNotiFreqActive = false;
//        isScriptureDayActive = false;
//        btnNotificationFreqScripture.setVisibility(View.GONE);
//        btnDayWeekScripture.setVisibility(View.GONE);
//        timePickerScripture.setVisibility(View.GONE);
//        btnSaveScripture.setVisibility(View.GONE);
//        btnInfoScripture.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//        btnInfoScripture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isScriptureInfoActive = !isScriptureInfoActive;
//                if(isScriptureInfoActive){
//                    btnInfoScripture.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
//                    btnNotificationFreqScripture.setVisibility(View.VISIBLE);
//                    btnSaveScripture.setVisibility(View.VISIBLE);
//                    switch (mScriptureNotification){
//                        case DAILY:
//                            btnDayWeekScripture.setVisibility(View.GONE);
//                            timePickerScripture.setVisibility(View.VISIBLE);
//                            break;
//                        case WEEKLY:
//                            btnDayWeekScripture.setVisibility(View.VISIBLE);
//                            timePickerScripture.setVisibility(View.VISIBLE);
//                            break;
//                        case NONE:
//                            btnDayWeekScripture.setVisibility(View.GONE);
//                            timePickerScripture.setVisibility(View.GONE);
//                            break;
//                        default:
//                            break;
//                    }
//
//                    if (Build.VERSION.SDK_INT >= 23 ) {
//                        String hour = loadString("ScriptureTimeHour",getContext());
//                        String minute = loadString("ScriptureTimeMinute",getContext());
//                        if(!hour.isEmpty()){
//                            timePickerScripture.setHour(Integer.parseInt(hour));
//                        }
//                        if(!minute.isEmpty()){
//                            timePickerScripture.setMinute(Integer.parseInt(minute));
//                        }
//                    }else {
//                        String hour = loadString("ScriptureTimeHour",getContext());
//                        String minute = loadString("ScriptureTimeMinute",getContext());
//                        if(!hour.isEmpty()){
//                            timePickerScripture.setCurrentHour(Integer.parseInt(hour));
//                        }
//                        if(!minute.isEmpty()){
//                            timePickerScripture.setCurrentMinute(Integer.parseInt(minute));
//                        }
//                    }
//
//
//                }else{
//                    btnNotificationFreqScripture.setVisibility(View.GONE);
//                    btnDayWeekScripture.setVisibility(View.GONE);
//                    timePickerScripture.setVisibility(View.GONE);
//                    btnSaveScripture.setVisibility(View.GONE);
//                    btnInfoScripture.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//                }
//            }
//        });
//        btnNotificationFreqScripture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isPrayerNotiFreqActive = false;
//                isPrayerDayActive = false;
//                isEncouragementDayActive = false;
//                isEncouragementNotiFreqActive = false;
//                isScriptureDayActive = false;
//                isScriptureNotiFreqActive = true;
//                showPopUp(view);
//            }
//        });
//        btnDayWeekScripture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isPrayerNotiFreqActive = false;
//                isPrayerDayActive = false;
//                isEncouragementDayActive = false;
//                isEncouragementNotiFreqActive = false;
//                isScriptureDayActive = true;
//                isScriptureNotiFreqActive = false;
//                showPopUp(view);
//            }
//        });
//        btnSaveScripture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int day = 0;
//                int hour = 1;
//                int minute = 1;
//
//                if (Build.VERSION.SDK_INT >= 23 ) {
//                    saveString("ScriptureTimeHour",Integer.toString(timePickerScripture.getHour()));
//                    saveString("ScriptureTimeMinute",Integer.toString(timePickerScripture.getMinute()));
//                    hour = timePickerScripture.getHour();
//                    minute = timePickerScripture.getMinute();
//                }else {
//                    saveString("ScriptureTimeHour",Integer.toString(timePickerScripture.getCurrentHour()));
//                    saveString("ScriptureTimeMinute",Integer.toString(timePickerScripture.getCurrentMinute()));
//                    hour = timePickerScripture.getCurrentHour();
//                    minute = timePickerScripture.getCurrentMinute();
//                }
//                switch (mScriptureDay){
//                    case SUNDAY:
//                        saveString("ScriptureDay","Sunday");
//                        day = 1;
//                        break;
//                    case MONDAY:
//                        saveString("ScriptureDay","Monday");
//                        day = 2;
//                        break;
//                    case TUESDAY:
//                        saveString("ScriptureDay","Tuesday");
//                        day = 3;
//                        break;
//                    case WEDNESDAY:
//                        saveString("ScriptureDay","Wednesday");
//                        day = 4;
//                        break;
//                    case THURSDAY:
//                        saveString("ScriptureDay","Thursday");
//                        day = 5;
//                        break;
//                    case FRIDAY:
//                        saveString("ScriptureDay","Friday");
//                        day = 6;
//                        break;
//                    case SATURDAY:
//                        saveString("ScriptureDay","Saturday");
//                        day = 7;
//                        break;
//                }
//                switch (mScriptureNotification){
//                    case DAILY:
//                        saveString("ScriptureNotiFreq","Daily");
//                        setDailyAlarm("Daily",day,hour,minute,5,encouragementList.get(positionScripture),Integer.toString(positionScripture));
//                        break;
//                    case WEEKLY:
//                        saveString("ScriptureNotiFreq","Weekly");
//                        setDailyAlarm("Weekly",day,hour,minute,5,encouragementList.get(positionScripture),Integer.toString(positionScripture));
//                        break;
//                    case NONE:
//                        saveString("ScriptureNotiFreq","None");
//                        stopNotification(5);
//                        break;
//                }
//
//
//
//                btnInfoScripture.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//                isScriptureInfoActive = false;
//                isScriptureNotiFreqActive = false;
//                isScriptureDayActive = false;
//                btnDayWeekScripture.setVisibility(View.GONE);
//                btnNotificationFreqScripture.setVisibility(View.GONE);
//                btnSaveScripture.setVisibility(View.GONE);
//                timePickerScripture.setVisibility(View.GONE);
//
//                Toast.makeText(getActivity().getApplicationContext(),
//                        "Scripture Notification Settings Saved",
//                        Toast.LENGTH_LONG).show();
//
//            }
//        });
    }

    @Override
    public void onItemClick(AdapterView<?>parent,View view,int position,long id){
        if(isPrayerDropDownActive){
            selectedEncouragement.add(0,encouragementList.get(intPrayerList.get(position)));
            saveArray(selectedEncouragement,"selectedEncouragement");
            Intent intent = new Intent(getContext(),CurrentEncouragement.class);
            startActivity(intent);
        }else if(isScriptureDropDownActive){
            selectedEncouragement.add(0,encouragementList.get(intScriptureList.get(position)));
            saveArray(selectedEncouragement,"selectedEncouragement");
            Intent intent = new Intent(getContext(),CurrentEncouragement.class);
            startActivity(intent);
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


        Intent alertIntent = new Intent(getContext(),AlertReceiver.class);
        alertIntent.putExtra("msg",msg);
        alertIntent.putExtra("msgCategory",category);
        alertIntent.putExtra("msgTitle", category + subCategory);
        alertIntent.putExtra("msgText",message);
        alertIntent.putExtra("msgAlert","New " + category + " Notification");
        alertIntent.putExtra("msgPos",pos);
        alertIntent.putExtra("notifyID",notifyID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext().getApplicationContext(),notifyID,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        try {
            if(!notificationEncouragementList.get(0).equals("/nEncouragement/n /nNo Entries in Encouragement/nnone/n3/nAlarm Off")) {
                for (int l = 0; l < notificationEncouragementList.size(); l++) {
                    String[] encEntry = notificationEncouragementList.get(l).split("/n");
                    encEntry[6] = "Alarm " + notificationType;
                    String revisedMsg = "/n" + encEntry[1] + "/n" + encEntry[2] + "/n" + encEntry[3] +
                            "/n" + encEntry[4] + "/n" + encEntry[5] + "/n" + encEntry[6];
                    notificationEncouragementList.set(l, revisedMsg);
                }
                saveArray(notificationEncouragementList, "notificationEncouragementList");
            }

            loadArray(alarmList, getContext(), "alarmList");
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

//        Toast.makeText(getContext(),Long.toString(calendar.getTimeInMillis()),Toast.LENGTH_LONG).show();
//        Toast.makeText(getContext(),Long.toString(System.currentTimeMillis()),Toast.LENGTH_LONG).show();



        AlarmManager alarmManager = (AlarmManager)
                getContext().getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        if(notificationType.equals("Daily")) {
            // if calendar time has already passed then add a day to the time
            if(calendar.getTimeInMillis() < System.currentTimeMillis()){
                calendar.setTimeInMillis(calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY);
//                Toast.makeText(getContext(),"Time Added",Toast.LENGTH_LONG).show();

            }

            // alarm setWindow
            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
//            alarmManager.setWindow(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }else if(notificationType.equals("Weekly")){
            // if calendar time has already passed then add a week to the time
            if(calendar.getTimeInMillis() < System.currentTimeMillis()){
                calendar.setTimeInMillis(calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY*7);
//                Toast.makeText(getContext(),"Time Added",Toast.LENGTH_LONG).show();
            }

            // alarm setWindow
            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
//            alarmManager.setWindow(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                    AlarmManager.INTERVAL_DAY*7, pendingIntent);
        }

    }

    public void stopNotification(int notifID){
        Intent intentstop = new Intent(getContext(), AlertReceiver.class);
        PendingIntent senderstop = PendingIntent.getBroadcast(getContext(),
                notifID, intentstop, 0);
        AlarmManager alarmManagerstop = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);

        alarmManagerstop.cancel(senderstop);
    }


//    public void setAlarm1(View view){
//        Toast.makeText(getContext().getApplicationContext(),"Setting Alarm 5 seconds",Toast.LENGTH_SHORT).show();
//        //this is 5 seconds
//        long alertTime = new GregorianCalendar().getTimeInMillis();
//        alertTime += 5*1000;
//
//
//        Intent alertIntent = new Intent(getContext(),AlertReceiver.class);
//        alertIntent.putExtra("msg","This is msg for 5 seconds");
//        alertIntent.putExtra("msgText","This is msgText for 5 seconds");
//        alertIntent.putExtra("msgAlert","This is msgAlert for 5 seconds");
//        alertIntent.putExtra("msgSub","This is msgSub for 5 seconds");
//        alertIntent.putExtra("notifyID",1);
//
//        AlarmManager alarmManager = (AlarmManager)
//                getContext().getSystemService(Context.ALARM_SERVICE);
//
//        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime,
//                PendingIntent.getBroadcast(getContext(),1,alertIntent,
//                        0));
//
//    }
//
//    public void setAlarm2(View view){
//        Toast.makeText(getContext().getApplicationContext(),"Setting Alarm 10 seconds",Toast.LENGTH_SHORT).show();
//        //this is 5 seconds
//        long alertTime = new GregorianCalendar().getTimeInMillis();
//        alertTime += 10*1000;
//
//
//        Intent alertIntent = new Intent(getContext(),AlertReceiver.class);
//        alertIntent.putExtra("msg","This is msg for 10 seconds");
//        alertIntent.putExtra("msgText","This is msgText for 10 seconds");
//        alertIntent.putExtra("msgAlert","This is msgAlert for 10 seconds");
//        alertIntent.putExtra("msgSub","This is msgSub for 10 seconds");
//        alertIntent.putExtra("notifyID",2);
//
//        AlarmManager alarmManager = (AlarmManager)
//                getContext().getSystemService(Context.ALARM_SERVICE);
//
//        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime,
//                PendingIntent.getBroadcast(getContext(),2,alertIntent,
//                        0));
//
//    }


    public void showPopUp(View v){
        PopupMenu popupMenu = new PopupMenu(getActivity(),v,1,0,R.style.PopupMenu);

        popupMenu.setOnMenuItemClickListener(HomeFragment.this);
        MenuInflater inflater = popupMenu.getMenuInflater();

        inflater.inflate(R.menu.popup_home_menu, popupMenu.getMenu());

        if(isEncouragementNotiFreqActive || isPrayerNotiFreqActive || isScriptureNotiFreqActive){
            for(int i = 3; i <= 9; i++) {
                popupMenu.getMenu().getItem(i).setVisible(false);
            }
        }
        if(isEncouragementDayActive || isPrayerDayActive || isScriptureDayActive){
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
                if(isEncouragementNotiFreqActive) {
                    tvEncouragement.setText("Daily Encouragement");
                    mEncouragementNotification = EncouragementNotification.DAILY;
                    btnNotificationFreqEncouragement.setText("Daily Notification");
                    btnDayWeekEncouragement.setVisibility(View.GONE);
                    timePickerEncouragement.setVisibility(View.VISIBLE);
                    isEncouragementNotiFreqActive = false;
                }
//                }else if(isPrayerNotiFreqActive) {
//                    tvPrayer.setText("Daily Prayer");
//                    mPrayerNotification = PrayerNotification.DAILY;
//                    btnNotificationFreqPrayer.setText("Daily Notification");
//                    btnDayWeekPrayer.setVisibility(View.GONE);
//                    timePickerPrayer.setVisibility(View.VISIBLE);
//                    isPrayerNotiFreqActive = false;
//                }else if(isScriptureNotiFreqActive) {
//                    tvScripture.setText("Daily Scripture");
//                    mScriptureNotification = ScriptureNotification.DAILY;
//                    btnNotificationFreqScripture.setText("Daily Notification");
//                    btnDayWeekScripture.setVisibility(View.GONE);
//                    timePickerScripture.setVisibility(View.VISIBLE);
//                    isScriptureNotiFreqActive = false;
//                }
                return true;
            case R.id.popup_weeklyNotification:
                if(isEncouragementNotiFreqActive) {
                    tvEncouragement.setText("Weekly Encouragement");
                    mEncouragementNotification = EncouragementNotification.WEEKLY;
                    btnNotificationFreqEncouragement.setText("Weekly Notification");
                    btnDayWeekEncouragement.setVisibility(View.VISIBLE);
                    timePickerEncouragement.setVisibility(View.VISIBLE);
                    isEncouragementNotiFreqActive = false;
                }
//                }else if(isPrayerNotiFreqActive) {
//                    tvPrayer.setText("Weekly Prayer");
//                    mPrayerNotification = PrayerNotification.WEEKLY;
//                    btnNotificationFreqPrayer.setText("Weekly Notification");
//                    btnDayWeekPrayer.setVisibility(View.VISIBLE);
//                    timePickerPrayer.setVisibility(View.VISIBLE);
//                    isPrayerNotiFreqActive = false;
//                }else if(isScriptureNotiFreqActive) {
//                    tvScripture.setText("Weekly Scripture");
//                    mScriptureNotification = ScriptureNotification.WEEKLY;
//                    btnNotificationFreqScripture.setText("Weekly Notification");
//                    btnDayWeekScripture.setVisibility(View.VISIBLE);
//                    timePickerScripture.setVisibility(View.VISIBLE);
//                    isScriptureNotiFreqActive = false;
//                }
                return true;
            case R.id.popup_noNotification:
                if(isEncouragementNotiFreqActive) {
                    tvEncouragement.setText("Encouragement");
                    mEncouragementNotification = EncouragementNotification.NONE;
                    btnNotificationFreqEncouragement.setText("No Notification");
                    btnDayWeekEncouragement.setVisibility(View.GONE);
                    timePickerEncouragement.setVisibility(View.GONE);
                    isEncouragementNotiFreqActive = false;
                }
//                }else if(isPrayerNotiFreqActive) {
//                    tvPrayer.setText("Prayer");
//                    mPrayerNotification = PrayerNotification.NONE;
//                    btnNotificationFreqPrayer.setText("No Notification");
//                    btnDayWeekPrayer.setVisibility(View.GONE);
//                    timePickerPrayer.setVisibility(View.GONE);
//                    isPrayerNotiFreqActive = false;
//                }else if(isScriptureNotiFreqActive) {
//                    tvScripture.setText("Scripture");
//                    mScriptureNotification = ScriptureNotification.NONE;
//                    btnNotificationFreqScripture.setText("No Notification");
//                    btnDayWeekScripture.setVisibility(View.GONE);
//                    timePickerScripture.setVisibility(View.GONE);
//                    isScriptureNotiFreqActive = false;
//                }
                return true;
            case R.id.popup_sunday:
                if(isEncouragementDayActive) {
                    btnDayWeekEncouragement.setText("Sunday");
                    mEncouragementDay = EncouragementDay.SUNDAY;
                    isEncouragementDayActive = false;
                }
//                }else if(isPrayerDayActive) {
//                    btnDayWeekPrayer.setText("Sunday");
//                    mPrayerDay = PrayerDay.SUNDAY;
//                    isPrayerDayActive = false;
//                }else if(isScriptureDayActive) {
//                    btnDayWeekScripture.setText("Sunday");
//                    mScriptureDay = ScriptureDay.SUNDAY;
//                    isScriptureDayActive = false;
//                }
                return true;
            case R.id.popup_monday:
                if(isEncouragementDayActive) {
                    btnDayWeekEncouragement.setText("Monday");
                    mEncouragementDay = EncouragementDay.MONDAY;
                    isEncouragementDayActive = false;
                }
//                }else if(isPrayerDayActive) {
//                    btnDayWeekPrayer.setText("Monday");
//                    mPrayerDay = PrayerDay.MONDAY;
//                    isPrayerDayActive = false;
//                }else if(isScriptureDayActive) {
//                    btnDayWeekScripture.setText("Monday");
//                    mScriptureDay = ScriptureDay.MONDAY;
//                    isScriptureDayActive = false;
//                }
                return true;
            case R.id.popup_tuesday:
                if(isEncouragementDayActive) {
                    btnDayWeekEncouragement.setText("Tuesday");
                    mEncouragementDay = EncouragementDay.TUESDAY;
                    isEncouragementDayActive = false;
                }
//                }else if(isPrayerDayActive) {
//                    btnDayWeekPrayer.setText("Tuesday");
//                    mPrayerDay = PrayerDay.TUESDAY;
//                    isPrayerDayActive = false;
//                }else if(isScriptureDayActive) {
//                    btnDayWeekScripture.setText("Tuesday");
//                    mScriptureDay = ScriptureDay.TUESDAY;
//                    isScriptureDayActive = false;
//                }
                return true;
            case R.id.popup_wednesday:
                if(isEncouragementDayActive) {
                    btnDayWeekEncouragement.setText("Wednesday");
                    mEncouragementDay = EncouragementDay.WEDNESDAY;
                    isEncouragementDayActive = false;
                }
//                }else if(isPrayerDayActive) {
//                    btnDayWeekPrayer.setText("Wednesday");
//                    mPrayerDay = PrayerDay.WEDNESDAY;
//                    isPrayerDayActive = false;
//                }else if(isScriptureDayActive) {
//                    btnDayWeekScripture.setText("Wednesday");
//                    mScriptureDay = ScriptureDay.WEDNESDAY;
//                    isScriptureDayActive = false;
//                }
                return true;
            case R.id.popup_thursday:
                if(isEncouragementDayActive) {
                    btnDayWeekEncouragement.setText("Thursday");
                    mEncouragementDay = EncouragementDay.THURSDAY;
                    isEncouragementDayActive = false;
                }
//                }else if(isPrayerDayActive) {
//                    btnDayWeekPrayer.setText("Thursday");
//                    mPrayerDay = PrayerDay.THURSDAY;
//                    isPrayerDayActive = false;
//                }else if(isScriptureDayActive) {
//                    btnDayWeekScripture.setText("Thursday");
//                    mScriptureDay = ScriptureDay.THURSDAY;
//                    isScriptureDayActive = false;
//                }
                return true;
            case R.id.popup_friday:
                if(isEncouragementDayActive) {
                    btnDayWeekEncouragement.setText("Friday");
                    mEncouragementDay = EncouragementDay.FRIDAY;
                    isEncouragementDayActive = false;
                }
//                }else if(isPrayerDayActive) {
//                    btnDayWeekPrayer.setText("Friday");
//                    mPrayerDay = PrayerDay.FRIDAY;
//                    isPrayerDayActive = false;
//                }else if(isScriptureDayActive) {
//                    btnDayWeekScripture.setText("Friday");
//                    mScriptureDay = ScriptureDay.FRIDAY;
//                    isScriptureDayActive = false;
//                }
                return true;
            case R.id.popup_saturday:
                if(isEncouragementDayActive) {
                    btnDayWeekEncouragement.setText("Saturday");
                    mEncouragementDay = EncouragementDay.SATURDAY;
                    isEncouragementDayActive = false;
                }
//                }else if(isPrayerDayActive) {
//                    btnDayWeekPrayer.setText("Saturday");
//                    mPrayerDay = PrayerDay.SATURDAY;
//                    isPrayerDayActive = false;
//                }else if(isScriptureDayActive) {
//                    btnDayWeekScripture.setText("Saturday");
//                    mScriptureDay = ScriptureDay.SATURDAY;
//                    isScriptureDayActive = false;
//                }
                return true;
            default:
                return false;
        }
    }


    private boolean saveArray(List<String> sKey, String arrayName){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
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

    private boolean saveString(String stringName,String sKey){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
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


    public class ViewHolder{
        //ImageView thumbnail;
        TextView title;
        TextView title2;
        Button button;
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;

        public MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;

            if(convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                convertView = layoutInflater.inflate(layout,parent,false);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView)convertView.findViewById(R.id.list_item_textview);
                viewHolder.title2 = (TextView)convertView.findViewById(R.id.list_item_textview2);
                viewHolder.button = (Button)convertView.findViewById(R.id.list_item_btn);
                convertView.setTag(viewHolder);
            }




            mainViewHolder = (ViewHolder)convertView.getTag();
            mainViewHolder.button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.ic_media_play);
            mainViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isPrayerDropDownActive){
                        selectedEncouragement.add(0,encouragementList.get(intPrayerList.get(position)));
                        saveArray(selectedEncouragement,"selectedEncouragement");
                        Intent intent = new Intent(getContext(),CurrentEncouragement.class);
                        startActivity(intent);
                    }else if(isScriptureDropDownActive){
                        selectedEncouragement.add(0,encouragementList.get(intScriptureList.get(position)));
                        saveArray(selectedEncouragement,"selectedEncouragement");
                        Intent intent = new Intent(getContext(),CurrentEncouragement.class);
                        startActivity(intent);
                    }
                }
            });

            try{
                String[] entry = getItem(position).split("/n");
                String title = entry[3];
                String title2 = entry[2] + "\n" + entry[6];
                mainViewHolder.title.setText(title);
                mainViewHolder.title2.setText(title2);
            }catch (Exception e){
                e.printStackTrace();
            }


            return convertView;
        }
    }

}
