package com.example.yishafang.healthpro.API;

import com.example.yishafang.healthpro.Model.Appointment;


import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.mime.TypedInput;


/**
 * @author Winniewlz on 8/31/15.
 */
public interface AppointmentsAPI {
    /** Get all appointments under a doctor */
    @GET("/doctors/{id}/appointments")      //here is the other url part.best way is to start using /
    void getFeed(@Path("id") int id,Callback<Appointment[]> response);     //string user is for passing values from edittext for eg: user=basil2style,google
    //response is the response from the server which is now in the POJO

    /** Get an appointment details */
    @GET("/doctors/{id}/appointments/{appointmentId}")
    void getDetail(@Path("id") int id, @Path("appointmentId") String appointmentId,
                   Callback<Appointment> response);

    /** Create a new time slot for */
    @POST("/doctors/{id}/appointments")
    void createTimeSlot(@Path("id") int id, @Body TypedInput time, Callback<Appointment> response);
}


