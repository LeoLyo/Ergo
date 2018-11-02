package com.washedup.anagnosti.ergo.otherHomePossibilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.eventPerspective.Event;
import com.washedup.anagnosti.ergo.eventPerspective.EventPerspectiveActivity;

import java.util.ArrayList;
import java.util.Objects;

public class EventInvitationsRecyclerAdapter extends RecyclerView.Adapter<EventInvitationsRecyclerAdapter.EventInvitationsRecyclerViewHolder> {

    private ArrayList<Event> events;
    private Context context;
    private Activity activity;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public EventInvitationsRecyclerAdapter(ArrayList<Event> events, Context context, Activity activity) {
        this.events=events;
        this.context=context;
        this.activity=activity;
    }

    @Override
    public EventInvitationsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_activity_event_invitations, parent,false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new EventInvitationsRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EventInvitationsRecyclerViewHolder holder, int position) {
        final Event tempEvent = events.get(holder.getAdapterPosition());
        String fullDate = events.get(holder.getAdapterPosition()).getDate_start() + " - " + events.get(holder.getAdapterPosition()).getDate_end();
        holder.child_activity_ei_et_name.setText(tempEvent.getEvent_name());
        holder.child_activity_ei_et_date.setText(fullDate);

        holder.child_activity_ei_cv.setOnClickListener(new View.OnClickListener() {

            Dialog popUpDialog = new Dialog(context);
            ImageView child_activity_event_invitations_iv_pop_up;
            EditText child_activity_event_invitations_et_name_pop_up, child_activity_event_invitations_et_description_pop_up;
            Button child_activity_event_invitations_button_accept_pop_up, child_activity_event_invitations_button_decline_pop_up;
            ProgressBar child_activity_event_invitations_pb_pop_up;
            @Override
            public void onClick(View view) {
                popUpDialog.setContentView(R.layout.child_activity_event_invitations_pop_up);

                child_activity_event_invitations_iv_pop_up = popUpDialog.findViewById(R.id.child_activity_event_invitations_iv_pop_up);
                child_activity_event_invitations_et_name_pop_up = popUpDialog.findViewById(R.id.child_activity_event_invitations_et_name_pop_up);
                child_activity_event_invitations_et_description_pop_up = popUpDialog.findViewById(R.id.child_activity_event_invitations_et_description_pop_up);
                child_activity_event_invitations_button_accept_pop_up = popUpDialog.findViewById(R.id.child_activity_event_invitations_button_accept_pop_up);
                child_activity_event_invitations_button_decline_pop_up = popUpDialog.findViewById(R.id.child_activity_event_invitations_button_decline_pop_up);
                child_activity_event_invitations_pb_pop_up = popUpDialog.findViewById(R.id.child_activity_event_invitations_pb_pop_up);

                Picasso.with(context)
                        .load(tempEvent.getEvent_image_url())
                        .fit()
                        .centerCrop()
                        .into(child_activity_event_invitations_iv_pop_up);

                child_activity_event_invitations_et_name_pop_up.setText(tempEvent.getEvent_name());
                child_activity_event_invitations_et_description_pop_up.setText(tempEvent.getDescription_of_event());


                child_activity_event_invitations_button_accept_pop_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        child_activity_event_invitations_pb_pop_up.setVisibility(View.VISIBLE);
                        db = FirebaseFirestore.getInstance();
                        mAuth = FirebaseAuth.getInstance();
                        String eventId = events.get(holder.getAdapterPosition()).getEvent_id();
                        final DocumentReference eventRef = db.collection("events").document(eventId);
                        String userEmail = mAuth.getCurrentUser().getEmail();

                        eventRef.update("invited_users", FieldValue.arrayRemove(userEmail));
                        eventRef.update("accepted_users", FieldValue.arrayUnion(userEmail));

                        popUpDialog.dismiss();
                        child_activity_event_invitations_pb_pop_up.setVisibility(View.GONE);
                        Intent intent = new Intent(context,EventPerspectiveActivity.class);
                        intent.putExtra("current_event",tempEvent);
                        context.startActivity(intent);
                        activity.finish();
                    }
                });

                child_activity_event_invitations_button_decline_pop_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        child_activity_event_invitations_pb_pop_up.setVisibility(View.VISIBLE);
                        db = FirebaseFirestore.getInstance();
                        mAuth = FirebaseAuth.getInstance();
                        String eventId = events.get(holder.getAdapterPosition()).getEvent_id();
                        final DocumentReference eventRef = db.collection("events").document(eventId);
                        String userEmail = mAuth.getCurrentUser().getEmail();

                        eventRef.update("invited_users", FieldValue.arrayRemove(userEmail));
                        eventRef.update("declined_users", FieldValue.arrayUnion(userEmail));

                        popUpDialog.dismiss();
                        child_activity_event_invitations_pb_pop_up.setVisibility(View.GONE);
                        context.startActivity(new Intent(context,EventInvitationsActivity.class));
                        activity.finish();
                    }
                });
                Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventInvitationsRecyclerViewHolder extends RecyclerView.ViewHolder {

        CardView child_activity_ei_cv;
        EditText child_activity_ei_et_name, child_activity_ei_et_date;

        public EventInvitationsRecyclerViewHolder(View itemView) {
            super(itemView);

            child_activity_ei_cv = itemView.findViewById(R.id.child_activity_event_invitations_cv);
            child_activity_ei_et_name = itemView.findViewById(R.id.child_activity_event_invitations_et_name);
            child_activity_ei_et_date = itemView.findViewById(R.id.child_activity_event_invitations_et_date);
        }
    }
}
