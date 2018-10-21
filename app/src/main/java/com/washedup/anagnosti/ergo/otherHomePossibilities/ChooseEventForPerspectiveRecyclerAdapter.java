package com.washedup.anagnosti.ergo.otherHomePossibilities;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.eventPerspective.Event;

import java.util.ArrayList;

public class ChooseEventForPerspectiveRecyclerAdapter extends RecyclerView.Adapter<ChooseEventForPerspectiveRecyclerAdapter.ChooseEventForPerspectiveRecyclerViewHolder> {

    private ArrayList<Event> events;



    public ChooseEventForPerspectiveRecyclerAdapter(ArrayList<Event> events){
        this.events=events;
    }

    @Override
    public ChooseEventForPerspectiveRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_activity_choose_event_for_perspective,parent,false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ChooseEventForPerspectiveRecyclerViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(ChooseEventForPerspectiveRecyclerViewHolder holder, int position) {

    }




    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ChooseEventForPerspectiveRecyclerViewHolder extends RecyclerView.ViewHolder {

        CardView child_activity_cefp_cv;
        EditText child_activity_cefp_et;
        ImageView child_activity_cefp_iv;

        public ChooseEventForPerspectiveRecyclerViewHolder(View itemView) {
            super(itemView);

            child_activity_cefp_cv = itemView.findViewById(R.id.child_activity_choose_event_for_perspective_cv);
            child_activity_cefp_et = itemView.findViewById(R.id.child_activity_choose_event_for_perspective_et);
            child_activity_cefp_iv = itemView.findViewById(R.id.child_activity_choose_event_for_perspective_iv);


        }
    }
}
