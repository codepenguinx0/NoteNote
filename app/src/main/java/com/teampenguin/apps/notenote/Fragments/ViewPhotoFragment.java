package com.teampenguin.apps.notenote.Fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.teampenguin.apps.notenote.Models.NoteEntryPhoto;
import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewPhotoFragment extends Fragment {

    public static final String TAG = "ViewPhotoFragment";
    @BindView(R.id.view_photo_photo_iv)
    ImageView photoIV;
    @BindView(R.id.view_photo_caption_et)
    EditText captionET;
    @BindView(R.id.view_photo_create_date_tv)
    TextView createDateTV;

    private NoteEntryPhoto photo = null;

    private CommonFragmentInterface commonListener = null;
    private ViewPhotoFragmentCallBack callBackListener = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_photo, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(photo!=null)
        {
            Bitmap bmp = BitmapFactory.decodeByteArray(photo.getImage(), 0, photo.getImage().length);

            if(bmp!=null)
            {
                photoIV.setImageBitmap(bmp);
            }else
            {
                Toast.makeText(getActivity(), "Cannot load photo", Toast.LENGTH_SHORT).show();
                photoIV.setImageDrawable(getResources().getDrawable(R.drawable.demo_photo));
            }

            createDateTV.setText(photo.getCreatedDate());
            captionET.setText(photo.getPhotoCaption());

        }else
        {
            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            close();
        }
    }

    public void setNoteEntryPhoto(NoteEntryPhoto photo)
    {
        this.photo = photo;
    }

    public void setListeners(CommonFragmentInterface commonListener, ViewPhotoFragmentCallBack callBackListener)
    {
        this.commonListener = commonListener;
        this.callBackListener = callBackListener;
    }

    @OnClick(R.id.view_photo_close_iv)
    public void close()
    {
        if(commonListener!=null)
        {
            commonListener.closeFragment(TAG);
        }
    }

    @OnClick(R.id.view_photo_delete_btn)
    public void deletePhoto()
    {
        showDeleteAlertMessage();
    }

    @OnClick(R.id.view_photo_save_btn)
    public void savePhoto()
    {
        if(photo!=null)
        {
            NoteEntryPhoto tempPhoto = new NoteEntryPhoto();
            tempPhoto.setId(photo.getId());
            tempPhoto.setOwnerNoteId(photo.getOwnerNoteId());
            tempPhoto.setCreatedDate(photo.getCreatedDate());
            tempPhoto.setImage(photo.getImage());
            if(Utils.isEditTextEmpty(captionET))
            {
                tempPhoto.setPhotoCaption("");
            }else
            {
                tempPhoto.setPhotoCaption(captionET.getText().toString());
            }


            if(callBackListener!=null)
            {
                callBackListener.onSavePhoto(tempPhoto);
            }
        }else
        {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteAlertMessage()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Delete Photo")
                .setMessage("Are you sure to permenantly delete this photo?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(callBackListener!=null)
                        {
                            callBackListener.onDeletePhoto(photo);
                        }
                    }
                })
                .setNegativeButton("Cancel",null)
                .create();

        alertDialog.show();
    }

    public interface ViewPhotoFragmentCallBack {
        void onSavePhoto(NoteEntryPhoto photo);
        void onDeletePhoto(NoteEntryPhoto photo);
    }
}
