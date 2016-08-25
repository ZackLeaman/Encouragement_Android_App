package com.wordpress.zackleaman.materialtablayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zack on 6/29/2016.
 */
//TODO FIX THIS CLASS
public class DialogPopup extends DialogFragment {
    private static final String LOG_TAG = DialogPopup.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    public static final String DIALOG_TYPE = "command";
    public static final String DELETE_RECORD = "deleteRecord";
    private ArrayList<String> encouragementList = new ArrayList<>();
    private ArrayList<String> shownEncouragementList = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme);
        loadArray(encouragementList,getContext(),"encouragementList");
        loadArray(shownEncouragementList,getContext(),"shownEncouragementList");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mLayoutInflater = getActivity().getLayoutInflater();
        Fragment fragment = getFragmentManager().findFragmentByTag("fragment_review_library");
        final View view = mLayoutInflater.inflate(R.layout.dialog_layout, null);
        String command = getArguments().getString(DIALOG_TYPE);

        if (command.equals(DELETE_RECORD)) {
            final int entryPos = getArguments().getInt("entryPos");
            final String msg = getArguments().getString("msg");
            final String type = getArguments().getString("type");
            final int FragmentId = getArguments().getInt("FragmentID");
            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("Are you sure you want to delete this entry?");
            builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                    ContentResolver contentResolver = getActivity().getContentResolver();
//                    Uri uri = FriendsContract.Friends.buildFriendUri(String.valueOf(_id));
//                    contentResolver.delete(uri, null, null);
//                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                    if(entryPos != -1){
                        encouragementList.remove(entryPos);
                        saveArray(encouragementList,"encouragementList");
                    }else{
                        for(int j = 0; j < encouragementList.size(); j ++){
                            if(msg.equals(encouragementList.get(j))){
                                encouragementList.remove(j);
                                saveArray(encouragementList,"encouragementList");
                                j = encouragementList.size();
                            }
                        }
                    }

                    //This is here to delete the entry from notification list
                    ArrayList<String> notificationEncouragementList = new ArrayList<>();
                    loadArray(notificationEncouragementList,getContext(),"notificationEncouragementList");
                    for(int j = 0; j < notificationEncouragementList.size(); j++) {
                        if (notificationEncouragementList.get(j).equals(msg)) {
                            notificationEncouragementList.remove(j);
                            saveArray(notificationEncouragementList, "notificationEncouragementList");
                            j = notificationEncouragementList.size();
                        }
                    }


                    if(type.equals("Review")) {
                        for (int l = 0; l < shownEncouragementList.size(); l++) {
                            if (msg.equals(shownEncouragementList.get(l))) {
                                shownEncouragementList.remove(l);
                                saveArray(shownEncouragementList, "shownEncouragementList");
//                                ((CustomFieldsFragmentAlertDialog)getFragmentManager().findFragmentById(FragmentId)).updateAdapter();
                                //((CustomFieldsFragmentAlertDialog)getTargetFragment()).updateAdapter();
                                //((CustomFieldsFragmentAlertDialog)getFragmentManager().findFragmentByTag("ReviewLibrary")).updateAdapter();
                                l = shownEncouragementList.size();
                            }
                        }
                    }

                    Toast.makeText(getContext().getApplicationContext(),"Entry Deleted",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

        } else {
            Log.d(LOG_TAG, "Invalid command passed as parameter");
        }


        return builder.create();

    }


    private boolean saveArray(List<String> sKey, String arrayName){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
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
