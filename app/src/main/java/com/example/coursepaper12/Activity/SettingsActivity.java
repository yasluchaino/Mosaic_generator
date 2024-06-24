package com.example.coursepaper12.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coursepaper12.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    TextView aboutApp;
    FloatingActionButton backBtn ;
    TextView logoutTextView;
    TextView passwordChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", "");
         aboutApp = findViewById(R.id.aboutApp);
        aboutApp.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, AboutAppActivity.class));
            finish();
        });

       backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

         logoutTextView = findViewById(R.id.logout);
        logoutTextView.setOnClickListener(v->{
            this.finishAffinity();
        });

         passwordChange = findViewById(R.id.passwordChange);
        passwordChange.setOnClickListener(v->{
            startActivity(new Intent(SettingsActivity.this, ResetPassword.class));
        });

    }

}
