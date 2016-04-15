package com.example.yishafang.healthpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yishafang.healthpro.API.DoctorInfoAPI;
import com.example.yishafang.healthpro.Model.Doctor;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

import static com.example.yishafang.healthpro.Constants.baseAPI;

/**
 * @author yishafang on 11/29/15.
 */
public class UpdateIntroductionActivity extends Activity{
    public static final String TAG = "UpdateIntro";

    EditText introduction;
    Button update;
    Button cancel;

    private int doctorId;
    private String inputIntroduction;

    private UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_introduction);

        introduction = (EditText) findViewById(R.id.edit_introduction);
        update = (Button) findViewById(R.id.update);
        cancel = (Button) findViewById(R.id.cancel);

        userSessionManager = new UserSessionManager(getApplicationContext());
        doctorId = userSessionManager.getUserId();

        //getIntroduction();
        Intent intent = getIntent();
        introduction.setText(intent.getStringExtra("INTRO"));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateIntroduction();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateIntroduction() {
        inputIntroduction = introduction.getText().toString();
        Log.w(TAG, inputIntroduction);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("introduction", inputIntroduction);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TypedInput typedInput = new TypedByteArray("application/json", requestBody.toString().getBytes());

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(baseAPI).build();
        DoctorInfoAPI api = restAdapter.create(DoctorInfoAPI.class);
        api.updateDoctorInfo(doctorId, typedInput, new Callback<Doctor>() {
            @Override
            public void success(Doctor doctor, Response response) {
                Log.w(TAG, "Update introduction successfully!");

                Context context = getApplicationContext();
                CharSequence text = "Update introduction successfully!";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                toast.show();

                // TODO: go back to setting tab
                Intent intent = new Intent(UpdateIntroductionActivity.this, MainActivity.class);
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
