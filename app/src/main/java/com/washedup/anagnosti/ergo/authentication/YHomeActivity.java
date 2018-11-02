package com.washedup.anagnosti.ergo.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.createEvent.CreateEventActivity;
import com.washedup.anagnosti.ergo.otherHomePossibilities.ChooseEventForPerspectiveActivity;
import com.washedup.anagnosti.ergo.otherHomePossibilities.EventInvitationsActivity;

public class YHomeActivity extends Activity implements View.OnClickListener{

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.home_log_out).setOnClickListener(this);
        findViewById(R.id.home_create_event).setOnClickListener(this);
        findViewById(R.id.home_event_perspective).setOnClickListener(this);
        findViewById(R.id.home_profile).setOnClickListener(this);
        findViewById(R.id.home_event_invitations).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            //Toast.makeText(this, "HEY HOME SAYS THE USER IS NULL", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(this,YLoginActivity.class));
                finish();

                break;

            case R.id.home_create_event:

                startActivity(new Intent(this,CreateEventActivity.class));

                break;

            case R.id.home_event_perspective:

                startActivity(new Intent(this,ChooseEventForPerspectiveActivity.class));

                break;
                
            case R.id.home_profile:

                Toast.makeText(this, "Profie", Toast.LENGTH_SHORT).show();
                
                break;
                
            case R.id.home_event_invitations:

                startActivity(new Intent(this,EventInvitationsActivity.class));

                break;
        }
    }
}
