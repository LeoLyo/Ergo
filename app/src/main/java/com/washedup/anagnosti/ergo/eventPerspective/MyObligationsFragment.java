package com.washedup.anagnosti.ergo.eventPerspective;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.washedup.anagnosti.ergo.R;

import java.util.ArrayList;

public class MyObligationsFragment extends Fragment {

    private ProgressBar pb;
    private TextView tv;
    private RecyclerView rv;
    private LinearLayoutManager rLayoutManager;
    private FirebaseFirestore db;
    private CollectionReference obligationsRef;
    private ArrayList<Obligation> obligations = new ArrayList<>();
    private String eventId;
    private double busy_ob_count;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_perspective_my_obligations, container, false);

        Event currentEvent = ((EventPerspectiveActivity) this.getActivity()).getCurrentEvent();
        Person currentUser = ((EventPerspectiveActivity) this.getActivity()).getCurrentUser();
        db = FirebaseFirestore.getInstance();

        eventId = currentEvent.getEvent_id();
        String userEmail;
        if(currentUser ==null){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            userEmail = mAuth.getCurrentUser().getEmail();
        }else{
            userEmail = currentUser.getEmail();
        }
        obligationsRef = db.collection("events").document(eventId).collection("people")
                .document(userEmail).collection("obligations");

        pb = rootView.findViewById(R.id.fragment_ep_mo_pb);
        pb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.dirtierWhite),PorterDuff.Mode.MULTIPLY);
        tv = rootView.findViewById(R.id.fragment_ep_mo_tv);
        rv = rootView.findViewById(R.id.fragment_ep_mo_rv);
        rLayoutManager = new LinearLayoutManager(rootView.getContext());
        rLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(rLayoutManager);

        refreshObligationsRV();

        return rootView;
    }

    private void refreshObligationsRV() {
        pb.setVisibility(View.VISIBLE);
        obligationsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                obligations.clear();
                busy_ob_count=0;
                tv.setText(R.string.loading_obligations);
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Obligation obligation = documentSnapshot.toObject(Obligation.class);
                    if(obligation.getStatus()==2||obligation.getStatus()==3){
                        busy_ob_count++;
                    }
                    obligations.add(obligation);
                }

                if (!obligations.isEmpty())
                    runAnimation(rv, rLayoutManager, 1);
                pb.setVisibility(View.GONE);

                if (obligations.isEmpty()) {
                    tv.setText(R.string.no_obligations);
                    tv.setVisibility(View.VISIBLE);
                } else {
                    tv.setVisibility(View.GONE);

                }


            }
        });
    }

    private void runAnimation(final RecyclerView rv, final LinearLayoutManager llm, int type) {
        //Context context = rv.getContext();
        //LayoutAnimationController controller = null;
        MyObligationsRecyclerAdapter rAdapter = new MyObligationsRecyclerAdapter(obligations, rv.getContext(), getActivity(), db, eventId, busy_ob_count);
        rv.setAdapter(rAdapter);
        rv.setAlpha(0);

        //controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        if (type == 0) {
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
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
        } else if (type == 1) {
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
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
        } else if (type == 2) {
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
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
