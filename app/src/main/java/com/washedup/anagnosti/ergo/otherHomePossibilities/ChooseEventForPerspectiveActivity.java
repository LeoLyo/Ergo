package com.washedup.anagnosti.ergo.otherHomePossibilities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.eventPerspective.Event;

import java.util.ArrayList;

public class ChooseEventForPerspectiveActivity extends Activity{

    private RecyclerView rv;
    private RecyclerView.LayoutManager rLayoutManager;
    private ChooseEventForPerspectiveRecyclerAdapter eRoleAdapter;
    private FirebaseFirestore  db = FirebaseFirestore.getInstance();
    private CollectionReference eventsRef = db.collection("events");
    private ArrayList<Event> events = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private TextView tv;
    private ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_event_for_perspective);
        tv = findViewById(R.id.activity_cefp_tv);
        pb = findViewById(R.id.activity_cefp_pb);
        refreshEventRV();



    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshEventRV();
    }


    private void refreshEventRV(){

        pb.setVisibility(View.VISIBLE);
        eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                events.clear();
                tv.setText(R.string.loading_events);
                String userEmail = user.getEmail();
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Event event = documentSnapshot.toObject(Event.class);
                    for(int i=0; i<event.getEmails_of_people().size();i++){
                        if(userEmail!=null){
                            if(event.getEmails_of_people().get(i).compareTo(userEmail)==0){
                                  event.setEvent_id(documentSnapshot.getId());
                                events.add(event);
                                break;
                            }
                        }else{
                            Toast.makeText(ChooseEventForPerspectiveActivity.this, "[YERROR]: Email is null", Toast.LENGTH_SHORT).show();
                        }
                       
                    }

                }
                rv = findViewById(R.id.activity_cefp_rv);
                rLayoutManager = new LinearLayoutManager(getApplicationContext());
                rv.setLayoutManager(rLayoutManager);
                eRoleAdapter = new ChooseEventForPerspectiveRecyclerAdapter(events);
                rv.setAdapter(eRoleAdapter);
                pb.setVisibility(View.GONE);


                if(events.isEmpty()){
                    tv.setText(R.string.no_events_to_choose_from);
                    tv.setVisibility(View.VISIBLE);
                }else{
                    tv.setVisibility(View.GONE);

                }
            }
        });
    }
}
