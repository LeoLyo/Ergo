package com.washedup.anagnosti.ergo.eventPerspective;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.washedup.anagnosti.ergo.R;

import java.util.ArrayList;
import java.util.Objects;

public class MyObligationsRecyclerAdapter extends RecyclerView.Adapter<MyObligationsRecyclerAdapter.MyObligationsViewHolder> {

    private static final String TAG = "MyObligationsRA";
    private ArrayList<Obligation> obligations;
    private Context context;
    private Activity activity;
    private FirebaseFirestore db;
    private String eventId;
    private double busy_ob_count;

    public MyObligationsRecyclerAdapter() {
    }

    public MyObligationsRecyclerAdapter(ArrayList<Obligation> obligations, Context context, Activity activity, FirebaseFirestore db, String eventId, double busy_ob_count) {
        this.obligations = obligations;
        this.context = context;
        this.activity = activity;
        this.db = db;
        this.eventId = eventId;
        this.busy_ob_count = busy_ob_count;
    }

    @Override
    public MyObligationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_event_perspective_my_obligations, parent, false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new MyObligationsRecyclerAdapter.MyObligationsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyObligationsViewHolder holder, int position) {
        final Obligation obligation = obligations.get(holder.getAdapterPosition());

        if (obligation.getStatus() == 0)
            holder.fragment_child_ep_mo_iv.setImageResource(R.drawable.ic_yes);
        else if (obligation.getStatus() == 1)
            holder.fragment_child_ep_mo_iv.setImageResource(R.drawable.ic_no);
        else if (obligation.getStatus() == 2)
            holder.fragment_child_ep_mo_iv.setImageResource(R.drawable.ic_maybe);
        else if (obligation.getStatus() == 3)
            holder.fragment_child_ep_mo_iv.setImageResource(R.drawable.ic_jebale_te_3_tacke);
        else
            holder.fragment_child_ep_mo_iv.setImageResource(R.drawable.ic_profile_picture_placeholder);

        holder.fragment_child_ep_mo_et_what.setText(obligation.getWhat());
        holder.fragment_child_ep_mo_et_where.setText(obligation.getWhere());

        holder.fragment_child_ep_mo_cv.setOnClickListener(new View.OnClickListener() {

            Dialog popUpDialog = new Dialog(context);
            ImageView fragment_child_ep_mo_iv_pop_up;
            TextView fragment_child_ep_mo_tv_subordinate_pop_up;
            EditText fragment_child_ep_mo_et_what_pop_up, fragment_child_ep_mo_et_where_pop_up, fragment_child_ep_mo_et_details_pop_up;
            ImageButton fragment_child_ep_mo_btn_yes_pop_up, fragment_child_ep_mo_btn_no_pop_up, fragment_child_ep_mo_btn_maybe_pop_up;
            ProgressBar fragment_child_ep_mo_pb_pop_up;

            @Override
            public void onClick(View view) {
                popUpDialog.setContentView(R.layout.fragment_child_event_perspective_my_obligations_pop_up);

                fragment_child_ep_mo_iv_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_mo_iv_pop_up);
                fragment_child_ep_mo_tv_subordinate_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_mo_tv_subordinate_pop_up);
                fragment_child_ep_mo_et_what_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_mo_et_what_pop_up);
                fragment_child_ep_mo_et_where_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_mo_et_where_pop_up);
                fragment_child_ep_mo_et_details_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_mo_et_details_pop_up);
                fragment_child_ep_mo_btn_yes_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_mo_btn_yes_pop_up);
                fragment_child_ep_mo_btn_no_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_mo_btn_no_pop_up);
                fragment_child_ep_mo_btn_maybe_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_mo_btn_maybe_pop_up);
                fragment_child_ep_mo_pb_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_mo_pb_pop_up);
                fragment_child_ep_mo_pb_pop_up.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.dirtierWhite), PorterDuff.Mode.MULTIPLY);

                if (obligation.getStatus() == 0) {
                    fragment_child_ep_mo_iv_pop_up.setImageResource(R.drawable.ic_yes);
                    fragment_child_ep_mo_btn_yes_pop_up.setFocusable(false);
                } else if (obligation.getStatus() == 1) {
                    fragment_child_ep_mo_iv_pop_up.setImageResource(R.drawable.ic_no);
                    fragment_child_ep_mo_btn_no_pop_up.setFocusable(false);
                } else if (obligation.getStatus() == 2) {
                    fragment_child_ep_mo_iv_pop_up.setImageResource(R.drawable.ic_maybe);
                    fragment_child_ep_mo_btn_maybe_pop_up.setFocusable(false);
                } else if (obligation.getStatus() == 3)
                    fragment_child_ep_mo_iv_pop_up.setImageResource(R.drawable.ic_jebale_te_3_tacke);
                else {
                    fragment_child_ep_mo_iv_pop_up.setImageResource(R.drawable.ic_profile_picture_placeholder);
                }

                String sup;
                if (obligation.getObligation_superior() != null) {
                    sup = "Sent by: " + obligation.getObligation_superior();
                } else {
                    sup = "No superior..";
                }
                fragment_child_ep_mo_tv_subordinate_pop_up.setText(sup);

                fragment_child_ep_mo_et_what_pop_up.setText(obligation.getWhat());
                fragment_child_ep_mo_et_where_pop_up.setText(obligation.getWhere());
                fragment_child_ep_mo_et_details_pop_up.setText(obligation.getDetails());

                final DocumentReference obRef = db.collection("events").document(eventId).collection("people")
                        .document(obligation.getObligation_subordinate());

                fragment_child_ep_mo_btn_yes_pop_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (obligation.getStatus() == 0) {
                            Toast.makeText(context, "Obligation already completed.", Toast.LENGTH_SHORT).show();
                        } else {
                            fragment_child_ep_mo_pb_pop_up.setVisibility(View.VISIBLE);
                            db.collection("events").document(eventId).collection("people")
                                    .document(obligation.getObligation_subordinate()).collection("obligations")
                                    .document(obligation.getObligation_id()).update("status", 0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Obligation status successfully updated!");
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating obligation status ", e);
                                        }
                                    });
                            db.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot snapshot = transaction.get(obRef);
                                    if (obligation.getStatus() == 2 || obligation.getStatus() == 3) {
                                        double c = snapshot.getDouble("busy_obligation_count") - 1;
                                        busy_ob_count--;
                                        transaction.update(obRef, "busy_obligation_count", c);
                                        Log.d(TAG, "[T] Busy obligation count successfully updated!");
                                        obligation.setStatus(0);

                                    }
                                    transaction.update(obRef, "status", "free");

                                    return null;
                                }
                            });

                            holder.fragment_child_ep_mo_iv.setImageResource(R.drawable.ic_yes);
                            fragment_child_ep_mo_pb_pop_up.setVisibility(View.GONE);
                            popUpDialog.dismiss();
                            Toast.makeText(context, "Obligation successfully completed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                fragment_child_ep_mo_btn_no_pop_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (obligation.getStatus() == 1) {
                            Toast.makeText(context, "Obligation already declined.", Toast.LENGTH_SHORT).show();
                        } else {
                            fragment_child_ep_mo_pb_pop_up.setVisibility(View.VISIBLE);
                            db.collection("events").document(eventId).collection("people")
                                    .document(obligation.getObligation_subordinate()).collection("obligations")
                                    .document(obligation.getObligation_id()).update("status", 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Obligation status successfully updated!");
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating obligation status ", e);
                                        }
                                    });

                            db.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot snapshot = transaction.get(obRef);
                                    if (obligation.getStatus() == 2 || obligation.getStatus() == 3) {
                                        double c = snapshot.getDouble("busy_obligation_count") - 1;
                                        busy_ob_count--;
                                        transaction.update(obRef, "busy_obligation_count", c);
                                        Log.d(TAG, "[T] Busy obligation count successfully updated!");
                                        obligation.setStatus(1);

                                    }
                                    transaction.update(obRef, "status", "declined");

                                    return null;
                                }
                            });
                            holder.fragment_child_ep_mo_iv.setImageResource(R.drawable.ic_no);
                            fragment_child_ep_mo_pb_pop_up.setVisibility(View.GONE);
                            popUpDialog.dismiss();
                            Toast.makeText(context, "Obligation successfully declined!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                fragment_child_ep_mo_btn_maybe_pop_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (obligation.getStatus() == 2) {
                            Toast.makeText(context, "Obligation already started.", Toast.LENGTH_SHORT).show();
                        } else if (busy_ob_count >= 5 && obligation.getStatus()!=3) {
                            Toast.makeText(context, "You already have 5 started and/or pending obligations. Please complete your current obligations before starting new ones.", Toast.LENGTH_LONG).show();
                        } else {
                            fragment_child_ep_mo_pb_pop_up.setVisibility(View.VISIBLE);
                            db.collection("events").document(eventId).collection("people")
                                    .document(obligation.getObligation_subordinate()).collection("obligations")
                                    .document(obligation.getObligation_id()).update("status", 2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Obligation status successfully updated!");
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating obligation status ", e);
                                        }
                                    });
                            db.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot snapshot = transaction.get(obRef);
                                    if (obligation.getStatus() == 0 || obligation.getStatus() == 1) {
                                        double c = snapshot.getDouble("busy_obligation_count") + 1;
                                        busy_ob_count++;
                                        transaction.update(obRef, "busy_obligation_count", c);
                                        Log.d(TAG, "[T] Busy obligation count successfully updated!");
                                        obligation.setStatus(2);
                                    }

                                    transaction.update(obRef, "status", "working");
                                    return null;
                                }
                            });
                            holder.fragment_child_ep_mo_iv.setImageResource(R.drawable.ic_maybe);
                            fragment_child_ep_mo_pb_pop_up.setVisibility(View.GONE);
                            popUpDialog.dismiss();
                            Toast.makeText(context, "Obligation successfully started!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return obligations.size();
    }

    public class MyObligationsViewHolder extends RecyclerView.ViewHolder {

        CardView fragment_child_ep_mo_cv;
        ImageView fragment_child_ep_mo_iv;
        EditText fragment_child_ep_mo_et_what, fragment_child_ep_mo_et_where;

        public MyObligationsViewHolder(View itemView) {
            super(itemView);

            fragment_child_ep_mo_cv = itemView.findViewById(R.id.fragment_child_ep_mo_cv);
            fragment_child_ep_mo_iv = itemView.findViewById(R.id.fragment_child_ep_mo_iv);
            fragment_child_ep_mo_et_what = itemView.findViewById(R.id.fragment_child_ep_mo_et_what);
            fragment_child_ep_mo_et_where = itemView.findViewById(R.id.fragment_child_ep_mo_et_where);
        }
    }
}
