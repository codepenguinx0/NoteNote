package com.teampenguin.apps.notenote.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InsertLinkPopupFragment extends Fragment {

    public static final String TAG = "InsertLinkPopupFragment";
    @BindView(R.id.insert_link_popup_url_et)
    EditText urlET;
    @BindView(R.id.insert_link_popup_display_name_et)
    EditText displayNameET;

    CommonFragmentInterface commonListener = null;
    InsertLinkPopupCallBack callBackListener = null;

    public InsertLinkPopupFragment(CommonFragmentInterface commonListener, InsertLinkPopupCallBack callBackListener) {
        this.commonListener = commonListener;
        this.callBackListener = callBackListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert_link_popup, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.insert_link_insert_button)
    public void checkInput() {
        if (Utils.isEditTextEmpty(urlET)) {
            Toast.makeText(getActivity(), "You cannot leave the url field empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (!urlET.getText().toString().contains("http") &&
                !urlET.getText().toString().contains("www") &&
                !urlET.getText().toString().contains(".")) {
            Toast.makeText(getActivity(), "Invalid Link", Toast.LENGTH_SHORT).show();
            return;
        }

        if (callBackListener != null) {
            String url = urlET.getText().toString();
            String displayName = Utils.isEditTextEmpty(displayNameET) ? url : displayNameET.getText().toString();
            callBackListener.insertLink(url, displayName);
        }
    }

    @OnClick(R.id.insert_link_popup_close_iv)
    public void close() {
        if (commonListener != null) {
            commonListener.closeFragment(TAG);
        }
    }

    public interface InsertLinkPopupCallBack {
        void insertLink(String url, String displayName);
    }
}
