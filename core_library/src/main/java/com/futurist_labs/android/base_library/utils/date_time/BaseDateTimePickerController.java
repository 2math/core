package com.futurist_labs.android.base_library.utils.date_time;

import android.app.Activity;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Galeen on 6/6/2018.
 */
public abstract class BaseDateTimePickerController {
    public static final int BOTH = 83;
    public static final int DATE_ONLY = 84;
    public static final int TIME_ONLY = 85;
    public static final int EMPTY = -1;
    int mYear, year;
    int mMonth, month;
    int mDay, day;

    int mHour, hour;
    int mMinute, minute;
    Activity activity;
    Callback callback;
    int type = BOTH;
    boolean is24 = false;
    int titleDate = EMPTY, titleTime = EMPTY;
    int styleDate = 0, styleTime = 0;//0 is default style
    TextView titleDateView, titleTimeView;
    long minDate, maxDate;
    int minHour = -1, minMinute = -1, minSeconds = -1, maxHour = -1, maxMinute = -1, maxSeconds = -1;

    public BaseDateTimePickerController(Activity activity, Callback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    abstract void datePicker();


    abstract void timePicker();

    /**
     * Show first date and then time picker with this date and time
     */
    public void showDateTimePicker(int mYear, int mMonth, int mDay, int mHour, int mMinute) {
        this.mYear = mYear;
        this.mMonth = mMonth;
        this.mDay = mDay;
        this.mHour = mHour;
        this.mMinute = mMinute;
        type = BOTH;
        datePicker();
    }

    /**
     * Show first date and then time picker with current date-time
     */
    public void showDateTimePicker() {
        type = BOTH;
        mYear = EMPTY;
        mHour = EMPTY;
        datePicker();
    }

    /**
     * Show  date picker with current date
     */
    public void showDatePicker() {
        type = DATE_ONLY;
        mYear = EMPTY;
        datePicker();
    }

    /**
     * Show date picker with this date
     */
    public void showDatePicker(int mYear, int mMonth, int mDay) {
        this.mYear = mYear;
        this.mMonth = mMonth;
        this.mDay = mDay;
        type = DATE_ONLY;
        datePicker();
    }

    /**
     * Show time picker with current time
     */
    public void showTimePicker() {
        type = TIME_ONLY;
        mHour = EMPTY;
        timePicker();
    }

    /**
     * Show time picker with this time
     */
    public void showTimePicker(int mHour, int mMinute) {
        this.mHour = mHour;
        this.mMinute = mMinute;
        type = TIME_ONLY;
        timePicker();
    }

    int accentColor;

    public BaseDateTimePickerController setAccentColor(int color) {
        accentColor = color;
        return this;
    }

    public BaseDateTimePickerController setIs24(boolean is24) {
        this.is24 = is24;
        return this;
    }

    public BaseDateTimePickerController setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public BaseDateTimePickerController setTitleDate(int titleDate) {
        this.titleDate = titleDate;
        return this;
    }

    public BaseDateTimePickerController setTitleTime(int titleTime) {
        this.titleTime = titleTime;
        return this;
    }

    public BaseDateTimePickerController setMinDate(long minDate) {
        this.minDate = minDate;
        return this;
    }

    public BaseDateTimePickerController setMaxDate(long maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    /**
     * 24-hour clock
     * This option is available only for MaterialDateTimePickerController, for NativeDateTimePickerController is
     * ignored and only MinDate and MaxDate works.
     * min max hours,minutes and seconds are with priority over min max Date, for setting min/max hours,minutes and
     * seconds
     */
    public BaseDateTimePickerController setMinHour(int minHour) {
        if (minHour >= 0) {
            this.minHour = minHour;
        }
        return this;
    }

    /**
     * This option is available only for MaterialDateTimePickerController, for NativeDateTimePickerController is
     * ignored and only MinDate and MaxDate works.
     * min max hours,minutes and seconds are with priority over min max Date, for setting min/max hours,minutes and
     * seconds
     */
    public BaseDateTimePickerController setMinMinute(int minMinute) {
        if (minMinute >= 0) {
            this.minMinute = minMinute;
        }
        return this;
    }

    /**
     * This option is available only for MaterialDateTimePickerController, for NativeDateTimePickerController is
     * ignored and only MinDate and MaxDate works.
     * min max hours,minutes and seconds are with priority over min max Date, for setting min/max hours,minutes and
     * seconds
     */
    public BaseDateTimePickerController setMinSeconds(int minSeconds) {
        if (minSeconds >= 0) {
            this.minSeconds = minSeconds;
        }
        return this;
    }

    /**
     * 24-hour clock
     * This option is available only for MaterialDateTimePickerController, for NativeDateTimePickerController is
     * ignored and only MinDate and MaxDate works.
     * min max hours,minutes and seconds are with priority over min max Date, for setting min/max hours,minutes and
     * seconds
     */
    public BaseDateTimePickerController setMaxHour(int maxHour) {
        if (maxHour >= 0) {
            this.maxHour = maxHour;
        }
        return this;
    }

    /**
     * This option is available only for MaterialDateTimePickerController, for NativeDateTimePickerController is
     * ignored and only MinDate and MaxDate works.
     * min max hours,minutes and seconds are with priority over min max Date, for setting min/max hours,minutes and
     * seconds
     */
    public BaseDateTimePickerController setMaxMinute(int maxMinute) {
        if (maxMinute >= 0) {
            this.maxMinute = maxMinute;
        }
        return this;
    }

    /**
     * This option is available only for MaterialDateTimePickerController, for NativeDateTimePickerController is
     * ignored and only MinDate and MaxDate works.
     * min max hours,minutes and seconds are with priority over min max Date, for setting min/max hours,minutes and
     * seconds
     */
    public BaseDateTimePickerController setMaxSeconds(int maxSeconds) {
        if (maxSeconds >= 0) {
            this.maxSeconds = maxSeconds;
        }
        return this;
    }

    //work around for pre-Lollipop, select date is called twice on dismiss
    boolean isDateSet = false, isTimeSet = false;


    void prepareDatePicker() {
        isDateSet = false;
        if (mYear == EMPTY) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    void onSetDate(int year, int monthOfYear, int dayOfMonth) {
        //work around for pre-Lollipop
        if (isDateSet) {
            return;
        } else {
            isDateSet = true;
        }
        this.year = year;
        month = monthOfYear;
        day = dayOfMonth;

        if (type == BOTH) {
            //*************Call Time Picker Here ********************
            timePicker();
        } else if (callback != null) {
            callback.onDateTimeSelected(year, month, day, EMPTY, EMPTY);
        }
    }

    void prepareTimePicker() {
        isTimeSet = false;
        if (mHour == EMPTY) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
        }
    }

    void onSetTime(int hourOfDay, int minute) {
        //work around for pre-Lollipop
        if (isTimeSet) {
            return;
        } else {
            isTimeSet = true;
        }
        hour = hourOfDay;
        this.minute = minute;

        if (callback != null) {
            callback.onDateTimeSelected(year, month, day, hour, minute);
        }
    }

    public interface Callback {
        /**
         * @param year
         * @param month   months start from 0-11 if you show this in String directly add 1 if you use a parser is OK
         * @param day
         * @param hour
         * @param minutes
         */
        void onDateTimeSelected(int year, int month, int day, int hour, int minutes);
    }
}
