package com.example.ekta.noir.fragments;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.ekta.noir.R;
import com.example.ekta.noir.adapters.AddDetailAdapter;
import com.example.ekta.noir.adapters.AddDetailAdapter.*;
import com.example.ekta.noir.model.PaletteData;
import com.example.ekta.noir.model.PaletteData.*;
import com.example.ekta.noir.activities.SettingsActivity.*;

import static com.example.ekta.noir.activities.SettingsActivity.SP_NAME;

public class AddDetailFragment extends Fragment {

    private ImageView imagePhoto, imageBack;
    private ArrayList<PaletteData> paletteList;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private AddDetailAdapter mAddDetailAdapter;

    public   Bitmap bitmap;
    private Animation slideDownAppBar, slideDownRecycler, slideUpAppBar, slideUpRecycler;
    private AppBarLayout appBarLayout;

    SharedPreferences settingsDatabase;

    public  AddDetailFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paletteList = new ArrayList();
        bitmap = getArguments().getParcelable("BitmapImage");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_add_detail, container, false);

        appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
        imagePhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        imageBack = (ImageView) view.findViewById(R.id.ivBack);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvPalette);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        slideDownAppBar = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        slideDownRecycler = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        slideUpAppBar = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        slideUpRecycler = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);

        slideDownAppBar.setDuration(600);
        slideDownRecycler.setDuration(500);
        slideUpAppBar.setDuration(500);
        slideUpRecycler.setDuration(600);

        appBarLayout.setAnimation(slideUpAppBar);
        mRecyclerView.setAnimation(slideUpRecycler);

        settingsDatabase = getActivity().getSharedPreferences(SP_NAME, 0);
        final int paletteValue = settingsDatabase.getInt("paletteStatus", 0);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        if (bitmap != null) {
            imagePhoto.setImageBitmap(bitmap);
            Palette palette = Palette.from(bitmap).generate();
            List<Palette.Swatch> swatchList = palette.getSwatches();

            for (int i = 0; i < swatchList.size(); i++) {

                Palette.Swatch swatch = swatchList.get(i);
                int rgbColor1 = swatch.getRgb();

                String hexColor = Integer.toHexString(rgbColor1);
                int pixelCount = swatch.getPopulation();

                PaletteData paletteData = new PaletteData();
                paletteData.setColorName("Color " + i);
                paletteData.setColorHexCode(hexColor);
                paletteData.setColorPopulation(pixelCount);
                paletteList.add(paletteData);
            }

            Collections.sort(paletteList);
                mRecyclerView.setBackgroundColor(Color.parseColor("#" + paletteList.get(paletteList.size() - 1).getColorHexCode()));

            mAddDetailAdapter = new AddDetailAdapter(getActivity(), paletteList);
            mRecyclerView.setAdapter(mAddDetailAdapter);
            mAddDetailAdapter.notifyDataSetChanged();
        }
        return view;
    }
}
