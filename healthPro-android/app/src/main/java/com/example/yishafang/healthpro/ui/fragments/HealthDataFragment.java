package com.example.yishafang.healthpro.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yishafang.healthpro.HealthDataTimerTask;
import com.example.yishafang.healthpro.Model.HealthDataRealTime;
import com.example.yishafang.healthpro.R;
import com.example.yishafang.healthpro.UIActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Timer;

/**
 * Created by yellowstar on 12/12/15.
 */
public class HealthDataFragment extends Fragment {
    private static final String TAG = "HealthDataFragment";

    private RelativeLayout mHealthDataLayout;
    private TextView mHeartBpm;
    private TextView mBodyWeight;
    private TextView mDistance;
    private UIActivity videoActivity;
    private Timer timer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.i(TAG, "On attach health data fragment");
        videoActivity = (UIActivity) activity;
        if (!(activity instanceof PublisherControlFragment.PublisherCallbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "On detach health data fragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_health_data, container, false);
        mHealthDataLayout = (RelativeLayout)videoActivity.findViewById(R.id.fragment_health_data_container);
        mHeartBpm = (TextView) rootView.findViewById(R.id.tv_heartBpm);
        mBodyWeight = (TextView) rootView.findViewById(R.id.tv_body_weight);
        mDistance = (TextView) rootView.findViewById(R.id.tv_distance);
        startUpdating();
        return rootView;
    }

    private void startUpdating() {
        timer = new Timer();
        HealthDataTimerTask task = new HealthDataTimerTask(videoActivity.getCurAppointment(), this);
        timer.scheduleAtFixedRate(task, 0, 5000);
    }

    public void updateHealthData(HealthDataRealTime healthDataRealTime) {
        NumberFormat formatter = new DecimalFormat("#0");
        mHeartBpm.setText("Heart Rate: " + formatter.format(healthDataRealTime.getHeartBPM()) + " BPM");
        formatter = new DecimalFormat("#0.00");
        mBodyWeight.setText("Body Weight: " + formatter.format(healthDataRealTime.getBodyWeight()) + " Pounds");
        mDistance.setText("Walking Dist:" + formatter.format(healthDataRealTime.getWalkingRunningDistance()) + " Miles");
    }
}
