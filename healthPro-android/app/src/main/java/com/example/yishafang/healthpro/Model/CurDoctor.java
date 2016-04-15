package com.example.yishafang.healthpro.Model;

/**
 * Created by yellowstar on 12/6/15.
 */
public class CurDoctor extends Doctor{
    private String token;
    private long expiration;
    private String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
