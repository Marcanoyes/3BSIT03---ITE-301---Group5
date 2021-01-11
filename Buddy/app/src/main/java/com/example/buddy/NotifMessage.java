package com.example.buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NotifMessage extends AppCompatActivity {
    TextView txtmessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_message);
        txtmessage = findViewById(R.id.tv_message);
        Bundle bundle = getIntent().getExtras();
        txtmessage.setText(bundle.getString("message"));

    }
}