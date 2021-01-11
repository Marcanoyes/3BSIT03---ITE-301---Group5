package com.example.buddy.Notes;

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
import com.example.buddy.R;
import com.example.buddy.Reminders.Reminders;
import com.example.buddy.Adapter.Notes;
import com.example.buddy.Schedule;
import com.example.buddy.Support;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
        DrawerLayout drawer;
        Toolbar toolbar;
        ActionBarDrawerToggle toggle;
        NavigationView navigationView;
        RecyclerView noteList;
        ImageView addNote;
        FirebaseFirestore firestore;
        FirestoreRecyclerAdapter<Notes, NotesViewHolder> notesAdapter;
        Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addNote = findViewById(R.id.img_addnotes);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.draw_layout);
        noteList = findViewById(R.id.notesRecyclerview);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navigationView = findViewById(R.id.nav_view);



        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
        toolbar.setTitle("Notes");

        firestore = FirebaseFirestore.getInstance();
        query = firestore.collection("samplenote").orderBy("title", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Notes> mynote = new FirestoreRecyclerOptions.Builder<Notes>()
                .setQuery(query,Notes.class)
                .build();




        notesAdapter = new FirestoreRecyclerAdapter<Notes, NotesViewHolder>(mynote) {
            @Override
            protected void onBindViewHolder(@NonNull NotesViewHolder notesViewHolder, int i, @NonNull final Notes notes) {
                notesViewHolder.noteTitle.setText(notes.getTitle());
                notesViewHolder.noteContent.setText(notes.getContent());
                final String docId = notesAdapter.getSnapshots().getSnapshot(i).getId();

                notesViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), Details.class);
                        intent.putExtra("title", notes.getTitle());
                        intent.putExtra("notes",notes.getContent());
                        intent.putExtra("noteId",docId);
                        v.getContext().startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout,parent,false);
                return new NotesViewHolder(view);
            }
        };

        noteList.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        noteList.setAdapter(notesAdapter);



        if (savedInstanceState == null){
            toolbar.setTitle("Notes");
            navigationView.setCheckedItem(R.id.nav_note);

        }

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNote.class);
                startActivity(intent);
            }
        });
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
    public class NotesViewHolder extends RecyclerView.ViewHolder{
        TextView noteTitle, noteContent;
        View view;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle =itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.notecontent);
            view = itemView;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        notesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}