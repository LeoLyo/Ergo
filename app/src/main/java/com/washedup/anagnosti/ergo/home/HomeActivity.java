package com.washedup.anagnosti.ergo.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.createEvent.CreateEventActivity;

public class HomeActivity extends Activity {

    public static final int RC_SIGN_IN = 1;
    private static final String TAG = "HomeActivityTag";


    private CardView cv, home_create_event, home_event_perspective, home_profile, home_join_event;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        initializeAuthListener();
        cv = findViewById(R.id.home_log_out);
        home_create_event = findViewById(R.id.home_create_event);
        home_event_perspective = findViewById(R.id.home_event_perspective);
        home_join_event = findViewById(R.id.home_join_event);
        home_profile = findViewById(R.id.home_profile);

        home_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cintent = new Intent(getApplicationContext(), CreateEventActivity.class);
                startActivity(cintent);
            }
        });

    }


    private void initializeAuthListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    if (user.isEmailVerified()) {
                        CardView cv = findViewById(R.id.home_log_out);

                        cv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                firebaseAuth.signOut();
                            }
                        });
                    } else {
                        Intent intent = new Intent(getApplicationContext(), EmailVerificationActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK)
                Toast.makeText(this, "Successfully signed in.", Toast.LENGTH_SHORT).show();
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
