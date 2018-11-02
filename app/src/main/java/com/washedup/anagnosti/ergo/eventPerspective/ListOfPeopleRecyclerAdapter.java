package com.washedup.anagnosti.ergo.eventPerspective;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.transformations.CircleTransform;

import java.util.ArrayList;

public class ListOfPeopleRecyclerAdapter extends RecyclerView.Adapter<ListOfPeopleRecyclerAdapter.ListOfPeopleRecyclerViewHolder> {

    private ArrayList<Person> people;
    private Context context;

    public ListOfPeopleRecyclerAdapter(ArrayList<Person> people, Context context){
        this.people=people;
        this.context=context;
    }


    @Override
    public ListOfPeopleRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_event_perspective_list_of_people,parent,false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ListOfPeopleRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListOfPeopleRecyclerViewHolder holder, int position) {

        final Person person = people.get(holder.getAdapterPosition());
        String combinedName = person.getFirstName() + " " + person.getNickname() + " " + person.getLastName();
        holder.fragment_child_ep_lop_et_name.setText(combinedName);
        holder.fragment_child_ep_lop_et_role.setText(person.getRole());
        if(!person.getProfileImageUrl().isEmpty()){
            Picasso.with(context)
                .load(person.getProfileImageUrl())
                .fit()
                .centerCrop()
                .transform(new CircleTransform())
                .into(holder.fragment_child_ep_lop_iv);
        }

        holder.fragment_child_ep_lop_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        public ListOfPeopleRecyclerViewHolder(View itemView) {
            super(itemView);

            fragment_child_ep_lop_cv = itemView.findViewById(R.id.fragment_child_ep_lop_cv);
            fragment_child_ep_lop_et_name = itemView.findViewById(R.id.fragment_child_ep_lop_et_name);
            fragment_child_ep_lop_et_role = itemView.findViewById(R.id.fragment_child_ep_lop_et_role);
            fragment_child_ep_lop_iv = itemView.findViewById(R.id.fragment_child_ep_lop_iv);

        }
    }
}
