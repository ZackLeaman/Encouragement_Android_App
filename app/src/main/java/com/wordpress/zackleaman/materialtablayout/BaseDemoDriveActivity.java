/**
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //showMessage("Im in on Create");
        //setContentView(R.layout.activity_second_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        onActivityResult = true;
        onActivityResult(REQUEST_CODE_RESOLUTION,RESULT_OK,null);

//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        if (sp.getBoolean("signedIn", true)) {
//            if(inAccountPicker) {
//                if(mGoogleApiClient2 != null) {
//                    mGoogleApiClient2.connect();
//                    showMessage("mGoogleApiClient2.connect()");
//                }
//
//                //inAccountPicker = false;
//            }
//            else{
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putBoolean("signedIn", false);
//                editor.commit();
//            }
//        }
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
        //showMessage("On Resume");

//        mGoogleApiClient2.hasConnectedApi(Drive.API);


        if (mGoogleApiClient2 != null) {
            //showMessage("onResume mGoogle");
            //showMessage("onResume connect");
            mGoogleApiClient2.connect();
        }


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.getBoolean("signedIn",false)) {

            Log.d("onResumeSignedin","sI = true " + sp.getBoolean("signedIn",false));
            try{
                if(myOptionMenu != null){
                    myOptionMenu.getItem(1).setVisible(false);
                    myOptionMenu.getItem(2).setVisible(true);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            Log.d("onResumeSignedin","sI = false " + sp.getBoolean("signedIn",false));
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

        //showMessage("onActivityResult");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            if (sp.getBoolean("signedIn", true)) {
                Log.d(TAG,"onActivity signedIn true");
                    if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
                        Log.d(TAG,"onActivity request and result");
                        if (mGoogleApiClient2 == null) {
                            mGoogleApiClient2 = new GoogleApiClient.Builder(this)
                                    .addApi(Drive.API)
                                    .addScope(Drive.SCOPE_FILE)
                                    // required for App Folder sample
                                    .addConnectionCallbacks(this)
                                    .addOnConnectionFailedListener(this)
                                    .build();
                            Log.d(TAG,"googleAPICLIENT build");
                        }

                        mGoogleApiClient2.connect();
                        Log.d(TAG,"googleAPICLIENT connect");
                    }
                    Log.d("accountPickerOnActiv", "account = true");
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
        Log.i(TAG, "GoogleApiClient connected");


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
        Log.d(TAG, "result.getErrorCode = " + result.getErrorCode());
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(result.getErrorCode() == 4) {
            if (sp.getBoolean("signedIn", false)) {

                MainActivity.isFirstTimeOpening = true;
                sp.edit().putBoolean("isFirstTimeOpening", true);
                sp.edit().commit();

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
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("signedIn", false);
                    editor.commit();
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


    public void clearGoogleApiClient(){

        if(mGoogleApiClient2 != null) {
            if(mGoogleApiClient2.isConnected() || mGoogleApiClient2.isConnecting()) {
                Log.d("onNewIntent", "in the mgoogleconnected to clear it");
                mGoogleApiClient2.unregisterConnectionCallbacks(this);
                mGoogleApiClient2.unregisterConnectionFailedListener(this);
                mGoogleApiClient2.clearDefaultAccountAndReconnect().setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if(status.isSuccess()){
                            Log.d("onNewIntent", "in success and bNeedToClear = " + MainActivity.bNeedToClear);
                            if(MainActivity.bNeedToClear) {
                                Log.d("onNewIntent", "in success and bNeedToClear");
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

        //mGoogleApiClient2 = null;

        if(mGoogleApiClient2 == null){
            //showMessage("mGoogleApiClient2 is null");
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
            Log.d(TAG,"googleAPICLIENT build");
        }

        mGoogleApiClient2.connect();
        Log.d(TAG,"googleAPICLIENT connect");
    }


}
