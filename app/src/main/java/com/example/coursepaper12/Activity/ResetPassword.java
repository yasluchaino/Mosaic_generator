package com.example.coursepaper12.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.coursepaper12.R;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;
    AppCompatButton resetPasswordBtn;
    EditText userEdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_reser_password);
        userEdt = findViewById(R.id.userEdt);


        resetPasswordBtn = findViewById(R.id.resetPassword);
        resetPasswordBtn.setOnClickListener(v->
        { String email = userEdt.getText().toString();
            resetPassword(email);
        });
    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPassword.this, "Ссылка для сброса пароля отправлена\nна ваш адрес электронной почты", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ResetPassword.this, "Ошибка!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}