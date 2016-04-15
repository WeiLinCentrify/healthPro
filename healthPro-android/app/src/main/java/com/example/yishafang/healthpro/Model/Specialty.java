package com.example.yishafang.healthpro.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by Winniewlz on 9/17/15.
 */
public class Specialty {
    @Expose
    private Integer id;
    @Expose
    private String name;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
