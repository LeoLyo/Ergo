package com.washedup.anagnosti.ergo.eventPerspective;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.washedup.anagnosti.ergo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BreakCentreRecyclerAdapter extends RecyclerView.Adapter<BreakCentreRecyclerAdapter.BreakCentreViewHolder> {

    private static final String TAG = "BreakCentreRA";

    private ArrayList<Break> breaks;
    private Context context;
    private Activity activity;
    private String eventId;

    public BreakCentreRecyclerAdapter() {
    }

    public BreakCentreRecyclerAdapter(ArrayList<Break> breaks, Context context, Activity activity, String eventId) {
        this.breaks = breaks;
        this.context = context;
        this.activity = activity;
        this.eventId = eventId;
    }

    @Override
    public BreakCentreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_event_perspective_break_centre, parent, false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new BreakCentreRecyclerAdapter.BreakCentreViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(BreakCentreViewHolder holder, int position) {
        Break cur_break = breaks.get(holder.getAdapterPosition());

        holder.fragment_child_ep_bc_et_duration.setText(cur_break.getDuration());

        SimpleDateFormat format = new SimpleDateFormat("hh:mm dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cur_break.getEndOfBreakTime());
        String dateatm = format.format(calendar.getTime());
        holder.fragment_child_ep_bc_et_date.setText(dateatm);

        if(cur_break.getIs_breaking()==0){
            holder.fragment_child_ep_bc_et_status.setText(R.string.break_pending);
        }else if(cur_break.getIs_breaking() == 1){
            holder.fragment_child_ep_bc_et_status.setText(R.string.break_on_break);
        }else if(cur_break.getIs_breaking() == 2){
            holder.fragment_child_ep_bc_et_status.setText(R.string.break_ended);
        }else if(cur_break.getIs_breaking() == 3){
            holder.fragment_child_ep_bc_et_status.setText(R.string.break_declined);
        }else
            holder.fragment_child_ep_bc_et_status.setText(R.string.error_date_status);


    }

    @Override
    public int getItemCount() {
        return breaks.size();
    }

    public class BreakCentreViewHolder extends RecyclerView.ViewHolder {

        CardView fragment_child_ep_bc_cv;
        EditText fragment_child_ep_bc_et_duration, fragment_child_ep_bc_et_date, fragment_child_ep_bc_et_status;

        public BreakCentreViewHolder(View itemView) {
            super(itemView);

            fragment_child_ep_bc_cv = itemView.findViewById(R.id.fragment_child_ep_bc_cv);
            fragment_child_ep_bc_et_duration = itemView.findViewById(R.id.fragment_child_ep_bc_et_duration);
            fragment_child_ep_bc_et_date = itemView.findViewById(R.id.fragment_child_ep_bc_et_date);
            fragment_child_ep_bc_et_status = itemView.findViewById(R.id.fragment_child_ep_bc_et_status);

        }
    }
}
