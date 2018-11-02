package com.washedup.anagnosti.ergo.eventPerspective;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.washedup.anagnosti.ergo.R;

public class ListOfPeopleFragment extends Fragment {

    Event currentEvent;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_perspective_list_of_people, container,false);
        currentEvent = ((EventPerspectiveActivity)this.getActivity()).getCurrentEvent();
        //Toast.makeText(getContext(), "TESTING INSIDE LIST ocv: " + currentEvent.toString(), Toast.LENGTH_SHORT).show();
        //TextView tv = rootView.findViewById(R.id.fep_list_of_people_tv);
        //tv.setText(currentEvent.getEvent_name());


        return rootView;
    }
}
