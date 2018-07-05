package com.washedup.anagnosti.ergo.createEvent;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.washedup.anagnosti.ergo.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SliderRolesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SliderRolesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SliderRolesFragment extends Fragment {
    private static final String TAG = "SliderRolesFragment";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager rLayoutManager;
    private ArrayList<CERole> eRoleList;
    private SliderRolesRecyclerAdapter eRoleAdapter;
    private Dialog popUpDialog;

    private Button confirmEditButton;
    private FloatingActionButton createButton;
    private EditText titleEdit, descriptionEdit;
    private TextView subordinatesEdit;
    private ListView listOfAvailableRoles;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SliderRolesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SliderRolesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SliderRolesFragment newInstance(String param1, String param2) {
        SliderRolesFragment fragment = new SliderRolesFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_slider_roles, container, false);
        createButton = rootView.findViewById(R.id.slider_roles_btn_add);
        popUpDialog = new Dialog(this.getContext());

        final CESingleton singleton = CESingleton.Instance();
        eRoleList = singleton.mCERoles;
        recyclerView = rootView.findViewById(R.id.slider_roles_rv);
        rLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(rLayoutManager);
        eRoleAdapter = new SliderRolesRecyclerAdapter(this.getActivity(), inflater.getContext(), eRoleList);
        recyclerView.setAdapter(eRoleAdapter);

        /*RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvClasses.setLayoutManager(layoutManager);
        rvClasses.setItemAnimator(new DefaultItemAnimator());
        classAdapter = new ClassAdapter(inflater.getContext(), selectedClasses);
        rvClasses.setAdapter(classAdapter);*/


        createButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                popUpDialog.setContentView(R.layout.fragment_child_slider_roles_edit);

                confirmEditButton = popUpDialog.findViewById(R.id.child_slider_roles_edit_btn_confirm);
                titleEdit = popUpDialog.findViewById(R.id.child_slider_roles_edit_et_title);
                descriptionEdit = popUpDialog.findViewById(R.id.child_slider_roles_edit_et_description);
                listOfAvailableRoles = popUpDialog.findViewById(R.id.child_slider_roles_edit_lv_subordinates);
                final SliderRolesInnerCustomAdapter cAdapter = new SliderRolesInnerCustomAdapter(getActivity(), eRoleList);
                listOfAvailableRoles.setAdapter(cAdapter);
                if (listOfAvailableRoles.getScaleY() > 200) {
                    listOfAvailableRoles.setScaleY(200);
                }
                listOfAvailableRoles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        CERole tempRole = eRoleList.get(i);

                        if (tempRole.isChecked()) {
                            tempRole.setChecked(false);
                        } else {
                            tempRole.setChecked(true);
                        }
                        eRoleList.set(i, tempRole);

                        cAdapter.updateRecords(eRoleList);
                    }
                });

                confirmEditButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CESingleton singleton1 = CESingleton.Instance();
                        String titleCheck = titleEdit.getText().toString();
                        if (titleCheck.matches("")) {
                            Toast.makeText(getContext(), "Title is empty. Please enter title.", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean ifExists = false;
                            for (int i = 0; i < eRoleList.size(); i++) {
                                if (titleCheck.matches(eRoleList.get(i).getName())) {
                                    ifExists = true;
                                    break;
                                }
                            }
                            if (ifExists) {
                                Toast.makeText(getContext(), "A position with this title already exists. Please select another title.", Toast.LENGTH_SHORT).show();
                            } else {

                                CERole newRole = new CERole(titleCheck, singleton1.mCERoles.size());
                                ArrayList<CERole> selectedSubs = new ArrayList<CERole>();
                                for (int j = 0; j < eRoleList.size(); j++) {
                                    if (eRoleList.get(j).isChecked()) {
                                        selectedSubs.add(eRoleList.get(j));
                                    }
                                }
                                newRole.setDescription(descriptionEdit.getText().toString());
                                newRole.setSubordinates(selectedSubs);
                                singleton1.mCERoles.add(newRole);
                                eRoleList = singleton1.mCERoles;
                                eRoleAdapter = new SliderRolesRecyclerAdapter(getActivity(), getContext(), eRoleList);
                                recyclerView.setAdapter(eRoleAdapter);
                                refreshRoleList(eRoleList);
                                popUpDialog.dismiss();
                            }
                        }
                    }
                });
                Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialog.show();
                singleton.somethingDoneInEveryPart[3] = true;
            }
        });

        return rootView;
    }

    private void refreshRoleList(ArrayList<CERole> roleListForRefreshing) {
        for (int i = 0; i < roleListForRefreshing.size(); i++) {
            if (roleListForRefreshing.get(i).isChecked() == true) {
                roleListForRefreshing.get(i).setChecked(false);
            }
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
