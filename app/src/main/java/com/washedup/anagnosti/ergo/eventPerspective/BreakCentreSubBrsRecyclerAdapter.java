package com.washedup.anagnosti.ergo.eventPerspective;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.firestore.FirebaseFirestore;
import com.washedup.anagnosti.ergo.R;

import java.util.ArrayList;
import java.util.Map;

public class BreakCentreSubBrsRecyclerAdapter extends RecyclerView.Adapter<BreakCentreSubBrsRecyclerAdapter.BreakCentreViewHolder> {

    private static final String TAG = "AssignObligtationsSubOpsRA";
    private ArrayList<Person> subsWithBreakRequests;
    private Context context;
    private Activity activity;
    private FirebaseFirestore db;
    private String eventId;

    public BreakCentreSubBrsRecyclerAdapter(ArrayList<Person> subsWithBreakRequests, Context context, Activity activity, FirebaseFirestore db, String eventId) {
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
    public void onBindViewHolder(BreakCentreViewHolder holder, int position) {

        Person sub = subsWithBreakRequests.get(holder.getAdapterPosition());
        final String combinedName = sub.getFirstName() + " " + sub.getNickname() + " " + sub.getLastName();

        holder.fragment_child_ep_bc_sub_brs_card_et_name.setText(combinedName);
        String durz = sub.getBreaks().get(0).getDuration();
        holder.fragment_child_ep_bc_sub_brs_card_et_duration.setText(durz);

        holder.fragment_child_ep_bc_sub_brs_card_btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.fragment_child_ep_bc_sub_brs_card_btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return subsWithBreakRequests.size();
    }

    public class BreakCentreViewHolder extends RecyclerView.ViewHolder {
        EditText fragment_child_ep_bc_sub_brs_card_et_name, fragment_child_ep_bc_sub_brs_card_et_duration;
        ImageButton fragment_child_ep_bc_sub_brs_card_btn_yes, fragment_child_ep_bc_sub_brs_card_btn_no;

        public BreakCentreViewHolder(View itemView) {
            super(itemView);
            fragment_child_ep_bc_sub_brs_card_et_name = itemView.findViewById(R.id.fragment_child_ep_bc_sub_brs_card_et_name);
            fragment_child_ep_bc_sub_brs_card_et_duration = itemView.findViewById(R.id.fragment_child_ep_bc_sub_brs_card_et_duration);
            fragment_child_ep_bc_sub_brs_card_btn_yes = itemView.findViewById(R.id.fragment_child_ep_bc_sub_brs_card_btn_yes);
            fragment_child_ep_bc_sub_brs_card_btn_no = itemView.findViewById(R.id.fragment_child_ep_bc_sub_brs_card_btn_no);

        }
    }
}
