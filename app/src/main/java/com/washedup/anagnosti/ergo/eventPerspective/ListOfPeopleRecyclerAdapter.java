package com.washedup.anagnosti.ergo.eventPerspective;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.transformations.CircleTransform;
import com.washedup.anagnosti.ergo.transformations.RoundCornerTransform;

import java.util.ArrayList;
import java.util.Objects;

public class ListOfPeopleRecyclerAdapter extends RecyclerView.Adapter<ListOfPeopleRecyclerAdapter.ListOfPeopleRecyclerViewHolder> {

    private ArrayList<Person> people;
    private Context context;
    private Activity activity;

    public ListOfPeopleRecyclerAdapter() {
    }

    public ListOfPeopleRecyclerAdapter(ArrayList<Person> people, Context context, Activity activity) {
        this.people = people;
        this.context = context;
        this.activity = activity;
    }


    @Override
    public ListOfPeopleRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_event_perspective_list_of_people, parent, false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ListOfPeopleRecyclerViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(ListOfPeopleRecyclerViewHolder holder, int position) {

        final Person person = people.get(holder.getAdapterPosition());
        final String combinedName = person.getFirstName() + " " + person.getNickname() + " " + person.getLastName();
        if (person.getInvitation_accepted()) {
            holder.fragment_child_ep_lop_tv_pending.setVisibility(View.GONE);
            holder.fragment_child_ep_lop_et_name.setText(combinedName);
        } else {
            holder.fragment_child_ep_lop_tv_pending.setVisibility(View.VISIBLE);
            holder.fragment_child_ep_lop_et_name.setText(person.getEmail());
            //RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotate45animation);
            //holder.fragment_child_ep_lop_tv_pending.setAnimation(rotate);
            holder.fragment_child_ep_lop_cv.setClickable(false);
        }
        holder.fragment_child_ep_lop_et_role.setText(person.getRole());
        if (person.getProfileImageUrl() != null && !person.getProfileImageUrl().isEmpty()) {
            Picasso.with(context)
                    .load(person.getProfileImageUrl())
                    .fit()
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(holder.fragment_child_ep_lop_iv);
        }

        holder.fragment_child_ep_lop_cv.setOnClickListener(new View.OnClickListener() {

            Dialog popUpDialog = new Dialog(context);
            ImageView fragment_child_ep_lop_iv_pop_up;
            EditText fragment_child_ep_lop_et_person_name_pop_up;
            Button fragment_child_ep_lop_button_call_pop_up, fragment_child_ep_lop_button_message_pop_up;
            ProgressBar pb;


            @Override
            public void onClick(View view) {

                if (!person.getInvitation_accepted()) {
                    Toast.makeText(context, "User hasn't accepted the event invitation", Toast.LENGTH_SHORT).show();
                    return;
                }
                popUpDialog.setContentView(R.layout.fragment_child_event_perspective_list_of_people_pop_up);

                fragment_child_ep_lop_iv_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_lop_iv_pop_up);
                fragment_child_ep_lop_et_person_name_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_lop_et_person_name_pop_up);
                fragment_child_ep_lop_button_call_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_lop_button_call_pop_up);
                fragment_child_ep_lop_button_message_pop_up = popUpDialog.findViewById(R.id.fragment_child_ep_lop_button_message_pop_up);
                pb = popUpDialog.findViewById(R.id.fragment_child_ep_lop_pb_pop_up);
                pb.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.dirtierWhite),PorterDuff.Mode.MULTIPLY);

                if (person.getProfileImageUrl() != null && !person.getProfileImageUrl().isEmpty()) {
                    Picasso.with(context)
                            .load(person.getProfileImageUrl())
                            .fit()
                            .centerCrop()
                            .into(fragment_child_ep_lop_iv_pop_up);
                }

                fragment_child_ep_lop_et_person_name_pop_up.setText(combinedName);

                fragment_child_ep_lop_button_call_pop_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pb.setVisibility(View.VISIBLE);
                        Intent callIntent = new Intent(Intent.ACTION_VIEW);
                        callIntent.setData(Uri.parse("tel:" + person.getPhoneNumber()));
                        activity.startActivity(callIntent);
                        pb.setVisibility(View.GONE);
                        popUpDialog.dismiss();
                    }
                });
                fragment_child_ep_lop_button_message_pop_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pb.setVisibility(View.VISIBLE);
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.setData(Uri.parse("sms:" + person.getPhoneNumber()));
                        activity.startActivity(sendIntent);
                        pb.setVisibility(View.GONE);
                        popUpDialog.dismiss();
                    }
                });
                Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public class ListOfPeopleRecyclerViewHolder extends RecyclerView.ViewHolder {

        CardView fragment_child_ep_lop_cv;
        EditText fragment_child_ep_lop_et_name, fragment_child_ep_lop_et_role;
        ImageView fragment_child_ep_lop_iv;
        TextView fragment_child_ep_lop_tv_pending;

        public ListOfPeopleRecyclerViewHolder(View itemView) {
            super(itemView);

            fragment_child_ep_lop_cv = itemView.findViewById(R.id.fragment_child_ep_lop_cv);
            fragment_child_ep_lop_et_name = itemView.findViewById(R.id.fragment_child_ep_lop_et_name);
            fragment_child_ep_lop_et_role = itemView.findViewById(R.id.fragment_child_ep_lop_et_role);
            fragment_child_ep_lop_iv = itemView.findViewById(R.id.fragment_child_ep_lop_iv);
            fragment_child_ep_lop_tv_pending = itemView.findViewById(R.id.fragment_child_ep_lop_tv_pending);

        }
    }
}
