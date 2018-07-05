package com.washedup.anagnosti.ergo.createEvent;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Klasa za odabir datume u DatePicker-u i njihovo modifikovanje.
 */
public class MyDatePickerDialog implements View.OnClickListener {

    Context localContext;
    EditText localEditText;
    int dayE;

    /**
     * Konstruktor za preuzimanje konteksta, TextView vizuelne komponente u koju ce se smestati tekst i jedinstven broj dana po kome se razlikuje prvi dan od poslednjeg.
     */
    public MyDatePickerDialog(Context context, EditText editText, int dayE) {

        this.localContext = context;
        this.localEditText = editText;
        this.dayE = dayE;
    }

    /**
     * Nakon odabira datuma, proveri se o kom je danu rec. U zavisnosti od izabranog dana se rezultujuci string formatira na odredjen nacin.
     */
    @Override
    public void onClick(View view) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(localContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                String dayFormated = dayOfMonth + "|" + monthOfYear + "|" + year;

                CESingleton singleton = CESingleton.Instance();
                if (dayE == 67) {
                    localEditText.setText(dayFormated);
                    singleton.dateStartChanged = true;

                } else if (dayE == 76) {
                    localEditText.setText(dayFormated);
                    singleton.dateEndChanged = true;
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }


}

