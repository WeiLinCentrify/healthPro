package com.example.yishafang.healthpro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yishafang.healthpro.API.DoctorInfoAPI;
import com.example.yishafang.healthpro.Adapter.SpecialtiesAdapter;
import com.example.yishafang.healthpro.Model.Doctor;
import com.example.yishafang.healthpro.Model.Specialty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.yishafang.healthpro.Constants.baseAPI;

/**
 * @author yishafang on 11/29/15.
 */
public class SettingFragment extends Fragment{
    public static final String TAG = "SettingFragment";

    TextView firstName;
    TextView name;
    TextView gender;
    TextView birthday;
    TextView introduction;
    ListView specialtyList;
    ImageView avatar;

    private int doctorId;
    private String intro;

    private UserSessionManager userSessionManager;

    public static SettingFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(TAG, page);
        SettingFragment settingFragment = new SettingFragment();
        settingFragment.setArguments(args);
        return settingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSessionManager = new UserSessionManager(getActivity().getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        firstName = (TextView) v.findViewById(R.id.firstname);
        name = (TextView) v.findViewById(R.id.name);
        gender = (TextView) v.findViewById(R.id.gender);
        birthday = (TextView) v.findViewById(R.id.birthday);
        introduction = (TextView) v.findViewById(R.id.introduction);
        specialtyList = (ListView) v.findViewById(R.id.spec_list);

        avatar = (ImageView) v.findViewById(R.id.avatar);
        if (userSessionManager.getUserDetail().getAvatarUrl() != null) {
            avatar.setImageURI(Uri.parse(userSessionManager.getUserDetail().getAvatarUrl()));
        } else {
            avatar.setBackgroundResource(R.drawable.avatar);
            avatar.getLayoutParams().height = 350;
            avatar.getLayoutParams().width = 350;
        }

        RelativeLayout namePart = (RelativeLayout) v.findViewById(R.id.name_part);
        RelativeLayout genderPart = (RelativeLayout) v.findViewById(R.id.gender_part);
        RelativeLayout birthdayPart = (RelativeLayout) v.findViewById(R.id.birthday_part);
        RelativeLayout introductionPart = (RelativeLayout) v.findViewById(R.id.introduction_part);
        RelativeLayout specialtyPart = (RelativeLayout) v.findViewById(R.id.specialties_part);

        getDoctorInfo();

        namePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateNameActivity.class);
                startActivity(intent);
            }
        });

        genderPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateGenderActivity.class);
                startActivity(intent);
            }
        });

        birthdayPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateBirthdayActivity.class);
                startActivity(intent);
            }
        });

        introductionPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateIntroductionActivity.class);
                intent.putExtra("INTRO", intro);
                startActivity(intent);
            }
        });

        specialtyPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateSpecialtyActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    private void getDoctorInfo() {
        doctorId = userSessionManager.getUserId();

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(baseAPI).build();
        DoctorInfoAPI api = restAdapter.create(DoctorInfoAPI.class);
        api.getInfo(doctorId, new Callback<Doctor>() {
            @Override
            public void success(Doctor doctor, Response response) {
                Log.w(TAG, "fetch doctor information successfully!!");

                firstName.setText(String.format(getResources().getString(R.string.hello), doctor.getFirstName()));
                name.setText(doctor.getFirstName() + " " + doctor.getLastName());
                gender.setText(doctor.getGender());
                if (doctor.getBirthday() != null) {
                    birthday.setText(convertEpochTimeToDate(doctor.getBirthday()));
                }
                introduction.setText(doctor.getIntroduction());
                intro = doctor.getIntroduction();

                List<Specialty> specialties = new ArrayList<>(doctor.getSpecialties());

                SpecialtiesAdapter specialtiesAdapter = new SpecialtiesAdapter(getContext(), specialties);
                specialtyList.setAdapter(specialtiesAdapter);

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private String convertEpochTimeToDate(String epoch) {
        Date date = new Date(Long.parseLong(epoch));
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return format.format(date);
    }

}
