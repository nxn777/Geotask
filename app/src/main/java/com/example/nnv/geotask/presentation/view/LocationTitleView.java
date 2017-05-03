package com.example.nnv.geotask.presentation.view;

import android.location.Address;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.nnv.geotask.common.Globals;

import java.util.ArrayList;

/**
 * Created by nnv on 25.04.17.
 * Moxy View describing autoCompleteTextView with progressbar and clear button on address search screen
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface LocationTitleView  extends MvpView {
    /**
     * Updates data for dropdown list of hints
     * @param addressList new data
     */
    void updateLocationList(ArrayList<Address> addressList);

    /**
     * Shows error message
     * @param error String message to show
     */
    void showError(String error);

    /**
     * Toggles UI according to state
     * @param searchState State to be toggled to
     */
    void toggleControls(Globals.SearchState searchState);

    /**
     * Shows selected address
     * @param address Address to be shown
     */
    void showSelected(Address address);
}
