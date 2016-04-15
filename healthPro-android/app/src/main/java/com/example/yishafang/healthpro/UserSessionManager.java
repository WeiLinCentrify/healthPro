package com.example.yishafang.healthpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.yishafang.healthpro.Model.Appointment;
import com.example.yishafang.healthpro.Model.CurDoctor;
import com.example.yishafang.healthpro.Model.Doctor;
import com.example.yishafang.healthpro.Model.Patient;
import com.example.yishafang.healthpro.Model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by yellowstar on 11/25/15.
 */
public class UserSessionManager {
    public static final String TAG = "UserSessionManager";
    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "HealthProPreference";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User
    public static final String KEY_USER = "UserInfo";

    // Id
    public static final String KEY_ID = "UserId";

    // Id
    public static final String KEY_USERNAME = "Username";

    // Id
    public static final String KEY_PASSWORD = "Password";

    // token
    public static final String KEY_TOKEN = "UserToken";

    // contact
    public static final String KEY_CONTACT = "Contacts";

    public static final String KEY_CUR_APPOINTMENT = "CurAppointment";


    // Constructor
    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(CurDoctor curDoctor){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);
        // Storing User as Json
        String json = new Gson().toJson(curDoctor);
        editor.putString(KEY_USER, json);
        // Storing Id
        editor.putInt(KEY_ID, curDoctor.getId());
        // Storing username
        editor.putString(KEY_USERNAME, curDoctor.getUsername());
        // Storing password
        editor.putString(KEY_PASSWORD, curDoctor.getPassword());
        // Storing token
        editor.putString(KEY_TOKEN, curDoctor.getToken());
        // commit changes
        editor.commit();
        Log.i(TAG, "createUserLoginSession: " + json);
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }

    /**
     * Get stored session data
     * */
    public CurDoctor getUserDetail() {
        CurDoctor curDoctor = new Gson().fromJson(pref.getString(KEY_USER, null), CurDoctor.class);
        return curDoctor;
    }

    public int getUserId() {
        return pref.getInt(KEY_ID, 0);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public String getPassword() {
        return pref.getString(KEY_PASSWORD, null);
    }

    public String getUserToken() {
        return pref.getString(KEY_TOKEN, null);
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public void saveContacts(List<Patient> patients) {
        String json = new Gson().toJson(patients);
        editor.putString(KEY_CONTACT, json);
        editor.commit();
        Log.i(TAG, "saveContacts:" + json);
    }

    public List<Patient> getContacts() {
        String json = pref.getString(KEY_CONTACT, null);
        if (json == null) return null;
        List<Patient> list = new Gson().fromJson(json, new TypeToken<List<Patient>>(){}.getType());
        return list;
    }

    public Patient getContact(int id) {
        List<Patient> contacts = getContacts();
        for (Patient p : contacts) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public void saveCurAppiontment(Appointment appointment) {
        String json = new Gson().toJson(appointment);
        editor.putString(KEY_CUR_APPOINTMENT, json);
        editor.commit();
    }

    public Appointment getCurAppiontment() {
        return new Gson().fromJson(pref.getString(KEY_CUR_APPOINTMENT, null), Appointment.class);
    }
}
