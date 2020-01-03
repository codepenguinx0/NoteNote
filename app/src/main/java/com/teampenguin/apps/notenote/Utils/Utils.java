package com.teampenguin.apps.notenote.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private static final String TAG = "Utils";
    public static String convertDateToString(Date date){

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy hh:mm a");
        String dateString = sdf.format(date);

        return dateString;
    }

    public static Date convertDateStringToDate(String dateString)
    {
        Date date;

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy hh:mm a");
        try{

            date = sdf.parse(dateString);

        }catch (Exception e)
        {
            Log.e(TAG, "convertDateStringToDate: ", e);
            date = new Date();
        }

        return date;
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

    public static boolean isEditTextEmpty(EditText et)
    {
        if(et.getText().toString().trim().length() > 0)
        {
            return false;
        }

        return true;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static String getTimeStringForFileName(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
        return sdf.format(date);
    }

}


