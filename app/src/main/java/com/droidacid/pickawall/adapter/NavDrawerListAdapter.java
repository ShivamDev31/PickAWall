package com.droidacid.pickawall.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.droidacid.pickawall.NavDrawerItem;
import com.droidacid.pickawall.R;

import java.util.ArrayList;

/**
 * Created by shivam.chopra on 17-01-2015.
 * the custom list adapter class which provides data to navigation list view. Create a class named NavDrawerListAdapter.
 * java under adapter package.
 * This adapter class inflates the drawer_list_item.xml layout by displaying appropriate wallpaper category name.
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.nav_drawer_list_item, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(navDrawerItems.get(position).getAlbumTitle());
        return convertView;
    }
}
