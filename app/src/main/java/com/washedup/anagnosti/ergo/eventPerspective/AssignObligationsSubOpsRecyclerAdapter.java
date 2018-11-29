package com.washedup.anagnosti.ergo.eventPerspective;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.washedup.anagnosti.ergo.R;

import java.util.ArrayList;

public class AssignObligationsSubOpsRecyclerAdapter extends RecyclerView.Adapter<AssignObligationsSubOpsRecyclerAdapter.AssignObligationsSubOpsViewHolder> {

    private static final String TAG = "AssignObligtationsSubOpsRA";
    private ArrayList<Obligation> obligations;
    private Context context;
    private Activity activity;
    private FirebaseFirestore db;
    private String eventId;

    public AssignObligationsSubOpsRecyclerAdapter(ArrayList<Obligation> obligations, Context context, Activity activity, FirebaseFirestore db, String eventId) {
        this.obligations = obligations;
        this.context = context;
        this.activity = activity;
        this.db = db;
        this.eventId = eventId;
    }

    @Override
    public AssignObligationsSubOpsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_event_perspective_assign_obligations_sub_ops, parent, false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new AssignObligationsSubOpsRecyclerAdapter.AssignObligationsSubOpsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AssignObligationsSubOpsViewHolder holder, int position) {

        final Obligation obligation = obligations.get(holder.getAdapterPosition());

        if (obligation.getStatus() == 0)
            holder.fragment_child_ep_ao_sub_ops_iv.setImageResource(R.drawable.ic_yes);
        else if (obligation.getStatus() == 1)
            holder.fragment_child_ep_ao_sub_ops_iv.setImageResource(R.drawable.ic_no);
        else if (obligation.getStatus() == 2)
            holder.fragment_child_ep_ao_sub_ops_iv.setImageResource(R.drawable.ic_maybe);
        else if (obligation.getStatus() == 3)
            holder.fragment_child_ep_ao_sub_ops_iv.setImageResource(R.drawable.ic_jebale_te_3_tacke);
        else
            holder.fragment_child_ep_ao_sub_ops_iv.setImageResource(R.drawable.ic_profile_picture_placeholder);

        holder.fragment_child_ep_ao_sub_ops_et_what.setText(obligation.getWhat());
        holder.fragment_child_ep_ao_sub_ops_et_what.setKeyListener( null );
        holder.fragment_child_ep_ao_sub_ops_et_what.setFocusable( false );
        holder.fragment_child_ep_ao_sub_ops_et_what.setCursorVisible(false);

        holder.fragment_child_ep_ao_sub_ops_et_where.setText(obligation.getWhere());
        holder.fragment_child_ep_ao_sub_ops_et_where.setKeyListener( null );
        holder.fragment_child_ep_ao_sub_ops_et_where.setFocusable( false );
        holder.fragment_child_ep_ao_sub_ops_et_where.setCursorVisible(false);

        holder.fragment_child_ep_ao_sub_ops_et_details.setText(obligation.getDetails());
        holder.fragment_child_ep_ao_sub_ops_et_details.setKeyListener( null );
        holder.fragment_child_ep_ao_sub_ops_et_details.setFocusable( false );
        holder.fragment_child_ep_ao_sub_ops_et_details.setCursorVisible(false);

    }

    @Override
    public int getItemCount() {
        return obligations.size();
    }

    public class AssignObligationsSubOpsViewHolder extends RecyclerView.ViewHolder {

        CardView fragment_child_ep_ao_sub_ops_cv;
        ImageView fragment_child_ep_ao_sub_ops_iv;
        EditText fragment_child_ep_ao_sub_ops_et_what, fragment_child_ep_ao_sub_ops_et_where, fragment_child_ep_ao_sub_ops_et_details;

        public AssignObligationsSubOpsViewHolder(View itemView) {
            super(itemView);

            fragment_child_ep_ao_sub_ops_cv = itemView.findViewById(R.id.fragment_child_ep_ao_sub_ops_cv);
            fragment_child_ep_ao_sub_ops_iv = itemView.findViewById(R.id.fragment_child_ep_ao_sub_ops_iv);
            fragment_child_ep_ao_sub_ops_et_what = itemView.findViewById(R.id.fragment_child_ep_ao_sub_ops_et_what);
            fragment_child_ep_ao_sub_ops_et_where = itemView.findViewById(R.id.fragment_child_ep_ao_sub_ops_et_where);
            fragment_child_ep_ao_sub_ops_et_details = itemView.findViewById(R.id.fragment_child_ep_ao_sub_ops_et_details);
        }
    }
}
