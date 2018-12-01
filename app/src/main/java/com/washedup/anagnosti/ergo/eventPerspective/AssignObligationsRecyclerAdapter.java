package com.washedup.anagnosti.ergo.eventPerspective;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.transformations.CircleTransform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class AssignObligationsRecyclerAdapter extends RecyclerView.Adapter<AssignObligationsRecyclerAdapter.AssignObligationsRecyclerViewHolder> {

    private static final String TAG = "AssignObligationsRA";
    private ArrayList<Person> subordinates;
    private Context context;
    private Activity activity;
    private FirebaseFirestore db;
    private String eventId;

    public AssignObligationsRecyclerAdapter() {
    }

    public AssignObligationsRecyclerAdapter(ArrayList<Person> subordinates, Context context, FragmentActivity activity, FirebaseFirestore db, String eventId) {
        this.subordinates = subordinates;
        this.context = context;
        this.activity = activity;
        this.db = db;
        this.eventId = eventId;
    }

    @Override
    public AssignObligationsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_event_perspective_assign_obligations, parent, false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new AssignObligationsRecyclerAdapter.AssignObligationsRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AssignObligationsRecyclerViewHolder holder, int position) {

        final Person subordinate = subordinates.get(holder.getAdapterPosition());
        final String combinedName = subordinate.getFirstName() + " " + subordinate.getNickname() + " " + subordinate.getLastName();
        if (subordinate.getInvitation_accepted()) {
            holder.fragment_child_ep_ao_tv_pending.setVisibility(View.GONE);
            holder.fragment_child_ep_ao_et_name.setText(combinedName);
            if (subordinate.getStatus().equals("free"))
                holder.fragment_child_ep_ao_iv_status.setImageResource(R.drawable.ic_yes);
            else if (subordinate.getStatus().equals("declined"))
                holder.fragment_child_ep_ao_iv_status.setImageResource(R.drawable.ic_no);
            else if (subordinate.getStatus().equals("working"))
                holder.fragment_child_ep_ao_iv_status.setImageResource(R.drawable.ic_maybe);
            else if (subordinate.getStatus().equals("pending_obligation"))
                holder.fragment_child_ep_ao_iv_status.setImageResource(R.drawable.ic_jebale_te_3_tacke);
            else if (subordinate.getStatus().equals("on_break"))
                holder.fragment_child_ep_ao_iv_status.setImageResource(R.drawable.ic_sukurac_pauza);

        } else {
            holder.fragment_child_ep_ao_tv_pending.setVisibility(View.VISIBLE);
            holder.fragment_child_ep_ao_et_name.setText(subordinate.getEmail());
            //RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotate45animation);
            //holder.fragment_child_ep_ao_tv_pending.setAnimation(rotate);
            holder.fragment_child_ep_ao_cv.setClickable(false);
        }

        holder.fragment_child_ep_ao_et_role.setText(subordinate.getRole());
        if (subordinate.getProfileImageUrl() != null && !subordinate.getProfileImageUrl().isEmpty()) {
            Picasso.with(context)
                    .load(subordinate.getProfileImageUrl())
                    .fit()
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(holder.fragment_child_ep_ao_iv_person);
        }

        holder.fragment_child_ep_ao_iv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subordinate.getStatus().equals("free"))
                    Toast.makeText(context, "Subordinate is free and ready for use.", Toast.LENGTH_SHORT).show();
                else if (subordinate.getStatus().equals("declined"))
                    Toast.makeText(context, "Subordinate has declined their last obligation. You should check why they declined.", Toast.LENGTH_LONG).show();
                else if (subordinate.getStatus().equals("working"))
                    Toast.makeText(context, "Subordinate is currently working on an obligation.", Toast.LENGTH_SHORT).show();
                else if (subordinate.getStatus().equals("pending_obligation"))
                    Toast.makeText(context, "Subordinate has not answered to the last obligation sent.", Toast.LENGTH_SHORT).show();
                else if (subordinate.getStatus().equals("on_break"))
                    Toast.makeText(context, "Subordinate is currently on a break.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.fragment_child_ep_ao_cv.setOnClickListener(new View.OnClickListener() {

            Dialog popUpDialogChoice = new Dialog(context);
            Button fragment_child_ep_ao_choice_btn_left, fragment_child_ep_ao_choice_btn_right;

            @Override
            public void onClick(View view) {
                if (!subordinate.getInvitation_accepted()) {
                    Toast.makeText(context, "User hasn't accepted the event invitation yet.", Toast.LENGTH_SHORT).show();
                    return;
                }


                popUpDialogChoice.setContentView(R.layout.fragment_child_event_perspective_assign_obligations_choice);

                fragment_child_ep_ao_choice_btn_left = popUpDialogChoice.findViewById(R.id.fragment_child_ep_ao_choice_btn_left);
                fragment_child_ep_ao_choice_btn_right = popUpDialogChoice.findViewById(R.id.fragment_child_ep_ao_choice_btn_right);

                fragment_child_ep_ao_choice_btn_left.setOnClickListener(new View.OnClickListener() {

                    Dialog popUpDialogAssign = new Dialog(context);
                    ImageView fragment_child_ep_ao_iv_pop_up;
                    TextView fragment_child_ep_ao_tv_subordinate_pop_up;
                    EditText fragment_child_ep_ao_et_what_pop_up, fragment_child_ep_ao_et_where_pop_up, fragment_child_ep_ao_et_details_pop_up;
                    ImageButton fragment_child_ep_ao_btn_send_obligation_pop_up;
                    ProgressBar fragment_child_ep_ao_pb_pop_up;

                    @Override
                    public void onClick(View view) {
                        if (subordinate.getBusy_obligation_count() >= 5) {
                            Toast.makeText(context, "This subordinate already has " + subordinate.getBusy_obligation_count() + " obligations pending for completion. Please assign the task ato another subordinate.", Toast.LENGTH_LONG).show();
                        } else {

                            popUpDialogChoice.dismiss();
                            popUpDialogAssign.setContentView(R.layout.fragment_child_event_perspective_assign_obligations_pop_up);

                            fragment_child_ep_ao_iv_pop_up = popUpDialogAssign.findViewById(R.id.fragment_child_ep_ao_iv_pop_up);
                            fragment_child_ep_ao_tv_subordinate_pop_up = popUpDialogAssign.findViewById(R.id.fragment_child_ep_ao_tv_subordinate_pop_up);
                            fragment_child_ep_ao_et_what_pop_up = popUpDialogAssign.findViewById(R.id.fragment_child_ep_ao_et_what_pop_up);
                            fragment_child_ep_ao_et_where_pop_up = popUpDialogAssign.findViewById(R.id.fragment_child_ep_ao_et_where_pop_up);
                            fragment_child_ep_ao_et_details_pop_up = popUpDialogAssign.findViewById(R.id.fragment_child_ep_ao_et_details_pop_up);
                            fragment_child_ep_ao_btn_send_obligation_pop_up = popUpDialogAssign.findViewById(R.id.fragment_child_ep_ao_btn_send_obligation_pop_up);
                            fragment_child_ep_ao_pb_pop_up = popUpDialogAssign.findViewById(R.id.fragment_child_ep_ao_pb_pop_up);
                            fragment_child_ep_ao_pb_pop_up.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.dirtierWhite), PorterDuff.Mode.MULTIPLY);


                            if (subordinate.getProfileImageUrl() != null && !subordinate.getProfileImageUrl().isEmpty()) {
                                Picasso.with(context)
                                        .load(subordinate.getProfileImageUrl())
                                        .fit()
                                        .centerCrop()
                                        .into(fragment_child_ep_ao_iv_pop_up);
                            }

                            fragment_child_ep_ao_tv_subordinate_pop_up.setText(combinedName);
                            fragment_child_ep_ao_btn_send_obligation_pop_up.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    final String what = fragment_child_ep_ao_et_what_pop_up.getText().toString().trim();
                                    final String where = fragment_child_ep_ao_et_where_pop_up.getText().toString().trim();
                                    final String details = fragment_child_ep_ao_et_details_pop_up.getText().toString().trim();

                                    if (what.isEmpty()) {
                                        fragment_child_ep_ao_et_what_pop_up.setError("Please write 'what' the obligation is.");
                                        fragment_child_ep_ao_et_what_pop_up.requestFocus();
                                    } else if (where.isEmpty()) {
                                        fragment_child_ep_ao_et_where_pop_up.setError("Please write 'where' the obligation should be done.");
                                        fragment_child_ep_ao_et_where_pop_up.requestFocus();
                                    } else if (details.isEmpty()) {
                                        fragment_child_ep_ao_et_details_pop_up.setError("Please write the 'details' of the obligation.");
                                        fragment_child_ep_ao_et_details_pop_up.requestFocus();
                                    } else {

                                        fragment_child_ep_ao_pb_pop_up.setVisibility(View.VISIBLE);
                                        //Date
                                        long yourmilliseconds = System.currentTimeMillis();
                                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                                        Date resultdate = new Date(yourmilliseconds);
                                        String curDate = sdf.format(resultdate);


                                        final Map<String, Object> obligation = new HashMap<>();
                                        obligation.put("date", curDate);
                                        obligation.put("what", what);
                                        obligation.put("where", where);
                                        obligation.put("details", details);
                                        obligation.put("obligation_superior", subordinate.getSuperior());
                                        obligation.put("obligation_subordinate", subordinate.getEmail());
                                        obligation.put("status", 3);

                                        db.collection("events").document(eventId).collection("people")
                                                .document(subordinate.getEmail()).collection("obligations")
                                                .add(obligation).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {

                                                Log.d(TAG, "Obligation " + documentReference.getId() + "successfully added for subordinate " + subordinate.getEmail());
                                                String obId = documentReference.getId();

                                                db.collection("events").document(eventId).collection("people")
                                                        .document(subordinate.getEmail()).collection("obligations")
                                                        .document(obId).update("obligation_id", obId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "Obligation id successfully registered!");
                                                    }
                                                })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error registering obligation id ", e);
                                                            }
                                                        });

                                                db.collection("events").document(eventId).collection("people")
                                                        .document(subordinate.getEmail()).update("status", "pending_obligation").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "Subordinate status successfully updated!");
                                                    }
                                                })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error updating Subordinate status ", e);
                                                            }
                                                        });

                                                int c = subordinate.getBusy_obligation_count() + 1;
                                                subordinate.setBusy_obligation_count(c);

                                                db.collection("events").document(eventId).collection("people")
                                                        .document(subordinate.getEmail()).update("busy_obligation_count", c).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "Subordinate busy obligation count successfully updated!");
                                                    }
                                                })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error updating Subordinate busy obligation count ", e);
                                                            }
                                                        });

                                                holder.fragment_child_ep_ao_iv_status.setImageResource(R.drawable.ic_jebale_te_3_tacke);
                                                subordinate.setStatus("pending_obligation");
                                                fragment_child_ep_ao_pb_pop_up.setVisibility(View.GONE);
                                                popUpDialogAssign.dismiss();
                                                Toast.makeText(context, "Obligation successfully sent to " + combinedName + "!", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding obligation to subordinate " + subordinate.getEmail(), e);
                                                Toast.makeText(context, "Error occurred while sending obligation: " + e, Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                }
                            });
                            Objects.requireNonNull(popUpDialogAssign.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            popUpDialogAssign.show();
                        }
                    }
                });

                fragment_child_ep_ao_choice_btn_right.setOnClickListener(new View.OnClickListener() {

                    Dialog popUpDialogSubObs = new Dialog(context);

                    ImageView fragment_child_ep_ao_view_sub_ops_iv_person;
                    TextView fragment_child_ep_ao_view_sub_ops_tv_user_email, fragment_child_ep_ao_view_sub_ops_tv;
                    RecyclerView fragment_child_ep_ao_view_sub_ops_rv;
                    ProgressBar fragment_child_ep_ao_view_sub_ops_pb;
                    private LinearLayoutManager obs_rLayoutManager;
                    private CollectionReference obs_obligationsRef;
                    private ArrayList<Obligation> obs_obligations = new ArrayList<>();
                    View v;

                    @Override
                    public void onClick(View view) {
                        popUpDialogChoice.dismiss();

                        popUpDialogSubObs.setContentView(R.layout.fragment_child_event_perspective_assign_obligations_view_sub_obs);

                        fragment_child_ep_ao_view_sub_ops_iv_person = popUpDialogSubObs.findViewById(R.id.fragment_child_ep_ao_view_sub_ops_iv_person);
                        fragment_child_ep_ao_view_sub_ops_tv_user_email = popUpDialogSubObs.findViewById(R.id.fragment_child_ep_ao_view_sub_ops_tv_user_email);
                        fragment_child_ep_ao_view_sub_ops_tv = popUpDialogSubObs.findViewById(R.id.fragment_child_ep_ao_view_sub_ops_tv);
                        fragment_child_ep_ao_view_sub_ops_rv = popUpDialogSubObs.findViewById(R.id.fragment_child_ep_ao_view_sub_ops_rv);
                        fragment_child_ep_ao_view_sub_ops_pb = popUpDialogSubObs.findViewById(R.id.fragment_child_ep_ao_view_sub_ops_pb);
                        fragment_child_ep_ao_view_sub_ops_pb.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.dirtierWhite), PorterDuff.Mode.MULTIPLY);
                        //v = popUpDialogSubObs.findViewById(R.id.fragment_child_ep_ao_view_sub_ops_v);

                        obs_obligationsRef = db.collection("events").document(eventId).collection("people")
                                .document(subordinate.getEmail()).collection("obligations");

                        obs_rLayoutManager = new LinearLayoutManager(popUpDialogChoice.getContext());
                        obs_rLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        fragment_child_ep_ao_view_sub_ops_rv.setLayoutManager(obs_rLayoutManager);

                        /*if (subordinate.getProfileImageUrl() != null && !subordinate.getProfileImageUrl().isEmpty()) {
                            Picasso.with(context)
                                    .load(subordinate.getProfileImageUrl())
                                    .fit()
                                    .centerCrop()
                                    .into(fragment_child_ep_ao_view_sub_ops_iv_person);
                        }

                        fragment_child_ep_ao_view_sub_ops_tv_user_email.setText(subordinate.getEmail());*/
                        obs_refreshObligationsRV();
                        Objects.requireNonNull(popUpDialogSubObs.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        popUpDialogSubObs.show();
                    }

                    private void obs_refreshObligationsRV() {
                        fragment_child_ep_ao_view_sub_ops_pb.setVisibility(View.VISIBLE);
                        obs_obligationsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                obs_obligations.clear();
                                fragment_child_ep_ao_view_sub_ops_tv.setText(R.string.loading_obligations_of_subordinate);
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Obligation y_obligation = documentSnapshot.toObject(Obligation.class);
                                    obs_obligations.add(y_obligation);
                                }
                                if (!obs_obligations.isEmpty())
                                    runAnimation(fragment_child_ep_ao_view_sub_ops_rv, obs_rLayoutManager, 1);
                                fragment_child_ep_ao_view_sub_ops_pb.setVisibility(View.GONE);

                                if (obs_obligations.isEmpty()) {
                                    fragment_child_ep_ao_view_sub_ops_tv.setText(R.string.no_obligations);
                                    fragment_child_ep_ao_view_sub_ops_tv.setVisibility(View.VISIBLE);
                                } else {
                                    fragment_child_ep_ao_view_sub_ops_tv.setVisibility(View.GONE);

                                }
                            }

                        });
                    }

                    private void runAnimation(final RecyclerView rv, final LinearLayoutManager llm, int type) {
                        //Context context = rv.getContext();
                        //LayoutAnimationController controller = null;
                        AssignObligationsSubOpsRecyclerAdapter rAdapter = new AssignObligationsSubOpsRecyclerAdapter(obs_obligations, rv.getContext(), activity, db, eventId);
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
                                        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
                                        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
                                        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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

                Objects.requireNonNull(popUpDialogChoice.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialogChoice.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return subordinates.size();
    }

    public class AssignObligationsRecyclerViewHolder extends RecyclerView.ViewHolder {

        CardView fragment_child_ep_ao_cv;
        ImageView fragment_child_ep_ao_iv_person, fragment_child_ep_ao_iv_status;
        EditText fragment_child_ep_ao_et_name, fragment_child_ep_ao_et_role;
        TextView fragment_child_ep_ao_tv_pending;

        public AssignObligationsRecyclerViewHolder(View itemView) {
            super(itemView);

            fragment_child_ep_ao_cv = itemView.findViewById(R.id.fragment_child_ep_ao_cv);
            fragment_child_ep_ao_iv_person = itemView.findViewById(R.id.fragment_child_ep_ao_iv_person);
            fragment_child_ep_ao_iv_status = itemView.findViewById(R.id.fragment_child_ep_ao_iv_status);
            fragment_child_ep_ao_et_name = itemView.findViewById(R.id.fragment_child_ep_ao_et_name);
            fragment_child_ep_ao_et_role = itemView.findViewById(R.id.fragment_child_ep_ao_et_role);
            fragment_child_ep_ao_tv_pending = itemView.findViewById(R.id.fragment_child_ep_ao_tv_pending);
        }
    }
}
