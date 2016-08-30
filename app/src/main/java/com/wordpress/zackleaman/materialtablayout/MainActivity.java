package com.wordpress.zackleaman.materialtablayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.Random;

public class MainActivity extends AbsRuntimePermission {
    public static boolean isFirstTimeOpening = true;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private RelativeLayout mainRelativeLayout;
    static Context myContext;

    private ScrollView svPermissions;

//    public static ArrayAdapter<String> adapter;
//    // Store contacts values in these arraylist
//    public static ArrayList<String> phoneValueArr = new ArrayList<String>();
//    public static ArrayList<String> nameValueArr = new ArrayList<String>();

    private static final int REQUEST_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        svPermissions = (ScrollView)findViewById(R.id.svPermissions);

//        isFirstTimeOpening = false;
//        saveBoolean("isFirstTimeOpening",isFirstTimeOpening);
        isFirstTimeOpening = loadBoolean("isFirstTimeOpening",getApplicationContext());

        requestAppPermissions(new String[]{
                        Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        Manifest.permission.SET_ALARM,
                },
                R.string.msg_enable_permissions,
                REQUEST_PERMISSION);


    }


//    private void readContactData() {
//
//        try {
//
//            /*********** Reading Contacts Name And Number **********/
//
//            String phoneNumber = "";
//            ContentResolver cr = getBaseContext()
//                    .getContentResolver();
//
//            //Query to get contact name
//
//            Cursor cur = cr
//                    .query(ContactsContract.Contacts.CONTENT_URI,
//                            null,
//                            null,
//                            null,
//                            null);
//
//            // If data data found in contacts
//            if (cur.getCount() > 0) {
//
//                Log.i("AutocompleteContacts", "Reading   contacts........");
//
//                int k=0;
//                String name = "";
//
//                while (cur.moveToNext())
//                {
//
//                    String id = cur
//                            .getString(cur
//                                    .getColumnIndex(ContactsContract.Contacts._ID));
//                    name = cur
//                            .getString(cur
//                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//
//                    //Check contact have phone number
//                    if (Integer
//                            .parseInt(cur
//                                    .getString(cur
//                                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
//                    {
//
//                        //Create query to get phone number by contact id
//                        Cursor pCur = cr
//                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                                        null,
//                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
//                                                + " = ?",
//                                        new String[] { id },
//                                        null);
//                        int j=0;
//
//                        while (pCur
//                                .moveToNext())
//                        {
//
//                            if(j == 0) {
//                                int phoneType = pCur.getInt(pCur.getColumnIndex(
//                                        ContactsContract.CommonDataKinds.Phone.TYPE));
//                                phoneNumber = pCur.getString(pCur.getColumnIndex(
//                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                switch (phoneType) {
//                                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
//                                        adapter.add(name + " Mobile" + " - " + phoneNumber);
//                                        phoneValueArr.add(phoneNumber.toString());
//                                        nameValueArr.add(name.toString());
//                                        j++;
//                                        k++;
//                                        break;
////                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
////                                    phoneValueArr.add(phoneNumber.toString());
////                                    nameValueArr.add(name.toString());
////                                    break;
////                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
////                                    adapter.add(name + " - " + phoneNumber + " - Work");
////                                    phoneValueArr.add(phoneNumber.toString());
////                                    nameValueArr.add(name.toString());
////                                    break;
////                                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
////                                    adapter.add(name + " - " + phoneNumber + " - Other");
////                                    phoneValueArr.add(phoneNumber.toString());
////                                    nameValueArr.add(name.toString());
////                                    break;
//                                    default:
//                                        break;
//                                }
//
//
//                            }
//
//                            /*
//                            // Sometimes get multiple data
//                            if(j==0)
//                            {
//                                // Get Phone number
//                                phoneNumber =""+pCur.getString(pCur
//                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//                                // Add contacts names to adapter
//                                adapter.add(name + " - " + phoneNumber);
//
//                                // Add ArrayList names to adapter
//                                phoneValueArr.add(phoneNumber.toString());
//                                nameValueArr.add(name.toString());
//
//
//                                j++;
//                                k++;
//                            }
//                            */
//
//                        }  // End while loop
//                        pCur.close();
//                    } // End if
//
//                }  // End while loop
//
//            } // End Cursor value check
//            cur.close();
//
//
//        } catch (Exception e) {
//            Log.i("AutocompleteContacts","Exception : "+ e);
//        }
//
//        Log.d("ContactsList",phoneValueArr.toString());
//        Log.d("ContactsList",nameValueArr.toString());
//    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        // Do anything when permission granted
        svPermissions.setVisibility(View.GONE);

