package com.example.davidyuan7536.workoutlog;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class ProfilePictureFragment extends Fragment {

    Context context;
    RecyclerView rv;
    Button listView;
    Button gridView;
    private List<ProfilePictureRecyclerView> initializer;

    ProfilePictureListAdapter listAdapter;
    ProfilePictureGridAdapter gridAdapter;

    String currentAdapter;


    public ProfilePictureFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_picture, container, false);

        context = getContext();

        initializer = new ArrayList<ProfilePictureRecyclerView>();


        rv = (RecyclerView) view.findViewById(R.id.profilePictureRecycleView);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.gridview_space);
        rv.addItemDecoration(new SpacesItemDecoration(spacingInPixels));


        listAdapter = new ProfilePictureListAdapter(getContext(), initializer, ((ProfileActivity)context).personalProfile());
        gridAdapter = new ProfilePictureGridAdapter(getContext(), initializer, ((ProfileActivity)context).personalProfile());

        initListDisplay();

        currentAdapter = "list";


        listView = (Button) view.findViewById(R.id.profilePictureListView);
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initListDisplay();
                currentAdapter = "list";
            }
        });

        gridView = (Button) view.findViewById(R.id.profilePictureGridView);
        gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initGridDisplay();
                currentAdapter = "grid";
            }
        });




        return view;
    }




    public void initializeAddPicture(String username, FirebasePictures picture, String userAvatar){


        ProfilePictureRecyclerView temp = new ProfilePictureRecyclerView(username, picture.getKey(), picture.getLink(), picture.getCaption(), picture.getDate(), userAvatar);


        for (int i = 0; i < initializer.size(); i++) {
            // if the element you are looking at is smaller than x,
            // go to the next element
            if (initializer.get(i).getPictureID().compareTo(temp.getPictureID()) >=0){
                continue;
            }
            initializer.add(i, temp);
            listAdapter.notifyItemInserted(i);
            gridAdapter.notifyItemInserted(i);
            return;
        }

        initializer.add(temp);
        listAdapter.notifyItemChanged(initializer.size()-1);
        gridAdapter.notifyItemChanged(initializer.size()-1);

//        if(currentAdapter.equals("list")){
//            listAdapter.notifyItemChanged(0);
//        }
//        else{
//            gridAdapter.notifyItemChanged(0);
//        }

    }

    private void initListDisplay(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(listAdapter);
    }

    // Display the Grid
    private void initGridDisplay(){

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(gridAdapter);
    }

//    public void setUserAvatar(String link){
//
//        userAvatar = link;
//
//    }
//
//    public void setUsername(String name){
//        username = name;
//    }

    public void updateUserAvatar(String link){
        for(int x = 0; x!= initializer.size(); x++){
            initializer.get(x).setAvatarLink(link);
            listAdapter.notifyItemChanged(x);
            gridAdapter.notifyItemChanged(x);

        }
    }

    public void updatePicture(String key, String caption){
        for(int x = 0; x!= initializer.size(); x++){
            if(initializer.get(x).getPictureID().matches(key)){
                initializer.get(x).setPictureCaption(caption);
//                if(currentAdapter == "grid"){
//                    gridAdapter.notifyItemChanged(x);
//                }
//                else if(currentAdapter == "list"){
//                    listAdapter.notifyItemChanged(x);
//                }
                listAdapter.notifyItemChanged(x);
                gridAdapter.notifyItemChanged(x);
                return;
            }
        }
    }

    public void deletePicture(String key){
        for(int x = 0; x!= initializer.size(); x++){
            if(initializer.get(x).getPictureID().matches(key)){
                initializer.remove(x);
                listAdapter.notifyItemRemoved(x);
                listAdapter.notifyItemRangeChanged(x, initializer.size());
                gridAdapter.notifyItemRemoved(x);
                gridAdapter.notifyItemRangeChanged(x, initializer.size());
                return;
            }
        }
    }

    public void onPictureSaved(String username, String caption, String today, String date, String key, String link, String userAvatar){


        ProfilePictureRecyclerView temp = new ProfilePictureRecyclerView(username, key, link, caption, today, userAvatar);
        initializer.add(0, temp);

        listAdapter.notifyDataSetChanged();
        gridAdapter.notifyDataSetChanged();

    }

    public void updateUserName(String name){
        for(int x = 0; x != initializer.size(); x++){
            initializer.get(x).setUserName(name);
            listAdapter.notifyItemChanged(x);
            gridAdapter.notifyItemChanged(x);
        }
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1 || parent.getChildLayoutPosition(view) == 2) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }


}


