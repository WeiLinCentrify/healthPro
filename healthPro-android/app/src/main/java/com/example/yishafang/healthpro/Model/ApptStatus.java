package com.example.yishafang.healthpro.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by Winniewlz on 9/17/15.
 */
public class ApptStatus {
    @Expose
    private String name;
    @Expose
    private Integer id;

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
