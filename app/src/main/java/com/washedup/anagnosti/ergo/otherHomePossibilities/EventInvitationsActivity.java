package com.washedup.anagnosti.ergo.otherHomePossibilities;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
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
import com.washedup.anagnosti.ergo.eventPerspective.Day;
import com.washedup.anagnosti.ergo.eventPerspective.Event;

import java.util.ArrayList;
import java.util.List;

public class EventInvitationsActivity extends Activity {

    private RecyclerView rv;
    private LinearLayoutManager rLayoutManager;
    private EventInvitationsRecyclerAdapter eRoleAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventsRef = db.collection("events");
    private ArrayList<Event> events = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private TextView tv;
    private ProgressBar pb;
    private String TAG = "EventInvitationsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_invitations);

        tv = findViewById(R.id.activity_event_invitations_tv);
        pb = findViewById(R.id.activity_event_invitations_pb);

        pb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.dirtierWhite),PorterDuff.Mode.MULTIPLY);

        rv = findViewById(R.id.activity_event_invitations_rv);
        rLayoutManager = new LinearLayoutManager(this);
        rLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(rLayoutManager);
        eRoleAdapter = new EventInvitationsRecyclerAdapter(events,this, this);
        rv.setAdapter(eRoleAdapter);

        refreshInvitations();
    }

    private void refreshInvitations() {

        pb.setVisibility(View.VISIBLE);
        eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                events.clear();
                tv.setText(R.string.loading_invitations);
                String userEmail = user.getEmail();
                for(final QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    final Event event = documentSnapshot.toObject(Event.class);
                    for(int i=0;i<events.size();i++){
                        if(userEmail!=null){
                            if(event.getInvited_users().get(i).compareTo(userEmail)==0){
                                event.setEvent_id(documentSnapshot.getId());
                                events.add(event);
                                break;
                            }
                        }else{
                            Toast.makeText(EventInvitationsActivity.this, "[YERROR]: Email is null", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                if(!events.isEmpty()){
                    runAnimation(rv,rLayoutManager, 1);
                }

                pb.setVisibility(View.GONE);


                if(events.isEmpty()){
                    tv.setText(R.string.no_invitations);
                    tv.setVisibility(View.VISIBLE);
                }else{
                    tv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void runAnimation(final RecyclerView rv, final LinearLayoutManager llm, int type) {
        //Context context = rv.getContext();
        //LayoutAnimationController controller = null;
        eRoleAdapter = new EventInvitationsRecyclerAdapter(events, rv.getContext(), this);
        rv.setAdapter(eRoleAdapter);
        rv.setAlpha(0);

        //controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        if(type == 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    // This will give me the initial first and last visible element's position.
                    // This is required as only this elements needs to be animated
                    // Start will be always zero in this case as we are calling in onCreate
                    int start = llm.findFirstVisibleItemPosition();
                    int end = llm.findLastVisibleItemPosition();

                    Log.i("Start: ", start + "");
                    Log.i("End: ", end + "");

                    // Multiplication factor
                    int DELAY = 50;

                    // Loop through all visible element
                    for (int i = start; i <= end; i++) {
                        Log.i("Animatining: ", i + "");

                        // Get View
                        View v = rv.findViewHolderForAdapterPosition(i).itemView;

                        // Hide that view initially
                        v.setAlpha(0);

                        // Setting animations: slide and alpha 0 to 1
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        //int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;



                        PropertyValuesHolder slide = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -width, 0);
                        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0, 1);
                        ObjectAnimator a = ObjectAnimator.ofPropertyValuesHolder(v, slide, alpha);
                        a.setDuration(300);

                        // It will set delay. As loop progress it will increment
                        // And it will look like items are appearing one by one.
                        // Not all at a time
                        a.setStartDelay(i * DELAY);

                        a.setInterpolator(new DecelerateInterpolator());

                        a.start();

                    }

                    // Set Recycler View visible as all visible are now hidden
                    // Animation will start, so set it visible
                    rv.setAlpha(1);

                }
            }, 50);
        }else if(type == 1){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    // This will give me the initial first and last visible element's position.
                    // This is required as only this elements needs to be animated
                    // Start will be always zero in this case as we are calling in onCreate
                    int start = llm.findFirstVisibleItemPosition();
                    int end = llm.findLastVisibleItemPosition();

                    Log.i("Start: ", start + "");
                    Log.i("End: ", end + "");

                    // Multiplication factor
                    int DELAY = 50;

                    // Loop through all visible element
                    for (int i = start; i <= end; i++) {
                        Log.i("Animatining: ", i + "");

                        // Get View
                        View v = rv.findViewHolderForAdapterPosition(i).itemView;

                        // Hide that view initially
                        v.setAlpha(0);

                        // Setting animations: slide and alpha 0 to 1
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        //int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;

                        //int screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
                        //int screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;

                        PropertyValuesHolder slide = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, width, 0);
                        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0, 1);
                        ObjectAnimator a = ObjectAnimator.ofPropertyValuesHolder(v, slide, alpha);
                        a.setDuration(300);

                        // It will set delay. As loop progress it will increment
                        // And it will look like items are appearing one by one.
                        // Not all at a time
                        a.setStartDelay(i * DELAY);

                        a.setInterpolator(new DecelerateInterpolator());

                        a.start();

                    }

                    // Set Recycler View visible as all visible are now hidden
                    // Animation will start, so set it visible
                    rv.setAlpha(1);

                }
            }, 50);
        }else if(type == 2){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    // This will give me the initial first and last visible element's position.
                    // This is required as only this elements needs to be animated
                    // Start will be always zero in this case as we are calling in onCreate
                    int start = llm.findFirstVisibleItemPosition();
                    int end = llm.findLastVisibleItemPosition();

                    Log.i("Start: ", start + "");
                    Log.i("End: ", end + "");

                    // Multiplication factor
                    int DELAY = 50;

                    // Loop through all visible element
                    for (int i = start; i <= end; i++) {
                        Log.i("Animatining: ", i + "");

                        // Get View
                        View v = rv.findViewHolderForAdapterPosition(i).itemView;

                        // Hide that view initially
                        v.setAlpha(0);

                        // Setting animations: slide and alpha 0 to 1
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        //int height = displayMetrics.heightPixels;
                        //int width = displayMetrics.widthPixels;

                        //int screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
                        //int screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;


                        PropertyValuesHolder slide = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0, 0);
                        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0, 1);
                        ObjectAnimator a = ObjectAnimator.ofPropertyValuesHolder(v, slide, alpha);
                        a.setDuration(300);

                        // It will set delay. As loop progress it will increment
                        // And it will look like items are appearing one by one.
                        // Not all at a time
                        a.setStartDelay(i * DELAY);

                        a.setInterpolator(new DecelerateInterpolator());

                        a.start();

                    }

                    // Set Recycler View visible as all visible are now hidden
                    // Animation will start, so set it visible
                    rv.setAlpha(1);

                }
            }, 50);
        }

        //rv.setLayoutAnimation(controller);
        //rv.getAdapter().notifyDataSetChanged();
        //rv.scheduleLayoutAnimation();

    }

}
