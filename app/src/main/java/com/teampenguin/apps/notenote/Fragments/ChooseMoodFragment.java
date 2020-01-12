package com.teampenguin.apps.notenote.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.teampenguin.apps.notenote.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseMoodFragment extends Fragment {

    public static final String TAG = "ChooseMoodFragment";
    public static final String ARGS_MOOD = "mood";
    private CommonFragmentInterface commonListener = null;

    @BindView(R.id.mood_happy_btn)
    RelativeLayout happyRL;
    @BindView(R.id.mood_grateful_btn)
    RelativeLayout gratefulRL;
    @BindView(R.id.mood_content_btn)
    RelativeLayout contentRL;
    @BindView(R.id.mood_sad_btn)
    RelativeLayout sadRL;
    @BindView(R.id.mood_shocked_btn)
    RelativeLayout shockedRL;
    @BindView(R.id.mood_angry_btn)
    RelativeLayout angryRL;

    private ChooseMoodFragmentCallBack callBackListener = null;
    private ArrayList<RelativeLayout> buttons;
    private int currentMood = 0;

    public static ChooseMoodFragment getInstance(int mood)
    {
        ChooseMoodFragment fragment = new ChooseMoodFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_MOOD, mood);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if(args!=null)
        {
            currentMood = args.getInt(ARGS_MOOD);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose_mood, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setButtons();
        setChosenButton(currentMood);
    }

    private void setButtons()
    {
        //need to follow the order
        buttons = new ArrayList<>();
        buttons.add(happyRL);
        buttons.add(gratefulRL);
        buttons.add(contentRL);
        buttons.add(sadRL);
        buttons.add(shockedRL);
        buttons.add(angryRL);

        for (int i = 0; i < buttons.size(); i++) {

            final int mood = i;

            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentMood = mood;
                    setChosenButton(mood);
//                    Toast.makeText(getActivity(), "Set mood " + mood, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setCallBackListener(ChooseMoodFragmentCallBack callBackListener)
    {
        this.callBackListener = callBackListener;
    }


    @OnClick(R.id.choose_mood_close_iv)
    public void close()
    {
        if(callBackListener!=null)
        {
            callBackListener.onChooseMoodFragmentClosed(currentMood);
        }
    }

    private void setChosenButton(int mood)
    {
        for (int i = 0; i < buttons.size(); i++) {

            if(i == mood)
            {
                buttons.get(i).setBackground(getResources().getDrawable(R.drawable.mood_selected_bg));
            }else
            {
                buttons.get(i).setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        }
    }

    public interface ChooseMoodFragmentCallBack {

        void onChooseMoodFragmentClosed(int mood);
    }
}
