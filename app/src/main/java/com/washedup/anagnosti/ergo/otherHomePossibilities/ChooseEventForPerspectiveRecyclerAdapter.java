package com.washedup.anagnosti.ergo.otherHomePossibilities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.eventPerspective.Event;
import com.washedup.anagnosti.ergo.eventPerspective.EventPerspectiveActivity;
import com.washedup.anagnosti.ergo.transformations.CircleTransform;

import java.util.ArrayList;

public class ChooseEventForPerspectiveRecyclerAdapter extends RecyclerView.Adapter<ChooseEventForPerspectiveRecyclerAdapter.ChooseEventForPerspectiveRecyclerViewHolder> {

    private ArrayList<Event> events;
    private Context context;


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

        final Event tempEvent = events.get(holder.getAdapterPosition());

        holder.child_activity_cefp_et.setText(tempEvent.getEvent_name());

        //Toast.makeText(context, "Number " + holder.getAdapterPosition() + ": " + tempEvent.toString(), Toast.LENGTH_SHORT).show();
        //URL url = new URL(tempEvent.getEvent_image_url());
        //Bitmap bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        //Uri uri = Uri.parse(tempEvent.getEvent_image_url());
        //final Bitmap drawableBitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
        final ViewTreeObserver observer= holder.child_activity_cefp_iv.getViewTreeObserver();
        final ImageView iv = holder.child_activity_cefp_iv;
        //iv.setImageURI(uri);
        //final CardView cv = holder.child_activity_cefp_cv;

        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Log.d("Log", "Height of ImageView: " + iv.getHeight());
                        Log.d("Log", "Width of ImageView: " + iv.getWidth());

                        int dimensionW = iv.getWidth();
                        int dimensionH = iv.getHeight();

                        Picasso.with(context)
                                .load(tempEvent.getEvent_image_url())
                                .resize(dimensionW,dimensionH)
                                .centerCrop()
                                .transform(new CircleTransform())
                                .into(iv);


                       /* Bitmap changedDrawableBitmap  = ThumbnailUtils.extractThumbnail(drawableBitmap,dimensionW,dimensionH);
                        //drawableBitmap = ThumbnailUtils.extractThumbnail(drawableBitmap,dimension,dimension);

                        //Bitmap darkened = darkenBitMap(changedDrawableBitmap);
                        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(context.getResources(),changedDrawableBitmap);
                        dr.setCornerRadius(16);
                        iv.setBackground(dr);
                        cv.setCardElevation(0);*/
                    }
                });

        holder.child_activity_cefp_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,EventPerspectiveActivity.class);
                intent.putExtra("current_event",tempEvent);
                context.startActivity(intent);
            }
        });


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
