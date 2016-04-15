package com.example.yishafang.healthpro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yishafang.healthpro.Model.Patient;
import com.example.yishafang.healthpro.R;
import com.example.yishafang.healthpro.common.ViewHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by yellowstar on 12/6/15.
 */
public class ContactAdapter extends BaseAdapter {
    private Context mContext;
    private List<Patient> patients;

    public ContactAdapter(Context mContext, List<Patient> UserInfos) {
        this.mContext = mContext;
        this.patients = UserInfos;
    }

    @Override
    public int getCount() {
        return patients.size();
    }

    @Override
    public Object getItem(int position) {
        return patients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Patient patient = patients.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.contact_item, null);

        }
        ImageView ivAvatar = ViewHolder.get(convertView,
                R.id.contactitem_avatar_iv);
        TextView tvCatalog = ViewHolder.get(convertView,
                R.id.contactitem_catalog);
        TextView tvNick = ViewHolder.get(convertView, R.id.contactitem_nick);
        String catalog = patient.getFirstName().substring(0, 1).toUpperCase();
        if (position == 0) {
            tvCatalog.setVisibility(View.VISIBLE);
            tvCatalog.setText(catalog);
        } else {
            Patient nextPat = patients.get(position - 1);
            String lastCatalog = nextPat.getFirstName().substring(0, 1).toUpperCase();
            if (catalog.equals(lastCatalog)) {
                tvCatalog.setVisibility(View.GONE);
            } else {
                tvCatalog.setVisibility(View.VISIBLE);
                tvCatalog.setText(catalog);
            }
        }

        if (patient.getAvatarUrl() == null) {
            String gender = patient.getGender();
            ivAvatar.setImageResource(gender == null || gender.equals("male") ? R.drawable.p_male : R.drawable.p_female);
        } else {
            ivAvatar.setImageResource(R.drawable.head);
        }
        tvNick.setText(patient.getFirstName() + " " + patient.getLastName());
        return convertView;
    }
}
