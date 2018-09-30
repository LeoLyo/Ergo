package com.washedup.anagnosti.ergo.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.washedup.anagnosti.ergo.R;

public class YNotVerifiedActivity extends Activity implements View.OnClickListener{

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.email_verification_btn_verify).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.email_verification_btn_verify:

                FirebaseUser user = mAuth.getCurrentUser();

                if(!(user.isEmailVerified())){
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mAuth.signOut();
                            Toast.makeText(YNotVerifiedActivity.this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(YNotVerifiedActivity.this, YLoginActivity.class));
                            finish();

                        }
                    });


                }else{
                    mAuth.signOut();
                    Toast.makeText(this, "User is already verified.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, YHomeActivity.class));
                    finish();

                }
                break;

        }
    }
}
