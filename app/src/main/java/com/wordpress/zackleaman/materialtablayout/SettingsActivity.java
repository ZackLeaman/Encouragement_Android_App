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
            }else if(picChoice.equals("14")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.city_2_small);
            }else if(picChoice.equals("15")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.crumbled_paper_small);
            }else if(picChoice.equals("16")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.forest_small);
            }else if(picChoice.equals("17")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.hut_wall_small);
            }else if(picChoice.equals("18")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea_4_small);
            }else if(picChoice.equals("19")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.shore_small);
            }else if(picChoice.equals("20")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.train_small);
            }else if(picChoice.equals("21")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.tropic_2_small);
            }else if(picChoice.equals("22")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.wood_small);
            }else if(picChoice.equals("23")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.aridcliff_small);
            }else if(picChoice.equals("24")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.aridroad_small);
            }else if(picChoice.equals("25")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.brickwall2_small);
            }else if(picChoice.equals("26")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.cementwall_small);
            }else if(picChoice.equals("27")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.city3_small);
            }else if(picChoice.equals("28")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.crackedsoil_small);
            }else if(picChoice.equals("29")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.crumbledpaper2_small);
            }else if(picChoice.equals("30")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.elephant_small);
            }else if(picChoice.equals("31")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.emeraldbrick_small);
            }else if(picChoice.equals("32")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.fabric_small);
            }else if(picChoice.equals("33")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.floortiled1_small);
            }else if(picChoice.equals("34")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.floortiled2_small);
            }else if(picChoice.equals("35")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.flowers_small);
            }else if(picChoice.equals("36")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.forestmountain2_small);
            }else if(picChoice.equals("37")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.giraffe_small);
            }else if(picChoice.equals("38")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.grungemap_small);
            }else if(picChoice.equals("39")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.jellyfish_small);
            }else if(picChoice.equals("40")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.metalstripes_small);
            }else if(picChoice.equals("41")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.mountainpeak2_small);
            }else if(picChoice.equals("42")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.ocean_small);
            }else if(picChoice.equals("43")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.owl_small);
            }else if(picChoice.equals("44")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.panda_small);
            }else if(picChoice.equals("45")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.patternedbrick_small);
            }else if(picChoice.equals("46")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.prairiedog_small);
            }else if(picChoice.equals("47")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.redpanda_small);
            }else if(picChoice.equals("48")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.rockformation_small);
            }else if(picChoice.equals("49")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.savannah1_small);
            }else if(picChoice.equals("50")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.savannah2_small);
            }else if(picChoice.equals("51")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.snowymountain1_small);
            }else if(picChoice.equals("52")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.snowymountain2_small);
            }else if(picChoice.equals("53")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.stonedoorway_small);
            }else if(picChoice.equals("54")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.tiger_small);
            }else if(picChoice.equals("55")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.tree_small);
            }else if(picChoice.equals("56")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.tropic4_small);
            }else if(picChoice.equals("57")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.wildwest1_small);
            }else if(picChoice.equals("58")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.wildwest2_small);
            }else if(picChoice.equals("59")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.woodendoors_small);
            }else if(picChoice.equals("60")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.plain_small);
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
            }else if(picChoice.equals("14")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.city_2_small);
            }else if(picChoice.equals("15")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.crumbled_paper_small);
            }else if(picChoice.equals("16")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.forest_small);
            }else if(picChoice.equals("17")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.hut_wall_small);
            }else if(picChoice.equals("18")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea_4_small);
            }else if(picChoice.equals("19")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.shore_small);
            }else if(picChoice.equals("20")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.train_small);
            }else if(picChoice.equals("21")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.tropic_2_small);
            }else if(picChoice.equals("22")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.wood_small);
            }else if(picChoice.equals("23")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.aridcliff_small);
            }else if(picChoice.equals("24")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.aridroad_small);
            }else if(picChoice.equals("25")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.brickwall2_small);
            }else if(picChoice.equals("26")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.cementwall_small);
            }else if(picChoice.equals("27")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.city3_small);
            }else if(picChoice.equals("28")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.crackedsoil_small);
            }else if(picChoice.equals("29")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.crumbledpaper2_small);
            }else if(picChoice.equals("30")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.elephant_small);
            }else if(picChoice.equals("31")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.emeraldbrick_small);
            }else if(picChoice.equals("32")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.fabric_small);
            }else if(picChoice.equals("33")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.floortiled1_small);
            }else if(picChoice.equals("34")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.floortiled2_small);
            }else if(picChoice.equals("35")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.flowers_small);
            }else if(picChoice.equals("36")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.forestmountain2_small);
            }else if(picChoice.equals("37")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.giraffe_small);
            }else if(picChoice.equals("38")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.grungemap_small);
            }else if(picChoice.equals("39")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.jellyfish_small);
            }else if(picChoice.equals("40")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.metalstripes_small);
            }else if(picChoice.equals("41")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.mountainpeak2_small);
            }else if(picChoice.equals("42")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.ocean_small);
            }else if(picChoice.equals("43")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.owl_small);
            }else if(picChoice.equals("44")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.panda_small);
            }else if(picChoice.equals("45")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.patternedbrick_small);
            }else if(picChoice.equals("46")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.prairiedog_small);
            }else if(picChoice.equals("47")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.redpanda_small);
            }else if(picChoice.equals("48")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.rockformation_small);
            }else if(picChoice.equals("49")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.savannah1_small);
            }else if(picChoice.equals("50")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.savannah2_small);
            }else if(picChoice.equals("51")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.snowymountain1_small);
            }else if(picChoice.equals("52")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.snowymountain2_small);
            }else if(picChoice.equals("53")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.stonedoorway_small);
            }else if(picChoice.equals("54")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.tiger_small);
            }else if(picChoice.equals("55")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.tree_small);
            }else if(picChoice.equals("56")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.tropic4_small);
            }else if(picChoice.equals("57")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.wildwest1_small);
            }else if(picChoice.equals("58")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.wildwest2_small);
            }else if(picChoice.equals("59")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.woodendoors_small);
            }else if(picChoice.equals("60")){
                imageViewBackgroundPreview.setBackgroundResource(R.drawable.plain_small);
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
