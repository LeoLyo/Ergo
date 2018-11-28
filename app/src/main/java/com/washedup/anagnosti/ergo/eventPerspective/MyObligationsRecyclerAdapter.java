package com.washedup.anagnosti.ergo.eventPerspective;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.washedup.anagnosti.ergo.R;

import java.util.ArrayList;

public class MyObligationsRecyclerAdapter extends RecyclerView.Adapter<MyObligationsRecyclerAdapter.MyObligationsViewHolder> {

    private static final String TAG = "MyObligationsRA";
    private ArrayList<Obligation> obligations;
    private Context context;
    private Activity activity;
    private FirebaseFirestore db;
    private String eventId;

    public MyObligationsRecyclerAdapter() {
    }

    public MyObligationsRecyclerAdapter(ArrayList<Obligation> obligations, Context context, Activity activity, FirebaseFirestore db, String eventId) {
        this.obligations = obligations;
        this.context = context;
        this.activity = activity;
        this.db = db;
        this.eventId = eventId;
    }

    @Override
    public MyObligationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_event_perspective_my_obligations, parent, false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new MyObligationsRecyclerAdapter.MyObligationsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyObligationsViewHolder holder, int position) {
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

                if (obligation.getStatus() == 0)
                    fragment_child_ep_mo_iv_pop_up.setImageResource(R.drawable.ic_yes);
                else if (obligation.getStatus() == 1)
                    fragment_child_ep_mo_iv_pop_up.setImageResource(R.drawable.ic_no);
                else if (obligation.getStatus() == 2)
                    fragment_child_ep_mo_iv_pop_up.setImageResource(R.drawable.ic_maybe);
                else if (obligation.getStatus() == 3)
                    fragment_child_ep_mo_iv_pop_up.setImageResource(R.drawable.ic_jebale_te_3_tacke);
                else
                    fragment_child_ep_mo_iv_pop_up.setImageResource(R.drawable.ic_profile_picture_placeholder);

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

                fragment_child_ep_mo_btn_yes_pop_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                fragment_child_ep_mo_btn_no_pop_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                fragment_child_ep_mo_btn_maybe_pop_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
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
