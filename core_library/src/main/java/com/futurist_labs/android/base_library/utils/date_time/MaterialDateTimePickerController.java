package com.futurist_labs.android.base_library.utils.date_time;

import android.support.v4.app.FragmentActivity;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Galeen on 6/15/2018.
 */
public class MaterialDateTimePickerController extends BaseDateTimePickerController {

    public MaterialDateTimePickerController(FragmentActivity activity, Callback callback) {
        super(activity, callback);
    }

    @Override
    void datePicker() {
        prepareDatePicker();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        onSetDate(year, monthOfYear, dayOfMonth);
                    }
                },
                mYear, mMonth, mDay
        );

        if (titleDateView != null) {
            dpd.setTitle(titleDateView.getText().toString());
        } else if (titleDate != EMPTY) {
            dpd.setTitle(activity.getString(titleDate));
        }
        if (minDate > 0) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(minDate);
            dpd.setMinDate(calendar);
        }

        if (maxDate > 0) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(maxDate);
            dpd.setMaxDate(calendar);
        }

        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        if (accentColor != 0) {
            dpd.setAccentColor(accentColor);
        }
        dpd.show(((FragmentActivity) activity).getSupportFragmentManager(), "dpd");
    }


    @Override
    void timePicker() {
        prepareTimePicker();
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        onSetTime(hourOfDay, minute);
                    }
                }
                , mHour, mMinute, is24);
        if (titleTimeView != null) {
            timePickerDialog.setTitle(titleTimeView.getText().toString());
        } else if (titleTime != EMPTY) {
            timePickerDialog.setTitle(activity.getString(titleTime));
        }
        if (minDate > 0) {
            Calendar min = new GregorianCalendar();
            min.setTimeInMillis(minDate);
            if (min.get(Calendar.YEAR) == year && min.get(Calendar.MONTH) == month
                    && min.get(Calendar.DAY_OF_MONTH) == day) {
//                we have selected minimum day
                timePickerDialog.setMinTime(min.get(Calendar.HOUR_OF_DAY), min.get(Calendar.MINUTE), 0);
            }
        }

        if (maxDate > 0) {
            Calendar max = new GregorianCalendar();
            max.setTimeInMillis(maxDate);
            if (max.get(Calendar.YEAR) == year && max.get(Calendar.MONTH) == month
                    && max.get(Calendar.DAY_OF_MONTH) == day) {
                timePickerDialog.setMaxTime(max.get(Calendar.HOUR_OF_DAY), max.get(Calendar.MINUTE), 0);
            }
        }

        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_1);
        if (accentColor != 0) {
            timePickerDialog.setAccentColor(accentColor);
        }
        timePickerDialog.show(((FragmentActivity) activity).getSupportFragmentManager(), "timePickerDialog");
    }
}
