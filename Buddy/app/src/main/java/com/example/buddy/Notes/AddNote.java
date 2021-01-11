package com.example.buddy.Notes;

import android.os.Bundle;

import com.example.buddy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AddNote extends AppCompatActivity {

    EditText noteTitle, noteConent;
    Toolbar toolbar;
    TextView toolbartitle;
    DocumentReference docref;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        noteTitle = findViewById(R.id.editTitle);
        noteConent = findViewById(R.id.editnotes);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbartitle = findViewById(R.id.toolbartitle);
        toolbartitle.setText(null);

        firestore = FirebaseFirestore.getInstance();


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }

        if(item.getItemId() == R.id.save_nav)
        {
            String mytitle = noteTitle.getText().toString();
            String mynotes = noteConent.getText().toString();

            if (mytitle.isEmpty() || mynotes.isEmpty())
            {
                Toast.makeText(AddNote.this,"Empty field",Toast.LENGTH_SHORT).show();
            }
            else
                {
                docref = firestore.collection("samplenote").document();
                Map<String, Object> note = new HashMap<>();
                note.put("title", mytitle);
                note.put("content", mynotes);

                docref.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater minflater = getMenuInflater();
        minflater.inflate(R.menu.sav_nav,menu);
        return super.onCreateOptionsMenu(menu);
    }

}