package com.washedup.anagnosti.ergo.otherHomePossibilities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        refreshEventRV();


    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshEventRV();
    }


    private void refreshEventRV(){

        eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                events.clear();
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Event event = documentSnapshot.toObject(Event.class);
                    event.setEvent_id(documentSnapshot.getId());
                    events.add(event);
                }
                setContentView(R.layout.activity_choose_event_for_perspective);
                rv = findViewById(R.id.activity_cefp_rv);
                rLayoutManager = new LinearLayoutManager(getApplicationContext());
                rv.setLayoutManager(rLayoutManager);
                eRoleAdapter = new ChooseEventForPerspectiveRecyclerAdapter(events);
            }
        });
    }
}
