package com.example.nnv.geotask.ui.fragment;

import android.content.Context;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.R;
import com.example.nnv.geotask.presentation.view.LocationTitleView;

import java.util.List;


public class MapFragment extends MvpAppCompatFragment implements LocationTitleView {
    private static final String TYPE_PARAM = "type";
    private Globals.PageType mPageType;
    private AutoCompleteTextView actvAdresses;

    /** LifeCycle */

    public MapFragment() {
        // Required empty public constructor
    }



    public static MapFragment newInstance(Globals.PageType pageType) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putSerializable(TYPE_PARAM, pageType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageType = (Globals.PageType) getArguments().getSerializable(TYPE_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actvAdresses = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
    }

    /** LocationTitleView*/

    @Override
    public void updateLocationList(List<Address> addressList) {

    }

    @Override
    public void showError(String error) {

    }

    class LocationAdapter extends ArrayAdapter<Address> {

        public LocationAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Address> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LocationViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.location_item, parent);
                holder = new LocationViewHolder();
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
                holder.tvCoords = (TextView) convertView.findViewById(R.id.tvCoords);
                convertView.setTag(holder);
            } else {
                holder = (LocationViewHolder) convertView.getTag();
            }
            Address item = getItem(position);
            String addresses = "";
            if (item == null) { return convertView; }
            holder.tvTitle.setText(item.getFeatureName());
            for (int i = 0; i < item.getMaxAddressLineIndex(); i++) {
                addresses = addresses + item.getAddressLine(i) + "\n";
            }
            holder.tvAddress.setText(addresses);
            holder.tvCoords.setText(String.format(getResources().getString(R.string.coords),
                    item.getLatitude(),
                    item.getLongitude()));

            return convertView;
        }
    }

    private static class LocationViewHolder {
        TextView tvTitle;
        TextView tvAddress;
        TextView tvCoords;
    }
}
