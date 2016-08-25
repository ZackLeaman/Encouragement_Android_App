package com.wordpress.zackleaman.materialtablayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    Activity activity;
    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentTransaction.add(R.id.linearLayoutSettings, settingsFragment, "SETTINGS_FRAGMENT");
        fragmentTransaction.commit();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        //ToolbarConfigurer(SettingsActivity.this, toolbar, true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        setSupportActionBar(toolbar);
        try {
            Log.d("ActionBar","at beginning");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Log.d("ActionBar","at end");

        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
        private ImageView imageViewBackgroundPreview;

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_pref);

            imageViewBackgroundPreview = (ImageView)getActivity().findViewById(R.id.imageViewBackgroundPreview);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            String picChoice = sp.getString("backgroundSelection","-1");
            imageViewBackgroundPreview.setVisibility(View.VISIBLE);
            if(picChoice.equals("-1")){
                imageViewBackgroundPreview.setVisibility(View.GONE);
            }else if(picChoice.equals("0")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.ariedlandscape_small);
            }else if(picChoice.equals("1")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.brickwall_small);
            }else if(picChoice.equals("2")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.city_small);
            }else if(picChoice.equals("3")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.mountains1_small);
            }else if(picChoice.equals("4")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.mountains2_small);
            }else if(picChoice.equals("5")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.roof_small);
            }else if(picChoice.equals("6")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea1_small);
            }else if(picChoice.equals("7")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea2_small);
            }else if(picChoice.equals("8")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea3_small);
            }else if(picChoice.equals("9")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sky1_small);
            }else if(picChoice.equals("10")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sky2_small);
            }else if(picChoice.equals("11")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sunset_small);
            }else if(picChoice.equals("12")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.tarp_small);
            }else if(picChoice.equals("13")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.wheatfield_small);
            }

        }

        @Override
        public void onResume() {
            super.onResume();
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            // Unregister the listener whenever a key changes
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            String picChoice = sp.getString("backgroundSelection","-1");
            imageViewBackgroundPreview.setVisibility(View.VISIBLE);
            if(picChoice.equals("-1")){
                imageViewBackgroundPreview.setVisibility(View.GONE);
            }else if(picChoice.equals("0")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.ariedlandscape_small);
            }else if(picChoice.equals("1")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.brickwall_small);
            }else if(picChoice.equals("2")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.city_small);
            }else if(picChoice.equals("3")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.mountains1_small);
            }else if(picChoice.equals("4")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.mountains2_small);
            }else if(picChoice.equals("5")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.roof_small);
            }else if(picChoice.equals("6")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea1_small);
            }else if(picChoice.equals("7")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea2_small);
            }else if(picChoice.equals("8")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea3_small);
            }else if(picChoice.equals("9")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sky1_small);
            }else if(picChoice.equals("10")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sky2_small);
            }else if(picChoice.equals("11")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sunset_small);
            }else if(picChoice.equals("12")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.tarp_small);
            }else if(picChoice.equals("13")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.wheatfield_small);
            }
        }

    }



    public void ToolbarConfigurer(Activity activity, Toolbar toolbar, boolean displayHomeAsUpEnabled) {
        toolbar.setTitle((this.activity = activity).getTitle());
        if (!displayHomeAsUpEnabled) return;
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            default:
//                Intent intent = new Intent(this,MainActivity.class);
//                startActivity(intent);
//                finish();
//                NavUtils.navigateUpFromSameTask(activity);

                break;
        }
    }

}
