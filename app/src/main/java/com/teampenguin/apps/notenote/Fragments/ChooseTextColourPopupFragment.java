package com.teampenguin.apps.notenote.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.teampenguin.apps.notenote.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseTextColourPopupFragment extends Fragment {

    public static final String TAG = "ChooseTextColourPopupFr";
    private static final int GRID_LAYOUT_COL_NUM = 4;
    private static final int GRID_LAYOUT_ROW_NUM = 3;


    @BindView(R.id.choose_text_colour_gl)
    GridLayout coloursGL;

    private ArrayList<String> colours;

    private CommonFragmentInterface commonListener = null;
    private ChooseTextColourPopupCallBack callBackListener = null;

    public ChooseTextColourPopupFragment(CommonFragmentInterface commonListener, ChooseTextColourPopupCallBack callBackListener)
    {
        this.commonListener = commonListener;
        this.callBackListener = callBackListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_text_colour_popup, container, false);
        ButterKnife.bind(this,view);

        setGridLayout();

        return view;
    }

    private void setGridLayout()
    {
        coloursGL.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                coloursGL.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                addColours();
            }
        });

        coloursGL.setOrientation(GridLayout.HORIZONTAL);
        coloursGL.setColumnCount(GRID_LAYOUT_COL_NUM);
        coloursGL.setRowCount(GRID_LAYOUT_ROW_NUM);
    }

    private void addColours()
    {
        List<String> tempList = Arrays.asList(getResources().getStringArray(R.array.editorTextColours));
        colours = new ArrayList<>(tempList);

        for (int i = 0; i < colours.size(); i++) {

            final String colour = colours.get(i);

            Button colourButton = new Button(getActivity());
            ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(100,100);
            mlp.setMargins(10,10,10,10);
            colourButton.setLayoutParams(mlp);
            colourButton.setBackgroundColor(Color.parseColor(colour));
            colourButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callBackListener!=null)
                    {
                        callBackListener.changeTextColour(colour);
                    }
                }
            });

            coloursGL.addView(colourButton);
        }
    }

    @OnClick(R.id.choose_text_colour_popup_close_iv)
    public void close()
    {
        if(commonListener!=null)
        {
            commonListener.closeFragment(TAG);
        }
    }

    public interface ChooseTextColourPopupCallBack
    {
        void changeTextColour(String colour);
    }

}
