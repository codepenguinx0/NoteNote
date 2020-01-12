package com.teampenguin.apps.notenote.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.teampenguin.apps.notenote.Activities.EditNoteActivity;
import com.teampenguin.apps.notenote.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.richeditor.RichEditor;

public class Utils {

    private static final String TAG = "Utils";

    private static Context mContext;

    public static void init(Context context)
    {
        mContext = context;
    }

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

    public static void showSoftKeyboard(Activity activity, View view)
    {
        if(view instanceof EditText || view instanceof RichEditor)
        {
            Log.d(TAG, "showSoftKeyboard: show!");
            view.requestFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
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

    public static byte[] getByteArrayFromPath(String photoPath) {

        byte[] b;
        Bitmap bm = BitmapFactory.decodeFile(photoPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        b = baos.toByteArray();

        return b;
    }

    public static Bitmap getBitmapFromByteArray(byte[] bytes)
    {
        Bitmap bmp;

        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bmp;
    }

    public static Drawable getMoodIcon(int mood)
    {
        if(mContext!=null)
        {
            switch (mood)
            {
                case EditNoteActivity
                        .MOOD_HAPPY:
                    return mContext.getResources().getDrawable(R.drawable.mood_happy);
                case EditNoteActivity
                        .MOOD_GRATEFUL:
                    return mContext.getResources().getDrawable(R.drawable.mood_grateful);
                case EditNoteActivity
                        .MOOD_CONTENT:
                    return mContext.getResources().getDrawable(R.drawable.mood_content);
                case EditNoteActivity
                        .MOOD_SAD:
                    return mContext.getResources().getDrawable(R.drawable.mood_sad);
                case EditNoteActivity
                        .MOOD_SHOCKED:
                    return mContext.getResources().getDrawable(R.drawable.mood_shocked);
                case EditNoteActivity
                        .MOOD_ANGRY:
                    return mContext.getResources().getDrawable(R.drawable.mood_angry);
                default:
                    return mContext.getResources().getDrawable(R.drawable.mood_happy);
            }
        }

        return null;
    }

}


