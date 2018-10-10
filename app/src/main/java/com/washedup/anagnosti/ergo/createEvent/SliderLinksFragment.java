package com.washedup.anagnosti.ergo.createEvent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.firestore.GeoPoint;
import com.washedup.anagnosti.ergo.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SliderLinksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SliderLinksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SliderLinksFragment extends Fragment {
    private static final String TAG = "SliderLinksFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int PLACE_PICKER_REQUEST = 101;
    private static final int CHOOSE_IMAGE_REQUEST = 102;

    private String firstDayDateArray;
    private String lastDayDateArray;
    private long dayOfEventCounter;

    EditText createEventFirstDay, createEventLastDay, createEventLocation, createEventImageView;
    Button createEventButtonConfirmInputsPage2;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SliderLinksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SliderLinksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SliderLinksFragment newInstance(String param1, String param2) {
        SliderLinksFragment fragment = new SliderLinksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firstDayDateArray = "";
        lastDayDateArray = "";
        dayOfEventCounter = -7;

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_slider_links, container, false);

        createEventFirstDay = rootView.findViewById(R.id.slider_links_first_day);
        createEventLastDay = rootView.findViewById(R.id.slider_links_last_day);
        createEventLocation = rootView.findViewById(R.id.slider_links_location);
        createEventImageView = rootView.findViewById(R.id.slider_links_event_image);
        createEventButtonConfirmInputsPage2 = rootView.findViewById(R.id.slider_links_btn_save);

        createEventFirstDay.setOnClickListener(new MyDatePickerDialog(getContext(), createEventFirstDay, 67));
        createEventLastDay.setOnClickListener(new MyDatePickerDialog(getContext(), createEventLastDay, 76));

        createEventLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(getActivity());
                    //intent.setFlags(0);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);


                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        createEventImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Choosing an image from storage", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select EventDay Image"), CHOOSE_IMAGE_REQUEST);
            }
        });

        createEventButtonConfirmInputsPage2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                firstDayDateArray = createEventFirstDay.getText().toString();
                lastDayDateArray = createEventLastDay.getText().toString();
                if (createEventFirstDay.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please select first day of event.", Toast.LENGTH_SHORT).show();
                } else if (createEventLastDay.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please select last day of event.", Toast.LENGTH_SHORT).show();
                } else {

                    long diff = -1;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd|MM|yyyy");
                    try {
                        Date dateStart = simpleDateFormat.parse(firstDayDateArray);
                        Date dateEnd = simpleDateFormat.parse(lastDayDateArray);
                        //time is always 00:00:00 so rounding should help to ignore missing hour when going from winter to summer time as well as the extra hour in the other direction
                        diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
                        diff++;
                        dayOfEventCounter = diff;
                        if (dayOfEventCounter < 0) {
                            dayOfEventCounter = -1;
                            Toast.makeText(getActivity(), "Invalid dates: please make sure the last day is after the first day of the event.", Toast.LENGTH_SHORT).show();
                        } else {

                            CESingleton singleton = CESingleton.Instance();
                            singleton.currentNumberOfDays = dayOfEventCounter;
                            if (singleton.dateStartChanged && singleton.dateEndChanged) {
                                singleton.currentEventDaysChanged = true;
                                singleton.dateStartChanged = false;
                                singleton.dateEndChanged = false;

                                singleton.dates.clear();
                                singleton.mCEDays.clear();
                                singleton.isDayAdded.clear();
                                singleton.mCEDaysY.clear();



                                Calendar calS = Calendar.getInstance();
                                calS.setTime(dateStart);

                                Calendar calE = Calendar.getInstance();
                                calE.setTime(dateEnd);
                                Calendar cal = new GregorianCalendar();

                                while (!calS.after(calE)) {

                                    singleton.dates.add(calS.getTime());
                                    calS.add(Calendar.DATE, 1);
                                }

                                for (int i = 0; i < singleton.currentNumberOfDays; i++) {
                                    singleton.mCEDays.add(new CEDay());
                                    singleton.mCEDays.get(i).setDate(singleton.dates.get(i));
                                    singleton.isDayAdded.add(false);
                                }

                            }



                            Toast.makeText(getContext(), "Information successfully entered.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), "Number of days: " + singleton.mCEDays.size() + " | Number of dates: " + singleton.dates.size(), Toast.LENGTH_SHORT).show();
                            //singleton.mEventDays.remove(2);
                            //singleton.mEventDays.add(ev);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                CESingleton singleton = CESingleton.Instance();
                singleton.somethingDoneInEveryPart[1] = true;
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                CESingleton singleton = CESingleton.Instance();
                Place place = PlacePicker.getPlace(this.getContext(), data);
                singleton.locationCoordinates = new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
                singleton.locationAddress = place.getAddress().toString();
                singleton.locationName = place.getName().toString();
                createEventLocation.setText(place.getAddress().toString());
            }
        }
        if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uriEventImage = data.getData();
            CESingleton singleton = CESingleton.Instance();
            singleton.uriEventImage = uriEventImage;
            String itxt = "Image selected.";
            createEventImageView.setText(itxt);

        }
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
