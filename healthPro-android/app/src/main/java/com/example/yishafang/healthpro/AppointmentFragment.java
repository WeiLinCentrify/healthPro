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
import android.widget.TextView;

import com.example.yishafang.healthpro.API.AppointmentsAPI;
import com.example.yishafang.healthpro.Adapter.AppointmentAdapter;
import com.example.yishafang.healthpro.Model.Appointment;
import com.example.yishafang.healthpro.Model.ApptStatusEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.yishafang.healthpro.Constants.APPOINTMENT_ID;

/**
 * @author yishafang on 9/3/15.
 */
public class AppointmentFragment extends Fragment {
    public static final String TAG = "AppointmentFragment";

    ListView listView;
    TextView noAppointment;
    private UserSessionManager userSessionManager;

    public static AppointmentFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(TAG, page);
        AppointmentFragment appointmentFragment = new AppointmentFragment();
        appointmentFragment.setArguments(args);
        return appointmentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userSessionManager = new UserSessionManager(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_appointment, container, false);
        listView = (ListView) v.findViewById(R.id.lv_appointment);
        noAppointment = (TextView) v.findViewById(R.id.tv_no_appointment);
        populateAppointmentsList();
        return v;
    }

    private void populateAppointmentsList() {
        Log.i(TAG, "" + userSessionManager.getUserId());
        AppointmentsAPI api = ServiceFactory.createService(AppointmentsAPI.class);                            //creating a service for adapter with our GET class
        api.getFeed(userSessionManager.getUserId(), new Callback<Appointment[]>() {
            @Override
            public void success(final Appointment[] appointments, Response response) {
                if (appointments == null || appointments.length == 0) {
                    noAppointment.setVisibility(View.VISIBLE);
                } else {
                    final List<Appointment> appointmentList = new LinkedList<>();
                    for (int i = 0; i < appointments.length; i++) {
                        if (appointments[i].getApptStatus().getId() <= ApptStatusEnum.BOOKED.id()) {
                            appointmentList.add(appointments[i]);
                        }
                    }
                    Collections.sort(appointmentList, new Comparator<Appointment>() {
                        @Override
                        public int compare(Appointment t1, Appointment t2) {
                            return t1.getApptStatus().getId() - t2.getApptStatus().getId();
                        }
                    });
                    AppointmentAdapter adapter = new AppointmentAdapter(getContext(), appointmentList);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Appointment appt = appointmentList.get(position);
                            if(!(appt.getApptStatus().getId() == ApptStatusEnum.NEW.id())){
                                String appointmentId = String.valueOf(appt.getId());
                                Intent intent = new Intent(getContext(), AppointmentDetailActivity.class);
                                intent.putExtra(APPOINTMENT_ID, appointmentId);
                                startActivity(intent);
                            }
                        }
                    });

                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w(TAG, error);
            }
        });
    }


}
