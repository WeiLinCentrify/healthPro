package com.example.yishafang.healthpro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yishafang.healthpro.Model.Specialty;
import com.example.yishafang.healthpro.R;

import java.util.List;

/**
 * @author yishafang on 12/8/15.
 */
public class SpecialtiesAdapter extends ArrayAdapter<Specialty> {

    public SpecialtiesAdapter(Context context, List<Specialty> specialties) {
        super(context, 0, specialties);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Specialty specialty = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.specialty_listview, parent, false);
        }

        TextView spec = (TextView) convertView.findViewById(R.id.spec);
        if (specialty != null) {
            spec.setText(specialty.getName());
        }

        return convertView;
    }

}
