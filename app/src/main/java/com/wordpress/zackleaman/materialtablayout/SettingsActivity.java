package com.wordpress.zackleaman.materialtablayout;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Zack on 6/20/2016.
 * This class is runs the settings activity and is responsible for showing the user the shared pref
 * editable for the user.
 */
public class SettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // CREATE FRAGMENT MANAGER AND ADD SETTINGS FRAGMENT TO THE FRAGMENT TRANSACTION
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentTransaction.add(R.id.linearLayoutSettings, settingsFragment, "SETTINGS_FRAGMENT");
        fragmentTransaction.commit();


        // CREATE A BACK BUTTON ON THE TOOLBAR TO EXIT SETTINGS ACTIVITY
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
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
            if(null != getSupportActionBar()) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * Created by Zack on 6/20/2016.
     * This class is runs the settings fragment that extends PreferenceFragment for shared pref
     * editing. Also implements OnSharedPreferenceChangeListener to check if shared pref changed.
     */
    public static class SettingsFragment extends PreferenceFragment implements
            SharedPreferences.OnSharedPreferenceChangeListener{

        private ImageView imageViewBackgroundPreview;

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_pref);

            // Get the background to display on image unless -1 which means cycle backgrounds
            imageViewBackgroundPreview = (ImageView)getActivity().findViewById(R.id.imageViewBackgroundPreview);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            String picChoice = sp.getString("backgroundSelection","-1");
            imageViewBackgroundPreview.setVisibility(View.VISIBLE);
            switch (picChoice) {
                case "-1":
                    imageViewBackgroundPreview.setVisibility(View.GONE);
                    break;
                case "0":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.ariedlandscape_small);
                    break;
                case "1":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.brickwall_small);
                    break;
                case "2":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.city_small);
                    break;
                case "3":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.mountains1_small);
                    break;
                case "4":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.mountains2_small);
                    break;
                case "5":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.roof_small);
                    break;
                case "6":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea1_small);
                    break;
                case "7":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea2_small);
                    break;
                case "8":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea3_small);
                    break;
                case "9":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sky1_small);
                    break;
                case "10":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sky2_small);
                    break;
                case "11":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sunset_small);
                    break;
                case "12":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.tarp_small);
                    break;
                case "13":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.wheatfield_small);
                    break;
                case "14":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.city_2_small);
                    break;
                case "15":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.crumbled_paper_small);
                    break;
                case "16":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.forest_small);
                    break;
                case "17":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.hut_wall_small);
                    break;
                case "18":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea_4_small);
                    break;
                case "19":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.shore_small);
                    break;
                case "20":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.train_small);
                    break;
                case "21":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.tropic_2_small);
                    break;
                case "22":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.wood_small);
                    break;
                case "23":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.aridcliff_small);
                    break;
                case "24":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.aridroad_small);
                    break;
                case "25":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.brickwall2_small);
                    break;
                case "26":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.cementwall_small);
                    break;
                case "27":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.city3_small);
                    break;
                case "28":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.crackedsoil_small);
                    break;
                case "29":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.crumbledpaper2_small);
                    break;
                case "30":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.elephant_small);
                    break;
                case "31":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.emeraldbrick_small);
                    break;
                case "32":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.fabric_small);
                    break;
                case "33":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.floortiled1_small);
                    break;
                case "34":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.floortiled2_small);
                    break;
                case "35":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.flowers_small);
                    break;
                case "36":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.forestmountain2_small);
                    break;
                case "37":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.giraffe_small);
                    break;
                case "38":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.grungemap_small);
                    break;
                case "39":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.jellyfish_small);
                    break;
                case "40":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.metalstripes_small);
                    break;
                case "41":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.mountainpeak2_small);
                    break;
                case "42":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.ocean_small);
                    break;
                case "43":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.owl_small);
                    break;
                case "44":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.panda_small);
                    break;
                case "45":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.patternedbrick_small);
                    break;
                case "46":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.prairiedog_small);
                    break;
                case "47":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.redpanda_small);
                    break;
                case "48":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.rockformation_small);
                    break;
                case "49":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.savannah1_small);
                    break;
                case "50":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.savannah2_small);
                    break;
                case "51":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.snowymountain1_small);
                    break;
                case "52":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.snowymountain2_small);
                    break;
                case "53":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.stonedoorway_small);
                    break;
                case "54":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.tiger_small);
                    break;
                case "55":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.tree_small);
                    break;
                case "56":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.tropic4_small);
                    break;
                case "57":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.wildwest1_small);
                    break;
                case "58":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.wildwest2_small);
                    break;
                case "59":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.woodendoors_small);
                    break;
                case "60":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.plain_small);
                    break;
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
            // Background was changed so update the new image view preview
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            String picChoice = sp.getString("backgroundSelection","-1");
            imageViewBackgroundPreview.setVisibility(View.VISIBLE);
            switch (picChoice) {
                case "-1":
                    imageViewBackgroundPreview.setVisibility(View.GONE);
                    break;
                case "0":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.ariedlandscape_small);
                    break;
                case "1":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.brickwall_small);
                    break;
                case "2":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.city_small);
                    break;
                case "3":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.mountains1_small);
                    break;
                case "4":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.mountains2_small);
                    break;
                case "5":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.roof_small);
                    break;
                case "6":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea1_small);
                    break;
                case "7":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea2_small);
                    break;
                case "8":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea3_small);
                    break;
                case "9":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sky1_small);
                    break;
                case "10":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sky2_small);
                    break;
                case "11":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sunset_small);
                    break;
                case "12":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.tarp_small);
                    break;
                case "13":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.wheatfield_small);
                    break;
                case "14":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.city_2_small);
                    break;
                case "15":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.crumbled_paper_small);
                    break;
                case "16":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.forest_small);
                    break;
                case "17":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.hut_wall_small);
                    break;
                case "18":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.sea_4_small);
                    break;
                case "19":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.shore_small);
                    break;
                case "20":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.train_small);
                    break;
                case "21":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.tropic_2_small);
                    break;
                case "22":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.wood_small);
                    break;
                case "23":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.aridcliff_small);
                    break;
                case "24":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.aridroad_small);
                    break;
                case "25":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.brickwall2_small);
                    break;
                case "26":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.cementwall_small);
                    break;
                case "27":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.city3_small);
                    break;
                case "28":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.crackedsoil_small);
                    break;
                case "29":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.crumbledpaper2_small);
                    break;
                case "30":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.elephant_small);
                    break;
                case "31":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.emeraldbrick_small);
                    break;
                case "32":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.fabric_small);
                    break;
                case "33":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.floortiled1_small);
                    break;
                case "34":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.floortiled2_small);
                    break;
                case "35":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.flowers_small);
                    break;
                case "36":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.forestmountain2_small);
                    break;
                case "37":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.giraffe_small);
                    break;
                case "38":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.grungemap_small);
                    break;
                case "39":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.jellyfish_small);
                    break;
                case "40":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.metalstripes_small);
                    break;
                case "41":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.mountainpeak2_small);
                    break;
                case "42":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.ocean_small);
                    break;
                case "43":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.owl_small);
                    break;
                case "44":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.panda_small);
                    break;
                case "45":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.patternedbrick_small);
                    break;
                case "46":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.prairiedog_small);
                    break;
                case "47":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.redpanda_small);
                    break;
                case "48":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.rockformation_small);
                    break;
                case "49":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.savannah1_small);
                    break;
                case "50":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.savannah2_small);
                    break;
                case "51":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.snowymountain1_small);
                    break;
                case "52":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.snowymountain2_small);
                    break;
                case "53":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.stonedoorway_small);
                    break;
                case "54":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.tiger_small);
                    break;
                case "55":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.tree_small);
                    break;
                case "56":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.tropic4_small);
                    break;
                case "57":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.wildwest1_small);
                    break;
                case "58":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.wildwest2_small);
                    break;
                case "59":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.woodendoors_small);
                    break;
                case "60":
                    imageViewBackgroundPreview.setBackgroundResource(R.drawable.plain_small);
                    break;
            }
        }

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();

    }


}
