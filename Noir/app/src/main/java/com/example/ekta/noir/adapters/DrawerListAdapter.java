package com.example.ekta.noir.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ekta.noir.R;

/**
 * Created by Ekta on 26-02-2017.
 */

public class DrawerListAdapter extends ArrayAdapter<String> {

    private final Activity Context;
    private final String[] ListItemsName;
    private final Integer[] ImageName;

    public DrawerListAdapter(Activity context, String[] content,
                             Integer[] ImageName) {

        super(context, R.layout.row_drawer_list, content);
        // TODO Auto-generated constructor stub

        this.Context = context;
        this.ListItemsName = content;
        this.ImageName = ImageName;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = Context.getLayoutInflater();
        View ListViewSingle = inflater.inflate(R.layout.row_drawer_list, null, true);

        TextView ListViewItems = (TextView) ListViewSingle.findViewById(R.id.tv_drawer_list_item);
        ImageView ListViewImage = (ImageView) ListViewSingle.findViewById(R.id.iv_drawer_list_item);

        ListViewItems.setText(ListItemsName[position]);
        ListViewImage.setImageResource(ImageName[position]);
        return ListViewSingle;

    };

}


