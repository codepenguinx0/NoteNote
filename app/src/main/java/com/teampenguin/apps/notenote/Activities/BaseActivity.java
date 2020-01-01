package com.teampenguin.apps.notenote.Activities;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.teampenguin.apps.notenote.R;
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

    protected void closeFragmentByTag(String tag)
    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

        if(fragment!=null)
        {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment);
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.animation_activity_slide_from_right, R.anim.animation_activity_slide_to_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.animation_activity_slide_from_left, R.anim.animation_activity_slide_to_right);
    }
}
