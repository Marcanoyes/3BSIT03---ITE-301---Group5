package com.example.buddy.Reminders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.buddy.Alarmbot;
import com.example.buddy.Notes.AddNote;
import com.example.buddy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddReminders extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Button btn_time, btn_date, btn_done;
    EditText remtitle;
    String timeTonotify;
    DocumentReference docref;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminders);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        remtitle = findViewById(R.id.remindertitle);
        btn_time = findViewById(R.id.btn_time);
        btn_date = findViewById(R.id.btn_date);
        btn_done = findViewById(R.id.btn_done);
        btn_time.setOnClickListener(this);
        btn_date.setOnClickListener(this);
        btn_done.setOnClickListener(this);

        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_time) {
            selectTime();
        }
        else if (v == btn_date) {
            selectDate();
        }
        else if (v == btn_done) {
            Intent intent = new Intent(getApplicationContext(),Reminders.class);
            startActivity(intent);
            submit();

        }

    }

    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeTonotify = hourOfDay + ":" + minute;
                btn_time.setText(FormatTime(hourOfDay, minute));
                setAlarm(calendar);
            }
            public void setAlarm(Calendar calendar)
            {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(AddReminders.this, Alarmbot.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddReminders.this, 1, intent, 0);
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 1);
                }
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }




        }, hour, minute, false);
        timePickerDialog.show();

    }

    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }
    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                btn_date.setText(currentdate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    private void submit()
        {
            String mytitle = remtitle.getText().toString();
            String myttime = btn_time.getText().toString();
            String mydate = btn_date.getText().toString();

            docref = firestore.collection("samplereminder").document();
            Map<String, Object> reminder = new HashMap<>();
            reminder.put("title", mytitle);
            reminder.put("time", myttime);
            reminder.put("date", mydate);

            docref.set(reminder).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    onBackPressed();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });




        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
