package com.example.nnv.geotask.presentation.presenter;

import android.content.Context;
import android.location.Address;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.common.utils.LocationAddressesLoader;
import com.example.nnv.geotask.presentation.view.LocationTitleView;

import java.util.ArrayList;

/**
 * Created by nnv on 25.04.17.
 * Moxy Presenter for LocationTitleView - AutoCompleteTextView with progressbar and clear button
 */
@InjectViewState
public class LocationTitlePresenter extends MvpPresenter<LocationTitleView>
        implements LocationAddressesLoader.LoaderDelegate{

    private LocationAddressesLoader mLoader;
    private final Context mCtx;
    private ArrayList<Address> mAddresses;
    private Address mSelectedAddress;


    public LocationTitlePresenter(Context context) {
        this.mCtx = context.getApplicationContext();
        mAddresses = new ArrayList<>();
    }

    /**
     * Starts geocoder query task
     * @param location String for querying
     */
    public void loadLocations(final String location) {
        if (mLoader != null) {
            mLoader.cancel(true);
        }
        mLoader = new LocationAddressesLoader(mCtx, this);
        mLoader.execute(location);
        getViewState().toggleControls(Globals.SearchState.Searching);
    }

    /**
     * Updates dropdown list with address list
     */
    public void getLocationList() {
        getViewState().toggleControls(Globals.SearchState.Typing);
        getViewState().updateLocationList(mAddresses);
    }

    public void clearLocationList() {
        mAddresses = new ArrayList<>();
    }

    public Address getSelectedAddress() {
        return mSelectedAddress;
    }

    /**
     * Sets selected address and shows it
     * @param selectedAddress address to be set
     */
    public void setSelectedAddress(Address selectedAddress) {
        this.mSelectedAddress = selectedAddress;
        getViewState().showSelected(mSelectedAddress);
    }

    /* LocationAddressesLoader.LoaderDelegate implementation
     * interaction with Geocoder querying task
     * */

    @Override
    public void onLoaderReady(ArrayList<Address> resultList) {
        getViewState().updateLocationList(resultList);
        mAddresses = resultList;
        getViewState().toggleControls(Globals.SearchState.Typing);
    }

    @Override
    public void onError(String errorDescription) {
        getViewState().showError(errorDescription);
    }
}
