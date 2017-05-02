package com.example.nnv.geotask.common.utils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by nnv on 02.05.17.
 */

public interface DirectionsService {
    @GET("json")
    Call<String>obtainDirections(@QueryMap Map<String, String> params);
}
