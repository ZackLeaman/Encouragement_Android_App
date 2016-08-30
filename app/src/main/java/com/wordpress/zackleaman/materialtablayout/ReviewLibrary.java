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
 * A simple {@link Fragment} subclass.
 */
public class ReviewLibrary extends Fragment implements AdapterView.OnItemClickListener,
        View.OnClickListener, PopupMenu.OnMenuItemClickListener, CustomFieldsFragmentAlertDialog{

    private ArrayList<String> encouragementList;
    private ArrayList<String> shownEncouragementList;
    private ArrayList<String> starterEncouragements;
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



    public ReviewLibrary() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_library, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reviewState = ReviewState.MAIN_CATEGORIES;
        positionInt = 0;
        mainCategory = 0;
        btnAddActive = false;
        editActive = false;
        encouragementList=new ArrayList<>();
        shownEncouragementList = new ArrayList<>();
//        starterEncouragements = new ArrayList<>();
        categoriesList = new ArrayList<>();
        categoriesListPrayer = new ArrayList<>();
        categoriesListScripture = new ArrayList<>();
        selectedEncouragement = new ArrayList<>();
        loadArray(encouragementList,getContext().getApplicationContext(),"encouragementList");
//        starterEncouragements.clear();
//        for(int i = 0; i < 30; i++){
//            //TODO set up 30 starter encouragements
//            starterEncouragements.add("/nOther/n" + "NameAddressDate" + "/n" +
//                    "Starter Encouragement " + i);
//        }
//
//        if(encouragementList.size() < 30){
//            int needSize = starterEncouragements.size() - encouragementList.size();
//            for(int j = 0; j < needSize; j++){
//                Random r = new Random();
//                int i1 = r.nextInt(30-j);
//                encouragementList.add(starterEncouragements.get(i1));
//                starterEncouragements.remove(i1);
//            }
//            saveArray(encouragementList,"encouragementList");
//        }

        //categoriesList.clear();
        loadArray(categoriesList,getContext().getApplicationContext(),"categoriesList");
        loadArray(categoriesListPrayer,getContext().getApplicationContext(),"categoriesListPrayer");
        loadArray(categoriesListScripture,getContext().getApplicationContext(),"categoriesListScripture");
//        if(categoriesList == null || categoriesList.isEmpty()) {
//            categoriesList.add("/nChurch/n");
//            categoriesList.add("/nFamily/n");
//            categoriesList.add("/nFriends/n");
//            categoriesList.add("/nWork/n");
//            categoriesList.add("/nMissions/n");
//            categoriesList.add("/nPrayer Requests/n");
//            categoriesList.add("/nOther/n");
//            saveArray(categoriesList,"categoriesList");
//        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean wantsPrayerAndScripture = sp.getBoolean("wantsPrayerAndScripture",true);
        if(!wantsPrayerAndScripture){
            categoriesList.clear();
            categoriesList.add("/nEncouragement/n");
        }




        lvEncouragement=(ListView)getView().findViewById(R.id.EncouragementList);

        lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesList));
        lvEncouragement.setOnItemClickListener(this);

        btnBack = (Button)getView().findViewById(R.id.btnBack);
        //btnBack.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher, 0, 0, 0);
        btnBack.setVisibility(View.GONE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (reviewState){
                    case MAIN_CATEGORIES:
//                        btnBackActive = false;
//                        btnBack.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
//                        btnCategoryCreate.setText("Create");
//                        editActive = false;
//                        btnBack.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//                        btnBack.setVisibility(View.INVISIBLE);
//                        linearLayoutAddCategory.setVisibility(View.GONE);
                        break;
                    case CATEGORIES:
                        btnAddActive = false;
                        //btnBack.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher, 0, 0, 0);
                        btnBack.setVisibility(View.GONE);
                        btnAdd.setVisibility(View.INVISIBLE);
                        btnCategoryCreate.setText("Create");
                        editActive = false;
                        tvTitle.setText("Categories");
                        mainCategory = 0;
                        lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesList));
//                        if(btnBackActive){
//                            btnBack.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
//                            linearLayoutAddCategory.setVisibility(View.VISIBLE);
//                        }else{
                        btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                        linearLayoutAddCategory.setVisibility(View.GONE);
