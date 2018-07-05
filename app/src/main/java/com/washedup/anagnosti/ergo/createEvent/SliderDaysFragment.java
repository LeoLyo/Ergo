package com.washedup.anagnosti.ergo.createEvent;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.washedup.anagnosti.ergo.R;

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

    private FloatingActionButton reloadDates;
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
        View rootView = inflater.inflate(R.layout.fragment_slider_days, container, false);

        reloadDates = rootView.findViewById(R.id.slider_days_btn_reload);
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
                    Toast.makeText(getContext(), R.string.vlada_je_peder, Toast.LENGTH_SHORT).show();
                }
            }
        });


        return rootView;

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
