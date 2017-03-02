package com.example.ekta.noir.adapters;

/**
 * Created by Ekta on 24-02-2017.
 */

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ekta.noir.R;
import com.example.ekta.noir.model.PaletteData;

import com.example.ekta.noir.activities.SettingsActivity.*;

import java.util.ArrayList;

import static com.example.ekta.noir.activities.SettingsActivity.SP_NAME;

public class AddDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<PaletteData> paletteList;
    private Context context;
    private LayoutInflater layoutInflater;

    SharedPreferences settingsDatabase;
    private final int paletteValue;

    public AddDetailAdapter(Context context, ArrayList<PaletteData> paletteList ){
        this.context = context;
        this.paletteList = paletteList;
        layoutInflater = LayoutInflater.from(context);

        settingsDatabase = context.getSharedPreferences(SP_NAME, 0);
        paletteValue = settingsDatabase.getInt("paletteStatus", 0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = layoutInflater.inflate(R.layout.add_holder_palette, parent, false);
        viewHolder = new PaletteViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PaletteData currentColor = paletteList.get(position);
        switch (getItemViewType(position)) {

            case 0:
                String hex = currentColor.getColorHexCode().substring(2);
                ((PaletteViewHolder)holder).paletteColor.setBackgroundColor(Color.parseColor("#" + hex));
                ((PaletteViewHolder)holder).textHexCode.setText("#" + hex);
                ((PaletteViewHolder)holder).textColorPopulation.setText(currentColor.getColorPopulation() + "");
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(paletteValue == 0) {
            return paletteList.size();
        }else if(paletteValue == 1){
            return 6;
        }
        return paletteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public class PaletteViewHolder extends RecyclerView.ViewHolder{

        public View paletteColor;
        public TextView textHexCode, textColorPopulation;
        private ClipboardManager myClipboard;
        private ClipData myClip;

        public PaletteViewHolder(View itemView) {
            super(itemView);

            paletteColor = (View) itemView.findViewById(R.id.vColor);
            textHexCode = (TextView) itemView.findViewById(R.id.tvHexCode);
            textColorPopulation = (TextView) itemView.findViewById(R.id.tvColorPopulation);
            myClipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);

            paletteColor.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    String hexCode = paletteList.get(getAdapterPosition()).getColorHexCode().substring(2);
                    myClip = ClipData.newPlainText("hexCode", "#" + hexCode);
                    myClipboard.setPrimaryClip(myClip);
                    Snackbar.make(v, "Hex code #" + hexCode + " copied", Snackbar.LENGTH_LONG)
                            .show();
                    return true;
                }
            });
        }
    }
}


