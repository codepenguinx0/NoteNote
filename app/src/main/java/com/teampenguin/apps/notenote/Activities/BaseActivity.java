package com.teampenguin.apps.notenote.Activities;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.Utils.Utils;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if(getCurrentFocus()!=null)
        {
//            boolean hideKeyboard = true;
//            Log.d(TAG, "dispatchTouchEvent: class of view" + getCurrentFocus().getClass().getName());
//
//            if(getCurrentFocus() instanceof LinearLayout)
//            {
//                Log.d(TAG, "dispatchTouchEvent: the touched view is a Linear Layout");
//                LinearLayout ll = (LinearLayout) getCurrentFocus();
//                int childCount = ll.getChildCount();
//                Log.d(TAG, "dispatchTouchEvent: childCount " + childCount);
//
//                for (int i = 0; i < childCount; i++) {
//                    View v = ll.getChildAt(i);
//                    if(v instanceof TextEditorImageView)
//                    {
//                        Log.d(TAG, "dispatchTouchEvent: contains TextEditorImageView");
//                        hideKeyboard = false;
//                        break;
//                    }
//                }
//            }
//
//            if(hideKeyboard)
//            {
//                Log.d(TAG, "dispatchTouchEvent: hide keyboard");
//                Utils.hideSoftKeyboard(this);
//            }
            Utils.hideSoftKeyboard(this);
        }

        return super.dispatchTouchEvent(ev);
    }

    protected void closeFragmentByTag(String tag)
    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

        if(fragment!=null)
        {
            Log.d(TAG, "closeFragmentByTag: remove fragment");
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
