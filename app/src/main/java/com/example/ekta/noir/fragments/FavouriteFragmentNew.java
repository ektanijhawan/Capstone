package com.example.ekta.noir.fragments;


import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.ekta.noir.R;
import com.example.ekta.noir.adapters.FavouriteAdapter;
import com.example.ekta.noir.adapters.FavouriteAdapterNew;
import com.example.ekta.noir.adapters.UnsplashAdapter;
import com.example.ekta.noir.model.PhotosProvider;
import com.example.ekta.noir.model.ResponseKeys;
import com.example.ekta.noir.model.UnsplashData;
import com.example.ekta.noir.model.UnsplashProviderMethods;
import com.example.ekta.noir.utils.VolleySingleton;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.example.ekta.noir.model.Endpoints.PHOTOS_URL;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_COLOR;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_CREATED_AT;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_HEIGHT;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_URLS;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_URLS_FULL;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_URLS_REGULAR;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_USER;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_USER_NAME;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_WIDTH;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragmentNew extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Activity activity;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FavouriteAdapterNew mFavouriteAdapterNew;
    private ArrayList<UnsplashData> unsplashList;
    private GridLayoutManager gridLayoutManager;

    public FavouriteFragmentNew() {
        // Required empty public constructor
    }

    public static FavouriteFragmentNew newInstance(String param1, String param2) {
        FavouriteFragmentNew fragment = new FavouriteFragmentNew();
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
        view = inflater.inflate(R.layout.fragment_favourite_new, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvFavouriteNew);
        mLinearLayoutManager = new LinearLayoutManager(activity);
        getActivity().getSupportLoaderManager().initLoader(1, null, this);
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

//            mFavouriteAdapter = new FavouriteAdapter(activity, new FavouriteAdapter.FavouriteAdapterOnClickHandler() {
//                @Override
//                public void onClick(UnsplashData unsplashData, RecyclerView.ViewHolder vh) {
//
//                    Bundle bundle = new Bundle();
//                    Fragment unsplashDetail = new UnsplashDetailFragment();
//                    bundle.putSerializable("unsplashData", unsplashData);
//                    unsplashDetail.setArguments(bundle);
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .setCustomAnimations(R.anim.slide_down, R.anim.slide_down, R.anim.slide_down, R.anim.slide_down)
//                            .replace(R.id.flDetailUnsplash, unsplashDetail)
//                            .addToBackStack(null)
//                            .commit();
//                }
//            });

//            getPhotos();

//            mRecyclerView.setLayoutManager(gridLayoutManager);
   //        mRecyclerView.setAdapter(mFavouriteAdapter);
//            mFavouriteAdapter.setAppList(unsplashList);
//            mFavouriteAdapter.notifyDataSetChanged();

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
            //getPhotos();
            mFavouriteAdapterNew.notifyDataSetChanged();
        }
        if (!isVisibleToUser) {
            // TODO stop audio playback
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri= PhotosProvider.BASE_CONTENT_URI;
        return new CursorLoader(getActivity(), uri, null, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        mFavouriteAdapterNew = new FavouriteAdapterNew(getActivity(), data, new FavouriteAdapterNew.FavouriteAdapterNewOnClickHandler() {
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
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mFavouriteAdapterNew);
        mRecyclerView.setAdapter(mFavouriteAdapterNew);
        mFavouriteAdapterNew.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
