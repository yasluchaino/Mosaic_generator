package com.example.coursepaper12.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coursepaper12.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {
    FirebaseAuth nAuth;
    FirebaseDatabase database;
    public String TAG = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        nAuth = FirebaseAuth.getInstance();

        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
    }
}
