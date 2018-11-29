package com.washedup.anagnosti.ergo.authentication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.washedup.anagnosti.ergo.R;

public class YNotVerifiedActivity extends Activity implements View.OnClickListener{

    ProgressBar email_verification_pb;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.email_verification_btn_verify).setOnClickListener(this);
        findViewById(R.id.email_verification_btn_back_to_login).setOnClickListener(this);

        email_verification_pb = findViewById(R.id.email_verification_pb);
        email_verification_pb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.dirtierWhite), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.email_verification_btn_verify:

                sendVerificationEmail();

                break;
            case R.id.email_verification_btn_back_to_login:

                startActivity(new Intent(this, YLoginActivity.class));
                finish();

                break;

        }
    }

    private void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        email_verification_pb.setVisibility(View.VISIBLE);
        if (user != null) {
            if(!(user.isEmailVerified())){
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        email_verification_pb.setVisibility(View.GONE);
                        mAuth.signOut();
                        Toast.makeText(YNotVerifiedActivity.this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(YNotVerifiedActivity.this, YLoginActivity.class));
                        finish();

                    }
                });


            }else{
                email_verification_pb.setVisibility(View.GONE);
                mAuth.signOut();
                Toast.makeText(YNotVerifiedActivity.this, "User is already verified.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(YNotVerifiedActivity.this, YHomeActivity.class));
                finish();

            }
        }else{
            email_verification_pb.setVisibility(View.GONE);
            mAuth.signOut();
            Toast.makeText(YNotVerifiedActivity.this, "[ERROR] User is null.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(YNotVerifiedActivity.this, YHomeActivity.class));
            finish();
        }
    }
}
