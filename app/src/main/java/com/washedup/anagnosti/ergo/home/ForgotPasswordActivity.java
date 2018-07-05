package com.washedup.anagnosti.ergo.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.washedup.anagnosti.ergo.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText forgot_password_et_email;
    private Button forgot_password_btn_reset_password;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgot_password_et_email = findViewById(R.id.forgot_password_et_email);
        forgot_password_btn_reset_password = findViewById(R.id.forgot_password_btn_reset_password);

        firebaseAuth = FirebaseAuth.getInstance();

        forgot_password_btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = forgot_password_et_email.getText().toString().trim();

                if (email.isEmpty()) {
                    forgot_password_et_email.setError("Please enter your registered email.");
                    forgot_password_et_email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    forgot_password_et_email.setError("Please enter a valid email.");
                    forgot_password_et_email.requestFocus();
                    return;
                }
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Password reset email successfully sent.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Error in sending password reset email. The entered email is not registered in the database.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
