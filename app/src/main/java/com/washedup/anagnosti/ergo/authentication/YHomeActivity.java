package com.washedup.anagnosti.ergo.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.home.LoginActivity;

public class YHomeActivity extends Activity implements View.OnClickListener{

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.home_log_out).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){

            startActivity(new Intent(this, YLoginActivity.class));
            finish();

        }else{
            FirebaseUser user = mAuth.getCurrentUser();
            if(!(user.isEmailVerified())){

                startActivity(new Intent(this, YNotVerifiedActivity.class));
                finish();

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_log_out:

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,LoginActivity.class));
                finish();

                break;
        }
    }
}
