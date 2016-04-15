package com.example.yishafang.healthpro.Model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Winniewlz on 8/31/15.
 */
public class Appointment implements Serializable{
    @Expose
    private Integer id;
    @Expose
    private String startTime;
    @Expose
    private String endTime;
    @Expose
    private String submitTime;
    @Expose
    private Doctor doctor;
    @Expose
    private String title;
    @Expose
    private String description;
    @Expose
    private Patient patient;
    @Expose
    private String diagnosticReport;
    @Expose
    private ApptStatus apptStatus;

    public Integer getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getDiagnosticReport() {
        return diagnosticReport;
    }

    public ApptStatus getApptStatus() {
        return apptStatus;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDiagnosticReport(String diagnosticReport) {
        this.diagnosticReport = diagnosticReport;
    }

    public void setApptStatus(ApptStatus apptStatus) {
        this.apptStatus = apptStatus;
    }
}
