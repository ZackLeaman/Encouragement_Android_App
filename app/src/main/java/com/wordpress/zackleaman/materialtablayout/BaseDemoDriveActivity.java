
package com.wordpress.zackleaman.materialtablayout;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;

/**
 * Created by Zack on 8/15/2016.
 * An abstract activity that handles authorization and connection to the Drive
 * services.
 */
public abstract class BaseDemoDriveActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BaseDrActivity";

    /**
     * DriveId of an existing folder to be used as a parent folder in
     * folder operations samples.
     */
    public static final String EXISTING_FOLDER_ID = "0B2EEtIjPUdX6MERsWlYxN3J6RU0";

    /**
     * DriveId of an existing file to be used in file operation samples..
     */
//    public static String EXISTING_FILE_ID = "0ByfSjdPVs9MZTHBmMVdSeWxaNTg";

    /**
     * Extra for account name.
     */
    protected static final String EXTRA_ACCOUNT_NAME = "account_name";

    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_CODE_RESOLUTION = 1;

    /**
     * Next available request code.
     */
    protected static final int NEXT_AVAILABLE_REQUEST_CODE = 2;

    /**
     * Google API client.
     */
    public GoogleApiClient mGoogleApiClient2;

    public static boolean backButtonPressed = false;
    public static boolean inAccountPicker = false;
    public static Menu myOptionMenu = null;
    private boolean onActivityResult;
    private int numOfConnectionFailed = 0;

    public static boolean NEED_CLEAR_GAC = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onActivityResult = true;
        onActivityResult(REQUEST_CODE_RESOLUTION,RESULT_OK,null);

    }

    /**
     * Called when activity gets visible. A connection to Drive services need to
     * be initiated as soon as the activity is visible. Registers
     * {@code ConnectionCallbacks} and {@code OnConnectionFailedListener} on the
     * activities itself.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient2 != null) {

            mGoogleApiClient2.connect();
        }


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.getBoolean("signedIn",false)) {

            try{
                if(myOptionMenu != null){
                    myOptionMenu.getItem(1).setVisible(false);
                    myOptionMenu.getItem(2).setVisible(true);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            try{
                if(myOptionMenu != null){
                    myOptionMenu.getItem(1).setVisible(true);
                    myOptionMenu.getItem(2).setVisible(false);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * Handles resolution callbacks.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            if (sp.getBoolean("signedIn", true)) {
                    if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
                        if (mGoogleApiClient2 == null) {
                            mGoogleApiClient2 = new GoogleApiClient.Builder(this)
                                    .addApi(Drive.API)
                                    .addScope(Drive.SCOPE_FILE)
                                    // required for App Folder sample
                                    .addConnectionCallbacks(this)
                                    .addOnConnectionFailedListener(this)
                                    .build();
                        }

                        mGoogleApiClient2.connect();
                    }
        }

    }

    /**
     * Called when activity gets invisible. Connection to Drive service needs to
     * be disconnected as soon as an activity is invisible.
     */
    @Override
    protected void onPause() {
        if (mGoogleApiClient2 != null) {
            mGoogleApiClient2.disconnect();
        }
        super.onPause();
    }


    /**
     * Called when {@code mGoogleApiClient} is connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        if(NEED_CLEAR_GAC){
            clearGoogleApiClient();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("signedIn", false);
            editor.putBoolean("signedIn_Pressed", false);
            editor.commit();
            try{
                if(myOptionMenu != null){
                    myOptionMenu.getItem(1).setVisible(true);
                    myOptionMenu.getItem(2).setVisible(false);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            NEED_CLEAR_GAC = false;
        }

    }

    /**
     * Called when {@code mGoogleApiClient} is disconnected.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    /**
     * Called when {@code mGoogleApiClient} is trying to connect but failed.
     * Handle {@code result.getResolution()} if there is a resolution is
     * available.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(result.getErrorCode() == 4) {
            if (sp.getBoolean("signedIn", false)) {

                MainActivity.isFirstTimeOpening = true;
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isFirstTimeOpening", true);
                editor.commit();

                if (numOfConnectionFailed == 0) {
                    numOfConnectionFailed++;
                    if (!result.hasResolution()) {
                        // show the localized error dialog.
                        GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
                        return;
                    }
                    try {
                        result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e(TAG, "Exception while starting resolution activity", e);
                    }


                } else if (numOfConnectionFailed > 0) {
                    mGoogleApiClient2.disconnect();
                    SharedPreferences.Editor editor2 = sp.edit();
                    editor2.putBoolean("signedIn", false);
                    editor2.commit();
                    myOptionMenu.getItem(1).setVisible(true);
                    myOptionMenu.getItem(2).setVisible(false);
                }
            }
        }else{
            if (!result.hasResolution()) {
                // show the localized error dialog.
                GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
                return;
            }
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "Exception while starting resolution activity", e);
            }
        }

    }

    /**
     * Shows a toast message.
     */
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Getter for the {@code GoogleApiClient}.
     */
    public GoogleApiClient getGoogleApiClient() {
      return mGoogleApiClient2;
    }

    public static void clearGACOnConnected(){
        NEED_CLEAR_GAC = true;
    }

    public void clearGoogleApiClient(){

        if(mGoogleApiClient2 != null) {
            if(mGoogleApiClient2.isConnected() || mGoogleApiClient2.isConnecting()) {
                mGoogleApiClient2.unregisterConnectionCallbacks(this);
                mGoogleApiClient2.unregisterConnectionFailedListener(this);
                mGoogleApiClient2.clearDefaultAccountAndReconnect().setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if(status.isSuccess()){
                            if(MainActivity.bNeedToClear) {
                                MainActivity.bNeedToClear = false;
                                mGoogleApiClient2.disconnect();
                                reconnectGoogleApiClient();
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        }
                    }
                });

            }
        }

    }

    public void reconnectGoogleApiClient(){
        if (mGoogleApiClient2 == null) {
            mGoogleApiClient2 = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    // required for App Folder sample
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        mGoogleApiClient2.connect();
    }


}
