package com.example.nnv.geotask.common;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import static java.lang.Math.round;

/**
 * Created by nnv on 25.04.17.
 * Global constants and commonly used static methods
 */

public final class Globals {
    /**
     * Represents state of location choosing screen UI
     */
    public enum SearchState {
        Searching, Typing
    }

    /**
     * Represents state of results screen UI
     */
    public enum ResultState {
        Searching, Found, NotFound
    }

    /**
     * Max geocoder query results
     */
    public static final int MAX_RESULTS = 7;
    /**
     * Threshold of chars count for initiating geocoder query
     */
    public static final int SEARCH_THRESHOLD = 3;
    /**
     * Tag for log.i
     */
    public static final String TAG = "ZZY:";
    /**
     * Google maps zoom level
     */
    public static final int MAP_ZOOM = 10; // 1 - world, 5 - continent, 10 - city, 15 - streets, 20 - buildings
    /**
     * Time interval in milliseconds between typing last char and initiating geocoder query
     */
    public static final int DEFAULT_AUTOCOMPLETE_DELAY = 750;
    //used as key
    public static final String FROM_KEY = "fromkey";
    //used as key
    public static final String TO_KEY = "tokey";
    /**
     * Google directions api base url
     */
    public static final String BASE_DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions/";

    /**
     * Returns empty string for null, and input string otherwise
     * @param str nullable String
     * @return not null String
     */
    @NonNull
    private static String nullAsString(@Nullable String str) {
        return (str == null) ? "" : str;
    }

    /**
     * Returns String, consisting of concatenated input strings, separated by specified String.
     * If one of input strings is null, it is represented by empty one
     * @param separator String separator, for example ", "
     * @param strings Strings to concat
     * @return String
     */
    public static String concatNullableStrings(String separator, String... strings) {
        String result = "";
        for (int i = 0; i < strings.length - 1; i++) {
            String addition = nullAsString(strings[i]).equals("") ? "" : nullAsString(strings[i]) + separator;
            result = result + addition;
        }
        result = result + nullAsString(strings[strings.length -1]);
        return result;
    }

    /**
     * Converts dp to pixels
     * @param dp float count of dp
     * @param context Context
     * @return int number of pixels
     */
    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return round(dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}