//                        }
                        reviewState = ReviewState.MAIN_CATEGORIES;
                        break;
                    case CATEGORY:
                        //btnBack.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher, 0, 0, 0);
                        btnBack.setVisibility(View.VISIBLE);
                        if(mainCategory == 1) {
                            tvTitle.setText("Categories");
                            lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesList));
                            reviewState = ReviewState.MAIN_CATEGORIES;
                            btnBack.setVisibility(View.GONE);
                            btnAdd.setVisibility(View.INVISIBLE);
                            btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                            btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                        }else if(mainCategory == 2) {
                            tvTitle.setText(" - Prayer");
                            lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListPrayer));
                            reviewState = ReviewState.CATEGORIES;
                        }else if(mainCategory == 3){
                            tvTitle.setText(" - Scripture");
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
        btnAdd = (Button)getView().findViewById(R.id.btnAdd);
        btnAdd.setVisibility(View.INVISIBLE);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (reviewState){
                    case CATEGORIES:
                        //TODO make the stuff appear to add another sub category to either Prayer or Scripture Categories
                        btnAddActive = !btnAddActive;
                        if(btnAddActive){
                            linearLayoutAddCategory.setVisibility(View.VISIBLE);
                            btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                            btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                            etCategoryEntry.setText("");
                            btnCategoryCreate.setText("Create");
                        }else{
                            linearLayoutAddCategory.setVisibility(View.GONE);
                            btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                            btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                            etCategoryEntry.setText("");
                            btnCategoryCreate.setText("Create");
                        }
                        break;
                    case CATEGORY:
                        Intent intent = new Intent(getContext(),AddEntryActivity.class);
                        if(mainCategory == 1){
                            intent.putExtra("category_createEntry","Encouragement");
                        }else if(mainCategory == 2){
                            intent.putExtra("category_createEntry","Prayer");
                            String[] subCategory = categoriesListPrayer.get(positionInt).split("/n");
                            intent.putExtra("subCategory_createEntry",subCategory[1]);
                        }else if(mainCategory == 3){
                            intent.putExtra("category_createEntry","Scripture");
                            String[] subCategory = categoriesListScripture.get(positionInt).split("/n");
                            intent.putExtra("subCategory_createEntry",subCategory[1]);
                        }
                        startActivity(intent);
                        break;
                }
            }
        });

        linearLayoutAddCategory = (LinearLayout)getView().findViewById(R.id.linearLayoutAddCategory);
        btnCategoryCreate = (Button)getView().findViewById(R.id.btnCategoryCreate);
        etCategoryEntry = (EditText)getView().findViewById(R.id.etCategoryEntry);
        linearLayoutAddCategory.setVisibility(View.GONE);
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
        tvTitle.setText("Categories");
        Log.d("encouragementList",encouragementList.toString());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

    @Override
    public void updateAdapter() {
        Toast.makeText(getContext(),"in the update adapter",Toast.LENGTH_LONG).show();
        if(mainCategory == 1){
            shownEncouragementList.clear();
            shownEncouragementList = sortEntriesByCategory("Encouragement", encouragementList);
            lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
        }else if(mainCategory == 2){
            shownEncouragementList.clear();
            shownEncouragementList = sortEntriesBySubCategory(subCategory[1], encouragementList, "Prayer");
            lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
        }else if(mainCategory == 3){
            shownEncouragementList.clear();
            shownEncouragementList = sortEntriesBySubCategory(subCategory[1], encouragementList, "Scripture");
            lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
        }
    }

    @Override
    public void onItemClick(AdapterView<?>parent,View view,int position,long id){
        try{
            switch (reviewState){
                case MAIN_CATEGORIES:
                    String[] main_category = categoriesList.get(position).split("/n");
//                    shownEncouragementList.clear();
//                    shownEncouragementList = sortEntriesByCategory(main_category[1],encouragementList);
                    btnBack.setVisibility(View.VISIBLE);
                    if(main_category[1].equals("Encouragement")){
                        mainCategory = 1;
                        shownEncouragementList.clear();
                        shownEncouragementList = sortEntriesByCategory(main_category[1],encouragementList);
                        lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
                        reviewState = ReviewState.CATEGORY;
                    }else if(main_category[1].equals("Prayer")){
                        mainCategory = 2;
                        //loadArray(categoriesListPrayer,getContext().getApplicationContext(),"categoriesListPrayer");
                        lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListPrayer));
                        reviewState = ReviewState.CATEGORIES;
                    }else if(main_category[1].equals("Scripture")){
                        mainCategory = 3;
                        //loadArray(categoriesListScripture,getContext().getApplicationContext(),"categoriesListScripture");
                        lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListScripture));
                        reviewState = ReviewState.CATEGORIES;
                    }
                    btnAdd.setVisibility(View.VISIBLE);
                    btnBack.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    linearLayoutAddCategory.setVisibility(View.GONE);
                    etCategoryEntry.setText("");
                    btnAddActive = false;
                    positionInt = position;
                    tvTitle.setText(" - " + main_category[1]);
                    btnBack.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
                    break;
                case CATEGORIES:
                    if(mainCategory == 2){
                        subCategory = categoriesListPrayer.get(position).split("/n");
                        shownEncouragementList.clear();
                        shownEncouragementList = sortEntriesBySubCategory(subCategory[1], encouragementList, "Prayer");
                        lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
                        tvTitle.setText(" - " + subCategory[1]);
                    }else if(mainCategory == 3){
                        subCategory = categoriesListScripture.get(position).split("/n");
                        shownEncouragementList.clear();
                        shownEncouragementList = sortEntriesBySubCategory(subCategory[1], encouragementList, "Scripture");
                        lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
                        tvTitle.setText(" - " + subCategory[1]);
                    }

