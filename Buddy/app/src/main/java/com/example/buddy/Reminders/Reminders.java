package com.example.buddy.Reminders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buddy.Calendar;
import com.example.buddy.Notes.MainActivity;
import com.example.buddy.R;
import com.example.buddy.Adapter.AdapterR;
import com.example.buddy.Schedule;
import com.example.buddy.Support;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Reminders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    RecyclerView noteList;
    ImageView addRem;
    FirebaseFirestore firestore;
    FirestoreRecyclerAdapter<AdapterR, Reminders.ReminderViewHolder> remAdapter;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        addRem = findViewById(R.id.img_addrem);

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.draw_layout);
        noteList = findViewById(R.id.remRecyclerview);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navigationView = findViewById(R.id.nav_view);

        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
        toolbar.setTitle("Reminders");



        firestore = FirebaseFirestore.getInstance();
        query = firestore.collection("samplereminder").orderBy("date", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<AdapterR> rem = new FirestoreRecyclerOptions.Builder<AdapterR>()
                .setQuery(query, AdapterR.class)
                .build();

        remAdapter = new FirestoreRecyclerAdapter<AdapterR, ReminderViewHolder>(rem) {
            @Override
            protected void onBindViewHolder(@NonNull ReminderViewHolder reminderViewHolder, int i, @NonNull final AdapterR adapterR) {
                reminderViewHolder.btn_date.setText(adapterR.getDate());
                reminderViewHolder.btn_time.setText(adapterR.getTime());
                reminderViewHolder.remtitle.setText(adapterR.getTitle());
                final String docId = remAdapter.getSnapshots().getSnapshot(i).getId();
                reminderViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailsRem.class);
                        intent.putExtra("title", adapterR.getTitle());
                        intent.putExtra("date", adapterR.getDate());
                        intent.putExtra("time", adapterR.getTime());
                        intent.putExtra("noteId",docId);
                        v.getContext().startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_layout,parent,false);
                return new ReminderViewHolder(view);
            }
        };
        noteList.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        noteList.setAdapter(remAdapter);

        if (savedInstanceState == null){
            toolbar.setTitle("Reminders");
            navigationView.setCheckedItem(R.id.nav_reminder);
        }


        addRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddReminders.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_note:
                startActivity(new Intent(this, MainActivity.class));
                toolbar.setTitle("Notes");
                break;
            case R.id.nav_reminder:
                startActivity(new Intent(this, Reminders.class));
                toolbar.setTitle("Reminders");
                break;
            case R.id.nav_calendar:
                startActivity(new Intent(this, Calendar.class));
                toolbar.setTitle("Calendar");
                break;
            case R.id.nav_support:
                startActivity(new Intent(this, Support.class));
                toolbar.setTitle("Support");
                break;
            case R.id.nav_schedule:
                startActivity(new Intent(this, Schedule.class));
                toolbar.setTitle("Schedule");
                break;
        }


        return false;
    }
    public class ReminderViewHolder extends RecyclerView.ViewHolder{
        TextView btn_time, btn_date,remtitle;
        View view;
        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_time =itemView.findViewById(R.id.remtime);
            btn_date = itemView.findViewById(R.id.remdate);
            remtitle = itemView.findViewById(R.id.remTitle);
            view = itemView;

        }
    }

    @Override
    public void onBackPressed() {
        toolbar.setTitle("Notes");
        super.onBackPressed();
    }

    protected void onStart() {
        super.onStart();
        remAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
