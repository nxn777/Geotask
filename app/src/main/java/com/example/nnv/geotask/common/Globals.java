package com.example.nnv.geotask.common;

/**
 * Created by nnv on 25.04.17.
 */

public final class Globals {
    public enum PageType {
        From, To
    }
    public enum SearchState {
        Searching, Typing
    }
    public static final int MAX_RESULTS = 7;
    public static final int SEARCH_THRESHOLD = 3;
    public static final String TAG = "GEO:";

    public static final int DEFAULT_AUTOCOMPLETE_DELAY = 750;

    public static String nullAsString(String str) {
        return (str == null) ? "" : str;
    }
}
