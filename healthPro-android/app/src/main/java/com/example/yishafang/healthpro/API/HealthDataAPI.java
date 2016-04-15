package com.example.yishafang.healthpro.API;

import com.example.yishafang.healthpro.Model.HealthDataRealTime;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by yellowstar on 12/13/15.
 */
public interface HealthDataAPI {
    @GET("/patients/{id}/healthrealtime")
    void getHealthDataRealTime(@Path("id") int id, Callback<HealthDataRealTime> response);
}
