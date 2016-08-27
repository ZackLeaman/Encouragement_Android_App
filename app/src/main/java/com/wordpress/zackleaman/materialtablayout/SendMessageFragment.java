package com.wordpress.zackleaman.materialtablayout;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendMessageFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener,
        PopupMenu.OnMenuItemClickListener{

    private Button sendSmsBtn, btnCancelContact, btnSyncContacts;
    private EditText toPhoneNumber;
    private EditText smsMessageET;
    private EditText etTo;
    private String scAddress;
    private String destinationAddress;

    private RelativeLayout relativeLayoutSend, relativeLayoutAdd;
    private Button btnSendDropdown, btnAddDropdown;
    private Button btnCategorySub, btnNotificationTypeAdd, btnNotificationDayAdd;
    private Button btnCreateAdd, btnClearAdd;
    private EditText etMessageAdd;
    private TextView tvNotificationTitleAdd;
    private TimePicker timePickerNotificationAdd;

    private Boolean isSendActive, isAddActive;


    private String category = "";
    private String nameAddressDate = "Me on ";
    private String message = "";
    private String subCategory = "";
    private String notifID = "";
    private String notifAlarmString = "";
    private int notID = 0;
    private ArrayList<String> encouragementList = new ArrayList<>();
    private ArrayList<String> categoriesList = new ArrayList<>();
    private ArrayList<String> categoriesListPrayer = new ArrayList<>();
    private ArrayList<String> categoriesListScripture = new ArrayList<>();
    private ArrayList<String> alarmList = new ArrayList<>();
    private enum Notification{DAILY,WEEKLY,NONE}
    private enum NotificationDay{SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY}
    private Notification mNotification;
    private NotificationDay mNotificationDay;
    private boolean isNotifFreqActive, isNotifDayActive, isCategorySubActive;
    private int hour, minute;
    private boolean wantsPrayerAndScripture;


    public static ArrayAdapter<String> adapter;
    // Store contacts values in these arraylist
    public static ArrayList<String> phoneValueArr = new ArrayList<String>();
    public static ArrayList<String> nameValueArr = new ArrayList<String>();



    // Initialize variables

    private AutoCompleteTextView textView=null;


    private EditText toNumber=null;
    private String name = "";
    private String toNumberValue="";

    public SendMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.d("ActivityCreated","line 71");
        isSendActive = true;
        isAddActive = false;
        isCategorySubActive = false;
        isNotifDayActive = false;
        isNotifFreqActive = false;
        relativeLayoutAdd = (RelativeLayout)getView().findViewById(R.id.RelativeLayoutNotificationAdd);
        relativeLayoutSend = (RelativeLayout)getView().findViewById(R.id.RelativeLayoutSend);
        btnSendDropdown = (Button)getView().findViewById(R.id.btnSendDropdown);
        btnAddDropdown = (Button)getView().findViewById(R.id.btnAddDropdown);
        btnCategorySub = (Button)getView().findViewById(R.id.btnCategorySub);
        btnNotificationTypeAdd = (Button)getView().findViewById(R.id.btnNotificationTypeAdd);
        btnNotificationDayAdd = (Button)getView().findViewById(R.id.btnNotificationDayAdd);
        btnCreateAdd = (Button)getView().findViewById(R.id.btnCreateAdd);
        btnClearAdd = (Button)getView().findViewById(R.id.btnClearAdd);
        etMessageAdd = (EditText)getView().findViewById(R.id.etMessageAdd);
        tvNotificationTitleAdd = (TextView)getView().findViewById(R.id.tvNotificationTitleAdd);
        timePickerNotificationAdd = (TimePicker)getView().findViewById(R.id.timePickerNotificationAdd);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        wantsPrayerAndScripture = sp.getBoolean("wantsPrayerAndScripture",true);

        btnSendDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_up_float);
        btnAddDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
        relativeLayoutAdd.setVisibility(View.GONE);

        btnSendDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSendActive){
                    isSendActive = false;
                    relativeLayoutSend.setVisibility(View.GONE);
                    btnSendDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                }else{
                    isSendActive = true;
                    isAddActive = false;
                    relativeLayoutSend.setVisibility(View.VISIBLE);
                    relativeLayoutAdd.setVisibility(View.GONE);
                    btnSendDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_up_float);
                    btnAddDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                }
            }
        });
        btnAddDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAddActive){
                    isAddActive = false;
                    relativeLayoutAdd.setVisibility(View.GONE);
                    btnAddDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                }else{
                    isAddActive = true;
                    isSendActive = false;
                    relativeLayoutSend.setVisibility(View.GONE);
                    relativeLayoutAdd.setVisibility(View.VISIBLE);
                    btnSendDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                    btnAddDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_up_float);
                    notID = findNextNotifID();
                }

            }
        });


        loadArray(categoriesList, getContext(), "categoriesList");
        loadArray(categoriesListPrayer,getContext(),"categoriesListPrayer");
        loadArray(categoriesListScripture,getContext(),"categoriesListScripture");
        loadArray(encouragementList, getContext(), "encouragementList");
        loadArray(alarmList, getContext(), "alarmList");

        category = "Encouragement";
        nameAddressDate = "Me on ";
        message = "";
        subCategory = "none";
        notifID = "3";
        notifAlarmString = "Alarm Off";
        notID = findNextNotifID();

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String dateText = format.format(date);
        nameAddressDate = nameAddressDate + dateText;

        btnNotificationTypeAdd.setText("No Notification");
        mNotification = Notification.NONE;
        btnNotificationDayAdd.setText("Sunday");
        mNotificationDay = NotificationDay.SUNDAY;
        tvNotificationTitleAdd.setVisibility(View.GONE);
        btnNotificationTypeAdd.setVisibility(View.GONE);
        btnNotificationDayAdd.setVisibility(View.GONE);
        timePickerNotificationAdd.setVisibility(View.GONE);

        btnCategorySub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCategorySubActive = true;
                isNotifFreqActive = false;
                isNotifDayActive = false;
                showPopUp(view);
            }
        });
        btnNotificationTypeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCategorySubActive = false;
                isNotifFreqActive = true;
                isNotifDayActive = false;
                showPopUp(view);
            }
        });
        btnNotificationDayAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCategorySubActive = false;
                isNotifFreqActive = false;
                isNotifDayActive = true;
                showPopUp(view);
            }
        });
        btnClearAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNotificationTypeAdd.setText("No Notification");
                mNotification = Notification.NONE;
                btnNotificationDayAdd.setText("Sunday");
                mNotificationDay = NotificationDay.SUNDAY;
                tvNotificationTitleAdd.setVisibility(View.GONE);
                btnNotificationTypeAdd.setVisibility(View.GONE);
                btnNotificationDayAdd.setVisibility(View.GONE);
                timePickerNotificationAdd.setVisibility(View.GONE);
                category = "Encouragement";
                message = "";
                subCategory = "none";
                notifID = "3";
                notifAlarmString = "Alarm Off";
                btnCategorySub.setText(category);
                etMessageAdd.setText(message);
            }
        });
        btnCreateAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etMessageAdd.getText().toString().isEmpty()) {
                    if(!btnCategorySub.getText().toString().equals("Encouragement")){
                        try{
                            notID = findNextNotifID();
                            notifID = Integer.toString(notID);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    message = etMessageAdd.getText().toString();
                    if (Build.VERSION.SDK_INT >= 23 ) {
                        hour = timePickerNotificationAdd.getHour();
                        minute = timePickerNotificationAdd.getMinute();
                    }else {
                        hour = timePickerNotificationAdd.getCurrentHour();
                        minute = timePickerNotificationAdd.getCurrentMinute();
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

                    Toast.makeText(getContext(),fullEntry,Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(),"Entry added to " + btnCategorySub.getText().toString(),Toast.LENGTH_LONG).show();

                    // Close the tab once created
                    isAddActive = false;
                    relativeLayoutAdd.setVisibility(View.GONE);
                    btnAddDropdown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, android.R.drawable.arrow_down_float);
                    btnNotificationTypeAdd.setText("No Notification");
                    mNotification = Notification.NONE;
                    btnNotificationDayAdd.setText("Sunday");
                    mNotificationDay = NotificationDay.SUNDAY;
                    tvNotificationTitleAdd.setVisibility(View.GONE);
                    btnNotificationTypeAdd.setVisibility(View.GONE);
                    btnNotificationDayAdd.setVisibility(View.GONE);
                    timePickerNotificationAdd.setVisibility(View.GONE);
                    category = "Encouragement";
                    message = "";
                    subCategory = "none";
                    notifID = "3";
                    notifAlarmString = "Alarm Off";
                    btnCategorySub.setText(category);
                    etMessageAdd.setText(message);
                }else{
                    Toast.makeText(getContext(),"Please enter a message to create", Toast.LENGTH_LONG).show();
                }
            }
        });






        sendSmsBtn = (Button)getView().findViewById(R.id.btnSendSMS);
