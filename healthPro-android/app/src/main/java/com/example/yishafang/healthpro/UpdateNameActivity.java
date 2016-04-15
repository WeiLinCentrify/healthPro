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
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * @author yishafang on 12/7/15.
 */
public class UpdateNameActivity extends Activity{
    private final static String TAG = UpdateNameActivity.class.getSimpleName();

    private EditText firstNameField;
    private EditText lastNameField;
    private Button update;
    private Button cancel;

    private int doctId;
    private String firstName;
    private String lastName;

    private UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_name);

        userSessionManager = new UserSessionManager(getApplicationContext());
        doctId = userSessionManager.getUserId();

        firstNameField = (EditText) findViewById(R.id.edit_first_name);
        lastNameField = (EditText) findViewById(R.id.edit_last_name);

        // Set up first name and last name
        firstName = userSessionManager.getUserDetail().getFirstName();
        firstNameField.setText(firstName);
        lastName = userSessionManager.getUserDetail().getLastName();
        lastNameField.setText(lastName);

        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName();
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

    private void updateName() {
        firstName = firstNameField.getText().toString();
        lastName = lastNameField.getText().toString();
        Log.w(TAG, firstName + " " + lastName);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("firstName", firstName);
            requestBody.put("lastName", lastName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TypedInput typedInput = new TypedByteArray("application/json", requestBody.toString().getBytes());

        DoctorInfoAPI api = ServiceFactory.createService(DoctorInfoAPI.class);
        api.updateDoctorInfo(doctId, typedInput, new Callback<Doctor>() {
            @Override
            public void success(Doctor doctor, Response response) {
                Log.w(TAG, "Update name successfully!");

                Context context = getApplicationContext();
                CharSequence text = "Update name successfully!";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                toast.show();

                // TODO: go back to setting tab
                Intent intent = new Intent(UpdateNameActivity.this, MainActivity.class);
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
