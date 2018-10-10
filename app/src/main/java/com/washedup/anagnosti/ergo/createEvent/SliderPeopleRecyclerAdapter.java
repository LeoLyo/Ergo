package com.washedup.anagnosti.ergo.createEvent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.washedup.anagnosti.ergo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SliderPeopleRecyclerAdapter extends RecyclerView.Adapter<SliderPeopleRecyclerAdapter.SliderPeopleRecyclerViewHolder>{


    private ArrayList<CEPerson> mEventPeople;
    private Context mContext;
    private Activity mActivity;
    private RecyclerView bloodyRecyclerView;


    private RecyclerView.LayoutManager rLayoutManager;
    private SliderPeopleRecyclerAdapter eRoleAdapter;


    public SliderPeopleRecyclerAdapter(Activity activity, Context context, ArrayList<CEPerson> mEventPeople){
        this.mActivity = activity;
        this.mContext = context;
        this.mEventPeople = mEventPeople;
    }


    @Override
    public SliderPeopleRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_slider_people, parent, false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new SliderPeopleRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SliderPeopleRecyclerViewHolder holder, int position) {

        final CEPerson tempEventPerson = mEventPeople.get(holder.getAdapterPosition());
        holder.child_slider_people_et_event_role.setText(tempEventPerson.getRoleOfIndividual().getName());
        holder.child_slider_people_et_email.setText(tempEventPerson.getEmail());
        holder.child_slider_people_et_parent_email.setText(tempEventPerson.getParentOfIndividual().getEmail());

        final CESingleton singleton = CESingleton.Instance();
        mEventPeople= singleton.mCEPeople;

        final int holdersPosition = holder.getAdapterPosition();

        final PopupMenu popupMenu = new PopupMenu(mActivity,holder.child_slider_people_iv_more_options);
        popupMenu.getMenuInflater().inflate(R.menu.child_slider_people_more_menu, popupMenu.getMenu());

        holder.child_slider_people_iv_more_options.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    Dialog popUpDialog = new Dialog(mContext);
                    Button editConfirmButton;
                    EditText editEmailText;
                    Spinner editRoleSpinner, editParentSpinner;

                    Button removeButton;
                    TextView personTV, messageText;

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getTitle().equals("EDIT")){

                            popUpDialog.setContentView(R.layout.fragment_child_slider_people_edit);
                            editEmailText = popUpDialog.findViewById(R.id.child_slider_people_edit_et_email);
                            editRoleSpinner = popUpDialog.findViewById(R.id.child_slider_people_edit_spinner);
                            editConfirmButton = popUpDialog.findViewById(R.id.child_slider_people_edit_btn_confirm);
                            editParentSpinner = popUpDialog.findViewById(R.id.child_slider_people_edit_parent_spinner);
                            editEmailText.setText(tempEventPerson.getEmail());

                            ArrayAdapter<CERole> spinnerAdapter = new ArrayAdapter<CERole>(mContext,android.R.layout.simple_spinner_item,singleton.mCERoles);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            editRoleSpinner.setAdapter(spinnerAdapter);
                            int spinnerPosition = spinnerAdapter.getPosition(tempEventPerson.getRoleOfIndividual());
                            editRoleSpinner.setSelection(spinnerPosition);


                            editRoleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    CERole spinnerRole = (CERole) editRoleSpinner.getSelectedItem();
                                    ArrayList<CEPerson> spinnerListOfPeople = new ArrayList<>();
                                    spinnerListOfPeople.add(new CEPerson("nobody@nonono.com",new CERole("Nothing",-8),0));
                                    for(int k=0;k<singleton.mCERoles.size();k++){
                                        for(int z=0;z<singleton.mCERoles.get(k).getSubordinates().size();z++){
                                            if(singleton.mCERoles.get(k).getSubordinates().get(z).equals(spinnerRole)){
                                                for(int t=0;t<mEventPeople.size();t++){
                                                    if(mEventPeople.get(t).getRoleOfIndividual().equals(singleton.mCERoles.get(k)))
                                                        spinnerListOfPeople.add(mEventPeople.get(t));

                                                }
                                            }
                                        }
                                    }
                                    if(spinnerListOfPeople.isEmpty()){

                                        ArrayList<String> nothing = new ArrayList<>();
                                        nothing.add("No parent.");
                                        ArrayAdapter<String> newSpinnerAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item,nothing);
                                        newSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        editParentSpinner.setAdapter(newSpinnerAdapter);
                                    }else{

                                        ArrayAdapter<CEPerson> newSpinnerAdapter = new ArrayAdapter<CEPerson>(mContext,android.R.layout.simple_spinner_item,spinnerListOfPeople);
                                        newSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        editParentSpinner.setAdapter(newSpinnerAdapter);
                                        int parentSpinnerPosition = newSpinnerAdapter.getPosition(tempEventPerson.getParentOfIndividual());
                                        editParentSpinner.setSelection(parentSpinnerPosition);

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
                                        Toast.makeText(mContext, "Email is empty. Please enter an email.", Toast.LENGTH_SHORT).show();
                                    }else if(!isEmailValid(emailCheck)){
                                        Toast.makeText(mContext, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                                    }else {
                                        singleton.mCEPeople.get(holdersPosition).setEmail(emailCheck);
                                        singleton.mCEPeople.get(holdersPosition).setRoleOfIndividual((CERole) editRoleSpinner.getSelectedItem());
                                        singleton.mCEPeople.get(holdersPosition).setParentOfIndividual((CEPerson) editParentSpinner.getSelectedItem());

                                        bloodyRecyclerView = mActivity.findViewById(R.id.slider_people_rv);
                                        rLayoutManager = new LinearLayoutManager(mActivity);
                                        bloodyRecyclerView.setLayoutManager(rLayoutManager);
                                        eRoleAdapter = new SliderPeopleRecyclerAdapter(mActivity, mContext, mEventPeople);
                                        bloodyRecyclerView.setAdapter(eRoleAdapter);
                                        popUpDialog.dismiss();


                                    }

                                }
                            });
                            Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            popUpDialog.show();

                        }
                        else if(menuItem.getTitle().equals("DELETE")){

                            popUpDialog.setContentView(R.layout.fragment_child_slider_people_remove);
                            removeButton=popUpDialog.findViewById(R.id.child_slider_people_remove_btn_remove);
                            messageText=popUpDialog.findViewById(R.id.child_slider_people_remove_message);
                            personTV=popUpDialog.findViewById(R.id.child_slider_people_remove_tv_role);
                            personTV.setText(singleton.mCEPeople.get(holdersPosition).getEmail());
                            String message="Are you sure you want to remove the following person?";
                            messageText.setText(message);
                            removeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CEPerson removedPerson = singleton.mCEPeople.get(holdersPosition);
                                    singleton.mCEPeople.remove(holdersPosition);
                                    singleton.mUsedEmails.remove(removedPerson.getEmail());
                                    for(int i=0;i<singleton.mCEPeople.size();i++){
                                        if(singleton.mCEPeople.get(i).getParentOfIndividual().equals(removedPerson)){
                                            singleton.mCEPeople.get(i).setParentOfIndividual(new CEPerson("nobody@nonono.com",new CERole("Nothing",-8),0));
                                        }
                                    }
                                    mEventPeople=singleton.mCEPeople;

                                    bloodyRecyclerView = mActivity.findViewById(R.id.slider_people_rv);
                                    rLayoutManager = new LinearLayoutManager(mActivity);
                                    bloodyRecyclerView.setLayoutManager(rLayoutManager);
                                    eRoleAdapter = new SliderPeopleRecyclerAdapter(mActivity,mContext,mEventPeople);
                                    bloodyRecyclerView.setAdapter(eRoleAdapter);
                                    popUpDialog.dismiss();

                                }
                            });
                            Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            popUpDialog.show();
                        }
                        return true;

                    }

                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mEventPeople.size();
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }



    public class SliderPeopleRecyclerViewHolder extends RecyclerView.ViewHolder {

        EditText child_slider_people_et_event_role, child_slider_people_et_email, child_slider_people_et_parent_email;
        ImageView child_slider_people_iv_more_options;
        CardView child_slider_people_cv_person;

        public SliderPeopleRecyclerViewHolder(View itemView) {
            super(itemView);

            child_slider_people_et_event_role=itemView.findViewById(R.id.child_slider_people_et_event_role);
            child_slider_people_et_email=itemView.findViewById(R.id.child_slider_people_et_email);
            child_slider_people_et_parent_email=itemView.findViewById(R.id.child_slider_people_et_parent_email);
            child_slider_people_iv_more_options=itemView.findViewById(R.id.child_slider_people_iv_more_options);
            child_slider_people_cv_person=itemView.findViewById(R.id.child_slider_people_cv_main);


        }
    }
}
