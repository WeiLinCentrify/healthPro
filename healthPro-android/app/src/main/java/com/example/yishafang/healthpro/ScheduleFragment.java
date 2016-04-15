package com.example.yishafang.healthpro;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.yishafang.healthpro.API.AppointmentsAPI;
import com.example.yishafang.healthpro.Model.Appointment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

import static com.example.yishafang.healthpro.Constants.baseAPI;

/**
 * @author yishafang on 11/18/15.
 */
public class ScheduleFragment extends Fragment {
    public static final String TAG = "ScheduleFragment";

    CalendarView calendarView;
    TextView date;
    TextView startTime;
    TextView endTime;
    Button startTimeButton;
    Button endTimeButton;
    Button createButton;

    private int hour;
    private int minute;
    private String start_hour;
    private String start_minute;
    private String end_hour;
    private String end_minute;
    private String dateChosen;

    private UserSessionManager userSessionManager;

    public static ScheduleFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(TAG, page);
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        scheduleFragment.setArguments(args);
        return scheduleFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userSessionManager = new UserSessionManager(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);

        date = (TextView) v.findViewById(R.id.date);
        startTime = (TextView) v.findViewById(R.id.start_time);
        endTime = (TextView) v.findViewById(R.id.end_time);

        startTimeButton = (Button) v.findViewById(R.id.select_start_time);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // set start time
                        start_hour = String.valueOf(selectedHour);
                        start_minute = String.valueOf(selectedMinute);

                        String startTimeChosen = updateTime(selectedHour, selectedMinute);
                        String show = String.format(getResources().getString(R.string.start_time), startTimeChosen);
                        startTime.setText(show);                    }
                }, hour, minute, false);

                mTimePicker.setTitle("Select Start Time");
                mTimePicker.show();
            }
        });

        endTimeButton = (Button) v.findViewById(R.id.select_end_time);
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // set end time
                        end_hour = String.valueOf(selectedHour);
                        end_minute = String.valueOf(selectedMinute);

                        String endTimeChosen = updateTime(selectedHour, selectedMinute);
                        String show = String.format(getResources().getString(R.string.end_time), endTimeChosen);
                        endTime.setText(show);
                    }
                }, hour, minute, DateFormat.is24HourFormat(getActivity()));

                mTimePicker.setTitle("Select End Time");
                mTimePicker.show();
            }
        });

        calendarView = (CalendarView) v.findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int mon = month + 1;
                dateChosen = year + "-" + mon + "-" + dayOfMonth;
                String dateShow = String.format(getResources().getString(R.string.date), dateChosen);
                date.setText(dateShow);
            }
        });

        createButton = (Button) v.findViewById(R.id.create);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start_hour != null && start_minute != null && end_hour != null && end_minute != null && dateChosen!= null) {
                    try {
                        create();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cleanField();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setMessage("Please chooser your date and time")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        return v;
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private String updateTime(int hours, int mins) {
        String timeSet;
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes;
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        return new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
    }

    private void create() throws Exception{
        String startTime = dateChosen + "T" + start_hour + ":" + start_minute + ":00"+"-08:00";
        String endTime = dateChosen + "T" + end_hour + ":" + end_minute + ":00"+"-08:00";
        Log.w(TAG, "start time: " + startTime + " end time: " + endTime);
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("startTime", convertDateTimeToEpoch(startTime));
            requestBody.put("endTime", convertDateTimeToEpoch(endTime));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TypedInput typedInput = new TypedByteArray("application/json", requestBody.toString().getBytes());

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(baseAPI).build();

//        Intent intent = getActivity().getIntent();
        int doctorId = userSessionManager.getUserId();
        Log.w(TAG + "dId", String.valueOf(doctorId));

        AppointmentsAPI api = restAdapter.create(AppointmentsAPI.class);
        api.createTimeSlot(doctorId, typedInput, new Callback<Appointment>() {
            @Override
            public void success(Appointment appointment, Response response) {
                Log.w(TAG, "Create time slot successfully!!");

                Context context = getActivity().getApplicationContext();
                CharSequence text = "Create time slot successfully!";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w(TAG + "=[", error);
                error.printStackTrace();
            }
        });
    }

    private long convertDateTimeToEpoch(String dateTime) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = simpleDateFormat.parse(dateTime);
        long epoch = date.getTime();
        Log.w(TAG, String.valueOf(epoch));
        return epoch;
    }

    private void cleanField() {
        dateChosen = null;
        start_hour = null;
        start_minute = null;
        end_hour = null;
        end_minute = null;
        date.setText("");
        startTime.setText(getResources().getString(R.string.start_time_hint));
        endTime.setText(getResources().getString(R.string.end_time_hint));
    }
}
