package com.futurist_labs.android.base_library.utils.date_time;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;

import androidx.fragment.app.FragmentActivity;

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
        Calendar minCal = null;
        if (minDate > 0) {
            minCal = new GregorianCalendar();
            minCal.setTimeInMillis(minDate);
        }
        if (minHour >= 0 && minMinute >= 0) {
            if (isToday(minCal)) {
                timePickerDialog.setMinTime(minCal.get(Calendar.HOUR_OF_DAY) > minHour ? minCal.get(Calendar.HOUR_OF_DAY) : minHour,
                        minCal.get(Calendar.MINUTE) > minMinute ? minCal.get(Calendar.MINUTE) : minMinute,
                        minSeconds < 0 ? 0 : minSeconds);
            } else {
                timePickerDialog.setMinTime(minHour, minMinute, minSeconds < 0 ? 0 : minSeconds);
            }
        } else if (isToday(minCal)) {
//                we have selected minimum day
            timePickerDialog.setMinTime(minCal.get(Calendar.HOUR_OF_DAY), minCal.get(Calendar.MINUTE), 0);
        }

        Calendar maxCal = new GregorianCalendar();
        maxCal.setTimeInMillis(maxDate);
        if (maxHour >= 0 && maxMinute >= 0) {
            if (isToday(maxCal)) {
                timePickerDialog.setMaxTime(maxCal.get(Calendar.HOUR_OF_DAY) > maxHour ? maxHour :
                                maxCal.get(Calendar.HOUR_OF_DAY),
                        maxCal.get(Calendar.MINUTE) > maxMinute ? maxMinute : maxCal.get(Calendar.MINUTE),
                        maxSeconds < 0 ? 0 : maxSeconds);
            } else {
                timePickerDialog.setMaxTime(maxHour, maxMinute, maxSeconds < 0 ? 0 : maxSeconds);
            }
        } else if (isToday(maxCal)) {
            timePickerDialog.setMaxTime(maxCal.get(Calendar.HOUR_OF_DAY), maxCal.get(Calendar.MINUTE), 0);
        }

        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_1);
        if (accentColor != 0) {
            timePickerDialog.setAccentColor(accentColor);
        }
        timePickerDialog.show(((FragmentActivity) activity).getSupportFragmentManager(), "timePickerDialog");
    }

    private boolean isToday(Calendar date) {
        return date != null && date.get(Calendar.YEAR) == year && date.get(Calendar.MONTH) == month
                && date.get(Calendar.DAY_OF_MONTH) == day;
    }
}
