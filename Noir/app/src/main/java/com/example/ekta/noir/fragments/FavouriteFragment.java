package com.example.ekta.noir.fragments;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.Collections;


import com.example.ekta.noir.adapters.FavouriteAdapter;
import com.example.ekta.noir.model.UnsplashData;
import com.example.ekta.noir.model.UnsplashProviderMethods;
import com.example.ekta.noir.R;


public class FavouriteFragment extends Fragment {

    private Activity activity;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FavouriteAdapter mFavouriteAdapter;

    private ArrayList<UnsplashData> unsplashList;
    private GridLayoutManager gridLayoutManager;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unsplashList = new ArrayList();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return enter ? AnimationUtils.loadAnimation(activity, R.anim.slide_down) : AnimationUtils.loadAnimation(activity, R.anim.slide_down);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View view;
        view = inflater.inflate(R.layout.fragment_favourite, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvFavourite);
        mLinearLayoutManager = new LinearLayoutManager(activity);

        // if((getResources().getInteger(R.integer.screen) == 1)){
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            gridLayoutManager = new GridLayoutManager(activity, 2);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(activity, 1);
     /*       }
        }else if((getResources().getInteger(R.integer.screen) == 2) || (getResources().getInteger(R.integer.screen) == 3)){
            if((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)){
                gridLayoutManager = new GridLayoutManager(activity, 3);
            }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                gridLayoutManager = new GridLayoutManager(activity, 2);
            }
        }
        */

            mFavouriteAdapter = new FavouriteAdapter(activity, new FavouriteAdapter.FavouriteAdapterOnClickHandler() {
                @Override
                public void onClick(UnsplashData unsplashData, RecyclerView.ViewHolder vh) {

                    Bundle bundle = new Bundle();
                    Fragment unsplashDetail = new UnsplashDetailFragment();
                    bundle.putSerializable("unsplashData", unsplashData);
                    unsplashDetail.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_down, R.anim.slide_down, R.anim.slide_down, R.anim.slide_down)
                            .replace(R.id.flDetailUnsplash, unsplashDetail)
                            .addToBackStack(null)
                            .commit();
                }
            });

            getPhotos();

            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.setAdapter(mFavouriteAdapter);
            mFavouriteAdapter.setAppList(unsplashList);
            mFavouriteAdapter.notifyDataSetChanged();

        }
        return view;

    }


    private void getPhotos(){

        ArrayList<UnsplashData> list = new ArrayList<>(UnsplashProviderMethods.getPhotoList(getActivity().getApplicationContext()));
        unsplashList.clear();
        for(UnsplashData photo : list){
            unsplashList.add(photo);
        }
        Collections.reverse(unsplashList);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // Make sure that we are currently visible
        if (this.isVisible()) {
            getPhotos();
            mFavouriteAdapter.notifyDataSetChanged();
        }
        if (!isVisibleToUser) {
            // TODO stop audio playback
        }
    }
}
