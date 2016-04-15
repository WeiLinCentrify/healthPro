package com.example.yishafang.healthpro.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by Winniewlz on 8/31/15.
 */
public class Doctor extends User{
    @Expose
    private String introduction;
    @Expose
    private ArrayList<Specialty> specialties;

    public String getIntroduction() {
        return introduction;
    }

    public ArrayList<Specialty> getSpecialties() {
        return specialties;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setSpecialties(ArrayList<Specialty> specialties) {
        this.specialties = specialties;
    }
}
