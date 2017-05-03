package com.example.nnv.geotask.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.nnv.geotask.R;
import com.example.nnv.geotask.common.Globals;

import java.util.ArrayList;

import static com.example.nnv.geotask.common.Globals.concatNullableStrings;
import static com.example.nnv.geotask.common.Globals.nullAsString;

/**
 * Created by nnv on 27.04.17.
 */

public class LocationAdapter<T> extends BaseAdapter implements Filterable{
    private LayoutInflater mLayoutInflater;
    private ArrayList<T> mItems;
    private Context mCtx;
    private int mLayout;

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateItems(ArrayList<T> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public LocationAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<T> objects) {
        mLayoutInflater = LayoutInflater.from(context);
        mCtx = context.getApplicationContext();
        mItems = objects;
        mLayout = resource;
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < getCount(); i++) {
            res = res + getItem(i).toString() + "\n";
        }
        return res;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LocationViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mLayout, parent, false);
            holder = new LocationViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            holder.tvCoords = (TextView) convertView.findViewById(R.id.tvCoords);
            convertView.setTag(holder);
        } else {
            holder = (LocationViewHolder) convertView.getTag();
        }
        T item = getItem(position);
        if (item instanceof Address) {
            String title = concatNullableStrings(", ", ((Address)item).getCountryName(),
                    ((Address)item).getAdminArea(),
                    ((Address)item).getLocality());
            holder.tvTitle.setText(title);
            holder.tvAddress.setText(((Address)item).getAddressLine(0));
            holder.tvCoords.setText(String.format(mCtx.getString(R.string.coords),
                    ((Address)item).getLatitude(),
                    ((Address)item).getLongitude()));
        }
        convertView.setBackgroundColor((position % 2 == 0) ?  Color.alpha(240) : Color.WHITE);
        return convertView;
    }

    private static class LocationViewHolder {
        TextView tvTitle;
        TextView tvAddress;
        TextView tvCoords;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                results.values = mItems; //no filter
                return  results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }
}

