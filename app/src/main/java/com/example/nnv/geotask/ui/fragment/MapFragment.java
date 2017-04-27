package com.example.nnv.geotask.ui.fragment;

import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.nnv.geotask.R;
import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.common.utils.LocationAdapter;
import com.example.nnv.geotask.presentation.presenter.LocationTitlePresenter;
import com.example.nnv.geotask.presentation.view.LocationTitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapFragment extends MvpAppCompatFragment implements LocationTitleView {
    private static final String TYPE_PARAM = "type";
    private Globals.PageType mPageType;
    private AutoCompleteTextView actvAdresses;
    private Button clearBtn;
    private LocationAdapter<Address> mAdapter;
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
        mAdapter = new LocationAdapter<>(getContext(), R.layout.location_item, new ArrayList<Address>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actvAdresses = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        actvAdresses.setAdapter(mAdapter);
        actvAdresses.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(Globals.TAG, "afterTextChanged: "+s.toString());
                mTitlePresenter.loadLocations(s.toString());
            }
        });
        actvAdresses.setThreshold(Globals.SEARCH_THRESHOLD);
        clearBtn = (Button) view.findViewById(R.id.btnClear);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actvAdresses.setText(null);
                mTitlePresenter.clearLocationList();
                mTitlePresenter.getLocationList();
            }
        });
    }

    /** LocationTitleView*/

    @Override
    public void updateLocationList(ArrayList<Address> addressList) {
        mAdapter.updateItems(addressList);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(getActivity().findViewById(R.id.main_content), error, Snackbar.LENGTH_LONG).show();
    }




}
