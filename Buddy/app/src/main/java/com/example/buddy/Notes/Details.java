package com.example.buddy.Notes;

import android.content.Intent;
import android.os.Bundle;

import com.example.buddy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Details extends AppCompatActivity {
    TextView toolbartitle;
    EditText content, titles;
    ImageView imgIcon;
    Intent data;
    Toolbar toolbar;
    FirebaseFirestore firestore;
    DocumentReference docref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        content = findViewById(R.id.noteupdate);
        titles = findViewById(R.id.titleupdate);
        toolbartitle = findViewById(R.id.toolbartitle);
        toolbartitle.setText(null);
        data = getIntent();

        firestore = firestore.getInstance();

        content.setMovementMethod(new ScrollingMovementMethod());
        content.setText(data.getStringExtra("notes"));
        titles.setText(data.getStringExtra("title"));

        imgIcon = findViewById(R.id.settings);
        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(v.getContext(),v);
                menu.getMenu().add("Save").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                            String mytitle = titles.getText().toString();
                            String mynotes = content.getText().toString();

                            if (mytitle.isEmpty() || mynotes.isEmpty())
                            {
                                Toast.makeText(Details.this,"Empty field",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                docref = firestore.collection("samplenote").document(data.getStringExtra("noteId"));
                                Map<String, Object> note = new HashMap<>();
                                note.put("title", mytitle);
                                note.put("content", mynotes);

                                docref.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                        return false;
                    }
                });

                menu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        docref = firestore.collection("samplenote").document(data.getStringExtra("noteId"));
                        docref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                onBackPressed();
                            }
                        });
                        return false;
                    }
                });
                menu.show();
            }


        });



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
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater minflater = getMenuInflater();
//        minflater.inflate(R.menu.sav_nav,menu);
        return super.onCreateOptionsMenu(menu);
    }
}