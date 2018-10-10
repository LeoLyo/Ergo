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
import android.widget.Button;
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
    private Calendar calendar;
    private int currentHour;
    private int currentMinute;
    private TimePickerDialog timerPickerDialog;

    public SliderDaysRecyclerAdapter(Context context, ArrayList<CEDay> eventDays) {
        this.context = context;
        this.eventDays = eventDays;
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public SliderDaysRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_child_slider_days, parent, false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new SliderDaysRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SliderDaysRecyclerViewHolder holder, int position) {

        final CESingleton singleton = CESingleton.Instance();

        String temp = "Day " + (position + 1) + " | " + singleton.dates.get(position);
        holder.dateTV.setText(temp);

        holder.editIV.setOnClickListener(new View.OnClickListener() {
            Dialog popUpDialog = new Dialog(context);
            private Button editConfirmButton;
            EditText editStartTime, editEndTime;

            @Override
            public void onClick(View view) {
                popUpDialog.setContentView(R.layout.fragment_child_slider_days_edit);
                editConfirmButton = popUpDialog.findViewById(R.id.child_slider_days_edit_btn_confirm);
                editStartTime = popUpDialog.findViewById(R.id.child_slider_days_edit_s_time);
                editEndTime = popUpDialog.findViewById(R.id.child_slider_days_edit_e_time);


                editStartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        currentMinute = calendar.get(Calendar.MINUTE);

                        timerPickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                boolean isMinuteSingleDigit = (minutes >= 0 && minutes < 10);
                                String combinedStartTime = "";
                                if (isMinuteSingleDigit) {
                                    combinedStartTime = hourOfDay + ":0" + minutes;

                                } else {
                                    combinedStartTime = hourOfDay + ":" + minutes;
                                }
                                boolean isHourSingleDigit = (hourOfDay >=0 && hourOfDay < 10);
                                if(isHourSingleDigit){
                                    combinedStartTime = "0"+combinedStartTime;
                                }
                                singleton.mCEDays.get(holder.getAdapterPosition()).setTimeStart(combinedStartTime);
                                editStartTime.setText(combinedStartTime);
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

                        timerPickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                boolean isMinuteSingleDigit = (minutes >= 0 && minutes < 10);
                                String combinedEndTime = "";
                                if (isMinuteSingleDigit) {
                                    combinedEndTime = hourOfDay + ":0" + minutes;

                                } else {
                                    combinedEndTime = hourOfDay + ":" + minutes;
                                }
                                boolean isHourSingleDigit = (hourOfDay >=0 && hourOfDay < 10);
                                if(isHourSingleDigit){
                                    combinedEndTime = "0"+combinedEndTime;
                                }
                                singleton.mCEDays.get(holder.getAdapterPosition()).setTimeEnd(combinedEndTime);
                                editEndTime.setText(combinedEndTime);
                            }
                        }, currentHour, currentMinute, true);
                        timerPickerDialog.show();
                    }
                });

                editConfirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String temp = "Day " + (holder.getAdapterPosition() + 1) + " | " + singleton.mCEDays.get(holder.getAdapterPosition()).getDate();
                        holder.dateTV.setText(temp);
                        holder.startTime.setText(singleton.mCEDays.get(holder.getAdapterPosition()).getTimeStart());
                        holder.endTime.setText(singleton.mCEDays.get(holder.getAdapterPosition()).getTimeEnd());

                        if(!(singleton.mCEDays.get(holder.getAdapterPosition()).getTimeStart().isEmpty())&&!(singleton.mCEDays.get(holder.getAdapterPosition()).getTimeEnd().isEmpty())){
                            singleton.isDayAdded.set(holder.getAdapterPosition(),true);
                        }else{
                            singleton.isDayAdded.set(holder.getAdapterPosition(),false);
                        }

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
