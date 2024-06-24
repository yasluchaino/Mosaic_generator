package com.example.coursepaper12.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.coursepaper12.databinding.ActivitySignup2Binding;

public class SignupActivity extends BaseActivity {
ActivitySignup2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignup2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();

    }

    private void setVariable() {
        binding.signupBtn.setOnClickListener(
                v -> {
                    String email = binding.userEdt.getText().toString();
                    String password = binding.passwordEdt.getText().toString();
                    if (password.length() < 8) {
                        Toast.makeText(SignupActivity.this, "Пароль должен содержать не менее 8 символов", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    nAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, task -> {
                        if (task.isComplete()) {
                            Log.i(TAG, "onComplete: ");
                            String userEmail = email;
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_email", userEmail);
                            editor.apply();
                            startActivity(new Intent(SignupActivity.this, MenuActivity.class));
                            finish();
                        } else {
                            Log.i(TAG, "failure: " + task.getException());
                            Toast.makeText(SignupActivity.this, "Ошибка идентификации", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
        binding.loginView.setOnClickListener(v-> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }
}