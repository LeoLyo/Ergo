package com.washedup.anagnosti.ergo.eventPerspective;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.washedup.anagnosti.ergo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BreakCentreSubBrsRecyclerAdapter extends RecyclerView.Adapter<BreakCentreSubBrsRecyclerAdapter.BreakCentreViewHolder> {

    private static final String TAG = "AssignObsSubOpsRA";
    private ArrayList<Person> subsWithBreakRequests;
    private Context context;
    private Activity activity;
    private FirebaseFirestore db;
    private String eventId;
    private Dialog popUpDialog;

    public BreakCentreSubBrsRecyclerAdapter(Dialog popUpDialog, ArrayList<Person> subsWithBreakRequests, Context context, Activity activity, FirebaseFirestore db, String eventId) {
        this.popUpDialog = popUpDialog;
        this.subsWithBreakRequests = subsWithBreakRequests;
        this.context = context;
        this.activity = activity;
        this.db = db;
        this.eventId = eventId;
    }

    @Override
    public BreakCentreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_event_perspective_break_centre_sub_brs_card, parent, false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new BreakCentreSubBrsRecyclerAdapter.BreakCentreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BreakCentreViewHolder holder, int position) {

        final Person sub = subsWithBreakRequests.get(holder.getAdapterPosition());
        final String combinedName = sub.getFirstName() + " " + sub.getNickname() + " " + sub.getLastName();

        holder.fragment_child_ep_bc_sub_brs_card_et_name.setText(combinedName);
        int wStat = sub.getBreaks_whole();
        String stats = "";
        if (wStat == 0)
            stats = "They haven't taken any breaks yet. You should congratulate them.";
        else if (wStat == 1)
            stats = "Let this one slide, it's their 2nd break request.";
        else if (wStat > 1 && wStat <= 5)
            stats = "They've only had " + wStat + " breaks during the event, they're not asking for much.";
        else if (wStat > 5 && wStat <= 10)
            stats = "They've already had " + wStat + " breaks during the event. Hold up cowboy.";
        else if (wStat > 10 && wStat <= 15)
            stats = "Okay, I can understand 10 during the whole event, but " + wStat + " breaks? Come on.";
        else if (wStat > 15)
            stats = "That's it, I'm done. Only leaving the number here: " + wStat;
        holder.fragment_child_ep_bc_sub_brs_card_et_statistics.setText(stats);

        holder.fragment_child_ep_bc_sub_brs_card_btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = holder.fragment_child_ep_bc_sub_brs_card_et_duration.getText().toString().trim();
                if (time.isEmpty()) {
                    holder.fragment_child_ep_bc_sub_brs_card_et_duration.setError(context.getResources().getString(R.string.minute_format_warning));
                    holder.fragment_child_ep_bc_sub_brs_card_et_duration.requestFocus();
                } else {
                    Long endTime = System.currentTimeMillis();
                    Long durationMinutes = Long.parseLong(time);
                    durationMinutes *= 60000;
                    endTime += durationMinutes;


                    final String bsub = sub.getEmail();
                    String bid = sub.getBreakRequestId();

                    WriteBatch batch = db.batch();
                    DocumentReference dRef = db.collection("events").document(eventId).collection("people").document(bsub).collection("breaks").document(bid);
                    batch.update(dRef, "duration", time);
                    batch.update(dRef, "endOfBreakTime", endTime);
                    batch.update(dRef, "is_breaking", 1);
                    batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            db.collection("events").document(eventId).collection("people").document(bsub).update("breakRequestId", "no_request_yet");
                            subsWithBreakRequests.remove(sub);
                            Toast.makeText(context, combinedName + " has been allowed to take a break.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, combinedName + " has been allowed to take a break.");
                            popUpDialog.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Error allowing " + combinedName + " to take a break: " + e, Toast.LENGTH_LONG).show();
                            Log.w(TAG, "Error allowing " + combinedName + " to take a break: ", e);
                        }
                    });
                    //db.collection("events").document(eventId).collection("people").document(bsub).collection("breaks").document(bid).update("duration",time);
                }


            }
        });

        holder.fragment_child_ep_bc_sub_brs_card_btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String bsub = sub.getEmail();
                String bid = sub.getBreakRequestId();

                db.collection("events").document(eventId).collection("people").document(bsub).collection("breaks").document(bid).update("is_breaking", 3);
                db.collection("events").document(eventId).collection("people").document(bsub).update("breakRequestId", "no_request_yet");
                subsWithBreakRequests.remove(sub);
                Toast.makeText(context, combinedName + " has NOT been allowed to take a break, they need to work more.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, combinedName + " has NOT been allowed to take a break, they need to work more.");
                popUpDialog.dismiss();


            }
        });
    }

    @Override
    public int getItemCount() {
        return subsWithBreakRequests.size();
    }

    public class BreakCentreViewHolder extends RecyclerView.ViewHolder {
        TextView fragment_child_ep_bc_sub_brs_card_et_name, fragment_child_ep_bc_sub_brs_card_et_statistics;
        EditText fragment_child_ep_bc_sub_brs_card_et_duration;
        ImageButton fragment_child_ep_bc_sub_brs_card_btn_yes, fragment_child_ep_bc_sub_brs_card_btn_no;

        public BreakCentreViewHolder(View itemView) {
            super(itemView);
            fragment_child_ep_bc_sub_brs_card_et_name = itemView.findViewById(R.id.fragment_child_ep_bc_sub_brs_card_et_name);
            fragment_child_ep_bc_sub_brs_card_et_statistics = itemView.findViewById(R.id.fragment_child_ep_bc_sub_brs_card_et_statistics);
            fragment_child_ep_bc_sub_brs_card_et_duration = itemView.findViewById(R.id.fragment_child_ep_bc_sub_brs_card_et_duration);
            fragment_child_ep_bc_sub_brs_card_btn_yes = itemView.findViewById(R.id.fragment_child_ep_bc_sub_brs_card_btn_yes);
            fragment_child_ep_bc_sub_brs_card_btn_no = itemView.findViewById(R.id.fragment_child_ep_bc_sub_brs_card_btn_no);

        }
    }
}
