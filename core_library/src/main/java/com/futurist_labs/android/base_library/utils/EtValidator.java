package com.futurist_labs.android.base_library.utils;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Galeen on 1/4/2018.
 */

public class EtValidator {

    static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String pass) {
        return !isEmpty(pass) && pass.length() >= 3;
    }

    public static boolean isValidPhone(String pass) {
        return !isEmpty(pass) && pass.length() >= 7;
    }

    public static boolean confirmSame(String pass, String confirm) {
        return !isEmpty(pass) && !isEmpty(confirm) && pass.equals(confirm);
    }

    public static boolean isValidEmail(EditText et, String errMessage) {
        return mainEtCheck(et, errMessage, isValidEmail(et.getText().toString()));
    }

    public static boolean isValidEmail(Activity atv, EditText et, TextInputLayout inputLayout, String errMessage) {
        return isValidEmail(atv, et, inputLayout, et.getText().toString(), errMessage, true);
    }

    public static boolean isValidEmail(Activity atv, EditText et, TextInputLayout inputLayout, String email, String errMessage) {
        return isValidEmail(atv, et, inputLayout, email, errMessage, true);
    }

    public static boolean isValidEmail(Activity atv, EditText et, TextInputLayout inputLayout, String email, String errMessage, boolean hideKeyboard) {
        return mainCheck(atv, et, inputLayout, errMessage, isValidEmail(email), hideKeyboard);
    }

    public static boolean isValidPhone(Activity atv, EditText et, TextInputLayout inputLayout, String errMessage, boolean hideKeyboard) {
        return mainCheck(atv, et, inputLayout, errMessage, isValidPhone(et.getText().toString()), hideKeyboard);
    }

    public static boolean isValidPhone(EditText et, String errMessage) {
        return mainEtCheck(et, errMessage, isValidPhone(et.getText().toString()));
    }

    public static boolean isValidPassword(Activity atv, EditText et, TextInputLayout inputLayout, String pass, String errMessage) {
        return isValidPassword(atv, et, inputLayout, pass, errMessage, true);
    }

    public static boolean isValidPassword(Activity atv, EditText et, TextInputLayout inputLayout, String errMessage) {
        return isValidPassword(atv, et, inputLayout, et.getText().toString(), errMessage, true);
    }


    public static boolean isValidPassword(Activity atv, EditText et, TextInputLayout inputLayout, String pass, String errMessage, boolean hideKeyboard) {
        return mainCheck(atv, et, inputLayout, errMessage, isValidPassword(pass), hideKeyboard);
    }

    public static boolean isValidPassword(EditText et, String errMessage) {
        return mainEtCheck(et, errMessage, isValidPassword(et.getText().toString().trim()));
    }


    public static boolean confirmSame(Activity atv, EditText et, TextInputLayout inputLayout, String pass, String errMessage) {
        return confirmSame(atv, et, inputLayout, pass, errMessage, true);
    }

    public static boolean confirmSame(Activity atv, EditText et, TextInputLayout inputLayout, String pass, String errMessage, boolean hideKeyboard) {
        return mainCheck(atv, et, inputLayout, errMessage, confirmSame(et.getText().toString(), (pass)), hideKeyboard);
    }

    public static boolean confirmSame(EditText et, String pass, String errMessage) {
        return mainEtCheck(et, errMessage, confirmSame(et.getText().toString(), (pass)));
    }

    public static boolean isNotEmptyField(Activity atv, EditText et, TextInputLayout inputLayout, String errMessage) {
        return isNotEmptyField(atv, et, inputLayout, et.getText().toString(), errMessage, true);
    }

    public static boolean isNotEmptyField(Activity atv, EditText et, TextInputLayout inputLayout, String text, String errMessage) {
        return isNotEmptyField(atv, et, inputLayout, text, errMessage, true);
    }

    public static boolean isNotEmptyField(Activity atv, EditText et, TextInputLayout inputLayout, String text, String errMessage, boolean hideKeyboard) {
        return mainCheck(atv, et, inputLayout, errMessage, !isEmpty(text), hideKeyboard);
    }

    public static boolean isNotEmptyField(EditText et, String errMessage) {
        return mainEtCheck(et, errMessage, !isEmpty(et.getText().toString()));
    }

    private static boolean mainCheck(Activity atv, EditText et, TextInputLayout inputLayout, String errMessage, boolean res) {
        return mainCheck(atv, et, inputLayout, errMessage, res, true);
    }

    private static boolean mainEtCheck(EditText et, String errMessage, boolean isValid) {
        if (isValid) {
            et.setError(null);
            return true;
        } else {
            et.setError(errMessage);
            et.requestFocus();
            return false;
        }
    }

    private static boolean mainCheck(Activity atv, EditText et, TextInputLayout inputLayout, String errMessage, boolean res, boolean hideKeyboard) {
        if (res) {
            setError(et, inputLayout, null);
            return true;
        } else {
            setError(et, inputLayout, errMessage);
            requestFocus(atv, et, hideKeyboard);
            return false;
        }
    }

    private static void setError(EditText et, TextInputLayout inputLayout, String errMessage) {
        if (inputLayout != null) {
            inputLayout.setError(errMessage);
        } else {
            et.setError(errMessage);
        }
    }

    private static void requestFocus(Activity atv, View view, boolean hideKeyboard) {
        if (view.requestFocus() && hideKeyboard) {
//            atv.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            SystemUtils.showKeyboard(atv);
        }
    }
}
