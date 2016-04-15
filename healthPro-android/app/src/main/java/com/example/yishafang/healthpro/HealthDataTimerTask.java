package com.example.yishafang.healthpro;

import android.util.Log;

import com.example.yishafang.healthpro.API.HealthDataAPI;
import com.example.yishafang.healthpro.Model.Appointment;
import com.example.yishafang.healthpro.Model.HealthDataRealTime;
import com.example.yishafang.healthpro.ui.fragments.HealthDataFragment;

import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by yellowstar on 12/12/15.
 */
public class HealthDataTimerTask extends TimerTask {
    private Appointment appt;
    private boolean isCalling = false;
    private HealthDataFragment healthDataFragment;
    public HealthDataTimerTask(Appointment appt, HealthDataFragment healthDataFragment) {
        this.appt = appt;
        this.healthDataFragment = healthDataFragment;
    }
    @Override
    public void run() {
        Log.i("HealthDataTimerTask", "run");
        HealthDataAPI api = ServiceFactory.createService(HealthDataAPI.class);
        if (isCalling) return;
        api.getHealthDataRealTime(appt.getPatient().getId(), new Callback<HealthDataRealTime>() {
            @Override
            public void success(final HealthDataRealTime healthDataRealTime, Response response) {
                isCalling = false;
                if (healthDataRealTime != null) {
                    healthDataFragment.updateHealthData(healthDataRealTime);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                isCalling = false;
                error.printStackTrace();
            }
        });
    }
}
