package com.washedup.anagnosti.ergo.createEvent;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.washedup.anagnosti.ergo.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class SliderDaysRecyclerAdapter extends RecyclerView.Adapter<SliderDaysRecyclerAdapter.SliderDaysRecyclerViewHolder> {

    private Context context;
    private ArrayList<CEDay> eventDays;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    private static LayoutInflater inflater = null;
    TimePickerDialog timerPickerDialog;

    public SliderDaysRecyclerAdapter(Context context, ArrayList<CEDay> eventDays) {
        this.context = context;
        this.eventDays = eventDays;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public SliderDaysRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_slider_days, parent, false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new SliderDaysRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SliderDaysRecyclerViewHolder holder, final int position) {

        final CESingleton singleton = CESingleton.Instance();

        String temp = "Day " + (position + 1) + " | " + singleton.dates.get(position);
        holder.dateTV.setText(temp);

        holder.editIV.setOnClickListener(new View.OnClickListener() {
            Dialog popUpDialog = new Dialog(context);
            ImageView confirmIV;
            EditText eStartTime, eEndTime;

            @Override
            public void onClick(View view) {
                popUpDialog.setContentView(R.layout.fragment_child_slider_days_edit);
                confirmIV = popUpDialog.findViewById(R.id.child_slider_days_edit_img_add);
                eStartTime = popUpDialog.findViewById(R.id.child_slider_days_edit_s_time);
                eEndTime = popUpDialog.findViewById(R.id.child_slider_days_edit_e_time);


                eStartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        currentMinute = calendar.get(Calendar.MINUTE);

                        timerPickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                String combinedStartTime = hourOfDay + ":" + minutes;
                                singleton.mCEDays.get(position).setTimeStart(combinedStartTime);
                            }
                        }, currentHour, currentMinute, true);
                        timerPickerDialog.show();
                    }
                });

                eEndTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        calendar = Calendar.getInstance();
                        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        currentMinute = calendar.get(Calendar.MINUTE);

                        timerPickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                boolean isSingleDigit = (minutes >= 0 && minutes < 10);
                                String combinedEndTime = "";
                                if (isSingleDigit) {
                                    combinedEndTime = hourOfDay + ":0" + minutes;

                                } else {
                                    combinedEndTime = hourOfDay + ":" + minutes;
                                }
                                singleton.mCEDays.get(position).setTimeStart(combinedEndTime);
                            }
                        }, currentHour, currentMinute, true);
                        timerPickerDialog.show();
                    }
                });

                confirmIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String temp = "Day " + (position + 1) + " | " + singleton.dates.get(position);
                        holder.dateTV.setText(temp);
                        holder.startTime.setText(singleton.mCEDays.get(position).getTimeStart());
                        holder.endTime.setText(singleton.mCEDays.get(position).getTimeEnd());
                        popUpDialog.dismiss();
                    }
                });
                Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialog.show();
            }
        });

        holder.endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return eventDays.size();
    }

    public class SliderDaysRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView dateTV;
        EditText startTime, endTime;
        ImageView editIV;

        public SliderDaysRecyclerViewHolder(View itemView) {
            super(itemView);

            dateTV = itemView.findViewById(R.id.child_slider_days_tv_day);
            startTime = itemView.findViewById(R.id.child_slider_days_s_time);
            endTime = itemView.findViewById(R.id.child_slider_days_e_time);
            editIV = itemView.findViewById(R.id.child_slider_days_img_edit);
        }
    }
}
