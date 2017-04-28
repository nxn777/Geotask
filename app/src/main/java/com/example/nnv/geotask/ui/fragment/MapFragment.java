package com.example.nnv.geotask.ui.fragment;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.nnv.geotask.R;
import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.common.utils.LocationAdapter;
import com.example.nnv.geotask.presentation.presenter.LocationTitlePresenter;
import com.example.nnv.geotask.presentation.view.LocationTitleView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.nnv.geotask.common.Globals.concatNullableStrings;
import static com.example.nnv.geotask.common.Globals.nullAsString;


public class MapFragment extends MvpAppCompatFragment implements LocationTitleView {
    private static final String TYPE_PARAM = "type";
    private Globals.PageType mPageType;
    private AutoCompleteTextView mAtvAdresses;
    private Button mClearBtn;
    private ProgressBar mProgressBar;
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
        mAtvAdresses = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.searchProgressBar);
        mAtvAdresses.setAdapter(mAdapter);
        mAtvAdresses.addTextChangedListener(new TextWatcher() {
            private Timer timer;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(final Editable s) {
                Log.i(Globals.TAG, "afterTextChanged: "+s.toString());
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.i(Globals.TAG, "delayed afterTextChanged: "+s.toString());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTitlePresenter.loadLocations(s.toString());
                            }
                        });

                    }

                }, Globals.DEFAULT_AUTOCOMPLETE_DELAY);
                mTitlePresenter.loadLocations(s.toString());
            }
        });
        mAtvAdresses.setThreshold(Globals.SEARCH_THRESHOLD);
        mAtvAdresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTitlePresenter.setSelectedAddress((Address) parent.getItemAtPosition(position)); //TODO: hide keyboard
                mAtvAdresses.clearFocus();
                hideKeyboard(mAtvAdresses);
            }
        });

        mClearBtn = (Button) view.findViewById(R.id.btnClear);
        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitlePresenter.setSelectedAddress(null);
                mTitlePresenter.clearLocationList();
                mTitlePresenter.getLocationList();
            }
        });
        mTitlePresenter.getLocationList();
    }

    private void hideKeyboard(View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
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

    @Override
    public void toggleControls(Globals.SearchState state) {
        switch (state){
            case Searching:
                mProgressBar.setVisibility(View.VISIBLE);
                mClearBtn.setVisibility(View.GONE);
                break;
            case Typing:
                mProgressBar.setVisibility(View.GONE);
                mClearBtn.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void showSelected(Address address) {
        if (address != null) {
            String title = concatNullableStrings(", ", address.getAdminArea(),
                    //address.getLocality(),
                    address.getAddressLine(0));
            mAtvAdresses.setText(title); //TODO: avoid second search
        } else {
            mAtvAdresses.setText(null);
        }
    }
}
