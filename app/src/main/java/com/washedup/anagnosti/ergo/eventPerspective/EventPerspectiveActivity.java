package com.washedup.anagnosti.ergo.eventPerspective;

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
import android.widget.ImageView;
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
    private ImageView iv;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Person user;
    private DocumentReference userRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_perspective);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loadUser();
        Intent intent = getIntent();
        currentEvent = intent.getParcelableExtra("current_event");
        // Toast.makeText(this, "TESTING EVENT INFO: " + currentEvent.toString(), Toast.LENGTH_SHORT).show();

        Toolbar toolbar = findViewById(R.id.activity_event_perspective_toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.activity_event_perspective_drawer_layout);
        NavigationView navigationView = findViewById(R.id.activity_event_perspective_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        name = findViewById(R.id.nav_header_name);
        role = findViewById(R.id.nav_header_role);
        iv = findViewById(R.id.nav_header_iv);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                    new MyObligationsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_my_obligations);
        }

        String fullName = user.getNickname().isEmpty() ? user.getFirstName() + " " + user.getLastName() : user.getFirstName() + " " + user.getNickname() + " " + user.getLastName();
        name.setText(fullName.trim());
        role.setText(user.getRole());

        int dimensionW = iv.getWidth();
        int dimensionH = iv.getHeight();
        if(!user.getProfileImageUrl().isEmpty()){
            Picasso.with(this)
                    .load(user.getProfileImageUrl())
                    .resize(dimensionW,dimensionH)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(iv);
        }
    }

    //WE GOTTA FINISH THE JOIN EVENT PART
    private void loadUser() {
        userRef = db.collection("Users").document().collection("people").document(mAuth.getCurrentUser().getEmail());

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        user = doc.toObject(Person.class);
                        Log.d(TAG, "User data: " + doc.getData());

                    }else{
                        Log.d(TAG,"No such user.");
                    }
                }else{
                    Log.d(TAG,"get failed with ", task.getException());
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
                Toast.makeText(this, "Calling Zlatko...", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
