package com.teampenguin.apps.notenote.Adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.teampenguin.apps.notenote.Models.NoteEntryM;
import com.teampenguin.apps.notenote.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteEntriesAdapter extends ListAdapter<NoteEntryM, NoteEntriesAdapter.NoteEntriesViewHolder> {

    private NoteEntriesAdapterCallBack mListener = null;

    //must be a static function so that it is available before the constructor is being called
    public static final DiffUtil.ItemCallback<NoteEntryM> DIFF_CALLBACK = new DiffUtil.ItemCallback<NoteEntryM>() {
        @Override
        public boolean areItemsTheSame(@NonNull NoteEntryM oldItem, @NonNull NoteEntryM newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NoteEntryM oldItem, @NonNull NoteEntryM newItem) {
            return oldItem.getCategory() == newItem.getCategory() &&
                    oldItem.getNoteTitle().equals(newItem.getNoteTitle()) &&
                    oldItem.getModifiedDate().equals(newItem.getModifiedDate());
        }
    };

    //no need to store the list of item in this class because the superclass will take care of it
    public NoteEntriesAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setCallBackListener(NoteEntriesAdapterCallBack listener)
    {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public NoteEntriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_note_entry_list, parent, false);
        return new NoteEntriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteEntriesViewHolder holder, int position) {

        NoteEntryM noteEntry = getItem(position);
        holder.titleTV.setText(noteEntry.getNoteTitle().isEmpty()?"No Title":noteEntry.getNoteTitle());
        holder.createDateTV.setText(noteEntry.getModifiedDate());
        holder.categoryTV.setText(String.valueOf(noteEntry.getCategory()));
        //TODO change mood icon
    }

    public NoteEntryM getNoteEntry(int position)
    {
        return getItem(position);
    }

    public class NoteEntriesViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.note_entry_title_tv)
        public TextView titleTV;
        @BindView(R.id.note_entry_create_date_tv)
        public TextView createDateTV;
        @BindView(R.id.note_entry_category_tv)
        public TextView categoryTV;
        @BindView(R.id.note_entry_mood_iv)
        public ImageView moodIV;

        public NoteEntriesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mListener!=null)
                    {
                        final int pos = getAdapterPosition();
                        mListener.onNoteEntryClicked(getNoteEntry(pos));
                    }
                }
            });
        }
    }

    public interface NoteEntriesAdapterCallBack{

        void onNoteEntryClicked(NoteEntryM noteEntry);
    }
}
