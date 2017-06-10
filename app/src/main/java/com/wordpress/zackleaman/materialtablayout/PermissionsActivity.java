package com.wordpress.zackleaman.materialtablayout;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/**
 * Created by Zack on 8/15/2016.
 * This is the PermissionActivity that is used to determine if user has granted all of the
 * necessary permissions to access the app.
 * Extends the AbsRuntimePermission class which handles RuntimePermission Utility Functions
 */
public class PermissionsActivity extends AbsRuntimePermission {

    private static final int REQUEST_PERMISSION = 10;
    private boolean bPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // RequestAppPermissions from extended AbsRuntimePermission class
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

    /**
     * Override extended classes onPermissionsGranted to launch MainActivity since permission granted
     * @param requestCode int that represents the request code for permissions
     */
    @Override
    public void onPermissionsGranted(int requestCode) {
        bPermissionGranted = true;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * If app is resumed then request the app permissions again from extended class
     */
    @Override
    protected void onResume() {
        super.onResume();
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
}
