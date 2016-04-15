package com.example.yishafang.healthpro;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yishafang.healthpro.API.DoctorInfoAPI;
import com.example.yishafang.healthpro.Model.Doctor;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * @author yishafang on 12/8/15.
 */
public class UpdateBirthdayActivity extends Activity {
    private static final String TAG = UpdateBirthdayActivity.class.getSimpleName();
    static final int DATE_DIALOG_ID = 999;

    private TextView birthday;
    private Button updateButton;
    private Button changeButton;

    private UserSessionManager userSessionManager;

    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_birthday);

        userSessionManager = new UserSessionManager(getApplicationContext());

        birthday = (TextView) findViewById(R.id.birthday);
        if (userSessionManager.getUserDetail().getBirthday() == null) {
            birthday.setText(getResources().getString(R.string.no_birthday_msg));
        } else {
            String date = String.format(getResources().getString(R.string.your_birthday), convertEpochTimeToDate(userSessionManager.getUserDetail().getBirthday()));
            birthday.setText(date);
        }


        updateButton = (Button) findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBirthday();
            }
        });

        changeButton = (Button) findViewById(R.id.change_button);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    private void updateBirthday() {
        String date = year + "-" + month + "-" + day;
        Log.w(TAG, date);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("birthday", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TypedInput typedInput = new TypedByteArray("application/json", requestBody.toString().getBytes());

        DoctorInfoAPI api = ServiceFactory.createService(DoctorInfoAPI.class);
        api.updateDoctorInfo(userSessionManager.getUserId(), typedInput, new Callback<Doctor>() {
            @Override
            public void success(Doctor doctor, Response response) {
                Log.w(TAG, "Update birthday successfully!!");

                Toast.makeText(UpdateBirthdayActivity.this, "Update birthday successfully!", Toast.LENGTH_LONG).show();

                // TODO: go back to setting tab
                Intent intent = new Intent(UpdateBirthdayActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w(TAG + "=[", error);
                error.printStackTrace();
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, datePickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener(){
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            StringBuilder newDate = new StringBuilder();
            newDate.append(month + 1).append("/").append(day).append("/").append(year);
            birthday.setText(String.format(getResources().getString(R.string.your_birthday), newDate.toString()));
        }
    };

    private String convertEpochTimeToDate(String epoch) {
        Date date = new Date(Long.parseLong(epoch));
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return format.format(date);
    }
}
