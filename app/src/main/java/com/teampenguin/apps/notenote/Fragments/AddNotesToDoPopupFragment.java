package com.teampenguin.apps.notenote.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.teampenguin.apps.notenote.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNotesToDoPopupFragment extends Fragment {

    public static final String TAG = "AddNotesToDoPopupFragme";

    @BindView(R.id.add_popup_contents_rl)
    RelativeLayout popupContentRL;

    private CommonFragmentInterface commonListener = null;
    private AddPopupFragmentCallBack callBackListener = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_notes_todo_popup, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setCommonListener(CommonFragmentInterface listener) {
        this.commonListener = listener;
    }

    public void setCallBackListener(AddPopupFragmentCallBack listener)
    {
        this.callBackListener = listener;
    }

    @OnClick({R.id.add_popup_close_iv, R.id.add_popup_frame_rl})
    public void closeFragment() {
        if (commonListener != null) {
            commonListener.closeFragment(TAG);
        }
    }

    @OnClick(R.id.add_popup_new_task_ll)
    public void openNewTaskActivity() {
        Toast.makeText(getActivity(), "Add New Task", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.add_popup_new_note_ll)
    public void openNewNoteActivity() {
        Toast.makeText(getActivity(), "Add New Note", Toast.LENGTH_SHORT).show();
        if(callBackListener!=null)
        {
            callBackListener.createNewNote();
        }
    }

    public interface AddPopupFragmentCallBack {
        void createNewNote();
        void createNewTask();
    }
}
