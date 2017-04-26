package com.example.nnv.geotask.ui.fragment;

import android.content.Context;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.R;
import com.example.nnv.geotask.common.LocationAddressesLoader;
import com.example.nnv.geotask.presentation.presenter.LocationTitlePresenter;
import com.example.nnv.geotask.presentation.view.LocationTitleView;

import java.util.Collections;
import java.util.List;


public class MapFragment extends MvpAppCompatFragment implements LocationTitleView {
    private static final String TYPE_PARAM = "type";
    private Globals.PageType mPageType;
    private AutoCompleteTextView actvAdresses;
    private LocationAdapter mAdapter;
    @InjectPresenter
    LocationTitlePresenter mTitlePresenter;

    @ProvidePresenter
    LocationTitlePresenter provideLocationTitlePresenter() {
        return new LocationTitlePresenter(getContext());
    }

    /** LifeCycle */

    public MapFragment() {

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
        mAdapter = new LocationAdapter(getContext(), R.layout.location_item, Collections.<Address>emptyList());
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
        actvAdresses.setAdapter(mAdapter);
        actvAdresses.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(Globals.TAG, "onTextChanged: " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(Globals.TAG, "afterTextChanged: "+s.toString());
                mTitlePresenter.loadLocations(s.toString());
            }
        });
        actvAdresses.setThreshold(3);
    }

    /** LocationTitleView*/

    @Override
    public void updateLocationList(List<Address> addressList) {
        Log.i("ZZY", "updateLocationList: count " + addressList.size()+ "\n" + addressList.toString());
        mAdapter.clear();
        mAdapter.addAll(addressList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String error) {
        Snackbar.make(getActivity().findViewById(R.id.main_content), error, Snackbar.LENGTH_LONG).show();
    }

    /** Auxiliary */

    private class LocationAdapter extends ArrayAdapter<Address> {
        private  LayoutInflater mLayoutInflater;

        LocationAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Address> objects) {
            super(context, resource, objects);
            mLayoutInflater = LayoutInflater.from(context);
            Log.i(Globals.TAG, "LocationAdapter: " + mLayoutInflater.toString());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Log.i("ZZY", "getView: " + position);
            LocationViewHolder holder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.location_item, parent, false);
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
            Log.i(Globals.TAG, "getView: "+position+" " + item.toString() + "\n");
            return convertView;
        }

    }

    private static class LocationViewHolder {
        TextView tvTitle;
        TextView tvAddress;
        TextView tvCoords;
    }
}
