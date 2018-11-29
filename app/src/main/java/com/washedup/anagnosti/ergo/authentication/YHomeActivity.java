package com.washedup.anagnosti.ergo.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.createEvent.CreateEventActivity;
import com.washedup.anagnosti.ergo.eventPerspective.Person;
import com.washedup.anagnosti.ergo.otherHomePossibilities.ChooseEventForPerspectiveActivity;
import com.washedup.anagnosti.ergo.otherHomePossibilities.EventInvitationsActivity;

public class YHomeActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "YHomeActivity";
    FirebaseAuth mAuth;
    String privileges;

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

        initFCM();


    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userEmail = mAuth.getCurrentUser().getEmail();
        db.collection("Users").document(userEmail).update("messaging_token",token);
    }


    private void initFCM() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                Log.d(TAG, "initFCM: token: " + deviceToken);
                sendRegistrationToServer(deviceToken);
            }
        });
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

            }else{
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

                if(privileges==null){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Users").document(mAuth.getCurrentUser().getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                Log.d(TAG, "Getting user privileges.");
                                privileges = documentSnapshot.getString("privileges");
                                if(privileges.equals("basic")){
                                    Toast.makeText(YHomeActivity.this, "You don't have the privileges to create an event.", Toast.LENGTH_SHORT).show();
                                }
                                else if(privileges.equals("creator")||privileges.equals("admin")){
                                    startActivity(new Intent(YHomeActivity.this ,CreateEventActivity.class));
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG,"Failure when getting user privileges on create event.");
                        }
                    });
                }else{

                    if(privileges.equals("basic")){
                        Toast.makeText(YHomeActivity.this, "You don't have the privileges to create an event.", Toast.LENGTH_SHORT).show();
                    }
                    else if(privileges.equals("creator")||privileges.equals("admin")){
                        startActivity(new Intent(YHomeActivity.this ,CreateEventActivity.class));
                    }
                }

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
