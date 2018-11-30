package com.washedup.anagnosti.ergo.eventPerspective;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.washedup.anagnosti.ergo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BreakCentreFragment extends Fragment {

    private static final String TAG = "BreakCentreFragment";
    private Event currentEvent;

    private ProgressBar pb;
    private TextView fragment_ep_bc_message_tv, fragment_ep_bc_countdown_tv, fragment_ep_bc_loading_tv, fragment_ep_bc_tv_subordinate_break_requests, fragment_ep_bc_tv_request_break;
    private FloatingActionButton fragment_ep_bc_fab_reply_to_subordinate_break_requests, fragment_ep_bc_fab_request_break, fragment_ep_bc_fab_main;
    private Animation fab_open, fab_close, fab_rotate_clockwise, fab_rotate_counter_clockwise;
    private RecyclerView rv;
    private LinearLayoutManager rLayoutManager;
    private BreakCentreRecyclerAdapter rAdapter;
    private FirebaseFirestore db;
    private CollectionReference breaksRef;
    private Person currentUser;
    private ArrayList<Break> breaks = new ArrayList<>();
    private String eventId;
    private String userEmail;
    private boolean isFabMenuOpen = false;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 0;
    //private long endTime;
    private boolean timeRunning;
    private String breakId;
    private int count = 0;
    int this_here;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_perspective_break_centre, container, false);

        currentEvent = ((EventPerspectiveActivity) this.getActivity()).getCurrentEvent();
        currentUser = ((EventPerspectiveActivity) this.getActivity()).getCurrentUser();
        ((EventPerspectiveActivity) this.getActivity()).setHere(2);
        this_here = ((EventPerspectiveActivity) this.getActivity()).getHere();

        db = FirebaseFirestore.getInstance();

        eventId = currentEvent.getEvent_id();
        userEmail = currentUser.getEmail();
        breaksRef = db.collection("events").document(eventId).collection("people").document(userEmail).collection("breaks");

        pb = rootView.findViewById(R.id.fragment_ep_bc_pb);
        pb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.dirtierWhite), PorterDuff.Mode.MULTIPLY);

        fragment_ep_bc_loading_tv = rootView.findViewById(R.id.fragment_ep_bc_loading_tv);
        fragment_ep_bc_message_tv = rootView.findViewById(R.id.fragment_ep_bc_message_tv);
        fragment_ep_bc_countdown_tv = rootView.findViewById(R.id.fragment_ep_bc_countdown_tv);
        fragment_ep_bc_tv_subordinate_break_requests = rootView.findViewById(R.id.fragment_ep_bc_tv_subordinate_break_requests);
        fragment_ep_bc_tv_request_break = rootView.findViewById(R.id.fragment_ep_bc_tv_request_break);

        fragment_ep_bc_fab_reply_to_subordinate_break_requests = rootView.findViewById(R.id.fragment_ep_bc_fab_reply_to_subordinate_break_requests);
        fragment_ep_bc_fab_request_break = rootView.findViewById(R.id.fragment_ep_bc_fab_request_break);
        fragment_ep_bc_fab_main = rootView.findViewById(R.id.fragment_ep_bc_fab_main);

        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        fab_rotate_clockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_clockwise);
        fab_rotate_counter_clockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_counter_clockwise);


        rv = rootView.findViewById(R.id.fragment_ep_bc_rv);

        rLayoutManager = new LinearLayoutManager(rootView.getContext());
        rLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(rLayoutManager);

        refreshBrakesRV();

        fragment_ep_bc_fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser.getSubordinates().isEmpty()) {
                    fragment_ep_bc_fab_reply_to_subordinate_break_requests.setVisibility(View.GONE);
                    fragment_ep_bc_tv_subordinate_break_requests.setVisibility(View.GONE);

                    if (isFabMenuOpen) {

                        fragment_ep_bc_fab_request_break.startAnimation(fab_close);
                        fragment_ep_bc_tv_request_break.startAnimation(fab_close);
                        fragment_ep_bc_fab_main.startAnimation(fab_rotate_counter_clockwise);
                        fragment_ep_bc_fab_request_break.setClickable(false);
                        isFabMenuOpen = false;

                    } else {
                        fragment_ep_bc_fab_request_break.startAnimation(fab_open);
                        fragment_ep_bc_tv_request_break.startAnimation(fab_open);
                        fragment_ep_bc_fab_main.startAnimation(fab_rotate_clockwise);
                        fragment_ep_bc_fab_request_break.setClickable(true);
                        isFabMenuOpen = true;
                    }
                } else {
                    if (isFabMenuOpen) {
                        fragment_ep_bc_fab_reply_to_subordinate_break_requests.startAnimation(fab_close);
                        fragment_ep_bc_fab_request_break.startAnimation(fab_close);
                        fragment_ep_bc_tv_subordinate_break_requests.startAnimation(fab_close);
                        fragment_ep_bc_tv_request_break.startAnimation(fab_close);
                        fragment_ep_bc_fab_main.startAnimation(fab_rotate_counter_clockwise);
                        fragment_ep_bc_fab_reply_to_subordinate_break_requests.setClickable(false);
                        fragment_ep_bc_fab_request_break.setClickable(false);
                        isFabMenuOpen = false;

                    } else {
                        fragment_ep_bc_fab_reply_to_subordinate_break_requests.startAnimation(fab_open);
                        fragment_ep_bc_fab_request_break.startAnimation(fab_open);
                        fragment_ep_bc_tv_subordinate_break_requests.startAnimation(fab_open);
                        fragment_ep_bc_tv_request_break.startAnimation(fab_open);
                        fragment_ep_bc_fab_main.startAnimation(fab_rotate_clockwise);
                        fragment_ep_bc_fab_reply_to_subordinate_break_requests.setClickable(true);
                        fragment_ep_bc_fab_request_break.setClickable(true);
                        isFabMenuOpen = true;
                    }
                }
            }
        });

        fragment_ep_bc_fab_reply_to_subordinate_break_requests.setOnClickListener(new View.OnClickListener() {

            Dialog popUpDialog = new Dialog(getContext());
            TextView fragment_child_ep_bc_sub_brs_message_tv;
            RecyclerView fragment_child_ep_bc_sub_brs_rv;
            ProgressBar fragment_child_ep_bc_sub_brs_pb;
            private LinearLayoutManager obs_rLayoutManager;
            private ArrayList<Person> subsWithBreakRequests = new ArrayList<>();

            @Override
            public void onClick(View view) {

                fragment_ep_bc_fab_reply_to_subordinate_break_requests.startAnimation(fab_close);
                fragment_ep_bc_fab_request_break.startAnimation(fab_close);
                fragment_ep_bc_tv_subordinate_break_requests.startAnimation(fab_close);
                fragment_ep_bc_tv_request_break.startAnimation(fab_close);
                fragment_ep_bc_fab_main.startAnimation(fab_rotate_counter_clockwise);
                fragment_ep_bc_fab_reply_to_subordinate_break_requests.setClickable(false);
                fragment_ep_bc_fab_request_break.setClickable(false);
                isFabMenuOpen = false;

                popUpDialog.setContentView(R.layout.fragment_child_event_perspective_break_centre_sub_brs);
                fragment_child_ep_bc_sub_brs_message_tv = popUpDialog.findViewById(R.id.fragment_child_ep_bc_sub_brs_message_tv);
                fragment_child_ep_bc_sub_brs_rv = popUpDialog.findViewById(R.id.fragment_child_ep_bc_sub_brs_rv);
                fragment_child_ep_bc_sub_brs_pb = popUpDialog.findViewById(R.id.fragment_child_ep_bc_sub_brs_pb);
                fragment_child_ep_bc_sub_brs_pb.getIndeterminateDrawable().setColorFilter(getContext().getResources().getColor(R.color.dirtierWhite), PorterDuff.Mode.MULTIPLY);


                obs_rLayoutManager = new LinearLayoutManager(popUpDialog.getContext());
                obs_rLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                fragment_child_ep_bc_sub_brs_rv.setLayoutManager(obs_rLayoutManager);

                obs_refreshBreaKRequestsRV();

                Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialog.show();
            }

            private void obs_refreshBreaKRequestsRV() {
                fragment_child_ep_bc_sub_brs_pb.setVisibility(View.VISIBLE);
                db.collection("events").document(eventId).collection("people").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<String> subordinatesEmails = currentUser.getSubordinates();
                        subsWithBreakRequests.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Person sub = documentSnapshot.toObject(Person.class);
                            for (String cur_sub : subordinatesEmails) {
                                if (sub.getEmail().equals(cur_sub) && !(sub.getBreakRequestId().equals("no_request_yet"))) {
                                    subsWithBreakRequests.add(sub);
                                }
                            }
                        }

                        if (!subsWithBreakRequests.isEmpty())
                            runLocalAnimation(popUpDialog, fragment_child_ep_bc_sub_brs_rv, obs_rLayoutManager, 1);
                        fragment_child_ep_bc_sub_brs_pb.setVisibility(View.GONE);

                        if (subsWithBreakRequests.isEmpty()) {
                            fragment_child_ep_bc_sub_brs_message_tv.setText(R.string.no_break_requests);
                            fragment_child_ep_bc_sub_brs_message_tv.setVisibility(View.VISIBLE);
                        } else {
                            fragment_child_ep_bc_sub_brs_message_tv.setVisibility(View.GONE);

                        }


                    }
                });
            }

            private void runLocalAnimation(Dialog popUpDialog, final RecyclerView rv, final LinearLayoutManager llm, int type) {

                //Context context = rv.getContext();
                //LayoutAnimationController controller = null;
                BreakCentreSubBrsRecyclerAdapter rAdapter = new BreakCentreSubBrsRecyclerAdapter(popUpDialog, subsWithBreakRequests, rv.getContext(), getActivity(), db, eventId);
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
        });

        fragment_ep_bc_fab_request_break.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!currentUser.getBreakRequestId().equals("no_request_yet")) {
                    Toast.makeText(getContext(), "You already have a break that's pending for approval. Wait for your superior to approve or decline it.", Toast.LENGTH_LONG).show();
                } else if (count > 0) {
                    Toast.makeText(getContext(), "You are already on a break!", Toast.LENGTH_SHORT).show();
                } else {

                    if (currentUser.getSubordinates().isEmpty()) {
                        fragment_ep_bc_fab_request_break.startAnimation(fab_close);
                        fragment_ep_bc_tv_request_break.startAnimation(fab_close);
                        fragment_ep_bc_fab_main.startAnimation(fab_rotate_counter_clockwise);
                        fragment_ep_bc_fab_request_break.setClickable(false);
                        isFabMenuOpen = false;
                    } else {
                        fragment_ep_bc_fab_reply_to_subordinate_break_requests.startAnimation(fab_close);
                        fragment_ep_bc_fab_request_break.startAnimation(fab_close);
                        fragment_ep_bc_tv_subordinate_break_requests.startAnimation(fab_close);
                        fragment_ep_bc_tv_request_break.startAnimation(fab_close);
                        fragment_ep_bc_fab_main.startAnimation(fab_rotate_counter_clockwise);
                        fragment_ep_bc_fab_reply_to_subordinate_break_requests.setClickable(false);
                        fragment_ep_bc_fab_request_break.setClickable(false);
                        isFabMenuOpen = false;
                    }

                    if (currentUser.getSuperior().equals("nobody@nonono.com")) {
                        final Dialog popUpDialog = new Dialog(getContext());
                        popUpDialog.setContentView(R.layout.fragment_child_event_perspective_break_centre_pop_up);
                        final EditText fragment_child_ep_bc_et_duration_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_bc_et_duration_pop_up);
                        ImageButton fragment_child_ep_bc_btn_send_break_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_bc_btn_send_break_pop_up);
                        final ProgressBar fragment_child_ep_bc_pb_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_bc_pb_pop_up);
                        fragment_child_ep_bc_pb_pop_up.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.dirtierWhite), PorterDuff.Mode.MULTIPLY);

                        fragment_child_ep_bc_btn_send_break_pop_up.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String time = fragment_child_ep_bc_et_duration_pop_up.getText().toString().trim();
                                if (time.isEmpty()) {
                                    fragment_child_ep_bc_et_duration_pop_up.setError("Please enter the minutes for the break");
                                    fragment_child_ep_bc_et_duration_pop_up.requestFocus();
                                } else {
                                    fragment_child_ep_bc_pb_pop_up.setVisibility(View.VISIBLE);
                                    Long endTime = System.currentTimeMillis();
                                    Long durationMinutes = Long.parseLong(time);
                                    durationMinutes *= 60000;
                                    endTime += durationMinutes;

                                    final Map<String, Object> new_break = new HashMap<>();
                                    new_break.put("duration", time);
                                    new_break.put("endOfBreakTime", endTime);
                                    new_break.put("break_superior", currentUser.getEmail());
                                    new_break.put("break_subordinate", currentUser.getEmail());
                                    new_break.put("is_breaking", 1);

                                    breaksRef.add(new_break).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            String breakId = documentReference.getId();
                                            breaksRef.document(breakId).update("break_id", breakId);
                                            db.collection("events").document(eventId).collection("people").document(userEmail).update("status", "on_break");
                                            Log.d(TAG, "Break request " + documentReference.getId() + "successfully added for yourself " + currentUser.getEmail());
                                            Toast.makeText(getContext(), "Your break has started, superior.", Toast.LENGTH_SHORT).show();
                                            refreshBrakesRV();
                                            popUpDialog.dismiss();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding new personal break request ", e);
                                            Toast.makeText(getContext(), "Error adding new personal break request: " + e, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });

                        Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        popUpDialog.show();

                    } else {
                        final Map<String, Object> new_break = new HashMap<>();
                        new_break.put("duration", "pending");
                        new_break.put("endOfBreakTime", -1);
                        new_break.put("break_superior", currentUser.getSuperior());
                        new_break.put("break_subordinate", currentUser.getEmail());
                        new_break.put("is_breaking", 0);
                        breaksRef.add(new_break).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String breakId = documentReference.getId();
                                breaksRef.document(breakId).update("break_id", breakId);
                                new_break.put("break_id", breakId);
                                db.collection("events").document(eventId).collection("people").document(userEmail).update("breakRequestId", breakId);
                                Log.d(TAG, "Break request " + documentReference.getId() + "successfully added for user " + currentUser.getEmail());
                                Toast.makeText(getContext(), "Break request successfully sent.", Toast.LENGTH_SHORT).show();
                                refreshBrakesRV();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding break request id to user's breakRequestId ", e);
                                Toast.makeText(getContext(), "Error adding break request id to user's breakRequestId: " + e, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });

        return rootView;
    }

    private void startStop() {
        if (timeRunning)
            stopTimer();
        else
            startTimer();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

                breaksRef.document(breakId).update("is_breaking", 2);
                //Toast.makeText(getContext(), "<=0", Toast.LENGTH_SHORT).show();
                fragment_ep_bc_message_tv.setText(R.string.no_break);
                fragment_ep_bc_countdown_tv.setVisibility(View.GONE);
                timeLeftInMilliseconds = 0;
                count = 0;
                breakId = "";
                db.collection("events").document(eventId).collection("people").document(currentUser.getEmail()).collection("obligations").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int working_count = 0;
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Obligation obt = documentSnapshot.toObject(Obligation.class);
                            if (obt.getStatus() == 2) {
                                working_count++;
                                break;
                            }
                        }
                        if (working_count > 0) {
                            db.collection("events").document(eventId).collection("people").document(currentUser.getEmail()).update("status", "working");

                        } else {
                            db.collection("events").document(eventId).collection("people").document(currentUser.getEmail()).update("status", "free");
                        }
                        int bw = currentUser.getBreaks_whole();
                        bw++;
                        db.collection("events").document(eventId).collection("people").document(userEmail).update("breaks_whole", bw);
                        refreshBrakesRV();
                        startStop();
                    }
                });


            }
        }.start();

        timeRunning = true;
    }

    private void updateTimer() {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        fragment_ep_bc_countdown_tv.setText(timeLeftText);
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timeRunning = false;
    }


    private void refreshBrakesRV() {
        fragment_ep_bc_message_tv.setText(R.string.fetching_active_break);
        pb.setVisibility(View.VISIBLE);

        breaksRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                long testingTimeLeftInMilliseconds = 0;
                String testingBreakId = "";
                int testingCount = 0;

                breaks.clear();
                fragment_ep_bc_loading_tv.setText(R.string.loading_all_breaks);
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Break cbreak = documentSnapshot.toObject(Break.class);
                    if (cbreak.getIs_breaking() == 1) {
                        testingTimeLeftInMilliseconds = cbreak.getEndOfBreakTime();
                        testingTimeLeftInMilliseconds = testingTimeLeftInMilliseconds - System.currentTimeMillis();
                        testingBreakId = cbreak.getBreak_id();
                        testingCount++;
                    }
                    breaks.add(cbreak);
                }

                if (testingTimeLeftInMilliseconds != 0 && !(testingBreakId.equals("")) && testingCount != 0) {
                    timeLeftInMilliseconds = testingTimeLeftInMilliseconds;
                    breakId = testingBreakId;
                    count = testingCount;
                }

                if (count < 1) {
                    //Toast.makeText(getContext(), "Count", Toast.LENGTH_SHORT).show();
                    fragment_ep_bc_message_tv.setText(R.string.no_break);
                    fragment_ep_bc_countdown_tv.setVisibility(View.GONE);

                } else if (count == 1) {
                    startStop();
                    fragment_ep_bc_message_tv.setText(R.string.time_until_break_ends);
                    fragment_ep_bc_countdown_tv.setVisibility(View.VISIBLE);
                }

                if (!breaks.isEmpty()) {
                    //if(((EventPerspectiveActivity) getActivity()).getHere()==2)//??
                    runAnimation(rv, rLayoutManager, 1);
                }
                pb.setVisibility(View.GONE);

                if (breaks.isEmpty()) {
                    fragment_ep_bc_loading_tv.setText(R.string.no_breaks);
                    fragment_ep_bc_loading_tv.setVisibility(View.VISIBLE);
                } else {
                    fragment_ep_bc_loading_tv.setVisibility(View.GONE);
                }

            }
        });
    }

    private void runAnimation(final RecyclerView rv, final LinearLayoutManager llm, int type) {
        //Context context = rv.getContext();
        //LayoutAnimationController controller = null;
        rAdapter = new BreakCentreRecyclerAdapter(breaks, rv.getContext(), getActivity(), eventId);
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
    }
}
