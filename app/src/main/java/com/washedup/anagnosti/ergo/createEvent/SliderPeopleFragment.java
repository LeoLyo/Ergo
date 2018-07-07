package com.washedup.anagnosti.ergo.createEvent;

import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.washedup.anagnosti.ergo.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SliderPeopleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SliderPeopleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SliderPeopleFragment extends Fragment {
    public static final String TAG="SliderPeopleFragment";

    private FloatingActionButton CEP5FAB;
    private Dialog popUpDialog;
    private ArrayList<CEPerson> allThePeople;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager rLayoutManager;
    private SliderPeopleRecyclerAdapter eRoleAdapter;

    private Button editConfirmButton;
    private EditText editEmailText;
    private Spinner editRoleSpinner, editParentSpinner;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SliderPeopleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SliderPeopleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SliderPeopleFragment newInstance(String param1, String param2) {
        SliderPeopleFragment fragment = new SliderPeopleFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_slider_people, container, false);
        CEP5FAB = rootView.findViewById(R.id.slider_people_btn_add);
        popUpDialog = new Dialog(this.getContext());

        final CESingleton singleton = CESingleton.Instance();
        allThePeople=singleton.mCEPeople;
        recyclerView = rootView.findViewById(R.id.slider_people_rv);
        rLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(rLayoutManager);
        eRoleAdapter = new SliderPeopleRecyclerAdapter(this.getActivity(),this.getContext(), allThePeople);
        recyclerView.setAdapter(eRoleAdapter);

        CEP5FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpDialog.setContentView(R.layout.fragment_child_slider_people_edit);
                editEmailText = popUpDialog.findViewById(R.id.child_slider_people_edit_et_email);
                editRoleSpinner = popUpDialog.findViewById(R.id.child_slider_people_edit_spinner);
                editConfirmButton = popUpDialog.findViewById(R.id.child_slider_people_edit_btn_confirm);
                editParentSpinner = popUpDialog.findViewById(R.id.child_slider_people_edit_parent_spinner);

                ArrayAdapter<CERole> spinnerAdapter = new ArrayAdapter<CERole>(getContext(),android.R.layout.simple_spinner_item,singleton.mCERoles);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editRoleSpinner.setAdapter(spinnerAdapter);

                editRoleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        CERole spinnerRole = (CERole) editRoleSpinner.getSelectedItem();
                        ArrayList<CEPerson> spinnerListOfPeople = new ArrayList<>();
                        spinnerListOfPeople.add(new CEPerson("nobody@nonono.com",new CERole("Nothing",-8),0));
                        for(int k=0;k<singleton.mCERoles.size();k++){
                            for(int z=0;z<singleton.mCERoles.get(k).getSubordinates().size();z++){
                                if(singleton.mCERoles.get(k).getSubordinates().get(z).equals(spinnerRole)){
                                    for(int t=0;t<allThePeople.size();t++){
                                        if(allThePeople.get(t).getRoleOfIndividual().equals(singleton.mCERoles.get(k)))
                                            spinnerListOfPeople.add(allThePeople.get(t));

                                    }
                                }
                            }
                        }
                        if(spinnerListOfPeople.isEmpty()){

                            ArrayList<String> nothing = new ArrayList<>();
                            nothing.add("No parent.");
                            ArrayAdapter<String> newSpinnerAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,nothing);
                            newSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            editParentSpinner.setAdapter(newSpinnerAdapter);
                        }else{

                            ArrayAdapter<CEPerson> newSpinnerAdapter = new ArrayAdapter<CEPerson>(getContext(),android.R.layout.simple_spinner_item,spinnerListOfPeople);
                            newSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            editParentSpinner.setAdapter(newSpinnerAdapter);
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                editConfirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String emailCheck = editEmailText.getText().toString();
                        if (emailCheck.matches("")) {
                            Toast.makeText(getContext(), "Email is empty. Please enter an email.", Toast.LENGTH_SHORT).show();
                        }else if(!isEmailValid(emailCheck)){
                            Toast.makeText(getContext(), "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                        }else{
                            boolean ifExists = false;
                            for (int i = 0; i < singleton.mUsedEmails.size(); i++) {
                                if (emailCheck.matches(singleton.mUsedEmails.get(i))) {
                                    ifExists = true;
                                    break;
                                }
                            }
                            if (ifExists) {
                                Toast.makeText(getContext(), "This email has already been entered in the list of people.", Toast.LENGTH_SHORT).show();
                            }else{
                                CERole selectedRole = (CERole) editRoleSpinner.getSelectedItem();
                                CEPerson selectedParent = (CEPerson) editParentSpinner.getSelectedItem();
                                CEPerson newPerson = new CEPerson(emailCheck,selectedRole,0,selectedParent);
                                singleton.mCEPeople.add(newPerson);
                                singleton.mUsedEmails.add(emailCheck);
                                allThePeople=singleton.mCEPeople;
                                Toast.makeText(getContext(), "Number of people: "+allThePeople.size(), Toast.LENGTH_SHORT).show();
                                eRoleAdapter = new SliderPeopleRecyclerAdapter(getActivity(), getContext(), allThePeople);
                                recyclerView.setAdapter(eRoleAdapter);

                                popUpDialog.dismiss();
                            }
                        }

                    }
                });
                Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialog.show();
                singleton.somethingDoneInEveryPart[4]=true;
            }
        });


        return rootView;
    }


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
