package com.wordpress.zackleaman.materialtablayout;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewMessages extends Fragment implements AdapterView.OnItemClickListener,
        View.OnClickListener, PopupMenu.OnMenuItemClickListener{



    private static NewMessages inst;
    private ArrayList<String> smsMessageList;
    private ListView smsListView;
    private MyListAdapter mMyListAdapter;
    private ArrayList<String> categoriesList;
    private ArrayList<String> categoriesListPrayer;
    private ArrayList<String> categoriesListScripture;
    private int position;
    private Button btnLastMessages;
    private int inboxAmount;
    private int mainCategory;
    private boolean isLastMessagesPressed;

    //TODO List created ReceiveSMSActivity
    private ArrayList<String> encouragementList;

    public static NewMessages instance(){
        return inst;
    }


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public NewMessages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_messages, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        smsListView = (ListView)getView().findViewById(R.id.SMSList);
        encouragementList = new ArrayList<>();
        inboxAmount = 10;
        position = 0;
        mainCategory = 0;
        isLastMessagesPressed = false;
        categoriesList= new ArrayList<>();
        smsMessageList = new ArrayList<>();
        categoriesListPrayer = new ArrayList<>();
        categoriesListScripture = new ArrayList<>();
        mMyListAdapter = new MyListAdapter(getActivity().getApplicationContext(), R.layout.list_add, smsMessageList);
        smsListView.setAdapter(mMyListAdapter);
        smsListView.setOnItemClickListener(this);
        smsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                setPosition(i);
                showPopUp(view);
                return false;
            }
        });

        //TODO load Array into encouragementList
        loadArray(encouragementList,getContext().getApplicationContext(),"encouragementList");
        loadArray(categoriesList,getContext().getApplicationContext(),"categoriesList");
        loadArray(categoriesListPrayer,getContext().getApplicationContext(), "categoriesListPrayer");
        loadArray(categoriesListScripture,getContext().getApplicationContext(),"categoriesListScripture");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean wantsPrayerAndScripture = sp.getBoolean("wantsPrayerAndScripture",true);
        if(!wantsPrayerAndScripture){
            categoriesList.clear();
            categoriesList.add("/nEncouragement/n");
        }

        refreshSmsInbox();

        btnLastMessages = (Button)getView().findViewById(R.id.btnLastMessages);
        btnLastMessages.setOnClickListener(this);


    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnLastMessages:
                isLastMessagesPressed = true;
                showPopUp(v);
                break;

            default:
                break;
        }
    }

    public void refreshSmsInbox(){
        //TODO poss prob
//        if(ContextCompat.checkSelfPermission(getActivity().getBaseContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(getActivity().getBaseContext(), "android.permission.READ_CONTACTS") != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(getActivity().getBaseContext(), "android.permission.WRITE_CONTACTS") != PackageManager.PERMISSION_GRANTED
//                ) {
//
//            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_READ_SMS);
//            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_CONTACTS"}, REQUEST_CODE_READ_CONTACTS);
//            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.WRITE_CONTACTS"}, REQUEST_CODE_WRITE_CONTACTS);
//
//        }else {
            ContentResolver contentResolver = getActivity().getContentResolver();
            Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
            int indexBody = smsInboxCursor.getColumnIndex("body");
            int indexAddress = smsInboxCursor.getColumnIndex("address");
            int indexName = smsInboxCursor.getColumnIndex("Name");
            Log.d("indexName", Integer.toString(indexName));
            int timeMillis = smsInboxCursor.getColumnIndex("date");


            if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
            mMyListAdapter.clear();
            int curIndex = 0;
            do {
                long indexTimeMillis = Long.parseLong(smsInboxCursor.getString(timeMillis));
                Date date = new Date(indexTimeMillis);
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                String dateText = format.format(date);

                String contactName = null;
                contactName = getContactName(getActivity().getApplicationContext(),
                        smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow("address")));
                String str = "";
                if (contactName != null) {
                    str = contactName + " on " + dateText + "/n" + smsInboxCursor.getString(indexBody);
                } else {
                    str = smsInboxCursor.getString(indexAddress) + " on " + dateText + "/n" + smsInboxCursor.getString(indexBody);
                }

                mMyListAdapter.add(str);
                curIndex++;
            } while (smsInboxCursor.moveToNext() && curIndex < inboxAmount);
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        if(requestCode == REQUEST_CODE_READ_SMS){
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                refreshSmsInbox();
//                // permission was granted, yay! Do the
//                // contacts-related task you need to do.
//
//            }
//        }
//
////        switch (requestCode) {
////            case REQUEST_CODE_READ_SMS: {
////                // If request is cancelled, the result arrays are empty.
////                if (grantResults.length > 0
////                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////
////                    // permission was granted, yay! Do the
////                    // contacts-related task you need to do.
////
////                } else {
////
////                    // permission denied, boo! Disable the
////                    // functionality that depends on this permission.
////                }
////                return;
////            }
////
////            // other 'case' lines to check for other
////            // permissions this app might request
////        }
//    }

    public String getContactName(Context context, String phoneNumber) {
//        if(ContextCompat.checkSelfPermission(getActivity().getBaseContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(getActivity().getBaseContext(), "android.permission.READ_CONTACTS") != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(getActivity().getBaseContext(), "android.permission.WRITE_CONTACTS") != PackageManager.PERMISSION_GRANTED
//                ) {
//
//            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_READ_SMS);
//            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_CONTACTS"}, REQUEST_CODE_READ_CONTACTS);
//            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.WRITE_CONTACTS"}, REQUEST_CODE_WRITE_CONTACTS);
//
//        }else {
            ContentResolver cr = context.getContentResolver();
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(phoneNumber));
            Cursor cursor = cr.query(uri,
                    new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
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
//        }return "";
    }

    public void updateList(final String smsMessage){
        mMyListAdapter.insert(smsMessage, 0);
        mMyListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        try{
//            String[] smsMessages = smsMessageList.get(position).split("/n");
//            String nameAddress = smsMessages[0];
//            //String address = smsMessages[1];
//            String date = smsMessages[1];
//            String smsMessage = "";
//
//            for(int i = 2; i < smsMessages.length; i++){
//                smsMessage += smsMessages[i];
//            }
//
//            String smsMessageStr = nameAddress + "/n" + date + "/n" + smsMessage;
//            Toast.makeText(getActivity().getApplicationContext(),smsMessageStr + "/nSaved to Encouragements",Toast.LENGTH_SHORT).show();
//
//            //TODO Add to encouragementList
//            encouragementList.add(0,smsMessageStr);
//            saveArray(encouragementList,"encouragementList");
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }




    private boolean saveArray(List<String> sKey, String arrayName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putInt("SMS_" + arrayName + "_size", sKey.size());

        for (int i = 0; i < sKey.size(); i++) {
            mEdit1.remove("SMS_" + arrayName + i);
            mEdit1.putString("SMS_" + arrayName + i, sKey.get(i));
        }

        return mEdit1.commit();
    }

    private static void loadArray(List<String> sKey, Context mContext, String arrayName) {
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(mContext);
        sKey.clear();
        int size = mSharedPreference1.getInt("SMS_" + arrayName + "_size", 0);

        for (int i = 0; i < size; i++) {
            sKey.add(mSharedPreference1.getString("SMS_" + arrayName + i, null));
        }

    }

    public class ViewHolder{
        TextView title;
        TextView title2;
        Button button;
    }

    public void setPosition(int pos){
        position = pos;
    }


    public void showPopUp(View v){
        PopupMenu popupMenu = new PopupMenu(getActivity(),v,1,0,R.style.PopupMenu);

        popupMenu.setOnMenuItemClickListener(NewMessages.this);
        MenuInflater inflater = popupMenu.getMenuInflater();
        if(!isLastMessagesPressed) {
            inflater.inflate(R.menu.popup_category_menu, popupMenu.getMenu());
            if(categoriesList.size() == 1){
                popupMenu.getMenu().getItem(1).setVisible(false);
                popupMenu.getMenu().getItem(2).setVisible(false);
            }

        }else{
            inflater.inflate(R.menu.menu_last_messages, popupMenu.getMenu());
        }



        //set the categoriesList to PopupMenu
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


        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.last10Messages:
                isLastMessagesPressed = false;
                btnLastMessages.setText("Last 10 Messages");
                inboxAmount = 10;
                refreshSmsInbox();
                return true;
            case R.id.last20Messages:
                isLastMessagesPressed = false;
                btnLastMessages.setText("Last 20 Messages");
                inboxAmount = 20;
                refreshSmsInbox();
                return true;
            case R.id.last50Messages:
                isLastMessagesPressed = false;
                btnLastMessages.setText("Last 50 Messages");
                inboxAmount = 50;
                refreshSmsInbox();
                return true;
            case R.id.encouragementCategory:
                mainCategory = 1;
                String title = item.getTitle().toString();

                try{
                    String[] smsMessages = smsMessageList.get(position).split("/n");
                    String nameAddressDate = smsMessages[0];
                    //String date = smsMessages[1];
                    String smsMessage = smsMessages[1];


                    String smsMessageStr = "/n" + title + "/n" + nameAddressDate + "/n" + smsMessage + "/nnone" + "/nnone"  + "/nnone";
                    Toast.makeText(getActivity().getApplicationContext(),"Entry Saved to " + title,Toast.LENGTH_SHORT).show();

                    encouragementList.add(0,smsMessageStr);
                    saveArray(encouragementList,"encouragementList");

                }catch (Exception e){
                    e.printStackTrace();
                }

                return true;
            case R.id.prayerCategory:
                mainCategory = 2;
                return true;
            case R.id.scriptureCategory:
                mainCategory = 3;
                return true;
            default:
                String subTitle = item.getTitle().toString();

                try{
                    String[] smsMessages = smsMessageList.get(position).split("/n");
                    String nameAddressDate = smsMessages[0];
                    //String date = smsMessages[1];
                    String smsMessage = smsMessages[1];

                    if(mainCategory == 2) {
                        int notifID = findNextNotifID();
                        String smsMessageStr = "/n" + "Prayer" + "/n" + nameAddressDate + "/n" + smsMessage + "/n" + subTitle + "/n" + notifID  + "/nnew";
                        Toast.makeText(getActivity().getApplicationContext(), "Entry Saved to Prayer - " + subTitle, Toast.LENGTH_SHORT).show();
                        encouragementList.add(0,smsMessageStr);
                        saveArray(encouragementList,"encouragementList");
                        ArrayList<String> selectedEncouragement = new ArrayList<>();
                        selectedEncouragement.add(0,smsMessageStr);
                        saveArray(selectedEncouragement,"selectedEncouragement");
                        Intent intent = new Intent(getContext(),CurrentEncouragement.class);
                        startActivity(intent);
                        //getActivity().finish();
                    }else if(mainCategory == 3){
                        int notifID = findNextNotifID();
                        String smsMessageStr = "/n" + "Scripture" + "/n" + nameAddressDate + "/n" + smsMessage + "/n" + subTitle + "/n" + notifID  + "/nnew";
                        Toast.makeText(getActivity().getApplicationContext(), "Entry Saved to Scripture - " + subTitle, Toast.LENGTH_SHORT).show();
                        encouragementList.add(0,smsMessageStr);
                        saveArray(encouragementList,"encouragementList");
                        ArrayList<String> selectedEncouragement = new ArrayList<>();
                        selectedEncouragement.add(0,smsMessageStr);
                        saveArray(selectedEncouragement,"selectedEncouragement");
                        Intent intent = new Intent(getContext(),CurrentEncouragement.class);
                        startActivity(intent);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }


            return false;
        }
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

    private class MyListAdapter extends ArrayAdapter<String>{
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
                viewHolder.button = (Button)convertView.findViewById(R.id.list_item_addBTN);
                convertView.setTag(viewHolder);
            }


            mainViewHolder = (ViewHolder)convertView.getTag();
            mainViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPosition(position);
                    isLastMessagesPressed = false;
                    showPopUp(view);
                }
            });
            String mainTitle = "";
            String subBody = getItem(position);

            try{
                String[] message = getItem(position).split("/n");
                mainTitle = message[0];
                subBody = message[1];

            }catch (Exception e){
                e.printStackTrace();
            }

            mainViewHolder.title.setText(subBody);
            mainViewHolder.title2.setText(mainTitle);

            return convertView;
        }
    }


}
