package com.wordpress.zackleaman.materialtablayout;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendMessageFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private Button sendSmsBtn, btnCancelContact, btnSyncContacts;
    private EditText toPhoneNumber;
    private EditText smsMessageET;
    private EditText etTo;
    private String scAddress;
    private String destinationAddress;

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
        sendSmsBtn = (Button)getView().findViewById(R.id.btnSendSMS);
        toPhoneNumber = (EditText)getView().findViewById(R.id.editTextPhoneNo);
        smsMessageET = (EditText)getView().findViewById(R.id.editTextMessage);
        etTo = (EditText)getView().findViewById(R.id.editTextTo);
        sendSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });
        btnCancelContact = (Button)getView().findViewById(R.id.btnCancelContact);
        btnCancelContact.setVisibility(View.GONE);
        btnCancelContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelContact();
            }
        });
        btnSyncContacts = (Button)getView().findViewById(R.id.btnSyncContacts);
        btnSyncContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSyncContacts.setText("Syncing Contacts...");
                readContactData();
                textView.setAdapter(adapter);
            }
        });

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());


        if(phoneValueArr.isEmpty() && nameValueArr.isEmpty()){
            btnSyncContacts.setText("Sync Contacts");
        }

        // Initialize AutoCompleteTextView values

        textView = (AutoCompleteTextView) getView().findViewById(R.id.toNumber);
        textView.setDropDownBackgroundResource(R.color.colorPrimaryDarkAlpha);
        //Create adapter

        textView.setThreshold(1);

        //Set adapter to AutoCompleteTextView
        textView.setAdapter(adapter);
        textView.setOnItemSelectedListener(this);
        textView.setOnItemClickListener(this);

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

        // Read contact data and add data to ArrayAdapter
        // ArrayAdapter used by AutoCompleteTextView




        /********** Button Click pass textView object ***********/
        //Send.setOnClickListener(BtnAction(textView));
    }

    private void readContactData() {

        try {
            btnSyncContacts.setText("Syncing Contacts...");

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

            btnSyncContacts.setText("Contacts Synced");

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


    private View.OnClickListener BtnAction(final AutoCompleteTextView toNumber) {
        return new View.OnClickListener() {

            public void onClick(View v) {

                String NameSel = "";
                NameSel = toNumber.getText().toString();


                final String ToNumber = toNumberValue;


                if (ToNumber.length() == 0 ) {
                    Toast.makeText(getActivity().getBaseContext(), "Please fill phone number",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity().getBaseContext(), NameSel+" : "+toNumberValue,
                            Toast.LENGTH_LONG).show();
                }

            }
        };
    }




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



}
