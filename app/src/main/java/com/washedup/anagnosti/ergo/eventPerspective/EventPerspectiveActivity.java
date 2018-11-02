package com.washedup.anagnosti.ergo.eventPerspective;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.transformations.CircleTransform;

public class EventPerspectiveActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "EPerspectiveActivity";
    private DrawerLayout drawer;
    private Event currentEvent;
    private TextView name, role;
    private ImageView iv, fragment_call_superior_iv_pop_up;
    private EditText fragment_call_superior_superior_name_pop_up;
    private Button fragment_call_superior_button_call_pop_up, fragment_call_superior_button_message_pop_up;
    private ProgressBar fragment_call_superior_pb_pop_up;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Person user;
    private Dialog popUpDialog;
    private DocumentReference userRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_perspective);
        
        popUpDialog = new Dialog(this);
        
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        currentEvent = intent.getParcelableExtra("current_event");
        // Toast.makeText(this, "TESTING EVENT INFO: " + currentEvent.toString(), Toast.LENGTH_SHORT).show();
        loadUser(currentEvent.getEvent_id());
        Toolbar toolbar = findViewById(R.id.activity_event_perspective_toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.activity_event_perspective_drawer_layout);
        NavigationView navigationView = findViewById(R.id.activity_event_perspective_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                    new MyObligationsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_my_obligations);
        }

    }

    //WE GOTTA FINISH THE JOIN EVENT PART
    private void loadUser(String eventId) {
        userRef = db.collection("events").document(eventId).collection("people").document(mAuth.getCurrentUser().getEmail());

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        //Toast.makeText(EventPerspectiveActivity.this, "YES", Toast.LENGTH_SHORT).show();
                        user = doc.toObject(Person.class);
                        String fullName = (user.getNickname()!=null && user.getNickname().isEmpty()) ? user.getFirstName() + " " + user.getLastName() : user.getFirstName() + " " + user.getNickname() + " " + user.getLastName();

                        name = findViewById(R.id.nav_header_name);
                        role = findViewById(R.id.nav_header_role);
                        name.setText(fullName.trim());
                        role.setText(user.getRole());

                        iv = findViewById(R.id.nav_header_iv);
                        int dimensionW = iv.getWidth();
                        int dimensionH = iv.getHeight();
                        if(user.getProfileImageUrl()!=null && !user.getProfileImageUrl().isEmpty()){
                            Picasso.with(EventPerspectiveActivity.this)
                                    .load(user.getProfileImageUrl())
                                    .resize(dimensionW,dimensionH)
                                    .centerCrop()
                                    .into(iv);
                        }

                        Log.d(TAG, "User data: " + doc.getData());

                    }else{
                        Log.d(TAG,"No such user.");
                        //Toast.makeText(EventPerspectiveActivity.this, "NO", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d(TAG,"get failed with ", task.getException());
                    //Toast.makeText(EventPerspectiveActivity.this, "MAYBE", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public Person getCurrentUser(){
        return this.user;
    }
    public Event getCurrentEvent(){
        return this.currentEvent;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_about_event:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                        new AboutEventFragment()).commit();
                break;

            case R.id.nav_assign_obligations:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                        new AssignObligationsFragment()).commit();
                break;

            case R.id.nav_break_centre:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                        new BreakCentreFragment()).commit();
                break;
                
            case R.id.nav_event_schedule:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                        new EventScheduleFragment()).commit();
                break;

            case R.id.nav_list_of_people:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                        new ListOfPeopleFragment()).commit();
                break;

            case R.id.nav_my_obligations:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                        new MyObligationsFragment()).commit();
                break;

            case R.id.nav_contact_superior:
                
                /*popUpDialog.setContentView(R.layout.fragment_call_superior);

                fragment_call_superior_iv_pop_up = popUpDialog.findViewById(R.id.fragment_call_superior_iv_pop_up);
                fragment_call_superior_superior_name_pop_up = popUpDialog.findViewById(R.id.fragment_call_superior_superior_name_pop_up);
                fragment_call_superior_button_call_pop_up = popUpDialog.findViewById(R.id.fragment_call_superior_button_call_pop_up);
                fragment_call_superior_button_message_pop_up = popUpDialog.findViewById(R.id.fragment_call_superior_button_message_pop_up);
                fragment_call_superior_pb_pop_up = popUpDialog.findViewById(R.id.fragment_call_superior_pb_pop_up);
                */

                Toast.makeText(this, "Calling Zlatko..", Toast.LENGTH_SHORT).show();
                
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
