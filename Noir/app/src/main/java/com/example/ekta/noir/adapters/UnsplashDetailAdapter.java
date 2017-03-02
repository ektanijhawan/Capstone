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
import com.example.ekta.noir.model.UnsplashData;

import java.util.ArrayList;

import static com.example.ekta.noir.activities.SettingsActivity.SP_NAME;


public class UnsplashDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<PaletteData> paletteList;
    private UnsplashData unsplashData;
    private Context context;

    private LayoutInflater layoutInflater;

    SharedPreferences settingsDatabase;
    private final int paletteValue;

    public UnsplashDetailAdapter(Context context, ArrayList<PaletteData> paletteList, UnsplashData unsplashData){
        this.context = context;
        this.paletteList = paletteList;
        this.unsplashData = unsplashData;

        layoutInflater = LayoutInflater.from(context);

        settingsDatabase = context.getSharedPreferences(SP_NAME, 0);
        paletteValue = settingsDatabase.getInt("paletteStatus", 0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == 0) {
            View view = layoutInflater.inflate(R.layout.holder_palette_detail, parent, false);
            viewHolder = new DetailViewHolder(view);
            return viewHolder;
        }

        if (viewType == 1) {
            View view = layoutInflater.inflate(R.layout.holder_palette, parent, false);
            viewHolder = new PaletteViewHolder(view);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case 0:
                ((DetailViewHolder) holder).relativeLayout.setBackgroundColor(Color.parseColor("#" + paletteList.get(0).getColorHexCode().substring(2)));
                ((DetailViewHolder) holder).textName.setText(unsplashData.getUsername());
                ((DetailViewHolder) holder).textCreated.setText(unsplashData.getCreatedAt());
                break;
            case 1:
                PaletteData currentColor = paletteList.get(position - 1);
                String hex = currentColor.getColorHexCode().substring(2);
                ((PaletteViewHolder)holder).paletteColor.setBackgroundColor(Color.parseColor("#" + hex));
                ((PaletteViewHolder)holder).textHexCode.setText("#" + hex);
                ((PaletteViewHolder)holder).textColorPopulation.setText(currentColor.getColorPopulation() + "");
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(paletteValue == 0){
            return paletteList.size() +  1;
        }else if(paletteValue == 1) {
            if (paletteList.size() >= 6) {
                return 7;
            } else if (paletteList.size() < 6) {
                return paletteList.size() + 1;
            }
        }
        return paletteList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        if (position > 0 && position <= paletteList.size())
            return 1;
        return 9999;
    }

    public class PaletteViewHolder extends RecyclerView.ViewHolder{

        public View paletteColor;
        public TextView textHexCode, textColorPopulation;
        private ClipboardManager myClipboard;
        private ClipData myClip;

        public PaletteViewHolder(final View itemView) {
            super(itemView);

            paletteColor = (View) itemView.findViewById(R.id.vColor);
            textHexCode = (TextView) itemView.findViewById(R.id.tvHexCode);
            textColorPopulation = (TextView) itemView.findViewById(R.id.tvColorPopulation);
            myClipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);

            paletteColor.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    String hexCode = paletteList.get(getAdapterPosition() - 1).getColorHexCode().substring(2);
                    myClip = ClipData.newPlainText("hexCode", "#" + hexCode);
                    myClipboard.setPrimaryClip(myClip);
                    Snackbar.make(v, "Hex code #" + hexCode + " copied", Snackbar.LENGTH_LONG)
                            .show();
                    return true;
                }
            });
        }
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder{

        private TextView textName, textCreated;
        private View relativeLayout;

        public DetailViewHolder(View itemView) {
            super(itemView);

            textName = (TextView) itemView.findViewById(R.id.tvImageName);
            textCreated = (TextView) itemView.findViewById(R.id.tvImageCreated);
            relativeLayout = (View) itemView.findViewById(R.id.vImageDetails);
        }
    }
}
