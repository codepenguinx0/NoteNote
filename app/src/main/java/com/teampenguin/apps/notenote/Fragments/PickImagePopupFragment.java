package com.teampenguin.apps.notenote.Fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.teampenguin.apps.notenote.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PickImagePopupFragment extends Fragment {

    public static final String TAG = "PickImagePopupFragment";
    @BindView(R.id.pick_image_popup_banner_cv)
    CardView contentCardView;

    private PickImagePopupCallBack callBackListener = null;
    private CommonFragmentInterface commonListener = null;

    public PickImagePopupFragment (PickImagePopupCallBack callBackListener, CommonFragmentInterface commonListener)
    {
        this.callBackListener = callBackListener;
        this.commonListener = commonListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note_image_popup, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playCardViewAnimation();
    }

    private void playCardViewAnimation()
    {
        ObjectAnimator animation = ObjectAnimator.ofFloat(contentCardView, "translationY", new float[]{300f,0});
        animation.setDuration(500);
        animation.start();
    }

    @OnClick(R.id.pick_image_popup_camera_ll)
    public void takePhoto()
    {
        if(callBackListener!=null)
        {
            callBackListener.takePhoto();
        }
    }

    @OnClick(R.id.pick_image_popup_gallery_ll)
    public void openGallery()
    {
        if(callBackListener!=null)
        {
            callBackListener.openGallery();
        }
    }

    @OnClick(R.id.pick_image_popup_bg_rl)
    public void closePopup()
    {
        if(commonListener!=null)
        {
            commonListener.closeFragment(TAG);
        }
    }

    public interface PickImagePopupCallBack{

        void takePhoto();
        void openGallery();
    }
}
