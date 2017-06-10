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
import java.util.Locale;


/**
 * Fragment for Inbox New Messages Tab of the main Material Layout
 */
public class NewMessages extends Fragment implements
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
    private String name;

    private ArrayList<String> encouragementList;

    /**
     * Used to get the instance of NewMessages Fragment Class
     * @return NewMessages instance
     */
    public static NewMessages instance(){
        return inst;
    }


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public NewMessages() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_messages, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // MAKE VIEW REFERENCES AND INITIALIZE VARIABLES
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
        smsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                setPosition(i);
                showPopUp(view);
                return false;
            }
        });

        // LOAD ARRAYS FROM SHARED PREF
        loadArray(encouragementList,getContext().getApplicationContext(),"encouragementList");
        loadArray(categoriesList,getContext().getApplicationContext(),"categoriesList");
        loadArray(categoriesListPrayer,getContext().getApplicationContext(), "categoriesListPrayer");
        loadArray(categoriesListScripture,getContext().getApplicationContext(),"categoriesListScripture");

        // CHECK THAT WANT PRAYER AND SCRIPTURE
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

    /**
     * This is used to refresh the sms displayed inbox to the new values
     */
    public void refreshSmsInbox(){
        // Get content resolver and make a cursor from it to step through the sms inbox
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"),
                null, null, null, null);

        // check that cursor is not null
        if(smsInboxCursor != null) {
            // get int index values for body, address, and date from inbox database
            int indexBody = smsInboxCursor.getColumnIndex("body");
            int indexAddress = smsInboxCursor.getColumnIndex("address");
            int timeMillis = smsInboxCursor.getColumnIndex("date");

            // if cursor cant move to first that means empty so return
            if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;

            // clear the list because about to repopulate
            mMyListAdapter.clear();
            int curIndex = 0;
            do {
                // get the time in millis from the cursor inbox indexed
                long indexTimeMillis = Long.parseLong(smsInboxCursor.getString(timeMillis));
                // convert millis to date
                Date date = new Date(indexTimeMillis);
                // make a date format to use in displaying a readable date
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                // format the date
                String dateText = format.format(date);

                // use getContactName from the context and the address gathered from the cursor index
                String contactName = null;
                contactName = getContactName(getActivity().getApplicationContext(),
                        smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow("address")));
                String str = "";

                // if have a contact name then display with contact name
                if (contactName != null) {
                    str = contactName + " on " + dateText + "/n" + smsInboxCursor.getString(indexBody);
                // if don't have a contact name then display phone number instead
                } else {
                    str = smsInboxCursor.getString(indexAddress) + " on " + dateText + "/n" +
                            smsInboxCursor.getString(indexBody);
                }

                // add string to the list adapter
                mMyListAdapter.add(str);
                curIndex++;
            } while (smsInboxCursor.moveToNext() && curIndex < inboxAmount);

            smsInboxCursor.close();
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

    /**
     * This is used to update the List once a SMS Broadcast Receiver has been received
     * @param smsMessage String this is the message that was most recently received
     */
    public void updateList(final String smsMessage){
        mMyListAdapter.insert(smsMessage, 0);
        mMyListAdapter.notifyDataSetChanged();
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

    /**
     * ViewHolder that holds reference to view items for List Adapter
     */
    public class ViewHolder{
        TextView title;
        TextView title2;
        Button button;
    }

    /**
     * Used to change the current position selected
     * @param pos int that represents the position of element selected
     */
    public void setPosition(int pos){
        position = pos;
    }

    /**
     * Used to show a popup menu for how many SMS to display as well as show popup menu for
     * which category to save the sms entry to
     * @param v view used to show the popup from
     */
    public void showPopUp(View v){
        PopupMenu popupMenu = new PopupMenu(getActivity(),v,1,0,R.style.PopupMenu);

        popupMenu.setOnMenuItemClickListener(NewMessages.this);
        MenuInflater inflater = popupMenu.getMenuInflater();

        // if showing categories to add entry to
        if(!isLastMessagesPressed) {
            inflater.inflate(R.menu.popup_category_menu, popupMenu.getMenu());
            if(categoriesList.size() == 1){
                popupMenu.getMenu().getItem(1).setVisible(false);
                popupMenu.getMenu().getItem(2).setVisible(false);
            }
        // if showing amount of last messages to show
        }else{
            inflater.inflate(R.menu.menu_last_messages, popupMenu.getMenu());
        }

        //set the prayer and scripture subcategoriesList to PopupMenu
        try {

            for (int i = 0; i < categoriesListPrayer.size(); i++) {
                String[] category = categoriesListPrayer.get(i).split("/n");
                CharSequence c_arr = category[1];
                popupMenu.getMenu().getItem(1).getSubMenu().add(0, i, 0, c_arr).setShortcut('3', 'c');
            }

            for (int i = 0; i < categoriesListScripture.size(); i++) {
                String[] category = categoriesListScripture.get(i).split("/n");
                CharSequence c_arr = category[1];
                popupMenu.getMenu().getItem(2).getSubMenu().add(0, i, 0, c_arr).setShortcut('3', 'c');
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            // Amount of Messages to display pressed
            case R.id.last10Messages:
                isLastMessagesPressed = false;
                btnLastMessages.setText(R.string.last_10_messages);
                inboxAmount = 10;
                refreshSmsInbox();
                return true;
            case R.id.last20Messages:
                isLastMessagesPressed = false;
                btnLastMessages.setText(R.string.last_20_messages);
                inboxAmount = 20;
                refreshSmsInbox();
                return true;
            case R.id.last50Messages:
                isLastMessagesPressed = false;
                btnLastMessages.setText(R.string.last_50_messages);
                inboxAmount = 50;
                refreshSmsInbox();
                return true;
            // Entry category type pressed
            case R.id.encouragementCategory:
                mainCategory = 1;
                String title = item.getTitle().toString();

                try{
                    String[] smsMessages = smsMessageList.get(position).split("/n");
                    String nameAddressDate = smsMessages[0];
                    String smsMessage = smsMessages[1];


                    String smsMessageStr = "/n" + title + "/n" + nameAddressDate + "/n" +
                            smsMessage + "/nnone" + "/n3"  + "/nAlarm Off";
                    Toast.makeText(getActivity().getApplicationContext(),"Entry Saved to " +
                            title,Toast.LENGTH_SHORT).show();

                    try{
                        String[] entry = nameAddressDate.split(" on ");
                        name = entry[0];
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    // Prompt user to send a reply SMS
                    if(name != null) {
                        Toast.makeText(getActivity().getApplicationContext(), "Reply to " + name +
                                " by going to Send Message in the Create Tab", Toast.LENGTH_LONG).show();
                    }
                    encouragementList.add(0,smsMessageStr);
                    saveArray(encouragementList,"encouragementList");

                }catch (Exception e){
                    e.printStackTrace();
                }

                return true;
            // Prayer and Scripture chosen so display subcategories
            case R.id.prayerCategory:
                mainCategory = 2;
                return true;
            case R.id.scriptureCategory:
                mainCategory = 3;
                return true;
            // Subcategory item chosen
            default:
                String subTitle = item.getTitle().toString();

                try{
                    String[] smsMessages = smsMessageList.get(position).split("/n");
                    String nameAddressDate = smsMessages[0];
                    String smsMessage = smsMessages[1];
                    try{
                        String[] entry = nameAddressDate.split(" on ");
                        name = entry[0];
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    if(mainCategory == 2) {
                        int notifID = findNextNotifID();
                        String smsMessageStr = "/n" + "Prayer" + "/n" + nameAddressDate + "/n" +
                                smsMessage + "/n" + subTitle + "/n" + notifID  + "/nnew";
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Entry Saved to Prayer - " + subTitle, Toast.LENGTH_SHORT).show();
                        // Prompt user to send a reply SMS
                        if(name != null) {
                            Toast.makeText(getActivity().getApplicationContext(), "Reply to " +
                                    name + " by going to Send Message in the Create Tab",
                                    Toast.LENGTH_SHORT).show();
                        }
                        encouragementList.add(0,smsMessageStr);
                        saveArray(encouragementList,"encouragementList");
                        ArrayList<String> selectedEncouragement = new ArrayList<>();
                        selectedEncouragement.add(0,smsMessageStr);
                        saveArray(selectedEncouragement,"selectedEncouragement");
                        Intent intent = new Intent(getContext(),CurrentEncouragement.class);
                        startActivity(intent);
                    }else if(mainCategory == 3){
                        int notifID = findNextNotifID();
                        String smsMessageStr = "/n" + "Scripture" + "/n" + nameAddressDate + "/n" +
                                smsMessage + "/n" + subTitle + "/n" + notifID  + "/nnew";
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Entry Saved to Scripture - " + subTitle, Toast.LENGTH_SHORT).show();
                        // Prompt user to send a reply SMS
                        if(name != null) {
                            Toast.makeText(getActivity().getApplicationContext(), "Reply to " +
                                    name + " by going to Send Message in the Create Tab",
                                    Toast.LENGTH_SHORT).show();
                        }
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

    /**
     * This is used to find the next available unique notification ID
     * @return notifID int that is a unique notification id
     */
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

    /**
     * Array Adapter class that holds the SMS inbox displayed entries
     */
    private class MyListAdapter extends ArrayAdapter<String>{
        private int layout;
        private List<String> mObjects;

        /**
         * constructor for MyListAdapter
         * @param context Context that will be using the ListAdapter
         * @param resource int resource id
         * @param objects List<String> of objects to display
         */
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
