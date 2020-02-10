package com.teampenguin.apps.notenote.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.teampenguin.apps.notenote.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateNewTaskFragment extends Fragment {

    public static final String TAG = "CreateNewTaskFragment";

    @BindView(R.id.create_task_picked_date_tv)
    TextView pickedDateTV;
    @BindView(R.id.create_task_reminder_switch)
    Switch remindSwitch;
    @BindView(R.id.create_task_alarm_rl)
    RelativeLayout alarmRL;

    private CommonFragmentInterface commonListener = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_new_task, container, false);
        ButterKnife.bind(this, view);

        remindSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    showAlarm();
                }else
                {
                    hideAlarm();
                }
            }
        });

        return view;
    }

    @OnClick(R.id.create_task_cancel_btn)
    public void close()
    {
        if(commonListener!=null)
        {
            commonListener.closeFragment(TAG);
        }
    }

    public void setCommonListener(CommonFragmentInterface commonListener)
    {
        this.commonListener = commonListener;
    }

    private void showAlarm()
    {
        alarmRL.setVisibility(View.VISIBLE);
    }

    private void hideAlarm()
    {
        alarmRL.setVisibility(View.GONE);
    }
}
