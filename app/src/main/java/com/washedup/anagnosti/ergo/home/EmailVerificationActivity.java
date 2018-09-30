package com.washedup.anagnosti.ergo.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.washedup.anagnosti.ergo.R;

public class EmailVerificationActivity extends Activity {

    private Button email_verification_btn_verify;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        email_verification_btn_verify = findViewById(R.id.email_verification_btn_verify);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        email_verification_btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailVerificationActivity.this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EmailVerificationActivity.this, "Error in sending verification email. The entered email is not registered in the database.", Toast.LENGTH_SHORT).show();
                            }

                    }
                });
            }
        });
        finish();
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }*/
}
