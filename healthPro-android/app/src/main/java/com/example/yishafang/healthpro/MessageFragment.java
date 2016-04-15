package com.example.yishafang.healthpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yishafang.healthpro.API.DoctorInfoAPI;
import com.example.yishafang.healthpro.Adapter.ContactAdapter;
import com.example.yishafang.healthpro.Model.Patient;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author yishafang on 9/6/15.
 */
public class MessageFragment extends Fragment implements AdapterView.OnItemClickListener {
    public static final String TAG = "MessageFragment";
    private Activity ctx;
    private View layout;
    private ListView lvContact;
    private TextView mDialogText;
    private WindowManager mWindowManager;
    private UserSessionManager userSessionManager;


    public static MessageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(TAG, page);
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setArguments(args);
        return messageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSessionManager = new UserSessionManager(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (layout == null) {
            ctx = this.getActivity();
            layout = ctx.getLayoutInflater().inflate(R.layout.fragment_message, null);
            mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            initViews();
            initData();
            setOnListener();
        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        return layout;
    }

    private void initViews() {
        lvContact = (ListView) layout.findViewById(R.id.lvContact);
    }

    public void refresh() {
        initData();
    }

    private void initData() {
//        if (userSessionManager.getContacts() != null) {
//            lvContact.setAdapter(new ContactAdapter(getActivity(),
//                    userSessionManager.getContacts()));
//        } else {
            DoctorInfoAPI api = ServiceFactory.createService(DoctorInfoAPI.class);
            api.getContacts(userSessionManager.getUserId(), new Callback<List<Patient>>() {
                @Override
                public void success(List<Patient> patients, Response response) {
                    Collections.sort(patients, new Comparator<Patient>() {
                        @Override
                        public int compare(Patient p1, Patient p2) {
                            int result = p1.getFirstName().toUpperCase().compareTo(p2.getFirstName().toUpperCase());
                            if (result == 0) {
                                result = p1.getLastName().toUpperCase().compareTo(p2.getLastName().toUpperCase());
                            }
                            return result;
                        }
                    });
                    lvContact.setAdapter(new ContactAdapter(getActivity(), patients));
                    userSessionManager.saveContacts(patients);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.getMessage());
                }
            });
       // }
    }

    private void setOnListener() {
        lvContact.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Patient user = userSessionManager.getContacts().get(arg2);
        if (user != null) {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra(Constants.CHAT_USER_ID, user.getId());
            getActivity().startActivity(intent);
        }

    }
}
