package com.washedup.anagnosti.ergo.eventPerspective;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.createEvent.CreateEventActivity;
import com.washedup.anagnosti.ergo.transformations.CircleTransform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EventPerspectiveActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "EPerspectiveActivity";
    private DrawerLayout drawer;
    private Event currentEvent;
    private TextView name, role;
    private ImageView iv;
    private ProgressBar fragment_call_superior_pb_pop_up;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Person user;
    private Dialog popUpDialog, popUpDialogReview, popUpDialogINP;
    private Person superior;
    private NavigationView navigationView;
    private int here = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        here = -1;

        setContentView(R.layout.activity_event_perspective);
        popUpDialog = new Dialog(this);
        popUpDialogReview = new Dialog(this);
        popUpDialogINP = new Dialog(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        currentEvent = intent.getParcelableExtra("current_event");
        // Toast.makeText(this, "TESTING EVENT INFO: " + currentEvent.toString(), Toast.LENGTH_SHORT).show();
        loadUser(currentEvent.getEvent_id());
        Toolbar toolbar = findViewById(R.id.activity_event_perspective_toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.activity_event_perspective_drawer_layout);
        navigationView = findViewById(R.id.activity_event_perspective_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                    new MyObligationsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_my_obligations);
        }

    }

    //WE GOTTA FINISH THE JOIN EVENT PART ???
    private void loadUser(final String eventId) {

        DocumentReference userRef = db.collection("events").document(eventId).collection("people").document(mAuth.getCurrentUser().getEmail());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        //Toast.makeText(EventPerspectiveActivity.this, "YES", Toast.LENGTH_SHORT).show();
                        user = doc.toObject(Person.class);
                        String fullName = (user.getNickname() != null && user.getNickname().isEmpty()) ? user.getFirstName() + " " + user.getLastName() : user.getFirstName() + " " + user.getNickname() + " " + user.getLastName();

                        name = findViewById(R.id.nav_header_name);
                        role = findViewById(R.id.nav_header_role);
                        name.setText(fullName.trim());
                        role.setText(user.getRole());

                        iv = findViewById(R.id.nav_header_iv);
                        int dimensionW = iv.getWidth();
                        int dimensionH = iv.getHeight();
                        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
                            Picasso.with(EventPerspectiveActivity.this)
                                    .load(user.getProfileImageUrl())
                                    .resize(dimensionW, dimensionH)
                                    .centerCrop()
                                    .into(iv);
                        }

                        Log.d(TAG, "User data: " + doc.getData());

                        if (user.getSuperior().equals("nobody@nonono.com")) {
                            Menu navMenu = navigationView.getMenu();
                            navMenu.findItem(R.id.nav_contact_superior).setVisible(false);
                        } else {
                            Menu navMenu = navigationView.getMenu();
                            navMenu.findItem(R.id.nav_contact_superior).setVisible(true);

                            DocumentReference superiorRef = db.collection("events").document(eventId).collection("people").document(user.getSuperior());
                            superiorRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot sDoc = task.getResult();
                                        if (sDoc.exists()) {

                                            superior = sDoc.toObject(Person.class);

                                            Log.d(TAG, "Superior data: " + sDoc.getData());

                                        } else {
                                            Log.d(TAG, "No such superior.");
                                        }
                                    } else {
                                        Log.d(TAG, "get superior failed with ", task.getException());
                                    }
                                }
                            });

                        }

                        if (user.getSubordinates() != null && !user.getSubordinates().isEmpty()) {
                            Menu navMenu = navigationView.getMenu();
                            navMenu.findItem(R.id.nav_assign_obligations).setVisible(true);
                        } else {
                            Menu navMenu = navigationView.getMenu();
                            navMenu.findItem(R.id.nav_assign_obligations).setVisible(false);
                        }

                        if (user.getPrivileges().equals("creator") || user.getPrivileges().equals("admin")) {
                            Menu navMenu = navigationView.getMenu();
                            navMenu.findItem(R.id.nav_invite_new_person).setVisible(true);
                        } else {
                            Menu navMenu = navigationView.getMenu();
                            navMenu.findItem(R.id.nav_invite_new_person).setVisible(false);
                        }

                    } else {
                        Log.d(TAG, "No such user.");
                        //Toast.makeText(EventPerspectiveActivity.this, "NO", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    //Toast.makeText(EventPerspectiveActivity.this, "MAYBE", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public Person getCurrentUser() {
        return this.user;
    }

    public Event getCurrentEvent() {
        return this.currentEvent;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public int getHere() {
        return this.here;
    }

    public void setHere(int here) {
        this.here = here;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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

                if (superior != null) {

                    if (superior.getInvitation_accepted()) {

                        final String sCombinedName = superior.getFirstName() + " " + superior.getNickname() + " " + superior.getLastName();

                        popUpDialog.setContentView(R.layout.fragment_call_superior);

                        ImageView fragment_call_superior_iv_pop_up = popUpDialog.findViewById(R.id.fragment_call_superior_iv_pop_up);
                        EditText fragment_call_superior_superior_name_pop_up = popUpDialog.findViewById(R.id.fragment_call_superior_superior_name_pop_up);
                        Button fragment_call_superior_button_call_pop_up = popUpDialog.findViewById(R.id.fragment_call_superior_button_call_pop_up);
                        Button fragment_call_superior_button_message_pop_up = popUpDialog.findViewById(R.id.fragment_call_superior_button_message_pop_up);
                        fragment_call_superior_pb_pop_up = popUpDialog.findViewById(R.id.fragment_call_superior_pb_pop_up);

                        if (superior.getProfileImageUrl() != null && !superior.getProfileImageUrl().isEmpty()) {
                            Picasso.with(EventPerspectiveActivity.this)
                                    .load(superior.getProfileImageUrl())
                                    .fit()
                                    .centerCrop()
                                    .into(fragment_call_superior_iv_pop_up);
                        }

                        fragment_call_superior_superior_name_pop_up.setText(sCombinedName);
                        fragment_call_superior_button_call_pop_up.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fragment_call_superior_pb_pop_up.setVisibility(View.VISIBLE);
                                Intent callIntent = new Intent(Intent.ACTION_VIEW);
                                callIntent.setData(Uri.parse("tel:" + superior.getPhoneNumber()));
                                EventPerspectiveActivity.this.startActivity(callIntent);
                                fragment_call_superior_pb_pop_up.setVisibility(View.GONE);
                                popUpDialog.dismiss();
                            }
                        });

                        fragment_call_superior_button_message_pop_up.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fragment_call_superior_pb_pop_up.setVisibility(View.VISIBLE);
                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.setData(Uri.parse("sms:" + superior.getPhoneNumber()));
                                EventPerspectiveActivity.this.startActivity(sendIntent);
                                fragment_call_superior_pb_pop_up.setVisibility(View.GONE);
                                popUpDialog.dismiss();
                            }
                        });
                        Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        popUpDialog.show();

                    } else {
                        Toast.makeText(this, "Superior has not accepted the event invitation yet.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Superior not loaded yet, please try again in a few seconds..", Toast.LENGTH_SHORT).show();
                }


                /*
                 *



                 * */

                //Toast.makeText(this, "Calling Zlatko..", Toast.LENGTH_SHORT).show();

                break;

            case R.id.nav_review_app:

                popUpDialogReview.setContentView(R.layout.fragment_review_app);

                final EditText fragment_review_app_et = popUpDialogReview.findViewById(R.id.fragment_review_app_et);
                Button fragment_review_app_btn = popUpDialogReview.findViewById(R.id.fragment_review_app_btn);
                final TextView fragment_review_app_tv_char_counter = popUpDialogReview.findViewById(R.id.fragment_review_app_tv_char_counter);
                fragment_review_app_et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String count = fragment_review_app_et.getText().toString();
                        fragment_review_app_tv_char_counter.setText(Integer.toString(count.length()));

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


                fragment_review_app_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (fragment_review_app_et.getText().toString().trim().isEmpty()) {
                            Toast.makeText(EventPerspectiveActivity.this, "No review entered. Please write a review before submitting.", Toast.LENGTH_SHORT).show();
                        } else if (fragment_review_app_et.getText().toString().length() >= 500) {
                            Toast.makeText(EventPerspectiveActivity.this, "Character count is exceeding the maximum number of 500.", Toast.LENGTH_SHORT).show();
                        } else {
                            db.collection("events").document(currentEvent.getEvent_id()).update(
                                    "live_reviews", FieldValue.arrayUnion(fragment_review_app_et.getText().toString().trim())
                            );
                            Toast.makeText(EventPerspectiveActivity.this, "Review successfully sent!", Toast.LENGTH_SHORT).show();
                            popUpDialogReview.dismiss();
                        }
                    }
                });
                Objects.requireNonNull(popUpDialogReview.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialogReview.show();
                break;

            case R.id.nav_invite_new_person:

                popUpDialogINP.setContentView(R.layout.fragment_invite_new_person);

                final EditText fragment_invite_new_person_email = popUpDialogINP.findViewById(R.id.fragment_invite_new_person_email);
                final EditText fragment_invite_new_person_role = popUpDialogINP.findViewById(R.id.fragment_invite_new_person_role);
                final EditText fragment_invite_new_person_superior_email = popUpDialogINP.findViewById(R.id.fragment_invite_new_person_superior_email);
                final EditText fragment_invite_new_person_subordinates = popUpDialogINP.findViewById(R.id.fragment_invite_new_person_subordinates);
                ImageButton fragment_invite_new_person_btn = popUpDialogINP.findViewById(R.id.fragment_invite_new_person_btn);
                final ProgressBar fragment_invite_new_person_pb = popUpDialogINP.findViewById(R.id.fragment_invite_new_person_pb);
                fragment_invite_new_person_pb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.dirtierWhite), PorterDuff.Mode.MULTIPLY);


                fragment_invite_new_person_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String email = fragment_invite_new_person_email.getText().toString().trim();
                        String role = fragment_invite_new_person_role.getText().toString().trim();
                        final String superior_email = fragment_invite_new_person_superior_email.getText().toString().trim();
                        String t_subordinates = fragment_invite_new_person_subordinates.getText().toString().trim();

                        if (email.isEmpty()) {
                            fragment_invite_new_person_email.setError("Please write the email of the invitee.");
                            fragment_invite_new_person_email.requestFocus();
                        } else if (role.isEmpty()) {
                            fragment_invite_new_person_role.setError("Please write the role of the invitee.");
                            fragment_invite_new_person_role.requestFocus();
                        } else if (superior_email.isEmpty()) {
                            fragment_invite_new_person_email.setError("Please write the name of the invitee's superior.");
                            fragment_invite_new_person_email.requestFocus();
                        } else {
                            int c = 0;
                            for (int iu = 0; iu < currentEvent.getInvited_users().size(); iu++) {
                                if (currentEvent.getInvited_users().get(iu).equals(email)) {
                                    c = 67;
                                    break;
                                }
                            }
                            if (c == 0) {
                                for (int au = 0; au < currentEvent.getAccepted_users().size(); au++) {
                                    if (currentEvent.getAccepted_users().get(au).equals(email)) {
                                        c = 89;
                                        break;
                                    }
                                }
                            }
                            if (c == 67) {
                                fragment_invite_new_person_email.setError("A person with this email has already been invited to the event.");
                                fragment_invite_new_person_email.requestFocus();
                            } else if (c == 89) {
                                fragment_invite_new_person_email.setError("A person with this email has already accepted the event invitation.");
                                fragment_invite_new_person_email.requestFocus();
                            } else {
                                if (!(t_subordinates.isEmpty())) {
                                    String[] parser = t_subordinates.split(" ");
                                    ArrayList<String> subordinates = new ArrayList<>(Arrays.asList(parser));
                                    int pc = -1;
                                    for (int p = 0; p < subordinates.size(); p++) {
                                        if (!Patterns.EMAIL_ADDRESS.matcher(subordinates.get(p)).matches()) {
                                            pc = p;
                                            break;
                                        }
                                    }

                                    if (pc != -1) {
                                        fragment_invite_new_person_subordinates.setError(subordinates.get(pc) + " is not a valid email.");
                                        fragment_invite_new_person_subordinates.requestFocus();
                                    } else {
                                    /*int new_c = subordinates.size();
                                    Boolean[] new_c_pointer = new Boolean[subordinates.size()];
                                    Arrays.fill(new_c_pointer, Boolean.FALSE);
                                    for (int s = 0; s < subordinates.size(); s++) {
                                        for (int i = 0; i < currentEvent.getInvited_users().size(); i++) {
                                            if (currentEvent.getInvited_users().get(i).equals(subordinates.get(s))) {
                                                new_c--;
                                                new_c_pointer[s] = true;
                                                break;
                                            }
                                        }
                                    }

                                    for (int q = 0; q < subordinates.size(); q++) {
                                        for (int a = 0; a < currentEvent.getAccepted_users().size(); a++) {
                                            if (currentEvent.getAccepted_users().get(a).equals(subordinates.get(q))) {
                                                new_c--;
                                                break;
                                            }
                                        }
                                    }

                                    if (new_c == 0) {*/
                                        fragment_invite_new_person_pb.setVisibility(View.VISIBLE);
                                        final Map<String, Object> person = new HashMap<>();
                                        person.put("superior", superior_email);
                                        person.put("email", email);
                                        person.put("role", role);
                                        person.put("status", "free");
                                        person.put("busy_obligation_count", 0);
                                        person.put("breakRequestId", "no_request_yet");
                                        person.put("breaks_today", 0);
                                        person.put("breaks_whole", 0);
                                        person.put("subordinates", subordinates);
                                        person.put("invitation_accepted", false);

                                        db.collection("events").document(currentEvent.getEvent_id()).collection("people").document(email).set(person).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                db.collection("events").document(currentEvent.getEvent_id()).update("invited_users",FieldValue.arrayUnion(email));
                                                db.collection("events").document(currentEvent.getEvent_id()).collection("people").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                                            Person currentDoc = documentSnapshot.toObject(Person.class);
                                                            if(currentDoc.getEmail().equals(superior_email)){
                                                                db.collection("events").document(currentEvent.getEvent_id()).collection("people").document(superior_email).update("subordinates",FieldValue.arrayUnion(email));
                                                            }
                                                        }
                                                        fragment_invite_new_person_pb.setVisibility(View.GONE);
                                                        Log.d(TAG, "Friend " + email + " has successfully been invited to the event!");
                                                        Toast.makeText(EventPerspectiveActivity.this, "Friend " + email + " has successfully been invited to the event!", Toast.LENGTH_LONG).show();
                                                        popUpDialogINP.dismiss();

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                fragment_invite_new_person_pb.setVisibility(View.GONE);
                                                Log.d(TAG, "Error inviting friend " + email + "to event: ", e);
                                                Toast.makeText(EventPerspectiveActivity.this, "Error inviting friend " + email + "to event: " + e, Toast.LENGTH_LONG).show();

                                            }
                                        });
                                   /* } else {
                                        String messageString = "";
                                        for (int y = 0; y < subordinates.size(); y++) {
                                            if(new_c_pointer[y]){
                                                messageString+=subordinates.get(y) + " ";
                                            }
                                        }
                                        if (new_c == 1) {
                                            messageString = "Subordinate " + messageString + " has not yet been added to the event. Please add them first before putting them as a subordinate of another invitee.";
                                        }else{
                                            messageString = "Subordinates " + messageString + "have not yet been added to the event. Please add them first before putting them as subordinates of another invitee.";
                                        }
                                        fragment_invite_new_person_subordinates.setError(messageString);
                                        fragment_invite_new_person_subordinates.requestFocus();
                                    }*/
                                    }
                                }else{
                                    fragment_invite_new_person_pb.setVisibility(View.VISIBLE);
                                    ArrayList subordinates = new ArrayList<>();
                                    final Map<String, Object> person = new HashMap<>();
                                    person.put("superior", superior_email);
                                    person.put("email", email);
                                    person.put("role", role);
                                    person.put("status", "free");
                                    person.put("busy_obligation_count", 0);
                                    person.put("breakRequestId", "no_request_yet");
                                    person.put("breaks_today", 0);
                                    person.put("breaks_whole", 0);
                                    person.put("subordinates", subordinates);
                                    person.put("invitation_accepted", false);

                                    db.collection("events").document(currentEvent.getEvent_id()).collection("people").document(email).set(person).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            db.collection("events").document(currentEvent.getEvent_id()).update("invited_users",FieldValue.arrayUnion(email));
                                            db.collection("events").document(currentEvent.getEvent_id()).collection("people").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                                        Person currentDoc = documentSnapshot.toObject(Person.class);
                                                        if(currentDoc.getEmail().equals(superior_email)){
                                                            db.collection("events").document(currentEvent.getEvent_id()).collection("people").document(superior_email).update("subordinates",FieldValue.arrayUnion(email));
                                                        }
                                                    }
                                                    fragment_invite_new_person_pb.setVisibility(View.GONE);
                                                    Log.d(TAG, "Friend " + email + " has successfully been invited to the event!");
                                                    Toast.makeText(EventPerspectiveActivity.this, "Friend " + email + " has successfully been invited to the event!", Toast.LENGTH_LONG).show();
                                                    popUpDialogINP.dismiss();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            fragment_invite_new_person_pb.setVisibility(View.GONE);
                                            Log.d(TAG, "Error inviting friend " + email + "to event: ", e);
                                            Toast.makeText(EventPerspectiveActivity.this, "Error inviting friend " + email + "to event: " + e, Toast.LENGTH_LONG).show();

                                        }
                                    });
                                }

                            }
                        }


                    }
                });

                Objects.requireNonNull(popUpDialogINP.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialogINP.show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
