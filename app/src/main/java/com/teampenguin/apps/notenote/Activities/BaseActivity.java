package com.teampenguin.apps.notenote.Activities;

import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.teampenguin.apps.notenote.Utils.Utils;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if(getCurrentFocus()!=null)
        {
            Utils.hideSoftKeyboard(this);
        }

        return super.dispatchTouchEvent(ev);
    }
}
