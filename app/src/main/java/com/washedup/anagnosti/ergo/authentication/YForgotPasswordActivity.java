package com.washedup.anagnosti.ergo.authentication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.washedup.anagnosti.ergo.R;

public class YForgotPasswordActivity extends Activity implements View.OnClickListener{

    private EditText forgot_password_et_email;
    private ProgressBar forgot_password_pb;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        findViewById(R.id.forgot_password_btn_back_to_login).setOnClickListener(this);
        findViewById(R.id.forgot_password_btn_reset_password).setOnClickListener(this);

        forgot_password_et_email = findViewById(R.id.forgot_password_et_email);
        forgot_password_pb = findViewById(R.id.forgot_password_pb);
        forgot_password_pb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.dirtierWhite), PorterDuff.Mode.MULTIPLY);

        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.forgot_password_btn_back_to_login:

                startActivity(new Intent(this, YLoginActivity.class));
                finish();

                break;

            case R.id.forgot_password_btn_reset_password:

                sendPasswordResetEmail();

                break;
        }
    }

    private void sendPasswordResetEmail() {
        final String email = forgot_password_et_email.getText().toString().trim();
        if (email.isEmpty()) {
            forgot_password_et_email.setError("Email is necessary");
            forgot_password_et_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            forgot_password_et_email.setError("Please enter a valid email");
            forgot_password_et_email.requestFocus();
            return;
        }

        forgot_password_pb.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                forgot_password_pb.setVisibility(View.GONE);
                if(task.isSuccessful()){

                    Toast.makeText(YForgotPasswordActivity.this, "Password reset email successfully sent.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(YForgotPasswordActivity.this, YLoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(YForgotPasswordActivity.this, "Error in sending password reset email. The entered email is not registered in the database.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
