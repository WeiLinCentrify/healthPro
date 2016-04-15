package com.example.yishafang.healthpro;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.example.yishafang.healthpro.Model.CurDoctor;
import com.example.yishafang.healthpro.Model.User;

import org.jivesoftware.smack.chat.Chat;

/**
 * Created by yellowstar on 12/9/15.
 */
public class MyService extends Service {
    public static ConnectivityManager cm;
    public static XMPP xmpp;
    public static boolean ServerchatCreated = false;
    String text = "";

    @Override
    public IBinder onBind(final Intent intent) {
        return new LocalBinder<>(this);
    }

    public Chat chat;

    @Override
    public void onCreate() {
        super.onCreate();
        UserSessionManager userSessionManager = new UserSessionManager(getApplicationContext());
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        xmpp = XMPP.getInstance(MyService.this, getString(R.string.xmpp_host),
                userSessionManager.getUsername(), userSessionManager.getPassword());
        xmpp.connect("onCreate");
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        xmpp.connection.disconnect();
    }

    public static boolean isNetworkConnected() {
        return cm.getActiveNetworkInfo() != null;
    }
}
