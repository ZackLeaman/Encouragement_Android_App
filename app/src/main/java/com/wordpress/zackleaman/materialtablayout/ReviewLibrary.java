package com.wordpress.zackleaman.materialtablayout;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Zack on 8/15/2016.
 * This is a fragment material layout tab to show the users encouragement library.
 * This is called and used by the MainActivity class
 */
public class ReviewLibrary extends Fragment implements AdapterView.OnItemClickListener,
        PopupMenu.OnMenuItemClickListener{

    private ArrayList<String> encouragementList;
    private ArrayList<String> shownEncouragementList;
    private ArrayList<String> selectedEncouragement;
    private ArrayList<String> categoriesList;
    private ArrayList<String> categoriesListPrayer;
    private ArrayList<String> categoriesListScripture;
    private ListView lvEncouragement;

    private enum ReviewState{MAIN_CATEGORIES, CATEGORIES, CATEGORY, INDIVIDUAL}
    private ReviewState reviewState;
    private Button btnBack,btnCategoryCreate, btnAdd;
    private TextView tvTitle;
    private EditText etCategoryEntry;
    private LinearLayout linearLayoutAddCategory;

    private int positionInt;
    private int mainCategory;
    private boolean btnAddActive, editActive;
    private String[] subCategory;



    public ReviewLibrary() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_library, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // MAKE REFERENCE TO VIEW ITEMS AND INITIALIZE VARIABLES
        reviewState = ReviewState.MAIN_CATEGORIES;
        positionInt = 0;
        mainCategory = 0;
        btnAddActive = false;
        editActive = false;
        encouragementList=new ArrayList<>();
        shownEncouragementList = new ArrayList<>();
        categoriesList = new ArrayList<>();
        categoriesListPrayer = new ArrayList<>();
        categoriesListScripture = new ArrayList<>();
        selectedEncouragement = new ArrayList<>();

        // LOAD ARRAYS FROM SHARED PREFERENCES
        loadArray(encouragementList,getContext().getApplicationContext(),"encouragementList");
        loadArray(categoriesList,getContext().getApplicationContext(),"categoriesList");
        loadArray(categoriesListPrayer,getContext().getApplicationContext(),"categoriesListPrayer");
        loadArray(categoriesListScripture,getContext().getApplicationContext(),"categoriesListScripture");

        // CHECK IF USER WANTS THE STARTER ENCOURAGEMENTS
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean wantsPrayerAndScripture = sp.getBoolean("wantsPrayerAndScripture",true);
        if(!wantsPrayerAndScripture){
            categoriesList.clear();
            categoriesList.add("/nEncouragement/n");
        }




        lvEncouragement=(ListView)getView().findViewById(R.id.EncouragementList);

        lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesList));
        lvEncouragement.setOnItemClickListener(this);

        // Add the back button functionality for the library
        btnBack = (Button)getView().findViewById(R.id.btnBack);
        btnBack.setVisibility(View.GONE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (reviewState){
                    // if user is currently in a category and wants to go back to category selection
                    case CATEGORIES:
                        btnAddActive = false;
                        btnBack.setVisibility(View.GONE);
                        btnAdd.setVisibility(View.INVISIBLE);
                        btnCategoryCreate.setText(R.string.create);
                        editActive = false;
                        tvTitle.setText(R.string.categories);
                        mainCategory = 0;
                        lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesList));
                        btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                        linearLayoutAddCategory.setVisibility(View.GONE);
                        reviewState = ReviewState.MAIN_CATEGORIES;
                        break;
                    // if user is in subcategory and wants to go back to subcategory selection
                    case CATEGORY:
                        btnBack.setVisibility(View.VISIBLE);
                        if(mainCategory == 1) {
                            tvTitle.setText(R.string.categories);
                            lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesList));
                            reviewState = ReviewState.MAIN_CATEGORIES;
                            btnBack.setVisibility(View.GONE);
                            btnAdd.setVisibility(View.INVISIBLE);
                            btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                            btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                        }else if(mainCategory == 2) {
                            tvTitle.setText(R.string._dash_Prayer);
                            lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListPrayer));
                            reviewState = ReviewState.CATEGORIES;
                        }else if(mainCategory == 3){
                            tvTitle.setText(R.string._dash_Scripture);
                            lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListScripture));
                            reviewState = ReviewState.CATEGORIES;
                        }
                        break;
                    case INDIVIDUAL:
                        reviewState = ReviewState.CATEGORY;
                        break;
                    default:
                        break;
                }
            }
        });
        // Add the add button functionality for adding subcategories or individual entries
        btnAdd = (Button)getView().findViewById(R.id.btnAdd);
        btnAdd.setVisibility(View.INVISIBLE);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (reviewState){
                    // If add button clicked while in subcategories menu
                    case CATEGORIES:
                        // Toggle displaying and hiding the add subcategory layout
                        btnAddActive = !btnAddActive;
                        if(btnAddActive){
                            linearLayoutAddCategory.setVisibility(View.VISIBLE);
                            btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                            btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                            etCategoryEntry.setText("");
                            btnCategoryCreate.setText(R.string.create);
                        }else{
                            linearLayoutAddCategory.setVisibility(View.GONE);
                            btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                            btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                            etCategoryEntry.setText("");
                            btnCategoryCreate.setText(R.string.create);
                        }
                        break;
                    // If add button clicked while in entries menu
                    case CATEGORY:
                        // Based on which category in put string extra and start Add entry activity
                        Intent intent = new Intent(getContext(),AddEntryActivity.class);
                        // Encouragement Main Category
                        if(mainCategory == 1){
                            // put extra category of entry
                            intent.putExtra("category_createEntry","Encouragement");

                        // Prayer Main Category
                        }else if(mainCategory == 2){
                            // put extra category of entry
                            intent.putExtra("category_createEntry","Prayer");

                            // put extra subcategory of entry
                            String[] subCategory = categoriesListPrayer.get(positionInt).split("/n");
                            intent.putExtra("subCategory_createEntry",subCategory[1]);

                        // Scripture Main Category
                        }else if(mainCategory == 3){
                            // put extra category of entry
                            intent.putExtra("category_createEntry","Scripture");

                            // put extra subcategory of entry
                            String[] subCategory = categoriesListScripture.get(positionInt).split("/n");
                            intent.putExtra("subCategory_createEntry",subCategory[1]);
                        }
                        getActivity().finish();
                        startActivity(intent);
                        break;
                }
            }
        });

        linearLayoutAddCategory = (LinearLayout)getView().findViewById(R.id.linearLayoutAddCategory);
        btnCategoryCreate = (Button)getView().findViewById(R.id.btnCategoryCreate);
        etCategoryEntry = (EditText)getView().findViewById(R.id.etCategoryEntry);
        linearLayoutAddCategory.setVisibility(View.GONE);

        // Button to Create a sub category
        btnCategoryCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (reviewState){
                    case CATEGORIES:
                        if(editActive){
                            editList(positionInt);
                            linearLayoutAddCategory.setVisibility(View.GONE);
                            btnAddActive = false;
                            editActive = false;
                            etCategoryEntry.setText("");
                            btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                            btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                        }else {
                            addListEntry(etCategoryEntry.getText().toString());
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        tvTitle = (TextView)getView().findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.categories);
        Log.d("encouragementList",encouragementList.toString());

    }

    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        // WHEN PHONE BACK BUTTON PRESSED
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if(keyEvent.getAction() == keyEvent.ACTION_UP && i == keyEvent.KEYCODE_BACK){
                    switch (reviewState) {
                        // If Main Categories showing then exit out of application
                        case MAIN_CATEGORIES:
                            getActivity().finish();
                            break;
                        // If in Sub Categories menu then go back to Main Categories
                        case CATEGORIES:
                            btnAddActive = false;
                            btnBack.setVisibility(View.GONE);
                            btnAdd.setVisibility(View.INVISIBLE);
                            btnCategoryCreate.setText(R.string.create);
                            editActive = false;
                            tvTitle.setText(R.string.categories);
                            mainCategory = 0;
                            lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesList));
                            btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                            btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                            linearLayoutAddCategory.setVisibility(View.GONE);
                            reviewState = ReviewState.MAIN_CATEGORIES;
                            break;
                        // If in Entry Listings then go back to sub or main categories
                        case CATEGORY:
                            btnBack.setVisibility(View.VISIBLE);
                            // Encouragement so go back to main categories
                            if (mainCategory == 1) {
                                tvTitle.setText(R.string.categories);
                                lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesList));
                                reviewState = ReviewState.MAIN_CATEGORIES;
                                btnBack.setVisibility(View.GONE);
                                btnAdd.setVisibility(View.INVISIBLE);
                                btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                                btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                            // Prayer so go back to sub categories
                            } else if (mainCategory == 2) {
                                tvTitle.setText(R.string._dash_Prayer);
                                lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListPrayer));
                                reviewState = ReviewState.CATEGORIES;
                            // Scripture so go back to sub categories
                            } else if (mainCategory == 3) {
                                tvTitle.setText(R.string._dash_Scripture);
                                lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListScripture));
                                reviewState = ReviewState.CATEGORIES;
                            }
                            break;
                        case INDIVIDUAL:
                            reviewState = ReviewState.CATEGORY;
                            break;
                        default:
                            break;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?>parent,View view,int position,long id){
        try{
            switch (reviewState){
                // If displaying Main Categories
                case MAIN_CATEGORIES:
                    String[] main_category = categoriesList.get(position).split("/n");
                    btnBack.setVisibility(View.VISIBLE);
                    switch (main_category[1]) {
                        case "Encouragement":
                            mainCategory = 1;
                            shownEncouragementList.clear();
                            shownEncouragementList = sortEntriesByCategory(main_category[1], encouragementList);
                            lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
                            reviewState = ReviewState.CATEGORY;
                            break;
                        case "Prayer":
                            mainCategory = 2;
                            lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListPrayer));
                            reviewState = ReviewState.CATEGORIES;
                            break;
                        case "Scripture":
                            mainCategory = 3;
                            lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListScripture));
                            reviewState = ReviewState.CATEGORIES;
                            break;
                    }
                    btnAdd.setVisibility(View.VISIBLE);
                    btnBack.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    linearLayoutAddCategory.setVisibility(View.GONE);
                    etCategoryEntry.setText("");
                    btnAddActive = false;
                    positionInt = position;
                    tvTitle.setText(String.format(" - %s", main_category[1]));
                    btnBack.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
                    break;
                // If displaying subcategories menu
                case CATEGORIES:
                    if(mainCategory == 2){
                        subCategory = categoriesListPrayer.get(position).split("/n");
                        shownEncouragementList.clear();
                        shownEncouragementList = sortEntriesBySubCategory(subCategory[1], encouragementList, "Prayer");
                        lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
                        tvTitle.setText(String.format(" - %s", subCategory[1]));
                    }else if(mainCategory == 3){
                        subCategory = categoriesListScripture.get(position).split("/n");
                        shownEncouragementList.clear();
                        shownEncouragementList = sortEntriesBySubCategory(subCategory[1], encouragementList, "Scripture");
                        lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
                        tvTitle.setText(String.format(" - %s", subCategory[1]));
                    }

                    positionInt = position;
                    btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                    linearLayoutAddCategory.setVisibility(View.GONE);
                    etCategoryEntry.setText("");
                    btnAddActive = false;


                    btnBack.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
                    reviewState = ReviewState.CATEGORY;
                    break;
                // If displaying entries in a category or subcategory
                case CATEGORY:
                    positionInt = position;
                    selectedEncouragement.clear();
                    selectedEncouragement.add(0,shownEncouragementList.get(position));
                    saveArray(selectedEncouragement,"selectedEncouragement");
                    Intent intent = new Intent(getContext(),CurrentEncouragement.class);
                    getActivity().finish();
                    startActivity(intent);
                    break;
                default:
                    break;
            }


        }catch(Exception e){
            e.printStackTrace();
        }
    }



    private boolean saveArray(List<String>sKey,String arrayName){
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


    /**
     * Used to gather entries from a specific main category
     * @param category String the main category to gather from
     * @param arrayList ArrayList<String> that contains all the main category entries
     * @return sortedArrayList ArrayList<String> contains all the entries in the selected main category
     */
    private ArrayList<String> sortEntriesByCategory(String category, ArrayList<String> arrayList){
        ArrayList<String> sortedArrayList = new ArrayList<>();

        // iterate through each entry in array list given
        for(int i = 0; i < arrayList.size(); i++){
            try{
                // get the category from each entry
                String[] totalEntry = arrayList.get(i).split("/n");
                String categoryEntry = totalEntry[1];

                // add entry to sorted array list if equals the category given
                if(categoryEntry.equals(category)){
                    sortedArrayList.add(arrayList.get(i));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return sortedArrayList;
    }

    /**
     * Used to gather entries only from a certain subcategory
     * @param subCategory String the certain subcategory to gather entries from
     * @param arrayList String contains the entire entry library
     * @param category String the certain category to gather entries from
     * @return sortedArrayList ArrayList<String> all the entries from a certain category and
     * subcategory
     */
    private ArrayList<String> sortEntriesBySubCategory(String subCategory,
                                                       ArrayList<String> arrayList, String category){
        ArrayList<String> sortedArrayList = new ArrayList<>();

        // Iterate through the array list
        for(int i = 0; i < arrayList.size(); i++){
            try{
                // get the category and subcategory of each entry
                String[] totalEntry = arrayList.get(i).split("/n");
                String categoryEntry = totalEntry[1];
                String subCategoryEntry = totalEntry[4];

                // if category and subcategory equal the ones given
                if(categoryEntry.equals(category) && subCategoryEntry.equals(subCategory)){
                    sortedArrayList.add(arrayList.get(i));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return sortedArrayList;
    }

    /**
     * Used to get the number of entries in a certain category
     * @param category String certain category to get the total number of entries
     * @param arrayList ArrayList<String> all entries in the library
     * @return int total number of entries in that category
     */
    private int getNumberEntriesCategory(String category, ArrayList<String> arrayList){
        int total = 0;
        ArrayList<String> sortedArrayList = new ArrayList<>();

        // Iterate through the entries
        for(int i = 0; i < arrayList.size(); i++){
            try {
                // get the category from the entry
                String[] totalEntry = arrayList.get(i).split("/n");
                String categoryEntry = totalEntry[1];

                // check if entry category is category given
                if (categoryEntry.equals(category)) {
                    sortedArrayList.add(arrayList.get(i));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // if list is not empty then get the total size of it
        if(!sortedArrayList.isEmpty()){
            total = sortedArrayList.size();

        }

        return total;
    }

    /**
     * Used to get the number of entries in a certain subcategory
     * @param category String certain category to get the total number of entries
     * @param arrayList ArrayList<String> all entries in the library
     * @param subCategory String certain subcategory to get the total number of entries
     * @return int total number of entries in that category
     */
    private int getNumberEntriesSubCategory(String category, ArrayList<String> arrayList, String subCategory){
        int total = 0;
        ArrayList<String> sortedArrayList = new ArrayList<>();

        // Iterate through the entries
        for(int i = 0; i < arrayList.size(); i++){
            try {
                // get the category and subcategory from the entry
                String[] totalEntry = arrayList.get(i).split("/n");
                String categoryEntry = totalEntry[1];
                String subCategoryEntry = totalEntry[4];

                // check if entry category and entry subcategory is category and subcategory given
                if (categoryEntry.equals(category) && subCategoryEntry.equals(subCategory)) {
                    sortedArrayList.add(arrayList.get(i));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // if list is not empty then get the total size of it
        if(!sortedArrayList.isEmpty()){
            total = sortedArrayList.size();

        }

        return total;
    }


    /**
     * Holds the view elements for the list adapter
     */
    public class ViewHolder{
        TextView title;
        TextView title2;
        Button button;
    }

    /**
     * Used to change the current position selected
     * @param pos int that represents the position of element selected
     */
    public void setPosition(int pos){
        positionInt = pos;
    }

    /**
     * Used to Edit the displayed library list at a certain position for subcategory edits
     * @param pos int that represents the position of element selected
     */
    public void editList(int pos){
        try{
            // If main category is Prayer
            if(mainCategory == 2) {
                // get the position of the subcategory chosen to edit
                String subCategory = categoriesListPrayer.get(pos);
                // set the newEntry to subCategory in case user did not type anything in
                String newEntry = subCategory;
                // set the newEntry to user text input for subcategory name
                if (!etCategoryEntry.getText().toString().isEmpty()) {
                    newEntry = "/n" + etCategoryEntry.getText().toString();
                }

                // Display to the user that the subcategory name has been changed
                String[] toastStartSubCategory = subCategory.split("/n");
                String[] toastEndSubCategory = newEntry.split("/n");
                Toast.makeText(getContext().getApplicationContext(), "Category " +
                        toastStartSubCategory[1] + " changed to " + toastEndSubCategory[1],
                        Toast.LENGTH_LONG).show();

                // Change the original subcategories for all entries of that subcategory to new value
                for (int j = 0; j < encouragementList.size(); j++) {
                    String[] entry = encouragementList.get(j).split("/n");
                    String entrySubCategory = "/n" + entry[4];
                    String entryCategory = "/n" + entry[1] + "/n";

                    if (subCategory.equals(entrySubCategory) && entryCategory.equals("/nPrayer/n")) {
                        encouragementList.set(j, entryCategory + entry[2] + "/n" + entry[3] +
                                newEntry + "/n" + entry[5] + "/n" + entry[6]);
                    }
                }

                // save arrays to shared pref
                categoriesListPrayer.set(pos, newEntry);
                saveArray(encouragementList, "encouragementList");
                saveArray(categoriesListPrayer, "categoriesListPrayer");
                lvEncouragement.setAdapter(new MyListAdapter(getActivity().getBaseContext(),
                        R.layout.list_options, categoriesListPrayer));

            // If main category is Scripture
            }else if(mainCategory == 3){
                // get the position of the subcategory chosen to edit
                String subCategory = categoriesListScripture.get(pos);
                // set the newEntry to subCategory in case user did not type anything in
                String newEntry = subCategory;
                // set the newEntry to user text input for subcategory name
                if (!etCategoryEntry.getText().toString().isEmpty()) {
                    newEntry = "/n" + etCategoryEntry.getText().toString();
                }

                // Display to the user that the subcategory name has been changed
                String[] toastStartSubCategory = subCategory.split("/n");
                String[] toastEndSubCategory = newEntry.split("/n");
                Toast.makeText(getContext().getApplicationContext(), "Category " +
                        toastStartSubCategory[1] + " changed to " + toastEndSubCategory[1],
                        Toast.LENGTH_LONG).show();

                // Change the original subcategories for all entries of that subcategory to new value
                for (int j = 0; j < encouragementList.size(); j++) {
                    String[] entry = encouragementList.get(j).split("/n");
                    String entrySubCategory = "/n" + entry[4];
                    String entryCategory = "/n" + entry[1] + "/n";

                    if (subCategory.equals(entrySubCategory) && entryCategory.equals("/nScripture/n")) {
                        encouragementList.set(j, entryCategory + entry[2] + "/n" + entry[3] +
                                newEntry + "/n" + entry[5] + "/n" + entry[6]);
                    }
                }

                // save arrays to shared pref
                categoriesListScripture.set(pos, newEntry);
                saveArray(encouragementList, "encouragementList");
                saveArray(categoriesListScripture, "categoriesListScripture");
                lvEncouragement.setAdapter(new MyListAdapter(getActivity().getBaseContext(),
                        R.layout.list_options, categoriesListScripture));

            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This is used to display to the user the edit subcategory layout
     * @param pos int position of value selected
     */
    public void editListEntry(int pos){
        try {
            // If Prayer Main Category
            if (mainCategory == 2) {
                // set the current text of subcategory to the one chosen
                String[] text = categoriesListPrayer.get(pos).split("/n");
                etCategoryEntry.setText(text[1]);
            // If Scripture Main Category
            } else if (mainCategory == 3) {
                // set the current text of subcategory to the one chosen
                String[] text = categoriesListScripture.get(pos).split("/n");
                etCategoryEntry.setText(text[1]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // Display the Input Views for Editing a subcategory
        btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_edit, 0, 0, 0);
        btnCategoryCreate.setText("Edit");
        btnAddActive = true;
        editActive = true;
        btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        linearLayoutAddCategory.setVisibility(View.VISIBLE);
        setPosition(pos);
    }

    /**
     * This is used to delete an entry or subcategory from the list
     * @param pos int position of entry to delete from the list
     */
    public void deleteListEntry(int pos){
        switch (reviewState) {
            // In Subcategory Menu
            case CATEGORIES:
                try{
                    // In Prayer Subcategories
                    if(mainCategory == 2) {

                        // show popup asking if sure
                        String sCategory = categoriesListPrayer.get(pos);
                        try{
                            String[] entry = sCategory.split("/n");
                            sCategory = entry[1];
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        DialogPopup dialog = new DialogPopup();
                        Bundle args = new Bundle();
                        args.putString(DialogPopup.DIALOG_TYPE, DialogPopup.DELETE_SUBCATEGORY);
                        args.putInt("entryPos", pos);
                        args.putInt("FragmentID",this.getId());
                        args.putString("subCategory", sCategory);
                        args.putString("category","Prayer");
                        args.putString("type","Review");
                        dialog.setArguments(args);
                        dialog.show(getFragmentManager(), "delete-subcategory");

                    // In Scripture Subcategories
                    }else if(mainCategory == 3){

                        // show popup asking if sure
                        String sCategory = categoriesListScripture.get(pos);
                        try{
                            String[] entry = sCategory.split("/n");
                            sCategory = entry[1];
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        DialogPopup dialog = new DialogPopup();
                        Bundle args = new Bundle();
                        args.putString(DialogPopup.DIALOG_TYPE, DialogPopup.DELETE_SUBCATEGORY);
                        args.putInt("entryPos", pos);
                        args.putInt("FragmentID",this.getId());
                        args.putString("subCategory", sCategory);
                        args.putString("category","Scripture");
                        args.putString("type","Review");
                        dialog.setArguments(args);
                        dialog.show(getFragmentManager(), "delete-subcategory");

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                linearLayoutAddCategory.setVisibility(View.GONE);
                btnAddActive = false;
                editActive = false;
                etCategoryEntry.setText("");
                break;

            // In Entries
            case CATEGORY:
                // Iterate and find the position of the entry selected because Encouragements,
                //      Prayer, and Scripture all in one list. Then exit out of iteration.
                String entry = shownEncouragementList.get(pos);
                int entryPos = -1;
                for(int i = 0; i < encouragementList.size(); i++){
                    if(entry.equals(encouragementList.get(i))){
                        entryPos = i;
                        i = encouragementList.size();
                    }
                }


                DialogPopup dialog = new DialogPopup();
                Bundle args = new Bundle();
                args.putString(DialogPopup.DIALOG_TYPE, DialogPopup.DELETE_RECORD);
                args.putInt("entryPos", entryPos);
                args.putInt("FragmentID",this.getId());
                args.putString("msg", entry);
                args.putString("type","Review");
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "delete-record");

                break;

        }
    }

    /**
     * This is used to add a completely new subcategory to the list of subcategories
     * @param subCategory String name of subcategory to add to the list
     */
    public void addListEntry(String subCategory){
        if(!subCategory.isEmpty()) {
            // In Prayer Subcategories
            if(mainCategory == 2) {
                // Add subcategory to prayer categories list and save the array
                categoriesListPrayer.add(0, "/n" + subCategory);
                saveArray(categoriesListPrayer, "categoriesListPrayer");
                // Update the list view adapter
                lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options,
                        categoriesListPrayer));
                // clear the Edit text for the new subcategory
                etCategoryEntry.setText("");
                // display success message to user
                Toast.makeText(getContext().getApplicationContext(), subCategory +
                        " successfully added to categories", Toast.LENGTH_SHORT).show();

            // In Scripture Subcategories
            }else if(mainCategory == 3){
                // Add subcategory to scripture categories list and save the array
                categoriesListScripture.add(0, "/n" + subCategory);
                saveArray(categoriesListScripture, "categoriesListScripture");
                // Update the list view adapter
                lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options,
                        categoriesListScripture));
                // clear the Edit text for the new subcategory
                etCategoryEntry.setText("");
                // display success message to user
                Toast.makeText(getContext().getApplicationContext(), subCategory + " " +
                        "successfully added to categories", Toast.LENGTH_SHORT).show();
            }
        }else{
            // display error message to user if edit text for name is empty
            Toast.makeText(getContext().getApplicationContext(), "Please type a category to add", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Used to show a popup menu for how many SMS to display as well as show popup menu for
     * which category to save the sms entry to
     * @param v view used to show the popup from
     * @param isCategory boolean used to determine if showing subcategory menu or not
     */
    public void showPopUp(View v, boolean isCategory){
        // create a popup menu with the app's style
        PopupMenu popupMenu = new PopupMenu(getActivity(),v,1,0,R.style.PopupMenu);

        popupMenu.setOnMenuItemClickListener(ReviewLibrary.this);
        MenuInflater inflater = popupMenu.getMenuInflater();

        // Display subcategory menu
        if(isCategory) {
            inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());

        // Display entry menu
        }else{
            // Display Encouragement entry menu
            if(mainCategory == 1){
                inflater.inflate(R.menu.popup_entry_menu_encouragement, popupMenu.getMenu());

            // Display Prayer or Scripture entry menu
            }else {
                inflater.inflate(R.menu.popup_entry_menu, popupMenu.getMenu());
            }
            try {
                // If in Prayer Subcategory
                if(mainCategory == 2) {
                    for (int i = 0; i < categoriesListPrayer.size(); i++) {
                        String[] category = categoriesListPrayer.get(i).split("/n");
                        String[] selectedCategory = tvTitle.getText().toString().split(" - ");

                        // Add the prayer subcategories to the submenu for moving an entry
                        if (!category[1].equals(selectedCategory[1])) {
                            CharSequence c_arr = category[1];
                            popupMenu.getMenu().getItem(1).getSubMenu().addSubMenu(0, i, 0, c_arr);
                        }
                    }

                // If in Scripture Subcategory
                }else if(mainCategory == 3){
                    for (int i = 0; i < categoriesListScripture.size(); i++) {
                        String[] category = categoriesListScripture.get(i).split("/n");
                        String[] selectedCategory = tvTitle.getText().toString().split(" - ");

                        // Add the scripture subcategories to the submenu for moving an entry
                        if (!category[1].equals(selectedCategory[1])) {
                            CharSequence c_arr = category[1];
                            popupMenu.getMenu().getItem(1).getSubMenu().addSubMenu(0, i, 0, c_arr);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_popup:
                editListEntry(positionInt);
                return true;
            case R.id.delete_popup:
                deleteListEntry(positionInt);
                return true;
            case R.id.view_popup:
                selectedEncouragement.clear();
                selectedEncouragement.add(0,shownEncouragementList.get(positionInt));
                saveArray(selectedEncouragement,"selectedEncouragement");
                Intent intent = new Intent(getContext(),CurrentEncouragement.class);
                getActivity().finish();
                startActivity(intent);
                return true;
            case R.id.view_category_popup:
                String[] subCategory;
                if(mainCategory == 2){
                    subCategory = categoriesListPrayer.get(positionInt).split("/n");
                    shownEncouragementList.clear();
                    shownEncouragementList = sortEntriesBySubCategory(subCategory[1], encouragementList, "Prayer");
                    lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
                    tvTitle.setText(String.format(" - %s", subCategory[1]));
                }else if(mainCategory == 3){
                    subCategory = categoriesListScripture.get(positionInt).split("/n");
                    shownEncouragementList.clear();
                    shownEncouragementList = sortEntriesBySubCategory(subCategory[1], encouragementList, "Scripture");
                    lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
                    tvTitle.setText(String.format(" - %s", subCategory[1]));
                }

                btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                linearLayoutAddCategory.setVisibility(View.GONE);
                etCategoryEntry.setText("");
                btnAddActive = false;


                btnBack.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
                reviewState = ReviewState.CATEGORY;
                return true;
            case R.id.move_popup:
                return true;
            default:
                String title = item.getTitle().toString();
                try{
                    String[] entry = shownEncouragementList.get(positionInt).split("/n");
                    String category = entry[1];
                    String nameAddressDate = entry[2];
                    String smsMessage = entry[3];
                    String lastTitle = entry[4];

                    if(!title.equals(lastTitle)) {
                        String smsMessageStr = "/n" + category + "/n" + nameAddressDate + "/n" +
                                smsMessage + "/n" + title + "/n" + entry[5] + "/n" + entry[6];
                        Toast.makeText(getActivity().getApplicationContext(), "Entry Moved to " +
                                title, Toast.LENGTH_SHORT).show();

                        for (int j = 0; j < encouragementList.size(); j++) {
                            if (encouragementList.get(j).equals(shownEncouragementList.get(positionInt))) {
                                encouragementList.set(j, smsMessageStr);
                                j = encouragementList.size();
                            }
                        }
                        shownEncouragementList.remove(positionInt);
                        lvEncouragement.setAdapter(
                                new MyListAdapter(getActivity().getApplicationContext(),
                                        R.layout.list_options, shownEncouragementList));

                        saveArray(encouragementList, "encouragementList");
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Entry already in selected category",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                return false;
        }
    }

    /**
     * Array Adapter class that holds the review page displayed entries
     */
    private class MyListAdapter extends ArrayAdapter<String>{
        private int layout;

        /**
         * constructor for MyListAdapter
         * @param context Context that will be using the ListAdapter
         * @param resource int resource id
         * @param objects List<String> of objects to display
         */
        public MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;

            if(convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                convertView = layoutInflater.inflate(layout,parent,false);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView)convertView.findViewById(R.id.list_item_textview);
                viewHolder.title2 = (TextView)convertView.findViewById(R.id.list_item_textview2);
                viewHolder.button = (Button)convertView.findViewById(R.id.list_item_btn);
                convertView.setTag(viewHolder);
            }

            // Get the main view holder button and set an on click listener on it
            mainViewHolder = (ViewHolder)convertView.getTag();
            mainViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPosition(position);
                    switch (reviewState){
                        // Main Categories Displayed
                        case MAIN_CATEGORIES:
                            // get the current category selected
                            String[] main_category = categoriesList.get(position).split("/n");

                            btnBack.setVisibility(View.VISIBLE);
                            switch (main_category[1]) {
                                case "Encouragement":
                                    // sort the list to get only the encouragements to display
                                    mainCategory = 1;
                                    shownEncouragementList.clear();
                                    shownEncouragementList = sortEntriesByCategory(main_category[1],
                                            encouragementList);
                                    lvEncouragement.setAdapter(new MyListAdapter(getContext(),
                                            R.layout.list_options, shownEncouragementList));
                                    reviewState = ReviewState.CATEGORY;
                                    break;
                                case "Prayer":
                                    // display the prayer subcategories
                                    mainCategory = 2;
                                    lvEncouragement.setAdapter(new MyListAdapter(getContext(),
                                            R.layout.list_options, categoriesListPrayer));
                                    reviewState = ReviewState.CATEGORIES;
                                    break;
                                case "Scripture":
                                    // display the scripture subcategories
                                    mainCategory = 3;
                                    lvEncouragement.setAdapter(new MyListAdapter(getContext(),
                                            R.layout.list_options, categoriesListScripture));
                                    reviewState = ReviewState.CATEGORIES;
                                    break;
                            }
                            btnAdd.setVisibility(View.VISIBLE);
                            btnBack.setBackgroundColor(ContextCompat.getColor(getContext(),
                                    R.color.colorPrimary));
                            linearLayoutAddCategory.setVisibility(View.GONE);
                            etCategoryEntry.setText("");
                            btnAddActive = false;

                            tvTitle.setText(String.format(" - %s", main_category[1]));
                            btnBack.setCompoundDrawablesWithIntrinsicBounds(
                                    android.R.drawable.ic_menu_revert, 0, 0, 0);
                            break;
                        // In the Subcategories
                        case CATEGORIES:
                            // Show the subcategories popup menu
                            showPopUp(view,true);
                            break;
                        // In the Entries
                        case CATEGORY:
                            // Show the entries popup menu
                            showPopUp(view,false);
                            break;
                        default:
                            break;
                    }

                }
            });

            // set the list views text values
            try{
                switch (reviewState) {
                    // In Main Categories Menu
                    case MAIN_CATEGORIES:
                        String[] entry1 = getItem(position).split("/n");
                        // Get Main Category Name
                        String entry1_mainCategory = entry1[1];
                        String entry1_entryAmount = "";

                        // Get the total number of entries in each main category
                        int numEntries = getNumberEntriesCategory(entry1_mainCategory,encouragementList);
                        if (numEntries != 1) {
                            entry1_entryAmount = Integer.toString(numEntries) + " Entries";
                        } else {
                            entry1_entryAmount = Integer.toString(numEntries) + " Entry";
                        }

                        mainViewHolder.button.setCompoundDrawablesWithIntrinsicBounds(0,0,0,
                                android.R.drawable.ic_media_play);

                        // Set the name of the main category
                        mainViewHolder.title.setText(entry1_mainCategory);
                        // Set the name of the number of entries in that category
                        mainViewHolder.title2.setText(entry1_entryAmount);
                        mainViewHolder.title.setTypeface(null, Typeface.BOLD_ITALIC);

                        break;
                    // In Subcategories Menu
                    case CATEGORIES:
                        String[] entry2 = getItem(position).split("/n");
                        // Get Main Category Name
                        String entry2_subcategory = entry2[1];

                        // Get the total number of entries in each subcategory
                        int numEntries2 = 0;
                        // If Prayer Main Category
                        if(mainCategory == 2) {
                            numEntries2 = getNumberEntriesSubCategory("Prayer",
                                    encouragementList, entry2_subcategory);
                        // If Scripture Main Category
                        }if(mainCategory == 3){
                            numEntries2 = getNumberEntriesSubCategory("Scripture",
                                    encouragementList, entry2_subcategory);
                        }

                        // Set the Number of entries in each subcategory
                        String entry2_entryAmount = "";
                        if (numEntries2 != 1) {
                            entry2_entryAmount = Integer.toString(numEntries2) + " Entries";
                        } else {
                            entry2_entryAmount = Integer.toString(numEntries2) + " Entry";
                        }

                        // Hide the button for Other because not allowed to delete Other
                        if (entry2_subcategory.equals("Other")) {
                            mainViewHolder.button.setVisibility(View.INVISIBLE);
                        } else {
                            mainViewHolder.button.setVisibility(View.VISIBLE);
                        }

                        // Show the Title of the Subcategory
                        mainViewHolder.title.setText(entry2_subcategory);
                        // Show the Number of entries in each subcategory
                        mainViewHolder.title2.setText(entry2_entryAmount);
                        mainViewHolder.title.setTypeface(null, Typeface.ITALIC);

                        break;
                    // In Entries Menu
                    case CATEGORY:
                        String[] entry3 = getItem(position).split("/n");
                        // Get the Name sent by and Date of the Entry
                        String entry3_entryBaseInfo = entry3[2];

                        // If it is not an encouragement add the current alarm settings string
                        //      message for that entry
                        if(!entry3[1].equals("Encouragement")){
                            entry3_entryBaseInfo = entry3_entryBaseInfo + "\n" + entry3[6];
                        }

                        // Get the message body of the current entry
                        String entry3_entryMessage = entry3[3];

                        // Show the Message body of the current entry
                        mainViewHolder.title.setText(entry3_entryMessage);
                        // Show the Name, Address, and Alarm String if not Encouragement entry
                        mainViewHolder.title2.setText(entry3_entryBaseInfo);
                        break;
                    default:
                        break;
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return convertView;
        }
    }


}
