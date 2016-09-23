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
    public static final String OTHER_CATEGORIES = "otherCategories";
    public static final String FIRST_FOUND_DRIVE  = "firstFoundDrive";
    public static final String NEXT_FOUND_DRIVE = "nextFoundDrive";
    public static final String CANT_FIND_DRIVE = "cantFindDrive";
    public static final String RESTORE_BACKUP_CONFIRM = "restoreBackupConfirm";
    public static final String BACKUP_DRIVE_CONFIRM = "backupDriveConfirm";
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

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String homeEncouragement = sp.getString("homeEncouragement","none");
                    if(msg.equals(homeEncouragement)){
                        if(!notificationEncouragementList.isEmpty()){
                            //homeEncouragement = notificationEncouragementList.get(0);
                            SharedPreferences.Editor mEdit1 = sp.edit();
                            mEdit1.remove("homeEncouragement");
                            //mEdit1.putString("homeEncouragement",homeEncouragement);
                            mEdit1.commit();
                        }else{
                           // homeEncouragement = "/nEncouragement/n /nNo Entries in Encouragement/nnone/n3/nAlarm Off";
                            SharedPreferences.Editor mEdit1 = sp.edit();
                            mEdit1.remove("homeEncouragement");
                            //mEdit1.putString("homeEncouragement",homeEncouragement);
                            mEdit1.commit();
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

        } else if(command.equals(OTHER_CATEGORIES)){

            final int FragmentId = getArguments().getInt("FragmentID");
            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("Would you like to include Prayer and Scripture Categories?");
            builder.setView(view).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor mEdit1 = sp.edit();
                    mEdit1.putBoolean("wantsPrayerAndScripture",true);
                    mEdit1.commit();
//                    Intent intent = new Intent(getContext(),MainActivity.class);
//                    getActivity().finish();
//                    startActivity(intent);
                }
            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor mEdit1 = sp.edit();
                            mEdit1.putBoolean("wantsPrayerAndScripture",false);
                            mEdit1.commit();
                            Intent intent = new Intent(getContext(),MainActivity.class);
                            getActivity().finish();
                            startActivity(intent);
                        }
                    });

        }else if(command.equals(FIRST_FOUND_DRIVE)) {

            final int FragmentId = getArguments().getInt("FragmentID");
            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("File Found!\n\nWould you like to Restore from Backup or Override Backup on Drive?");
            builder.setView(view).setPositiveButton("Restore Backup", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DialogPopup dialog = new DialogPopup();
                    Bundle args = new Bundle();
                    args.putString(DialogPopup.DIALOG_TYPE, DialogPopup.RESTORE_BACKUP_CONFIRM);
                    dialog.setArguments(args);
                    dialog.show(getFragmentManager(), "restore-backup-confirm");
                }
            })
                    .setNegativeButton("Override Backup", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DialogPopup dialog = new DialogPopup();
                            Bundle args = new Bundle();
                            args.putString(DialogPopup.DIALOG_TYPE, DialogPopup.BACKUP_DRIVE_CONFIRM);
                            dialog.setArguments(args);
                            dialog.show(getFragmentManager(), "backup-drive-confirm");
                        }
                    });


        }else if(command.equals(CANT_FIND_DRIVE)) {

            final int FragmentId = getArguments().getInt("FragmentID");
            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("File Not Found!\n\nWould you like to Sign In to a Different Account or Create a New Backup on this Account?");
            builder.setView(view).setPositiveButton("Sign In to a Different Account", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //TODO have a resign in
                    //signedIn = true;
                    //isFirstTimeOpening = true;
                    //MainActivity.firstTimeAppOpened = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("signedIn", true);
                    editor.putBoolean("signedIn_Pressed", true);
                    editor.putBoolean("isFirstTimeOpening",true);
                    editor.commit();
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("clearGAC","clearGAC");
                    intent.setAction("com.wordpress.zackleaman.materialtablayout.intent.action.ACTION_NAME" + 758520);
                    getActivity().finish();
                    startActivity(intent);
                }
            })
                    .setNegativeButton("Create a New Backup", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("signedIn", true);
                            editor.commit();
                            MainActivity.myOptionMenu.getItem(1).setVisible(false);
                            MainActivity.myOptionMenu.getItem(2).setVisible(true);
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("curState","3");
                            intent.setAction("com.wordpress.zackleaman.materialtablayout.intent.action.ACTION_NAME" + 758520);
                            getActivity().finish();
                            startActivity(intent);
                        }
                    });


        }else if(command.equals(RESTORE_BACKUP_CONFIRM)) {

            final int FragmentId = getArguments().getInt("FragmentID");
            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("Are you sure you wish to Restore from Drive Backup?");
            builder.setView(view).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("signedIn", true);
                    editor.commit();
                    MainActivity.myOptionMenu.getItem(1).setVisible(false);
                    MainActivity.myOptionMenu.getItem(2).setVisible(true);
                }
            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DialogPopup dialog = new DialogPopup();
                            Bundle args = new Bundle();
                            args.putString(DialogPopup.DIALOG_TYPE, DialogPopup.FIRST_FOUND_DRIVE);
                            dialog.setArguments(args);
                            dialog.show(getFragmentManager(), "first_found_drive");
                        }
                    });


        }else if(command.equals(BACKUP_DRIVE_CONFIRM)) {

            final int FragmentId = getArguments().getInt("FragmentID");
            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("Are you sure you wish to Backup Local to Drive? This will override Drive Backup.");
            builder.setView(view).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("signedIn", true);
                    editor.commit();
                    MainActivity.myOptionMenu.getItem(1).setVisible(false);
                    MainActivity.myOptionMenu.getItem(2).setVisible(true);
                }
            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DialogPopup dialog = new DialogPopup();
                            Bundle args = new Bundle();
                            args.putString(DialogPopup.DIALOG_TYPE, DialogPopup.FIRST_FOUND_DRIVE);
                            dialog.setArguments(args);
                            dialog.show(getFragmentManager(), "first_found_drive");
                        }
                    });


        }else {
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
