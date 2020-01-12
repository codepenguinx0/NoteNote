package com.teampenguin.apps.notenote.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.teampenguin.apps.notenote.Models.NoteEntryPhoto;
import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.Utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditNotePhotoListAdapter extends ListAdapter<NoteEntryPhoto, EditNotePhotoListAdapter.PhotoListAdapterViewHolder> {

    private Context context;
    private HashMap<Integer, Bitmap> photoMap;

    private static final DiffUtil.ItemCallback<NoteEntryPhoto> DIFF_CALLBACK = new DiffUtil.ItemCallback<NoteEntryPhoto>() {
        @Override
        public boolean areItemsTheSame(@NonNull NoteEntryPhoto oldItem, @NonNull NoteEntryPhoto newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NoteEntryPhoto oldItem, @NonNull NoteEntryPhoto newItem) {
            return oldItem.getPhotoCaption().equals(newItem.getPhotoCaption());
        }
    };

    public EditNotePhotoListAdapter() {
        super(DIFF_CALLBACK);
        photoMap = new HashMap<>();
    }

    @NonNull
    @Override
    public PhotoListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_edit_note_photo_list, parent, false);
        context = parent.getContext();
        return new PhotoListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoListAdapterViewHolder holder, int position) {

        if(position != RecyclerView.NO_POSITION)
        {
            NoteEntryPhoto photo = getItem(position);

            if(photoMap.containsKey(photo.getId()))
            {
                holder.photoIV.setImageBitmap(photoMap.get(photo.getId()));

            } else
            {
                Bitmap bmp = Utils.getBitmapFromByteArray(photo.getImage());

                if(bmp==null)
                {
                    holder.photoIV.setBackgroundColor(Color.WHITE);
                }else
                {
                    holder.photoIV.setImageBitmap(bmp);
                    cacheBitmap(photo.getId(), bmp);
                }

            }
        }
    }

    private void cacheBitmap(int photoId, Bitmap bmp)
    {
        photoMap.put(photoId, bmp);
    }

    public class PhotoListAdapterViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.adapter_edit_note_photos_list_iv)
        public ImageView photoIV;

        public PhotoListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
