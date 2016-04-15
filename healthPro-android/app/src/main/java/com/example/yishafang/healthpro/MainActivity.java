package com.example.yishafang.healthpro;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.yishafang.healthpro.Adapter.SampleFragmentPagerAdapter;

/**
 * @author yishafang on 9/8/15.
 */
public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    public static final String POSITION = "POSITION";

    TabLayout tabLayout;
    ViewPager viewPager;

    private boolean mBounded;
    public static MyService mService;
    private final ServiceConnection mConnection = new ServiceConnection() {
        @SuppressWarnings("unchecked")
        @Override
        public void onServiceConnected(final ComponentName name,
                                       final IBinder service) {
            mService = ((LocalBinder<MyService>) service).getService();
            mBounded = true;
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mService = null;
            mBounded = false;
            Log.d(TAG, "onServiceDisconnected");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doBindService();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        SampleFragmentPagerAdapter pagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    private void doBindService() {
        bindService(new Intent(this, MyService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    private void doUnbindService() {
        if (mConnection != null) {
            unbindService(mConnection);
        }
    }

    public MyService getmService() {
        return mService;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sign_out:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