//        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());

        mTabLayout = (TabLayout)findViewById(R.id.tabLayout);
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mainRelativeLayout = (RelativeLayout)findViewById(R.id.mainRelativeLayout);

        int picChoice = -1;
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            picChoice = Integer.parseInt(sp.getString("backgroundSelection", "-1"));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(picChoice == -1) {
            Random r = new Random();
            picChoice = r.nextInt(14);
        }
        if(picChoice == 0){
            mainRelativeLayout.setBackgroundResource(R.drawable.ariedlandscape);
        }else if(picChoice == 1){
            mainRelativeLayout.setBackgroundResource(R.drawable.brickwall);
        }else if(picChoice == 2){
            mainRelativeLayout.setBackgroundResource(R.drawable.city);
        }else if(picChoice == 3){
            mainRelativeLayout.setBackgroundResource(R.drawable.mountains1);
        }else if(picChoice == 4){
            mainRelativeLayout.setBackgroundResource(R.drawable.mountains2);
        }else if(picChoice == 5){
            mainRelativeLayout.setBackgroundResource(R.drawable.roof);
        }else if(picChoice == 6){
            mainRelativeLayout.setBackgroundResource(R.drawable.sea1);
        }else if(picChoice == 7){
            mainRelativeLayout.setBackgroundResource(R.drawable.sea2);
        }else if(picChoice == 8){
            mainRelativeLayout.setBackgroundResource(R.drawable.sea3);
        }else if(picChoice == 9){
            mainRelativeLayout.setBackgroundResource(R.drawable.sky1);
        }else if(picChoice == 10){
            mainRelativeLayout.setBackgroundResource(R.drawable.sky2);
        }else if(picChoice == 11){
            mainRelativeLayout.setBackgroundResource(R.drawable.sunset);
        }else if(picChoice == 12){
            mainRelativeLayout.setBackgroundResource(R.drawable.tarp);
        }else if(picChoice == 13){
            mainRelativeLayout.setBackgroundResource(R.drawable.wheatfield);
        }else if(picChoice == 14){
            mainRelativeLayout.setBackgroundResource(R.drawable.city_2);
        }else if(picChoice == 15){
            mainRelativeLayout.setBackgroundResource(R.drawable.crumbled_paper);
        }else if(picChoice == 16){
            mainRelativeLayout.setBackgroundResource(R.drawable.forest);
        }else if(picChoice == 17){
            mainRelativeLayout.setBackgroundResource(R.drawable.hut_wall);
        }else if(picChoice == 18){
            mainRelativeLayout.setBackgroundResource(R.drawable.sea_4);
        }else if(picChoice == 19){
            mainRelativeLayout.setBackgroundResource(R.drawable.shore);
        }else if(picChoice == 20){
            mainRelativeLayout.setBackgroundResource(R.drawable.train);
        }else if(picChoice == 21){
            mainRelativeLayout.setBackgroundResource(R.drawable.tropic_2);
        }else if(picChoice == 22){
            mainRelativeLayout.setBackgroundResource(R.drawable.wood);
        }



        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragments(new HomeFragment(), "Home");
        mViewPagerAdapter.addFragments(new ReviewLibrary(), "Library");
        mViewPagerAdapter.addFragments(new SendMessageFragment(), "Create");
        mViewPagerAdapter.addFragments(new NewMessages(), "Inbox");
        myContext = getApplicationContext();
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_home_white_36dp);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_view_agenda_white_36dp);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_create_white_36dp);
        mTabLayout.getTabAt(3).setIcon(R.drawable.message);


//        readContactData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivityForResult(intent,0);
            return true;
        }
        if(id == R.id.action_sign_out){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean loadBoolean(String stringName, Context mContext){
        SharedPreferences mSharedPreference1=PreferenceManager.getDefaultSharedPreferences(mContext);
        Boolean sKey = mSharedPreference1.getBoolean("SMS_" + stringName,true);
        return sKey;
    }
}
