package com.wordpress.zackleaman.materialtablayout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;

/**
 * Created by Zack on 8/15/2016.
 * This class will handle all necessary runtime permissions
 */
public abstract class AbsRuntimePermission extends AppCompatActivity {
    private SparseIntArray mErrorString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mErrorString = new SparseIntArray();

    }

    /**
     * Once permissions are granted this abstract method is called for child classes
     * @param requestCode int that represents the request code for permissions
     */
    public abstract void onPermissionsGranted(int requestCode);

    /**
     * This method requests app permissions from the user
     * @param requestedPermissions string array of all the permissions needed to be requested
     * @param stringId final int id of a string resource to display to user as request title
     * @param requestCode final int request code id
     */
    public void requestAppPermissions(final String[] requestedPermissions, final int stringId, final int requestCode){
        mErrorString.put(requestCode,stringId);

        // check the current permissions to see if already accepted or denied
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean showRequestPermission = false;
        for(String permission: requestedPermissions){
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            showRequestPermission = showRequestPermission || ActivityCompat.shouldShowRequestPermissionRationale(this,permission);
        }

        // if not granted then show the snackbar message on bottom of screen to grant permission
        if(permissionCheck!=PackageManager.PERMISSION_GRANTED){
            if(showRequestPermission){
                Snackbar.make(findViewById(android.R.id.content),stringId, Snackbar.LENGTH_INDEFINITE).setAction("Grant", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(AbsRuntimePermission.this,requestedPermissions,requestCode);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions,requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }

    /**
     * When app requests permissions, the system presents a dialog box. When user responds, the
     * system invokes app's onRequestPermissionsResult() passing it the user response.
     * App overrides this method to find out whether the permission was granted.
     * The callback is passed the same request code you passed to requestPermissions()
     * @param requestCode int that represents the requestCode
     * @param permissions string array that represents the permissions
     * @param grantResults int array that represents the permissions that were granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for(int permission : grantResults){
            permissionCheck = permissionCheck + permission;
        }

        if( (grantResults.length > 0) && PackageManager.PERMISSION_GRANTED == permissionCheck){
            onPermissionsGranted(requestCode);
        } else {
            // Display Message when contain some Dangerous Permission not accept
            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.setData(Uri.parse("package:" + getPackageName()));
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(i);
                }
            }).show();
        }
    }
}
