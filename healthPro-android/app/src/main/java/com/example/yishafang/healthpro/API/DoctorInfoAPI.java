package com.example.yishafang.healthpro.API;

import com.example.yishafang.healthpro.Model.Doctor;
import com.example.yishafang.healthpro.Model.Patient;
import com.example.yishafang.healthpro.Model.Specialty;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.mime.TypedInput;

/**
 * @author yishafang on 11/29/15.
 */
public interface DoctorInfoAPI {

    /** Get Doctor's information */
    @GET("/doctors/{id}")
    void getInfo(@Path("id") int id, Callback<Doctor> response);

    @PUT("/doctors/{id}")
    void updateDoctorInfo(@Path("id") int id, @Body TypedInput input, Callback<Doctor> response);

    @GET("/doctors/{id}/mypatients")
    void getContacts(@Path("id") int id, Callback<List<Patient>> response);

    /** Get all specialties */
    @GET("/specialties")
    void getAllSpeicalities(Callback<List<Specialty>> response);
}
