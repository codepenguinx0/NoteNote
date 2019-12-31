package com.teampenguin.apps.notenote.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String convertDateToString(Date date){

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy hh:mm a");
        String dateString = sdf.format(date);

        return dateString;
    }

    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);

        //make sure something is on focus meaning the keyboard is shown
        if(activity.getCurrentFocus()!=null)
        {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

}


