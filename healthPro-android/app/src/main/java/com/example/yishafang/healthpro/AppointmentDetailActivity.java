package com.example.yishafang.healthpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yishafang.healthpro.API.AppointmentsAPI;
import com.example.yishafang.healthpro.Model.Appointment;
import com.example.yishafang.healthpro.Model.Patient;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.yishafang.healthpro.Constants.APPOINTMENT_ID;
import static com.example.yishafang.healthpro.Constants.baseAPI;

/**
 * @author yishafang on 9/10/15.
 */
public class AppointmentDetailActivity extends Activity {
    private static final String TAG = "ApptDetailActivity";

    private Appointment curAppt;

    TextView appointmentTitle;
    TextView patientName;
    TextView appointmentDescription;
    TextView age;
    TextView gender;
    TextView allergyInfo;
    TextView illnessInfo;
    TextView otherInfo;
    RelativeLayout patientInfoField;

    TextView appointmentReminder;
    TextView comeBackReminder;
    TextView openSchedule;
    Button joinFaceTime;

    private UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_detail);

        appointmentTitle = (TextView) findViewById(R.id.appt_title);
        patientName = (TextView) findViewById(R.id.patient_name);
        appointmentDescription = (TextView) findViewById(R.id.appt_description);
        age = (TextView) findViewById(R.id.age);
        gender = (TextView) findViewById(R.id.gender);
        allergyInfo = (TextView) findViewById(R.id.allergy_info);
        illnessInfo = (TextView) findViewById(R.id.illness_info);
        otherInfo = (TextView) findViewById(R.id.other_info);
        patientInfoField = (RelativeLayout) findViewById(R.id.patient_info_field);

        appointmentReminder = (TextView) findViewById(R.id.appt_reminder);
        comeBackReminder = (TextView) findViewById(R.id.come_back_reminder);
        openSchedule = (TextView) findViewById(R.id.open_schedule);
        joinFaceTime = (Button) findViewById(R.id.join);
        joinFaceTime.setEnabled(false);

        userSessionManager = new UserSessionManager(getApplicationContext());

        getAppointmentDetail();

    }

    private void getAppointmentDetail() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(baseAPI).build();

        AppointmentsAPI api = restAdapter.create(AppointmentsAPI.class);

        Intent intent = getIntent();
        final String appointmentId = intent.getStringExtra(APPOINTMENT_ID);
        api.getDetail(userSessionManager.getUserId(), appointmentId, new Callback<Appointment>() {
            @Override
            public void success(Appointment appointment, Response response) {
                curAppt = appointment;
                if (appointment.getPatient() != null) {

                    if (appointment.getTitle() != null) {
                        appointmentTitle.setText(appointment.getTitle());
                    } else {
                        appointmentTitle.setVisibility(View.GONE);
                    }

                    if (appointment.getDescription() != null) {
                        appointmentDescription.setText(appointment.getDescription());
                    } else {
                        appointmentDescription.setVisibility(View.GONE);
                    }

                    if (appointment.getPatient().getAge() != 0) {
                        age.setText("Age: " + appointment.getPatient().getAge());
                    } else {
                        age.setVisibility(View.GONE);
                    }

                    if (appointment.getPatient().getGender() != null) {
                        gender.setText("Gender: " + appointment.getPatient().getGender());
                    } else {
                        gender.setVisibility(View.GONE);
                    }

                    if (appointment.getPatient().getAllergyInfo() != null) {
                        allergyInfo.setText("Allergy Information: " + appointment.getPatient().getAllergyInfo());
                    } else {
                        allergyInfo.setVisibility(View.GONE);
                    }

                    if (appointment.getPatient().getIllnessInfo() != null) {
                        illnessInfo.setText("Illness Information: " + appointment.getPatient().getIllnessInfo());
                    } else {
                        illnessInfo.setVisibility(View.GONE);
                    }

                    if (appointment.getPatient().getOtherInfo() != null) {
                        otherInfo.setText("Other Information: " + appointment.getPatient().getOtherInfo());
                    } else {
                        otherInfo.setVisibility(View.GONE);
                    }

                    String name = appointment.getPatient().getFirstName() + " " +
                            appointment.getPatient().getLastName();

                    patientName.setText("Patient Name: " + name);



                    String reminder = getResources().getString(R.string.appointment_reminder);
                    String msg = String.format(reminder, name);
                    appointmentReminder.setText(msg);
                    String appWaitingTime = getTimeDiff(appointment.getStartTime());
                    if(Integer.parseInt(appWaitingTime) <= -30 ) {
                        appointmentReminder.setText("Your appointment has passed.");
                        comeBackReminder.setText("");
                        joinFaceTime.setEnabled(false);
                    }

                    else if(Integer.parseInt(appWaitingTime) > -30 &&  Integer.parseInt(appWaitingTime) <= 0 ) {
                        appointmentReminder.setText("Your appointment has been started for " +appWaitingTime +" minutes. \nPlease join the face-time room");
                        comeBackReminder.setText("");
                        joinFaceTime.setEnabled(true);
                    }

                    else if(Integer.parseInt(appWaitingTime) == 1) {
                        appointmentReminder.setText("Your appointment will start in " + getTimeDiff(appointment.getStartTime())+ " minute. \nPlease join the face-time room");
                        comeBackReminder.setText("");
                        joinFaceTime.setEnabled(true);
                    }

                    else if(Integer.parseInt(appWaitingTime) > 1 && Integer.parseInt(appWaitingTime) <= 10) {
                        appointmentReminder.setText("Your appointment will start in " + getTimeDiff(appointment.getStartTime())+ " minutes. \nPlease join the face-time room");
                        comeBackReminder.setText("");
                        joinFaceTime.setEnabled(true);
                    }


                    joinFaceTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG, "facetime clicked");
                            userSessionManager.saveCurAppiontment(curAppt);
                            Intent intent = new Intent(AppointmentDetailActivity.this, UIActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                } else {
                    openSchedule.setVisibility(View.VISIBLE);
                    comeBackReminder.setVisibility(View.INVISIBLE);
                    patientInfoField.setVisibility(View.GONE);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.w("AppointmentDetail:: ", error);
            }
        });

    }

    private String getTimeDiff(String startTime){
        Calendar c = Calendar.getInstance();
        long currTimeStamp = c.getTimeInMillis();
        long diffTimeStamp = Long.parseLong(startTime)-currTimeStamp;
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffTimeStamp);
        Log.w(TAG,String.valueOf(diffInMinutes));
//        return Long.toString(diffInMinutes);
        return "0"; //for test
    }
}
