package com.example.coursepaper12.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.coursepaper12.R;
import com.example.coursepaper12.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity {
ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
    }
    private void setVariable() {
        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.userEdt.getText().toString();
            String password = binding.passwordEdt.getText().toString();

            if (!email.isEmpty()&&!password.isEmpty())
            {
                nAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful())
                    {
                        String userEmail = email;
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_email", userEmail);
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Ошибка идентификации", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(LoginActivity.this,"Введите логин и/или пароль", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        binding.regView.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });
        binding.forgotPassword.setOnClickListener(v->{
                startActivity(new Intent(LoginActivity.this, ResetPassword.class));
        });
    }
}