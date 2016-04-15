package com.example.yishafang.healthpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yishafang.healthpro.API.DoctorInfoAPI;
import com.example.yishafang.healthpro.Model.Doctor;
import com.example.yishafang.healthpro.Model.Specialty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * @author yishafang on 12/8/15.
 */
public class UpdateSpecialtyActivity extends Activity{
    private static final String TAG = UpdateSpecialtyActivity.class.getSimpleName();

    private ListView specialtyListView;
    private Button update;
    private Button cancel;
    private CheckBox checkBox;

    private List<Specialty> specialtyList;
    public List<Integer> updateSpecList;

    private UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_specialty);

        userSessionManager = new UserSessionManager(getApplicationContext());

        specialtyListView = (ListView) findViewById(R.id.specialty_list);

        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDoctorSpecialties();
            }
        });

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadSpecialtyData();
    }

    private void loadSpecialtyData() {
        DoctorInfoAPI api = ServiceFactory.createService(DoctorInfoAPI.class);
        api.getAllSpeicalities(new Callback<List<Specialty>>() {
            @Override
            public void success(List<Specialty> specialties, Response response) {
                Log.w(TAG, "loading specialty data...");

                specialtyList = new ArrayList<>(specialties);

                updateCheckBox();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w(TAG, "update error!!!!");
                error.printStackTrace();
            }
        });
    }

    private void updateDoctorSpecialties() {
        JSONObject requestBody = new JSONObject();
        JSONArray jsonArray = new JSONArray(updateSpecList);
        try {
            requestBody.put("specialIds", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TypedInput typedInput = new TypedByteArray("application/json", requestBody.toString().getBytes());

        DoctorInfoAPI api = ServiceFactory.createService(DoctorInfoAPI.class);
        api.updateDoctorInfo(userSessionManager.getUserId(), typedInput, new Callback<Doctor>() {
            @Override
            public void success(Doctor doctor, Response response) {
                Log.w(TAG, "Update specialty successfully!");

                Context context = getApplicationContext();
                CharSequence text = "Update specialties successfully!";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                toast.show();

                // TODO: go back to setting tab
                Intent intent = new Intent(UpdateSpecialtyActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w(TAG, "update error!!!!");
                error.printStackTrace();
            }
        });
    }

    private void updateCheckBox() {
        ArrayAdapter<Specialty> adapter = new ArrayAdapter<Specialty>(getApplicationContext(), R.layout.specialty_listview, specialtyList) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final Specialty specialty = getItem(position);

                updateSpecList = new ArrayList<>();

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.specialty_listview, parent, false);
                }

                TextView spec = (TextView) convertView.findViewById(R.id.spec);
                if (specialty != null) {
                    spec.setText(specialty.getName());
                    spec.setTextColor(getResources().getColor(R.color.black));
                }

                checkBox = (CheckBox) convertView.findViewById(R.id.spec_checkbox);
                checkBox.setVisibility(View.VISIBLE);

                for (int i = 0; i < userSessionManager.getUserDetail().getSpecialties().size(); i++) {
                    if (specialty != null &&
                            userSessionManager.getUserDetail().getSpecialties().get(i).toString().equals(specialty.getName())) {
                        checkBox.setChecked(true);
                    }
                }

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!checkBox.isChecked()) {
                            updateSpecList.add(position + 1);
                        }
                    }
                });

                return convertView;
            }
        };

        specialtyListView.setAdapter(adapter);
    }
}
