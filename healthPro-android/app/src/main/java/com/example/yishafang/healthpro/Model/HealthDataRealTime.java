package com.example.yishafang.healthpro.Model;

import java.util.Date;

/**
 * Created by yellowstar on 12/12/15.
 */
public class HealthDataRealTime {
    private long id;
    private Patient patient;
    private double heartBPM;
    private double bodyWeight;
    private double walkingRunningDistance;
    private long timeStamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public double getHeartBPM() {
        return heartBPM;
    }

    public void setHeartBPM(double heartBPM) {
        this.heartBPM = heartBPM;
    }

    public double getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(double bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public double getWalkingRunningDistance() {
        return walkingRunningDistance;
    }

    public void setWalkingRunningDistance(double walkingRunningDistance) {
        this.walkingRunningDistance = walkingRunningDistance;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
