package com.example.yishafang.healthpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yishafang.healthpro.API.AppointmentsAPI;
import com.example.yishafang.healthpro.Adapter.AppointmentAdapter;
import com.example.yishafang.healthpro.Model.Appointment;
import com.example.yishafang.healthpro.Model.ApptStatusEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.yishafang.healthpro.Constants.APPOINTMENT_ID;
import static com.example.yishafang.healthpro.Constants.DOCTOR_ID;
import static com.example.yishafang.healthpro.Constants.baseAPI;

/**
 * Only show appointments which booked by patients, this fragment doesn't show open schedules here.
 * Open schedules will be shown in APPT tab.
 *
 * @author yishafang on 11/21/15.
 */
public class RecordFragment extends Fragment{
    public static final String TAG = "RecordFragment";

    ListView listView;
    TextView noRecord;

    ProgressBar spinner;
    private UserSessionManager userSessionManager;

    public static RecordFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(TAG, page);
        RecordFragment recordFragment = new RecordFragment();
        recordFragment.setArguments(args);
        return recordFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSessionManager = new UserSessionManager(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_record, container, false);
        listView = (ListView) v.findViewById(R.id.listView_record);
        noRecord = (TextView) v.findViewById(R.id.no_record);

//        spinner = (ProgressBar) v.findViewById(R.id.progressBar);
//        spinner.setVisibility(View.GONE);

        populateRecordsList();

        return v;
    }

    public void populateRecordsList() {
        //spinner.setVisibility(View.VISIBLE);

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(baseAPI).build();

        AppointmentsAPI api = restAdapter.create(AppointmentsAPI.class);                            //creating a service for adapter with our GET class

        api.getFeed(userSessionManager.getUserId(), new Callback<Appointment[]>() {
            @Override
            public void success(final Appointment[] appointments, Response response) {

                // If no appointment found, show a message to user
                if (appointments == null || appointments.length == 0) {
                    noRecord.setVisibility(View.VISIBLE);
                } else {
                    ArrayList<Appointment> recordList = new ArrayList<>();
                    for (int i = 0; i < appointments.length; i++) {
                        if (appointments[i].getApptStatus().getId() > ApptStatusEnum.BOOKED.id()) {
                            recordList.add(appointments[i]);
                        }
                    }
                    Collections.sort(recordList, new Comparator<Appointment>() {
                        @Override
                        public int compare(Appointment t1, Appointment t2) {
                            return t1.getApptStatus().getId() - t2.getApptStatus().getId();
                        }
                    });

                    AppointmentAdapter adapter = new AppointmentAdapter(getContext(), recordList);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String appointmentId = String.valueOf(appointments[position].getId());
                            Intent intent = new Intent(getContext(), AppointmentDetailActivity.class);
                            intent.putExtra(APPOINTMENT_ID, appointmentId);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}
