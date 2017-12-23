package com.example.user.selectableexlistview;


import android.app.Dialog;

import android.os.Build;

import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ExpandableListView exListView;

    private static boolean flag_menu = false;
    private static boolean flag_edit=false;
    private static boolean flag_delete=false;

    private List<Items> tempIndex;

    private ExAdapter exAdapter;
    private List<String> parentList;
    private static HashMap<String,List<ChildItem>> childList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Ex List View");
        tempIndex = new ArrayList<>();
        exListView =  findViewById(R.id.exList);

        parentList = new ArrayList<>();
        childList = new HashMap<>();

        exAdapter = new ExAdapter(this,parentList,childList);
        exListView.setAdapter(exAdapter);
        exListView.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE);

        init();


    }

    private void init() {
        exListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                int indx = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,childPosition));


                //Toast.makeText(getApplicationContext(),s.getText(),Toast.LENGTH_SHORT).show();
                if(parent.isItemChecked(indx)){
                    Items ii = new Items(groupPosition,childPosition,indx);
                    parent.setItemChecked(indx,false);

                    //find item and remove from temp list
                    for (int i=0;i<tempIndex.size();i++ ) {
                        Items ix = tempIndex.get(i);
                        if(ii.getParentId()==ix.getParentId()&&ii.getChildId()==ix.getChildId()){
                            tempIndex.remove(i);
                            break;
                        }
                    }

                    Toast.makeText(getApplicationContext(),"Item not Selected "+tempIndex.size(),Toast.LENGTH_LONG).show();
                }else {
                    tempIndex.add(new Items(groupPosition, childPosition,indx));
                    parent.setItemChecked(indx,true);
                    Toast.makeText(getApplicationContext(),"Item Selected "+tempIndex.size(),Toast.LENGTH_LONG).show();
                }

                if(parent.getCheckedItemCount()>0){
                    flag_menu=true;
                    invalidateOptionsMenu();

                }else {
                    flag_menu=false;
                    invalidateOptionsMenu(); //recall OnCreateOptionMenu

                }

                return false;
            }
        });

        setData();
    }

    private void setData() {
        //parentList = new ArrayList<String>();
        //childList = new HashMap<String, List<String>>();

        // Adding child data
        parentList.add("Top 250");
        parentList.add("Now Showing");
        parentList.add("Coming Soon..");

        // Adding child data
        List<ChildItem> top250 = new ArrayList<ChildItem>();
        top250.add(new ChildItem(false,"The Shawshank Redemption"));
        top250.add(new ChildItem(false,"The Godfather"));
        top250.add(new ChildItem(false,"The Godfather: Part II"));
        top250.add(new ChildItem(false,"Pulp Fiction"));
        top250.add(new ChildItem(false,"The Good, the Bad and the Ugly"));
        top250.add(new ChildItem(false,"The Dark Knight"));
        top250.add(new ChildItem(false,"12 Angry Men"));

        List<ChildItem> nowShowing = new ArrayList<ChildItem>();
        nowShowing.add(new ChildItem(false,"The Conjuring"));
        nowShowing.add(new ChildItem(false,"Despicable Me 2"));
        nowShowing.add(new ChildItem(false,"Turbo"));
        nowShowing.add(new ChildItem(false,"Grown Ups 2"));
        nowShowing.add(new ChildItem(false,"Red 2"));
        nowShowing.add(new ChildItem(false,"The Wolverine"));

        List<ChildItem> comingSoon = new ArrayList<ChildItem>();
        comingSoon.add(new ChildItem(false,"2 Guns"));
        comingSoon.add(new ChildItem(false,"The Smurfs 2"));
        comingSoon.add(new ChildItem(false,"The Spectacular Now"));
        comingSoon.add(new ChildItem(false,"The Canyons"));
        comingSoon.add(new ChildItem(false,"Europa Report"));

        childList.put(parentList.get(0), top250); // Header, Child data
        childList.put(parentList.get(1), nowShowing);
        childList.put(parentList.get(2), comingSoon);

        exAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(flag_menu){
            getMenuInflater().inflate(R.menu.menu, menu);
            getSupportActionBar().setTitle("");
        }else {
            getSupportActionBar().setTitle("Ex List View");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                if(tempIndex.size()==1){
                    itemEdit();
                }else {
                    Toast.makeText(getApplicationContext(),"Edit Not Possible",Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.delete:
                itemDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void itemDelete() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_delete);

        Button btnDelete = dialog.findViewById(R.id.btnDeleteOk);
        Button btnDeleteCancel = dialog.findViewById(R.id.btnDeleteCancel);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<tempIndex.size();i++){
                    Items ix = tempIndex.get(i);
                    childList.get(parentList.get(ix.getParentId())).remove(ix.childId);

                    //uncheck selected item
                    exListView.setItemChecked(ix.checkPos,false);
                }
                tempIndex.clear();
                exAdapter.notifyDataSetChanged();

                //update menu
                flag_menu=false;
                invalidateOptionsMenu();


                dialog.dismiss();
            }
        });
        btnDeleteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void itemEdit() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_edit);

        final EditText editText = dialog.findViewById(R.id.etEditItem);
        Button btnEdit = dialog.findViewById(R.id.btnEditOk);
        Button btnEditCancel = dialog.findViewById(R.id.btnEditCancel);

        final Items ix = tempIndex.get(0);
        ChildItem s = childList.get(parentList.get(ix.getParentId())).get(ix.getChildId());

        editText.setText(s.getText());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ss = editText.getText().toString();
                ChildItem cs = new ChildItem(false,ss);
                childList.get(parentList.get(ix.getParentId())).remove(ix.childId);
                childList.get(parentList.get(ix.getParentId())).add(ix.childId,cs);
                exAdapter.notifyDataSetChanged();

                //uncheck selected items
                exListView.setItemChecked(ix.checkPos,false);

                //update menu
                flag_menu=false;
                invalidateOptionsMenu();
                dialog.dismiss();
            }
        });

        btnEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
