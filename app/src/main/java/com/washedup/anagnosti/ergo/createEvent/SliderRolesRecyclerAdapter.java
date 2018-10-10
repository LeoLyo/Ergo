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
import java.util.List;
import java.util.Objects;

public class SliderRolesRecyclerAdapter extends RecyclerView.Adapter<SliderRolesRecyclerAdapter.SliderRolesRecyclerViewHolder> {


    private Context recyclerContext;
    private Activity recyclerActivity;
    private List<CERole> eventRolesList;
    private ArrayList<CERole> eRoleList;
    private SliderRolesRecyclerAdapter eRoleAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager rLayoutManager;


    public SliderRolesRecyclerAdapter(Activity activity, Context context, ArrayList<CERole> eventRoles) {
        this.recyclerActivity = activity;
        this.recyclerContext = context;
        this.eventRolesList = eventRoles;
    }


    @Override
    public SliderRolesRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_slider_roles, parent, false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new SliderRolesRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SliderRolesRecyclerViewHolder holder, int position) {


        CERole tempEventRole = eventRolesList.get(position);
        holder.titleET.setText(tempEventRole.getName());
        holder.descriptionET.setText(tempEventRole.getDescription());
        List<CERole> checkedSubordinates = tempEventRole.getSubordinates();
        holder.subordinatesBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                CESingleton singleton = CESingleton.Instance();
                String tempString = singleton.mCERoles.get(holder.getAdapterPosition()).getAllSubordinatesInString();
                Toast.makeText(recyclerContext, tempString, Toast.LENGTH_SHORT).show();

            }
        });


        final CESingleton singleton = CESingleton.Instance();
        eRoleList = singleton.mCERoles;

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            Dialog popUpDialog = new Dialog(recyclerContext);
            Button confirmEditButton;
            EditText titleEdit, descriptionEdit;
            ListView listOfAvailableRoles;

            @Override
            public void onClick(View view) {
                popUpDialog.setContentView(R.layout.fragment_child_slider_roles_edit);

                confirmEditButton = popUpDialog.findViewById(R.id.child_slider_roles_edit_btn_confirm);
                titleEdit = popUpDialog.findViewById(R.id.child_slider_roles_edit_et_title);
                descriptionEdit = popUpDialog.findViewById(R.id.child_slider_roles_edit_et_description);
                listOfAvailableRoles = popUpDialog.findViewById(R.id.child_slider_roles_edit_lv_subordinates);
                ArrayList<CERole> checkedRoles = singleton.mCERoles.get(holder.getAdapterPosition()).getSubordinates();
                for (int i = 0; i < checkedRoles.size(); i++) {
                    for (int j = 0; j < eRoleList.size(); j++) {
                        if (checkedRoles.get(i).getName().matches(eRoleList.get(j).getName())) {
                            eRoleList.get(j).setChecked(true);
                        }
                    }
                }
                final SliderRolesInnerCustomAdapter cAdapter = new SliderRolesInnerCustomAdapter(recyclerActivity, eRoleList);
                titleEdit.setText(singleton.mCERoles.get(holder.getAdapterPosition()).getName());
                descriptionEdit.setText(singleton.mCERoles.get(holder.getAdapterPosition()).getDescription());
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
                        String titleCheck = titleEdit.getText().toString();
                        if (titleCheck.matches("")) {
                            Toast.makeText(recyclerContext, "Title is empty. Please enter title.", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean ifExists = false;
                            if (!titleCheck.matches(singleton.mCERoles.get(holder.getAdapterPosition()).getName())) {
                                for (int i = 0; i < eRoleList.size(); i++) {
                                    if (titleCheck.matches(eRoleList.get(i).getName())) {
                                        ifExists = true;
                                        break;
                                    }
                                }
                            }

                            if (ifExists) {
                                Toast.makeText(recyclerContext, "A position with this title already exists. Please select another title.", Toast.LENGTH_SHORT).show();
                            } else {

                                ArrayList<CERole> selectedSubs = new ArrayList<>();
                                for (int j = 0; j < eRoleList.size(); j++) {
                                    if (eRoleList.get(j).isChecked()) {
                                        selectedSubs.add(eRoleList.get(j));
                                    }
                                }
                                singleton.mCERoles.get(holder.getAdapterPosition()).setName(titleCheck);
                                singleton.mCERoles.get(holder.getAdapterPosition()).setDescription(descriptionEdit.getText().toString());
                                singleton.mCERoles.get(holder.getAdapterPosition()).setSubordinates(selectedSubs);

                                recyclerView = recyclerActivity.findViewById(R.id.slider_roles_rv);
                                rLayoutManager = new LinearLayoutManager(recyclerActivity);
                                recyclerView.setLayoutManager(rLayoutManager);
                                eRoleAdapter = new SliderRolesRecyclerAdapter(recyclerActivity, recyclerContext, eRoleList);
                                recyclerView.setAdapter(eRoleAdapter);
                                refreshRoleList(eRoleList);
                                popUpDialog.dismiss();
                            }
                        }
                    }
                });
                Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialog.show();
            }

        });

        holder.mainCardView.setOnLongClickListener(new View.OnLongClickListener() {

            Dialog popUpDialog = new Dialog(recyclerContext);
            Button removeButton;
            TextView roleTV, messageText;

            @Override
            public boolean onLongClick(View view) {
                popUpDialog.setContentView(R.layout.fragment_child_slider_roles_remove);
                removeButton = popUpDialog.findViewById(R.id.child_slider_roles_remove_btn_remove);
                messageText = popUpDialog.findViewById(R.id.child_slider_roles_remove_message);
                roleTV = popUpDialog.findViewById(R.id.child_slider_roles_remove_tv_role);
                roleTV.setText(singleton.mCERoles.get(holder.getAdapterPosition()).getName());
                String message = "Are you sure you want to remove the following role?";
                messageText.setText(message);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CERole removedRole = singleton.mCERoles.get(holder.getAdapterPosition());
                        singleton.mCERoles.remove(holder.getAdapterPosition());
                        for (int i = 0; i < singleton.mCERoles.size(); i++) {
                            for (int j = 0; j < singleton.mCERoles.get(i).getSubordinates().size(); j++) {
                                if (singleton.mCERoles.get(i).getSubordinates().get(j).equals(removedRole)) {
                                    singleton.mCERoles.get(i).getSubordinates().remove(j);
                                }
                            }
                        }
                        eRoleList = singleton.mCERoles;

                        recyclerView = recyclerActivity.findViewById(R.id.slider_roles_rv);
                        rLayoutManager = new LinearLayoutManager(recyclerActivity);
                        recyclerView.setLayoutManager(rLayoutManager);
                        eRoleAdapter = new SliderRolesRecyclerAdapter(recyclerActivity, recyclerContext, eRoleList);
                        recyclerView.setAdapter(eRoleAdapter);
                        popUpDialog.dismiss();
                    }
                });
                Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialog.show();
                return true;
            }
        });


    }


    private void refreshRoleList(ArrayList<CERole> roleListForRefreshing) {
        for (int i = 0; i < roleListForRefreshing.size(); i++) {
            if (roleListForRefreshing.get(i).isChecked()) {
                roleListForRefreshing.get(i).setChecked(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return eventRolesList.size();
    }

    public class SliderRolesRecyclerViewHolder extends RecyclerView.ViewHolder {

        EditText titleET, descriptionET;
        Button editBtn, subordinatesBtn;
        CardView mainCardView;

        public SliderRolesRecyclerViewHolder(View itemView) {
            super(itemView);

            titleET = itemView.findViewById(R.id.child_slider_roles_ed_title);
            descriptionET = itemView.findViewById(R.id.child_slider_roles_ed_description);
            editBtn = itemView.findViewById(R.id.child_slider_roles_btn_edit);
            subordinatesBtn = itemView.findViewById(R.id.child_slider_roles_btn_subordinates);
            mainCardView = itemView.findViewById(R.id.child_slider_roles_cv_main);
        }
    }
}