//        toPhoneNumber = (EditText)getView().findViewById(R.id.editTextPhoneNo);
//        Log.d("ActivityCreated","line 74");

        smsMessageET = (EditText)getView().findViewById(R.id.editTextMessage);
//        etTo = (EditText)getView().findViewById(R.id.editTextTo);
        sendSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });
//        Log.d("ActivityCreated","line 84");

        btnCancelContact = (Button)getView().findViewById(R.id.btnCancelContact);
        btnCancelContact.setVisibility(View.GONE);
//        Log.d("ActivityCreated","line 88");

        btnCancelContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelContact();
            }
        });
//        btnSyncContacts = (Button)getView().findViewById(R.id.btnSyncContacts);
//        btnSyncContacts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btnSyncContacts.setText("Syncing Contacts...");
//                readContactData();
//                textView.setAdapter(adapter);
//            }
//        });
//        Log.d("ActivityCreated","line 105");

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());


//        if(phoneValueArr.isEmpty() && nameValueArr.isEmpty()){
//            btnSyncContacts.setText("Sync Contacts");
//        }

        // Initialize AutoCompleteTextView values

        textView = (AutoCompleteTextView) getView().findViewById(R.id.toNumber);
        textView.setDropDownBackgroundResource(R.color.colorPrimaryDarkAlpha);
        //Create adapter