//                    String[] category = categoriesList.get(position).split("/n");
//
//                    shownEncouragementList.clear();
//                    shownEncouragementList = sortEntriesByCategory(category[1],encouragementList);
//                    lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
                    positionInt = position;
                    btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
                    linearLayoutAddCategory.setVisibility(View.GONE);
                    etCategoryEntry.setText("");
                    btnAddActive = false;


                    btnBack.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
                    reviewState = ReviewState.CATEGORY;
                    break;
                case CATEGORY:
                    positionInt = position;
                    selectedEncouragement.clear();
                    selectedEncouragement.add(0,shownEncouragementList.get(position));
                    saveArray(selectedEncouragement,"selectedEncouragement");
                    Intent intent = new Intent(getContext(),CurrentEncouragement.class);
                    startActivity(intent);
                    break;
                case INDIVIDUAL:
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



    //TODO possibly fix this class
    private ArrayList<String> sortEntriesByCategory(String category, ArrayList<String> arrayList){
        ArrayList<String> sortedArrayList = new ArrayList<>();

        for(int i = 0; i < arrayList.size(); i++){
            try{
                String[] totalEntry = arrayList.get(i).split("/n");
                String categoryEntry = totalEntry[1];

                if(categoryEntry.equals(category)){
                    sortedArrayList.add(arrayList.get(i));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return sortedArrayList;
    }

    private ArrayList<String> sortEntriesBySubCategory(String subCategory, ArrayList<String> arrayList, String category){
        ArrayList<String> sortedArrayList = new ArrayList<>();

        for(int i = 0; i < arrayList.size(); i++){
            try{
                String[] totalEntry = arrayList.get(i).split("/n");
                String categoryEntry = totalEntry[1];
                String subCategoryEntry = totalEntry[4];

                if(categoryEntry.equals(category) && subCategoryEntry.equals(subCategory)){
                    sortedArrayList.add(arrayList.get(i));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return sortedArrayList;
    }


    private int getNumberEntriesCategory(String category, ArrayList<String> arrayList){
        int total = 0;
        ArrayList<String> sortedArrayList = new ArrayList<>();

        for(int i = 0; i < arrayList.size(); i++){
            try {
                String[] totalEntry = arrayList.get(i).split("/n");
                String categoryEntry = totalEntry[1];

                if (categoryEntry.equals(category)) {
                    sortedArrayList.add(arrayList.get(i));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(!sortedArrayList.isEmpty()){
            total = sortedArrayList.size();

        }

        return total;
    }

    private int getNumberEntriesSubCategory(String category, ArrayList<String> arrayList, String subCategory){
        int total = 0;
        ArrayList<String> sortedArrayList = new ArrayList<>();

        for(int i = 0; i < arrayList.size(); i++){
            try {
                String[] totalEntry = arrayList.get(i).split("/n");
                String categoryEntry = totalEntry[1];
                String subCategoryEntry = totalEntry[4];

                if (categoryEntry.equals(category) && subCategoryEntry.equals(subCategory)) {
                    sortedArrayList.add(arrayList.get(i));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(!sortedArrayList.isEmpty()){
            total = sortedArrayList.size();

        }

        return total;
    }



    public class ViewHolder{
        //ImageView thumbnail;
        TextView title;
        TextView title2;
        Button button;
    }

    public void setPosition(int pos){
        positionInt = pos;
    }

    public void editList(int pos){
        try{
            if(mainCategory == 2) {
                String subCategory = categoriesListPrayer.get(pos);
                String newEntry = subCategory;
                if (!etCategoryEntry.getText().toString().isEmpty()) {
                    newEntry = "/n" + etCategoryEntry.getText().toString();
                }

                String[] toastStartSubCategory = subCategory.split("/n");
                String[] toastEndSubCategory = newEntry.split("/n");
                Toast.makeText(getContext().getApplicationContext(), "Category " + toastStartSubCategory[1] + " changed to " + toastEndSubCategory[1], Toast.LENGTH_LONG).show();

                for (int j = 0; j < encouragementList.size(); j++) {
                    String[] entry = encouragementList.get(j).split("/n");
                    String entrySubCategory = "/n" + entry[4];
                    String entryCategory = "/n" + entry[1] + "/n";

                    if (subCategory.equals(entrySubCategory) && entryCategory.equals("/nPrayer/n")) {
                        encouragementList.set(j, entryCategory + entry[2] + "/n" + entry[3] + newEntry + "/n" + entry[5] + "/n" + entry[6]);
                    }
                }

                categoriesListPrayer.set(pos, newEntry);
                saveArray(encouragementList, "encouragementList");
                saveArray(categoriesListPrayer, "categoriesListPrayer");
                lvEncouragement.setAdapter(new MyListAdapter(getActivity().getBaseContext(), R.layout.list_options, categoriesListPrayer));
            }else if(mainCategory == 3){
                String subCategory = categoriesListScripture.get(pos);
                String newEntry = subCategory;
                if (!etCategoryEntry.getText().toString().isEmpty()) {
                    newEntry = "/n" + etCategoryEntry.getText().toString();
                }

                String[] toastStartSubCategory = subCategory.split("/n");
                String[] toastEndSubCategory = newEntry.split("/n");
                Toast.makeText(getContext().getApplicationContext(), "Category " + toastStartSubCategory[1] + " changed to " + toastEndSubCategory[1], Toast.LENGTH_LONG).show();

                for (int j = 0; j < encouragementList.size(); j++) {
                    String[] entry = encouragementList.get(j).split("/n");
                    String entrySubCategory = "/n" + entry[4];
                    String entryCategory = "/n" + entry[1] + "/n";

                    if (subCategory.equals(entrySubCategory) && entryCategory.equals("/nScripture/n")) {
                        encouragementList.set(j, entryCategory + entry[2] + "/n" + entry[3] + newEntry + "/n" + entry[5] + "/n" + entry[6]);
                    }
                }

                categoriesListScripture.set(pos, newEntry);
                saveArray(encouragementList, "encouragementList");
                saveArray(categoriesListScripture, "categoriesListScripture");
                lvEncouragement.setAdapter(new MyListAdapter(getActivity().getBaseContext(), R.layout.list_options, categoriesListScripture));

            }

        }catch(Exception e){
            e.printStackTrace();
            //Toast.makeText(getContext().getApplicationContext(),"error in editList",Toast.LENGTH_LONG).show();
        }
    }

    public void editListEntry(int pos){
        try {
            if (mainCategory == 2) {
                String[] text = categoriesListPrayer.get(pos).split("/n");
                etCategoryEntry.setText(text[1]);
            } else if (mainCategory == 3) {
                String[] text = categoriesListScripture.get(pos).split("/n");
                etCategoryEntry.setText(text[1]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        btnAdd.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_edit, 0, 0, 0);
        btnCategoryCreate.setText("Edit");
        btnAddActive = true;
        editActive = true;
        btnAdd.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        linearLayoutAddCategory.setVisibility(View.VISIBLE);
        setPosition(pos);
    }

    public void deleteListEntry(int pos){
        switch (reviewState) {
            case CATEGORIES:
                try{
                    if(mainCategory == 2) {
                        String subCategory = categoriesListPrayer.get(pos);
                        Toast.makeText(getContext().getApplicationContext(), "Entries being moved to the Other Category", Toast.LENGTH_LONG).show();

                        for (int j = 0; j < encouragementList.size(); j++) {
                            String[] entry = encouragementList.get(j).split("/n");
                            String entryCategory = "/n" + entry[1] + "/n";
                            String entrySubCategory = "/n" + entry[4];
                            String newEntry = "/nOther";
                            if (subCategory.equals(entrySubCategory) && entryCategory.equals("/nPrayer/n")) {
                                //newEntry = newEntry + entry[2] + "/n" + entry[3];
                                encouragementList.set(j, entryCategory + entry[2] + "/n" + entry[3] + newEntry + "/n" + entry[5] + "/n" + entry[6]);
                            }
                        }

                        categoriesListPrayer.remove(pos);
                        saveArray(encouragementList, "encouragementList");
                        saveArray(categoriesListPrayer, "categoriesListPrayer");
                        lvEncouragement.setAdapter(new MyListAdapter(getActivity().getBaseContext(), R.layout.list_options, categoriesListPrayer));
                    }else if(mainCategory == 3){
                        String subCategory = categoriesListScripture.get(pos);
                        Toast.makeText(getContext().getApplicationContext(), "Entries being moved to the Other Category", Toast.LENGTH_LONG).show();

                        for (int j = 0; j < encouragementList.size(); j++) {
                            String[] entry = encouragementList.get(j).split("/n");
                            String entryCategory = "/n" + entry[1] + "/n";
                            String entrySubCategory = "/n" + entry[4];
                            String newEntry = "/nOther";
                            if (subCategory.equals(entrySubCategory) && entryCategory.equals("/nScripture/n")) {
                                //newEntry = newEntry + entry[2] + "/n" + entry[3];
                                encouragementList.set(j, entryCategory + entry[2] + "/n" + entry[3] + newEntry + "/n" + entry[5] + "/n" + entry[6]);
                            }
                        }

                        categoriesListScripture.remove(pos);
                        saveArray(encouragementList, "encouragementList");
                        saveArray(categoriesListScripture, "categoriesListScripture");
                        lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListScripture));

                    }
                }catch(Exception e){
                    e.printStackTrace();
                    //Toast.makeText(getContext(),"error in deleteListEntry",Toast.LENGTH_LONG).show();
                }
                linearLayoutAddCategory.setVisibility(View.GONE);
                btnAddActive = false;
                editActive = false;
                etCategoryEntry.setText("");
                break;
            case CATEGORY:
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

    public void addListEntry(String subCategory){
        //TODO have a sort method alphabetically for categories
        if(!subCategory.isEmpty()) {
            if(mainCategory == 2) {
                categoriesListPrayer.add(0, "/n" + subCategory);
                saveArray(categoriesListPrayer, "categoriesListPrayer");
                lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListPrayer));
                etCategoryEntry.setText("");
                Toast.makeText(getContext().getApplicationContext(), subCategory + " successfully added to categories", Toast.LENGTH_SHORT).show();
            }else if(mainCategory == 3){
                categoriesListScripture.add(0, "/n" + subCategory);
                saveArray(categoriesListScripture, "categoriesListScripture");
                lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListScripture));
                etCategoryEntry.setText("");
                Toast.makeText(getContext().getApplicationContext(), subCategory + " successfully added to categories", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext().getApplicationContext(), "Please type a category to add", Toast.LENGTH_SHORT).show();
        }
    }


    public void showPopUp(View v, boolean isCategory){
        PopupMenu popupMenu = new PopupMenu(getActivity(),v,1,0,R.style.PopupMenu);

        popupMenu.setOnMenuItemClickListener(ReviewLibrary.this);
        MenuInflater inflater = popupMenu.getMenuInflater();

        if(isCategory) {
            inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
        }else{
            if(mainCategory == 1){
                inflater.inflate(R.menu.popup_entry_menu_encouragement, popupMenu.getMenu());
            }else {
                inflater.inflate(R.menu.popup_entry_menu, popupMenu.getMenu());
            }
            try {
                if(mainCategory == 2) {
                    for (int i = 0; i < categoriesListPrayer.size(); i++) {
                        String[] category = categoriesListPrayer.get(i).split("/n");
                        String[] selectedCategory = tvTitle.getText().toString().split(" - ");

                        if (!category[1].equals(selectedCategory[1])) {
                            CharSequence c_arr = category[1];
                            popupMenu.getMenu().getItem(1).getSubMenu().addSubMenu(0, i, 0, c_arr);
                        }
                    }
                }else if(mainCategory == 3){
                    for (int i = 0; i < categoriesListScripture.size(); i++) {
                        String[] category = categoriesListScripture.get(i).split("/n");
                        String[] selectedCategory = tvTitle.getText().toString().split(" - ");

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
                startActivity(intent);
                return true;
            case R.id.view_category_popup:
                String[] subCategory;
                if(mainCategory == 2){
                    subCategory = categoriesListPrayer.get(positionInt).split("/n");
                    shownEncouragementList.clear();
                    shownEncouragementList = sortEntriesBySubCategory(subCategory[1], encouragementList, "Prayer");
                    lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
                    tvTitle.setText(" - " + subCategory[1]);
                }else if(mainCategory == 3){
                    subCategory = categoriesListScripture.get(positionInt).split("/n");
                    shownEncouragementList.clear();
                    shownEncouragementList = sortEntriesBySubCategory(subCategory[1], encouragementList, "Scripture");
                    lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
                    tvTitle.setText(" - " + subCategory[1]);
                }

//                    String[] category = categoriesList.get(position).split("/n");
//
//                    shownEncouragementList.clear();
//                    shownEncouragementList = sortEntriesByCategory(category[1],encouragementList);
//                    lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));

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
                    //String date = smsMessages[1];
                    String smsMessage = entry[3];
                    String lastTitle = entry[4];

//                    for(int i = 3; i < entry.length; i++){
//                        smsMessage += entry[i];
//                    }
                    if(!title.equals(lastTitle)) {
                        String smsMessageStr = "/n" + category + "/n" + nameAddressDate + "/n" + smsMessage + "/n" + title + "/n" + entry[5] + "/n" + entry[6];
                        Toast.makeText(getActivity().getApplicationContext(), "Entry Moved to " + title, Toast.LENGTH_SHORT).show();

                        for (int j = 0; j < encouragementList.size(); j++) {
                            if (encouragementList.get(j).equals(shownEncouragementList.get(positionInt))) {
                                encouragementList.set(j, smsMessageStr);
                                j = encouragementList.size();
                            }
                        }
                        shownEncouragementList.remove(positionInt);
                        lvEncouragement.setAdapter(new MyListAdapter(getActivity().getApplicationContext(), R.layout.list_options, shownEncouragementList));


                        //encouragementList.add(0, smsMessageStr);
                        saveArray(encouragementList, "encouragementList");
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),"Entry already in selected category",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                return false;
        }
    }

    private class MyListAdapter extends ArrayAdapter<String>{
        private int layout;
        private List<String> mObjects;

        public MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
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

            mainViewHolder = (ViewHolder)convertView.getTag();
            mainViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPosition(position);
                    switch (reviewState){
                        case MAIN_CATEGORIES:
                            String[] main_category = categoriesList.get(position).split("/n");
//                    shownEncouragementList.clear();
//                    shownEncouragementList = sortEntriesByCategory(main_category[1],encouragementList);
                            btnBack.setVisibility(View.VISIBLE);
                            if(main_category[1].equals("Encouragement")){
                                mainCategory = 1;
                                shownEncouragementList.clear();
                                shownEncouragementList = sortEntriesByCategory(main_category[1],encouragementList);
                                lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, shownEncouragementList));
                                reviewState = ReviewState.CATEGORY;
                            }else if(main_category[1].equals("Prayer")){
                                mainCategory = 2;
                                //loadArray(categoriesListPrayer,getContext().getApplicationContext(),"categoriesListPrayer");
                                lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListPrayer));
                                reviewState = ReviewState.CATEGORIES;
                            }else if(main_category[1].equals("Scripture")){
                                mainCategory = 3;
                                //loadArray(categoriesListScripture,getContext().getApplicationContext(),"categoriesListScripture");
                                lvEncouragement.setAdapter(new MyListAdapter(getContext(), R.layout.list_options, categoriesListScripture));
                                reviewState = ReviewState.CATEGORIES;
                            }
                            btnAdd.setVisibility(View.VISIBLE);
                            btnBack.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                            linearLayoutAddCategory.setVisibility(View.GONE);
                            etCategoryEntry.setText("");
                            btnAddActive = false;

                            tvTitle.setText(" - " + main_category[1]);
                            btnBack.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
                            break;
                        case CATEGORIES:
                            showPopUp(view,true);
                            break;
                        case CATEGORY:
                            showPopUp(view,false);
                            break;
                        default:
                            break;
                    }

                }
            });


            try{
                switch (reviewState) {
                    case MAIN_CATEGORIES:
                        String[] message1 = getItem(position).split("/n");
                        String subBody1 = message1[1];
                        String mainTitle1 = "";
                        //subBody = subBody +  "/n";
//                        if(subBody1.equals("Encouragement") ||
//                                subBody1.equals("Prayer") ||
//                                subBody1.equals("Scripture")){
                        int numEntries = getNumberEntriesCategory(subBody1,encouragementList);
                        if (numEntries != 1) {
                            mainTitle1 = Integer.toString(numEntries) + " Entries";
                        } else {
                            mainTitle1 = Integer.toString(numEntries) + " Entry";
                        }
                        //mainViewHolder.button.setVisibility(View.INVISIBLE);
                        mainViewHolder.button.setCompoundDrawablesWithIntrinsicBounds(0,0,0, android.R.drawable.ic_media_play);
//                        }else{
//                            mainViewHolder.button.setVisibility(View.VISIBLE);
//                        }
                        mainViewHolder.title.setText(subBody1);
                        mainViewHolder.title2.setText(mainTitle1);
                        mainViewHolder.title.setTypeface(null, Typeface.BOLD_ITALIC);

                        break;
                    case CATEGORIES:
                        String[] message2 = getItem(position).split("/n");
                        String subBody2 = message2[1];
                        int numEntries2 = 0;
                        if(mainCategory == 2) {
                            numEntries2 = getNumberEntriesSubCategory("Prayer", encouragementList, subBody2);
                        }if(mainCategory == 3){
                            numEntries2 = getNumberEntriesSubCategory("Scripture", encouragementList, subBody2);
                        }
                        String mainTitle2 = "";
                        if (numEntries2 != 1) {
                            mainTitle2 = Integer.toString(numEntries2) + " Entries";
                        } else {
                            mainTitle2 = Integer.toString(numEntries2) + " Entry";
                        }
                        //subBody = subBody +  "/n";
                        if (subBody2.equals("Other")) {
                            mainViewHolder.button.setVisibility(View.INVISIBLE);
                        } else {
                            mainViewHolder.button.setVisibility(View.VISIBLE);
                        }
                        mainViewHolder.title.setText(subBody2);
                        mainViewHolder.title2.setText(mainTitle2);
                        mainViewHolder.title.setTypeface(null, Typeface.ITALIC);

                        break;
                    case CATEGORY:
                        String[] message3 = getItem(position).split("/n");
                        String mainTitle3 = message3[2];

                        if(!message3[1].equals("Encouragement")){
                            mainTitle3 = mainTitle3 + "\n" + message3[6];
                        }

                        String subBody3 = message3[3];
                        //Toast.makeText(getContext(),mainTitle2,Toast.LENGTH_LONG).show();
                        mainViewHolder.title.setText(subBody3);
                        mainViewHolder.title2.setText(mainTitle3);
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
