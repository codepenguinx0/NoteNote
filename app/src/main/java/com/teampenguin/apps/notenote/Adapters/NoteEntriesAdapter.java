package com.teampenguin.apps.notenote.Adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.teampenguin.apps.notenote.Models.NoteEntryM;
import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.Utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteEntriesAdapter extends ListAdapter<NoteEntryM, NoteEntriesAdapter.NoteEntriesViewHolder> {

    private NoteEntriesAdapterCallBack mListener = null;
    private ViewBinderHelper viewBinderHelper;

    //must be a static function so that it is available before the constructor is being called
    public static final DiffUtil.ItemCallback<NoteEntryM> DIFF_CALLBACK = new DiffUtil.ItemCallback<NoteEntryM>() {
        @Override
        public boolean areItemsTheSame(@NonNull NoteEntryM oldItem, @NonNull NoteEntryM newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NoteEntryM oldItem, @NonNull NoteEntryM newItem) {
            return oldItem.getCategory().equals(newItem.getCategory()) &&
                    oldItem.getNoteTitle().equals(newItem.getNoteTitle()) &&
                    oldItem.getMood() == newItem.getMood();
        }
    };

    //no need to store the list of item in this class because the superclass will take care of it
    public NoteEntriesAdapter() {
        super(DIFF_CALLBACK);
        viewBinderHelper = new ViewBinderHelper();
        viewBinderHelper.setOpenOnlyOne(true);
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
        holder.moodIV.setImageDrawable(Utils.getMoodIcon(noteEntry.getMood()));
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
        @BindView(R.id.adapter_note_entry_frame)
        public FrameLayout frame;
        @BindView(R.id.adapter_note_entry_delete_rl)
        RelativeLayout deleteRL;
        @BindView(R.id.swipe_reveal_layout)
        SwipeRevealLayout srl;

        public NoteEntriesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mListener!=null)
                    {
                        final int pos = getAdapterPosition();
                        if(srl.isOpened())
                        {
                            srl.close(false);
                        }
                        mListener.onNoteEntryClicked(getItem(pos));
                    }
                }
            });

            deleteRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null)
                    {
                        final int pos = getAdapterPosition();
                        mListener.onDeleteClicked(getItem(pos));
                    }
                }
            });


        }
    }

    public interface NoteEntriesAdapterCallBack{

        void onNoteEntryClicked(NoteEntryM noteEntry);
        void onDeleteClicked(NoteEntryM noteEntryM);
    }
}
