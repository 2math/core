package com.futurist_labs.android.base_library.utils.date_time;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.futurist_labs.android.base_library.R;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
 * Created by Galeen on 6/15/2018.
 */
public class NativeDateTimePickerController extends BaseDateTimePickerController {

    public NativeDateTimePickerController(Activity activity, Callback callback) {
        super(activity, callback);
    }

    public BaseDateTimePickerController setStyleDate(int styleDate) {
        this.styleDate = styleDate;
        return this;
    }

    public BaseDateTimePickerController setStyleTime(int styleTime) {
        this.styleTime = styleTime;
        return this;
    }

    public BaseDateTimePickerController setTitleDateView(@StringRes int titleDate, @ColorRes int colorBkg, @ColorRes int textColor, int sizeSp) {
        titleDateView = setTitleValues(titleDate, colorBkg, textColor, sizeSp);
        return this;
    }

    public BaseDateTimePickerController setTitleTimeView(@StringRes int titleDate, @ColorRes int colorBkg, @ColorRes int textColor, int sizeSp) {
        titleTimeView = setTitleValues(titleDate, colorBkg, textColor, sizeSp);
        return this;
    }

    private TextView setTitleValues(@StringRes int titleDate, @ColorRes int colorBkg, @ColorRes int textColor, int sizeSp) {
        TextView title = getTileTextView();
        title.setBackgroundColor(ContextCompat.getColor(activity, colorBkg));
        title.setTextColor(ContextCompat.getColor(activity, textColor));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp);
        title.setText(titleDate);
        return title;
    }

    /**
     * @return prepared text view on which you can update properties and set for title
     */
    @NonNull
    public TextView getTileTextView() {
        TextView tv = new TextView(activity);
        // Create a TextView programmatically
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setPadding(10, 10, 10, 10);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorAccent));
        return tv;
    }

    public void setTitleDateView(TextView titleDateView) {
        this.titleDateView = titleDateView;
    }

    public void setTitleTimeView(TextView titleTimeView) {
        this.titleTimeView = titleTimeView;
    }

    @Override
    void datePicker() {
        prepareDatePicker();

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, styleDate,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        onSetDate(year, monthOfYear, dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        if (titleDateView != null) {
            datePickerDialog.setCustomTitle(titleDateView);
        } else if (titleDate != EMPTY) {
            datePickerDialog.setTitle(titleDate);
        }
        if(minDate>0){
            datePickerDialog.getDatePicker().setMinDate(minDate);
        }
        datePickerDialog.show();
    }

    @Override
    void timePicker() {
        prepareTimePicker();

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(activity, styleTime,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        onSetTime(hourOfDay,minute);
                    }
                }, mHour, mMinute, is24);
        if (titleTimeView != null) {
            timePickerDialog.setCustomTitle(titleTimeView);
        } else if (titleTime != EMPTY) {
            timePickerDialog.setTitle(titleTime);
        }

        timePickerDialog.show();
    }
}
