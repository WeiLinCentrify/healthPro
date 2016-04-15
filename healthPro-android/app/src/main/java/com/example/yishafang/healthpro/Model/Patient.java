package com.example.yishafang.healthpro.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by Winniewlz on 9/17/15.
 */
public class Patient extends User{
    @Expose
    private String allergyInfo;
    @Expose
    private String illnessInfo;
    @Expose
    private String otherInfo;

    public String getAllergyInfo() {
        return allergyInfo;
    }

    public String getIllnessInfo() {
        return illnessInfo;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setAllergyInfo(String allergyInfo) {
        this.allergyInfo = allergyInfo;
    }

    public void setIllnessInfo(String illnessInfo) {
        this.illnessInfo = illnessInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }
}

