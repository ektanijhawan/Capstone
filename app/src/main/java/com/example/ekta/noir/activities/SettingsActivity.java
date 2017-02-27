package com.example.ekta.noir.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ekta.noir.R;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat switchWallpaper, switchWallpaperQuality, switchPalette;
    private TextView textWallpaper, textWallpaperDetail;

    public static final String SP_NAME = "settings";
    SharedPreferences settingsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        switchWallpaper = (SwitchCompat) findViewById(R.id.sWallpaper);
        switchWallpaperQuality = (SwitchCompat) findViewById(R.id.sWallpaperQuality);
        switchPalette = (SwitchCompat) findViewById(R.id.sPalette);
        textWallpaper = (TextView) findViewById(R.id.tvWallpaperQuality);
        textWallpaperDetail = (TextView) findViewById(R.id.tvWallpaperQualityDetail);

        settingsDatabase = getSharedPreferences(SP_NAME, 0);
        int wallpaperValue = settingsDatabase.getInt("wallpaperStatus", 0);
        int wallpaperQualityValue = settingsDatabase.getInt("wallpaperQualityStatus", 1);
        int paletteValue = settingsDatabase.getInt("paletteStatus", 0);

        if (wallpaperValue == 1) {
            switchWallpaper.setChecked(true);
            switchWallpaperQuality.setEnabled(true);
            textWallpaper.setTextColor(getResources().getColor(R.color.colorWhite));
            textWallpaperDetail.setTextColor(getResources().getColor(R.color.colorOffWhite));
        } else {
            switchWallpaper.setChecked(false);
            switchWallpaperQuality.setEnabled(false);
            textWallpaper.setTextColor(getResources().getColor(R.color.disabledWhite));
            textWallpaperDetail.setTextColor(getResources().getColor(R.color.disabledWhite));
        }

        if (wallpaperQualityValue == 1) {
            switchWallpaperQuality.setChecked(true);
        } else {
            switchWallpaperQuality.setChecked(false);
        }

        if (paletteValue == 1) {
            switchPalette.setChecked(true);
        } else {
            switchPalette.setChecked(false);
        }

        switchWallpaper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    switchWallpaperQuality.setEnabled(true);
                    textWallpaper.setTextColor(getResources().getColor(R.color.colorWhite));
                    textWallpaperDetail.setTextColor(getResources().getColor(R.color.colorOffWhite));
                    SharedPreferences.Editor spEditor = settingsDatabase.edit();
                    spEditor.putInt("wallpaperStatus", 1);
                    spEditor.commit();
                }
                if (!isChecked) {
                    switchWallpaperQuality.setEnabled(false);
                    textWallpaper.setTextColor(getResources().getColor(R.color.disabledWhite));
                    textWallpaperDetail.setTextColor(getResources().getColor(R.color.disabledWhite));
                    SharedPreferences.Editor spEditor = settingsDatabase.edit();
                    spEditor.putInt("wallpaperStatus", 0);
                    spEditor.commit();
                }
            }
        });

        switchWallpaperQuality.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SharedPreferences.Editor spEditor = settingsDatabase.edit();
                    spEditor.putInt("wallpaperQualityStatus", 1);
                    spEditor.commit();
                }
                if (!isChecked) {
                    SharedPreferences.Editor spEditor = settingsDatabase.edit();
                    spEditor.putInt("wallpaperQualityStatus", 0);
                    spEditor.commit();
                }
            }
        });

        switchPalette.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SharedPreferences.Editor spEditor = settingsDatabase.edit();
                    spEditor.putInt("paletteStatus", 1);
                    spEditor.commit();
                }
                if (!isChecked) {
                    SharedPreferences.Editor spEditor = settingsDatabase.edit();
                    spEditor.putInt("paletteStatus", 0);
                    spEditor.commit();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
