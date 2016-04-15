package com.example.yishafang.healthpro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yishafang.healthpro.Model.Appointment;
import com.example.yishafang.healthpro.Model.ApptStatusEnum;
import com.example.yishafang.healthpro.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author yishafang on 9/8/15.
 */
public class AppointmentAdapter extends BaseAdapter {
    private Context mContext;
    private List<Appointment> appointments;

    public AppointmentAdapter(Context context, List<Appointment> appointments) {
        this.mContext = context;
        this.appointments = appointments;
    }

    @Override
    public int getCount() {
        return appointments.size();
    }

    @Override
    public Object getItem(int position) {
        return appointments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Appointment appointment = appointments.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.appointment_item, parent, false);
        }

        TextView cat = (TextView) convertView.findViewById(R.id.tv_appt_catalog);
        TextView icon = (TextView) convertView.findViewById(R.id.tv_appt_icon);
        TextView title = (TextView) convertView.findViewById(R.id.tv_appt_title);
        TextView time = (TextView) convertView.findViewById(R.id.tv_appt_time);
        TextView name = (TextView) convertView.findViewById(R.id.tv_patient_name);

        Appointment last = null;
        if (position > 0) last = appointments.get(position - 1);
        if (last != null && appointment.getApptStatus().getId() == last.getApptStatus().getId()) {
            cat.setVisibility(View.GONE);
        } else {
            cat.setVisibility(View.VISIBLE);
            if (appointment.getApptStatus().getId() == ApptStatusEnum.NEW.id()) {
                cat.setText("Available");
            } else if (appointment.getApptStatus().getId() == ApptStatusEnum.BOOKED.id()) {
                cat.setText("Upcoming");
            } else if (appointment.getApptStatus().getId() == ApptStatusEnum.COMPLETED.id()) {
                cat.setText("Completed");
            }
        }
        icon.setBackgroundResource(R.drawable.calendar_blank);
        icon.setText(getDate(appointment.getStartTime()));

        if (appointment.getPatient()!= null) {
            String firstName = appointment.getPatient().getFirstName();
            String lastName = appointment.getPatient().getLastName();
            if (firstName != null && lastName != null) {
                name.setText(firstName + " " + lastName);
            }
        }

        title.setText(appointment.getTitle());
        time.setText(getTime(appointment.getStartTime()) + " - " + getTime(appointment.getEndTime()));
        return convertView;
    }

    private String getTime(String epoch) {
        Date date = new Date(Long.parseLong(epoch));
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return format.format(date);
    }

    private String getDate(String epoch) {
        Date date = new Date(Long.parseLong(epoch));
        DateFormat format = new SimpleDateFormat(" MMM dd");
        format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return format.format(date);
    }


    private String convertEpochTimeToDate(String epoch) {
        Date date = new Date(Long.parseLong(epoch));
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return format.format(date);
    }
}
