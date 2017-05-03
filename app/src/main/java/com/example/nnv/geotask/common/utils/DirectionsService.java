package com.example.nnv.geotask.common.utils;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by nnv on 02.05.17.
 * Used by Retrofit
 * Describes call to google directions api
 */

public interface DirectionsService {
    @GET("json")
    Call<ResponseBody>obtainDirections(@QueryMap Map<String, String> params);
}