//        Log.d("ActivityCreated","line 119");


        textView.setThreshold(1);

        //Set adapter to AutoCompleteTextView
        textView.setAdapter(adapter);
        textView.setOnItemSelectedListener(this);
        textView.setOnItemClickListener(this);
//        Log.d("ActivityCreated","line 128");

        textView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(phoneValueArr.isEmpty() && nameValueArr.isEmpty()) {
                    readContactData();
                    textView.setAdapter(adapter);
                }
                else if(adapter.isEmpty()){
                    for(int i = 0; i < phoneValueArr.size(); i++){
                        adapter.add(nameValueArr.get(i) + " Mobile" + " - " + phoneValueArr.get(i));
                    }
                    textView.setAdapter(adapter);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void readContactData() {

        try {
//            btnSyncContacts.setText("Syncing Contacts...");

            /*********** Reading Contacts Name And Number **********/

            String phoneNumber = "";
            ContentResolver cr = getContext()
                    .getContentResolver();

            //Query to get contact name

            Cursor cur = cr
                    .query(ContactsContract.Contacts.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

            // If data data found in contacts
            if (cur.getCount() > 0) {

                Log.i("AutocompleteContacts", "Reading   contacts........");

                int k=0;
                String name = "";

                while (cur.moveToNext())
                {

                    String id = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts._ID));
                    name = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    //Check contact have phone number
                    if (Integer
                            .parseInt(cur
                                    .getString(cur
                                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                    {

                        //Create query to get phone number by contact id
                        Cursor pCur = cr
                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + " = ?",
                                        new String[] { id },
                                        null);
                        int j=0;

                        while (pCur
                                .moveToNext())
                        {

                            if(j == 0) {
                                int phoneType = pCur.getInt(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.TYPE));
                                phoneNumber = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                                switch (phoneType) {
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                        adapter.add(name + " Mobile" + " - " + phoneNumber);
                                        phoneValueArr.add(phoneNumber.toString());
                                        nameValueArr.add(name.toString());
                                        j++;
                                        k++;
                                        break;
//                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
//                                    phoneValueArr.add(phoneNumber.toString());
//                                    nameValueArr.add(name.toString());
//                                    break;
//                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
//                                    adapter.add(name + " - " + phoneNumber + " - Work");
//                                    phoneValueArr.add(phoneNumber.toString());
//                                    nameValueArr.add(name.toString());
//                                    break;
//                                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
//                                    adapter.add(name + " - " + phoneNumber + " - Other");
//                                    phoneValueArr.add(phoneNumber.toString());
//                                    nameValueArr.add(name.toString());
//                                    break;
                                    default:
                                        break;
                                }


                            }

                            /*
                            // Sometimes get multiple data
                            if(j==0)
                            {
                                // Get Phone number
                                phoneNumber =""+pCur.getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                // Add contacts names to adapter
                                adapter.add(name + " - " + phoneNumber);

                                // Add ArrayList names to adapter
                                phoneValueArr.add(phoneNumber.toString());
                                nameValueArr.add(name.toString());


                                j++;
                                k++;
                            }
                            */

                        }  // End while loop
                        pCur.close();
                    } // End if

                }  // End while loop

            } // End Cursor value check
            cur.close();

//            btnSyncContacts.setText("Contacts Synced");

        } catch (Exception e) {
            Log.i("AutocompleteContacts","Exception : "+ e);
        }

        Log.d("ContactsList",phoneValueArr.toString());
        Log.d("ContactsList",nameValueArr.toString());
    }

    private void cancelContact(){
        name = "";
        toNumberValue = "";
        btnCancelContact.setVisibility(View.GONE);
        textView.setText("");
        textView.setEnabled(true);
    }

    private void sendSms(){
        //String toPhone = toPhoneNumber.getText().toString();
        String smsMessage = smsMessageET.getText().toString();

        if(name.isEmpty() || name.equals("") || name == null){
            toNumberValue = textView.getText().toString();
        }

        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(toNumberValue,null,smsMessage, null, null);

            Toast.makeText(getActivity().getApplicationContext(),"SMS Sent", Toast.LENGTH_LONG).show();

            cancelContact();
            smsMessageET.setText("");
            textView.setText("");
            toNumberValue = "";
            name = "";

        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Error SMS Not Sent", Toast.LENGTH_LONG).show();
        }

    }

