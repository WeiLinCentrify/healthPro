package com.example.yishafang.healthpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.yishafang.healthpro.API.DoctorInfoAPI;
import com.example.yishafang.healthpro.Model.Doctor;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * @author yishafang on 12/8/15.
 */
public class UpdateGenderActivity extends Activity{
    private static final String TAG = UpdateGenderActivity.class.getSimpleName();

    private CheckBox femaleCheck;
    private CheckBox maleCheck;
    private Button cancel;

    private String gender;

    private UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_gender);

        userSessionManager = new UserSessionManager(getApplicationContext());

        femaleCheck = (CheckBox) findViewById(R.id.female_check);
        femaleCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (femaleCheck.isChecked()) {
                    maleCheck.setChecked(false);
                    gender = "Female";
                    Toast.makeText(UpdateGenderActivity.this, "Set your gender as female.", Toast.LENGTH_LONG).show();

                    updateGender();
                }
            }
        });

        maleCheck = (CheckBox) findViewById(R.id.male_check);
        maleCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (maleCheck.isChecked()) {
                    femaleCheck.setChecked(false);
                    gender = "Male";
                    Toast.makeText(UpdateGenderActivity.this, "Set your gender as male.", Toast.LENGTH_LONG).show();

                    updateGender();
                }
            }
        });

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateGender() {
        Log.w(TAG, gender);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("gender", gender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TypedInput typedInput = new TypedByteArray("application/json", requestBody.toString().getBytes());

        DoctorInfoAPI api = ServiceFactory.createService(DoctorInfoAPI.class);
        api.updateDoctorInfo(userSessionManager.getUserId(), typedInput, new Callback<Doctor>() {
            @Override
            public void success(Doctor doctor, Response response) {
                Log.w(TAG, "Update gender successfully!");

                // TODO: go back to setting tab
                Intent intent = new Intent(UpdateGenderActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w(TAG, "update error!!!!");
                error.printStackTrace();
            }
        });
    }

}
