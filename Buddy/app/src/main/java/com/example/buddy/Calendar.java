package com.example.buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.buddy.Notes.MainActivity;
import com.example.buddy.Reminders.Reminders;
import com.google.android.material.navigation.NavigationView;

public class Calendar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.draw_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navigationView = findViewById(R.id.nav_view);

        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
        toolbar.setTitle("Calendar");

        if (savedInstanceState == null) {
            toolbar.setTitle("Calendar");
            navigationView.setCheckedItem(R.id.nav_calendar);

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_note:
                startActivity(new Intent(this,MainActivity.class));
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

}
