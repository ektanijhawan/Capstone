package com.example.ekta.noir.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.ekta.noir.R;
import com.example.ekta.noir.adapters.UnsplashAdapter;
import com.example.ekta.noir.model.ResponseKeys;
import com.example.ekta.noir.model.UnsplashData;
import com.example.ekta.noir.utils.VolleySingleton;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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


public class UnsplashFragment extends Fragment {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue = null;

    private RecyclerView mRecyclerView;
    private UnsplashAdapter mUnsplashAdapter;

    private ArrayList<UnsplashData> unsplashList;
//    private String url;

    private int visibleItemCount, totalItemCount, firstVisibleItem;
    private int previousTotal = 0, pageCount = 1, visibleThreshold = 2;
    private boolean loading = true;
    private GridLayoutManager gridLayoutManager;

    private ProgressBar pbLoader;
    private Activity activity;
    Random r;
    int i;
    private AdView mAdView;

    public UnsplashFragment() {
    }

    public static UnsplashFragment newInstance(String param1, String param2) {
        UnsplashFragment fragment = new UnsplashFragment();
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
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
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
        view = inflater.inflate(R.layout.fragment_unsplash, container, false);
        mAdView = (AdView)view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rvUnsplash);
        pbLoader = (ProgressBar)view.findViewById(R.id.pbLoader);

      //  if((getResources().getInteger(R.integer.screen) == 1)){
            if((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)){
                gridLayoutManager = new GridLayoutManager(activity, 2);
            }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                gridLayoutManager = new GridLayoutManager(activity, 1);
            }
     /*   }else if((getResources().getInteger(R.integer.screen) == 2) || (getResources().getInteger(R.integer.screen) == 3)){
            if((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)){
                gridLayoutManager = new GridLayoutManager(activity, 3);
            }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                gridLayoutManager = new GridLayoutManager(activity, 2);
            }
        }
        */

        mUnsplashAdapter = new UnsplashAdapter(activity, new UnsplashAdapter.UnsplashAdapterOnClickHandler(){
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


        String url;
        url = PHOTOS_URL;

        sendJSONRequest(url);
        setUpRecyclerView(mRecyclerView);
        return view;
    }

    public void sendJSONRequest(String url){
//        Toast.makeText(getActivity(), url + "", Toast.LENGTH_SHORT).show();

        pbLoader.setVisibility(View.VISIBLE);
        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pbLoader.setVisibility(View.GONE);
                        unsplashList = parseJSONResponse(response);
                        mUnsplashAdapter.setAppList(unsplashList);
                        mUnsplashAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(request);
    }

    private ArrayList<UnsplashData> parseJSONResponse(JSONArray response){

        try{
            for(int i = 0; i < response.length(); i++){
                JSONObject image = (JSONObject)response.get(i);
                String id = image.getString(ResponseKeys.IMAGE_ID);
                String createdAt = image.getString(IMAGE_CREATED_AT);
                String color = image.getString(IMAGE_COLOR);
                int width = image.getInt(IMAGE_WIDTH);
                int height = image.getInt(IMAGE_HEIGHT);

                JSONObject urls = image.getJSONObject(IMAGE_URLS);
                String urlRegular = urls.getString(IMAGE_URLS_REGULAR);
                String urlFull = urls.getString(IMAGE_URLS_FULL);

                JSONObject user = image.getJSONObject(IMAGE_USER);
                String userName = user.getString(IMAGE_USER_NAME);

                UnsplashData unsplashData = new UnsplashData();
                unsplashData.setId(id);
                unsplashData.setCreatedAt(createdAt);
                unsplashData.setColor(color);
                unsplashData.setWidth(width);
                unsplashData.setHeight(height);
                unsplashData.setUrlRegular(urlRegular);
                unsplashData.setUrlFull(urlFull);
                unsplashData.setUsername(userName);
                unsplashList.add(unsplashData);
            }

        }catch(JSONException e){

        }
        return unsplashList;
    }

    private void setUpRecyclerView(RecyclerView rv){
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setLayoutManager(gridLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                visibleItemCount = rv.getChildCount();
                totalItemCount = gridLayoutManager.getItemCount();
                firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        pageCount++;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)){
//                    String url = null;
//                    if(i == 0) {
//                        url = PHOTOS_URL + "&page=" + String.valueOf(pageCount) + ORDER_BY;
//                    }else if(i == 1){
//                        url = PHOTOS_URL + "&page=" + String.valueOf(pageCount);
//                    }else{
//                        url = PHOTOS_URL + "&page=" + String.valueOf(pageCount);
//                    }
                    String url = PHOTOS_URL + "&page=" + String.valueOf(pageCount);

                    sendJSONRequest(url);
                    loading = true;
                }
            }
        });
        rv.setAdapter(mUnsplashAdapter);
    }
}
