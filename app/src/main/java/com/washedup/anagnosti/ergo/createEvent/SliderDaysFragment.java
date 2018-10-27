package com.washedup.anagnosti.ergo.createEvent;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.washedup.anagnosti.ergo.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SliderDaysFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SliderDaysFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SliderDaysFragment extends Fragment {
    private static final String TAG = "SliderDaysFragment";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager rLayoutManager;
    private SliderDaysRecyclerAdapter sliderDaysRecyclerAdapter;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SliderDaysFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SliderDaysFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SliderDaysFragment newInstance(String param1, String param2) {
        SliderDaysFragment fragment = new SliderDaysFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
        View rootView = inflater.inflate(R.layout.fragment_slider_days, container, false);

        addNewDateIfPossible = rootView.findViewById(R.id.slider_days_btn_reload);
        popUpDialog = new Dialog(this.getContext());

        final CESingleton singleton = CESingleton.Instance();
        recyclerView = rootView.findViewById(R.id.slider_days_rv);
        rLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(rLayoutManager);
        sliderDaysRecyclerAdapter = new SliderDaysRecyclerAdapter(getContext(), singleton.mCEDaysY);
        recyclerView.setAdapter(sliderDaysRecyclerAdapter);

        addNewDateIfPossible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpDialog.setContentView(R.layout.fragment_child_slider_days_edit);
                editStartTime = popUpDialog.findViewById(R.id.child_slider_days_edit_s_time);
                editEndTime = popUpDialog.findViewById(R.id.child_slider_days_edit_e_time);
                editConfirmButton = popUpDialog.findViewById(R.id.child_slider_days_edit_btn_confirm);
                editDaySpinner = popUpDialog.findViewById(R.id.child_slider_days_edit_day_spinner);

                ArrayList<CEDay> spinnerDays = new ArrayList<>();
                //String printerFormCEDays="";
                for(int i=0;i<singleton.mCEDays.size();i++){
                    //printerFormCEDays+=printerFormCEDays+" | Object(day) " + i + ": " + singleton.
                    if(!(singleton.isDayAdded.get(i))){
                        spinnerDays.add(singleton.mCEDays.get(i));

                    }
                }

                ArrayAdapter<CEDay> spinnerAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,spinnerDays);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editDaySpinner.setAdapter(spinnerAdapter);

                editStartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        calendar = Calendar.getInstance();
                        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        currentMinute = calendar.get(Calendar.MINUTE);

                        timerPickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                boolean isSingleDigit = (minutes >= 0 && minutes < 10);
                                String combinedEndTime = "";
                                if (isSingleDigit) {
                                    combinedEndTime = hourOfDay + ":0" + minutes;

                                } else {
                                    combinedEndTime = hourOfDay + ":" + minutes;
                                }
                                singleton.proxyDay.setTimeStart(combinedEndTime);
                                editStartTime.setText(combinedEndTime);
                            }
                        }, currentHour, currentMinute, true);
                        timerPickerDialog.show();
                    }
                });

                editEndTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        calendar = Calendar.getInstance();
                        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        currentMinute = calendar.get(Calendar.MINUTE);

                        timerPickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                boolean isSingleDigit = (minutes >= 0 && minutes < 10);
                                String combinedEndTime = "";
                                if (isSingleDigit) {
                                    combinedEndTime = hourOfDay + ":0" + minutes;

                                } else {
                                    combinedEndTime = hourOfDay + ":" + minutes;
                                }
                                singleton.proxyDay.setTimeEnd(combinedEndTime);
                                editEndTime.setText(combinedEndTime);
                            }
                        }, currentHour, currentMinute, true);
                        timerPickerDialog.show();
                    }
                });

                editConfirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CEDay newDay = (CEDay) editDaySpinner.getSelectedItem();
                        Toast.makeText(getContext(), "Patka: " + newDay.toString(), Toast.LENGTH_SHORT).show();
                        newDay.setTimeStart(singleton.proxyDay.getTimeStart());
                        newDay.setTimeEnd(singleton.proxyDay.getTimeEnd());
                        singleton.mCEDaysY.add(newDay);
                        for(int z=0;z<singleton.mCEDays.size();z++){
                            if(singleton.mCEDays.get(z).equals(newDay)){
                                singleton.isDayAdded.set(z,true); //IMPORTANT TO REMEMBER
                                break;
                            }
                        }

                        /*
                        * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        * !!!!!!!!!!!!!!!!!!!!REMEMBER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        * CHANGE singleton.date SO IT'S INSIDE THE DAY (IF NEEDED BUT PROLLY NEEDED)
                        * !!!!!!!!!!!!!!!!!!!!REMEMBER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        *

                        Toast.makeText(getContext(), "Number of days: " + singleton.mCEDaysY.size(), Toast.LENGTH_SHORT).show();
                        sliderDaysRecyclerAdapter = new SliderDaysRecyclerAdapter(getContext(), singleton.mCEDaysY);
                        recyclerView.setAdapter(sliderDaysRecyclerAdapter);

                        popUpDialog.dismiss();
                    }
                });

                Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialog.show();
                boolean daysFinished = true;
                for (int t=0;t<singleton.isDayAdded.size();t++){
                    if(!(singleton.isDayAdded.get(t))){
                        daysFinished=false;
                        break;
                    }
                }
                singleton.somethingDoneInEveryPart[2]=daysFinished;
            }
        });

        return rootView;
        */


        View rootView = inflater.inflate(R.layout.fragment_slider_days, container, false);

        FloatingActionButton reloadDates = rootView.findViewById(R.id.slider_days_btn_reload);
        FloatingActionButton checkCompletionDates = rootView.findViewById(R.id.slider_days_btn_check_completion);
        recyclerView = rootView.findViewById(R.id.slider_days_rv);

        final CESingleton singleton = CESingleton.Instance();

        reloadDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (singleton.currentEventDaysChanged) {

                    rLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(rLayoutManager);
                    sliderDaysRecyclerAdapter = new SliderDaysRecyclerAdapter(getContext(), singleton.mCEDays);
                    recyclerView.setAdapter(sliderDaysRecyclerAdapter);
                    singleton.currentEventDaysChanged = false;

                } else {
                    Toast.makeText(getContext(), "Days haven't been changed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkCompletionDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateMsg = "";
                int c = 0;
                for(int i=0;i<singleton.isDayAdded.size();i++){
                    if(!(singleton.isDayAdded.get(i))){
                        int no = i+1;
                        dateMsg = dateMsg + no + ", ";
                        c++;
                    }
                }

                if(c==1){
                    dateMsg=dateMsg.substring(0,dateMsg.length()-2);
                    dateMsg="The start and end times for day " + dateMsg + " have not been set yet.";
                }else if(c>1){
                    dateMsg=dateMsg.substring(0,dateMsg.length()-2);
                    dateMsg="The start and end times for days " + dateMsg + " have not been set yet.";
                }
                else{
                    dateMsg="All dates have been set successfully.";
                    singleton.somethingDoneInEveryPart[2] = true;
                }
                Toast.makeText(getContext(), dateMsg, Toast.LENGTH_LONG).show();
            }
        });


        return rootView;



    }

    @Override
    public void onResume() {
        super.onResume();
        final CESingleton singleton = CESingleton.Instance();
        rLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(rLayoutManager);
        sliderDaysRecyclerAdapter = new SliderDaysRecyclerAdapter(getContext(), singleton.mCEDays);
        recyclerView.setAdapter(sliderDaysRecyclerAdapter);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