//
//    private View.OnClickListener BtnAction(final AutoCompleteTextView toNumber) {
//        return new View.OnClickListener() {
//
//            public void onClick(View v) {
//
//                String NameSel = "";
//                NameSel = toNumber.getText().toString();
//
//
//                final String ToNumber = toNumberValue;
//
//
//                if (ToNumber.length() == 0 ) {
//                    Toast.makeText(getActivity().getBaseContext(), "Please fill phone number",
//                            Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Toast.makeText(getActivity().getBaseContext(), NameSel+" : "+toNumberValue,
//                            Toast.LENGTH_LONG).show();
//                }
//
//            }
//        };
//    }




    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
                               long arg3) {
        // TODO Auto-generated method stub
        //Log.d("AutocompleteContacts", "onItemSelected() position " + position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

        // Get Array index value for selected name


        int i = nameValueArr.indexOf(""+arg0.getItemAtPosition(arg2));

        // If name exist in name ArrayList
        if (i >= 0) {

            // Get Phone Number
            toNumberValue = phoneValueArr.get(i);

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    getActivity().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

            // Show Alert
            //Toast.makeText(getActivity().getBaseContext(), "Position:"+arg2+" Name:"+arg0.getItemAtPosition(arg2)+" Number:"+toNumberValue,
                    //Toast.LENGTH_LONG).show();

            //Log.d("AutocompleteContacts", "Position:"+arg2+" Name:"+arg0.getItemAtPosition(arg2)+" Number:"+toNumberValue);

        }
//        name = textView.getText().toString();
//        String text = name;// + " ("+toNumberValue+")";
//        textView.setText(text);
        btnCancelContact.setVisibility(View.VISIBLE);
        textView.setEnabled(false);

    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
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
        PopupMenu popupMenu = new PopupMenu(getContext(),v,1,0,R.style.PopupMenu);

        popupMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = popupMenu.getMenuInflater();

        if(isCategorySubActive){

            inflater.inflate(R.menu.popup_category_menu, popupMenu.getMenu());
            if(!wantsPrayerAndScripture){
                popupMenu.getMenu().getItem(1).setVisible(false);
                popupMenu.getMenu().getItem(2).setVisible(false);
            }
            try {
                //if(mainCategory == 2) {
                for (int i = 0; i < categoriesListPrayer.size(); i++) {
                    String[] category = categoriesListPrayer.get(i).split("/n");
                    CharSequence c_arr = category[1];
                    //popupMenu.getMenu().add(0, i, 0, c_arr).setShortcut('3', 'c');
                    popupMenu.getMenu().getItem(1).getSubMenu().add(0, i, 0, c_arr).setShortcut('3', 'c');
                }
                //}else if(mainCategory == 3) {
                for (int i = 0; i < categoriesListScripture.size(); i++) {
                    String[] category = categoriesListScripture.get(i).split("/n");
                    CharSequence c_arr = category[1];
                    //popupMenu.getMenu().add(0, i, 0, c_arr).setShortcut('3', 'c');
                    popupMenu.getMenu().getItem(2).getSubMenu().add(0, i, 0, c_arr).setShortcut('3', 'c');
                }
                //}
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {

            inflater.inflate(R.menu.popup_home_menu, popupMenu.getMenu());

            if (isNotifFreqActive) {
                for (int i = 3; i <= 9; i++) {
                    popupMenu.getMenu().getItem(i).setVisible(false);
                }
            }
            if (isNotifDayActive) {
                for (int i = 0; i <= 2; i++) {
                    popupMenu.getMenu().getItem(i).setVisible(false);
                }
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
                    btnNotificationTypeAdd.setText("Daily Notification");
                    btnNotificationDayAdd.setVisibility(View.GONE);
                    timePickerNotificationAdd.setVisibility(View.VISIBLE);
                    isNotifFreqActive = false;
                }
                return true;
            case R.id.popup_weeklyNotification:
                if(isNotifFreqActive) {
                    mNotification = Notification.WEEKLY;
                    btnNotificationTypeAdd.setText("Weekly Notification");
                    btnNotificationDayAdd.setVisibility(View.VISIBLE);
                    timePickerNotificationAdd.setVisibility(View.VISIBLE);
                    isNotifFreqActive = false;
                }
                return true;
            case R.id.popup_noNotification:
                if(isNotifFreqActive) {
                    mNotification = Notification.NONE;
                    btnNotificationTypeAdd.setText("No Notification");
                    btnNotificationDayAdd.setVisibility(View.GONE);
                    timePickerNotificationAdd.setVisibility(View.GONE);
                    isNotifFreqActive = false;
                }

                return true;
            case R.id.popup_sunday:
                if(isNotifDayActive) {
                    btnNotificationDayAdd.setText("Sunday");
                    mNotificationDay = NotificationDay.SUNDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_monday:
                if(isNotifDayActive) {
                    btnNotificationDayAdd.setText("Monday");
                    mNotificationDay = NotificationDay.MONDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_tuesday:
                if(isNotifDayActive) {
                    btnNotificationDayAdd.setText("Tuesday");
                    mNotificationDay = NotificationDay.TUESDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_wednesday:
                if(isNotifDayActive) {
                    btnNotificationDayAdd.setText("Wednesday");
                    mNotificationDay = NotificationDay.WEDNESDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_thursday:
                if(isNotifDayActive) {
                    btnNotificationDayAdd.setText("Thursday");
                    mNotificationDay = NotificationDay.THURSDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_friday:
                if(isNotifDayActive) {
                    btnNotificationDayAdd.setText("Friday");
                    mNotificationDay = NotificationDay.FRIDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.popup_saturday:
                if(isNotifDayActive) {
                    btnNotificationDayAdd.setText("Saturday");
                    mNotificationDay = NotificationDay.SATURDAY;
                    isNotifDayActive = false;
                }
                return true;
            case R.id.encouragementCategory:
                category = "Encouragement";
                subCategory = "none";
                btnNotificationTypeAdd.setText("No Notification");
                mNotification = Notification.NONE;
                btnNotificationDayAdd.setText("Sunday");
                mNotificationDay = NotificationDay.SUNDAY;
                tvNotificationTitleAdd.setVisibility(View.GONE);
                btnNotificationTypeAdd.setVisibility(View.GONE);
                btnNotificationDayAdd.setVisibility(View.GONE);
                timePickerNotificationAdd.setVisibility(View.GONE);
                notifID = "3";
                notifAlarmString = "Alarm Off";
                btnCategorySub.setText(category);
                return true;
            case R.id.prayerCategory:
                category = "Prayer";
                return true;
            case R.id.scriptureCategory:
                category = "Scripture";
                return true;
            default:
                String subTitle = item.getTitle().toString();
                if(category.equals("Prayer")){
                    showNotificationSettings();
                    subCategory = subTitle;
                    btnCategorySub.setText(category + " - " + subCategory);
                }else if(category.equals("Scripture")){
                    showNotificationSettings();
                    subCategory = subTitle;
                    btnCategorySub.setText(category + " - " + subCategory);
                }
                return false;
        }
    }

    private void showNotificationSettings(){
        switch (mNotification){
            case DAILY:
                tvNotificationTitleAdd.setVisibility(View.VISIBLE);
                btnNotificationTypeAdd.setVisibility(View.VISIBLE);
                btnNotificationDayAdd.setVisibility(View.GONE);
                timePickerNotificationAdd.setVisibility(View.VISIBLE);
                break;
            case WEEKLY:
                tvNotificationTitleAdd.setVisibility(View.VISIBLE);
                btnNotificationTypeAdd.setVisibility(View.VISIBLE);
                btnNotificationDayAdd.setVisibility(View.VISIBLE);
                timePickerNotificationAdd.setVisibility(View.VISIBLE);
                break;
            case NONE:
                tvNotificationTitleAdd.setVisibility(View.VISIBLE);
                btnNotificationTypeAdd.setVisibility(View.VISIBLE);
                btnNotificationDayAdd.setVisibility(View.GONE);
                timePickerNotificationAdd.setVisibility(View.GONE);
                break;
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

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getContext(),notifyID,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        try {
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

        AlarmManager alarmManager = (AlarmManager)
                this.getContext().getSystemService(Context.ALARM_SERVICE);
        if(notificationType.equals("Daily")) {

            // if calendar time has already passed then add a day to the time
            if(calendar.getTimeInMillis() < System.currentTimeMillis()){
                calendar.setTimeInMillis(calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY);
            }

            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

        }else if(notificationType.equals("Weekly")){

            // if calendar time has already passed then add a week to the time
            if(calendar.getTimeInMillis() < System.currentTimeMillis()){
                calendar.setTimeInMillis(calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY*7);
            }

            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }

    }

    private boolean saveArray(List<String> sKey, String arrayName){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getContext());
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
