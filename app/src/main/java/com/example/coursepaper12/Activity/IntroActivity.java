package com.example.coursepaper12.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coursepaper12.R;
import com.example.coursepaper12.databinding.ActivityIntroBinding;
import com.google.firebase.database.collection.LLRBNode;

public class IntroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);

        if (userEmail != null) {

            startActivity(new Intent(IntroActivity.this,MenuActivity.class));
        } else {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        }
        finish();
    }
    }