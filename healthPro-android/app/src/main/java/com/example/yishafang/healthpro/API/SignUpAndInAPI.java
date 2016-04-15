package com.example.yishafang.healthpro.API;

import com.example.yishafang.healthpro.Model.CurDoctor;
import com.example.yishafang.healthpro.Model.Doctor;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.mime.TypedInput;

/**
 * @author yishafang on 11/16/15.
 */
public interface SignUpAndInAPI {
    /** Sign Up */
    @POST("/doctors")
    void signUp(@Body TypedInput signUpInfo, Callback<CurDoctor> response);

    /** Sign In */
    @POST("/login")
    void login(@Body TypedInput loginInfo, Callback<CurDoctor> response);
}
