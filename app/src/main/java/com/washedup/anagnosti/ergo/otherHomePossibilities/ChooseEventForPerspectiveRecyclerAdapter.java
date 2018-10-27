package com.washedup.anagnosti.ergo.otherHomePossibilities;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.eventPerspective.Event;

import java.util.ArrayList;

public class ChooseEventForPerspectiveRecyclerAdapter extends RecyclerView.Adapter<ChooseEventForPerspectiveRecyclerAdapter.ChooseEventForPerspectiveRecyclerViewHolder> {

    private ArrayList<Event> events;
    private Context context;

    //private int lastPosition = -1;

    public ChooseEventForPerspectiveRecyclerAdapter(ArrayList<Event> events, Context context){
        this.events=events;
        this.context=context;
    }

    @Override
    public ChooseEventForPerspectiveRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_activity_choose_event_for_perspective, parent,false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ChooseEventForPerspectiveRecyclerViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(ChooseEventForPerspectiveRecyclerViewHolder holder, int position) {

        Event tempEvent = events.get(holder.getAdapterPosition());
        holder.child_activity_cefp_et.setText(tempEvent.getEvent_name());
        //setAnimation(holder.itemView,position);

    }

   /* private void setAnimation(View itemView, int position) {
        if(position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_right);
            itemView.startAnimation(animation);
            lastPosition = position;
        }
    }*/

    /*@Override
    public void onViewDetachedFromWindow(ChooseEventForPerspectiveRecyclerViewHolder holder) {

        (holder).clearAnimation();
    }*/

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ChooseEventForPerspectiveRecyclerViewHolder extends RecyclerView.ViewHolder {

        CardView child_activity_cefp_cv;
        EditText child_activity_cefp_et;
        ImageView child_activity_cefp_iv;
       // FrameLayout child_activity_cefp_container;

        public ChooseEventForPerspectiveRecyclerViewHolder(View itemView) {
            super(itemView);

            child_activity_cefp_cv = itemView.findViewById(R.id.child_activity_choose_event_for_perspective_cv);
            child_activity_cefp_et = itemView.findViewById(R.id.child_activity_choose_event_for_perspective_et);
            child_activity_cefp_iv = itemView.findViewById(R.id.child_activity_choose_event_for_perspective_iv);
           // child_activity_cefp_container = itemView.findViewById(R.id.child_activity_choose_event_for_perspective_item_layout_container);

        }

       /* public void clearAnimation() {
            child_activity_cefp_container.clearAnimation();
        }*/
    }
}
