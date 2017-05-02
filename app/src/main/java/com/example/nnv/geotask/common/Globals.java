package com.example.nnv.geotask.common;

/**
 * Created by nnv on 25.04.17.
 */

public final class Globals {

    public enum SearchState {
        Searching, Typing
    }

    public static final int MAX_RESULTS = 7;
    public static final int SEARCH_THRESHOLD = 3;
    public static final String TAG = "GEO:";
    public static final int MAP_ZOOM = 10; // 1 - world, 5 - continent, 10 - city, 15 - streets, 20 - buildings
    public static final int DEFAULT_AUTOCOMPLETE_DELAY = 750;
    public static final String FROM_KEY = "fromkey";
    public static final String TO_KEY = "tokey";
    public static final String BASE_DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions/";


    public static String nullAsString(String str) {
        return (str == null) ? "" : str;
    }

    public static String concatNullableStrings(String separator, String... strings) {
        String result = "";
        for (int i = 0; i < strings.length - 1; i++) {
            String addition = nullAsString(strings[i]).equals("") ? "" : nullAsString(strings[i]) + separator;
            result = result + addition;
        }
        result = result + nullAsString(strings[strings.length -1]);
        return result;
    }
}

