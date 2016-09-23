package com.wordpress.zackleaman.materialtablayout;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class PermissionsActivity extends AbsRuntimePermission {

    private static final int REQUEST_PERMISSION = 10;
    private boolean bPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    @Override
    public void onPermissionsGranted(int requestCode) {
        bPermissionGranted = true;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

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
