package com.teampenguin.apps.notenote.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teampenguin.apps.notenote.Models.NoteEntryM;
import com.teampenguin.apps.notenote.R;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NoteEntryListAdapter extends RecyclerView.Adapter<NoteEntryListAdapter.NoteEntryAdapterListViewHolder> {

    private List<NoteEntryM> noteEntries;
    public static final int SORT_MODE_CREATE_DATE = 0;
    public static final int SORT_MODE_TITLE_AZ = 1;
    public static final int SORT_MODE_TITLE_ZA = 2;
    public static final int SORT_MODE_CATEGORY = 3;
    public static final int SORT_MODE_MOOD = 4;

    public NoteEntryListAdapter(@NonNull List<NoteEntryM> noteEntries)
    {
        this.noteEntries = noteEntries;
    }

    @NonNull
    @Override
    public NoteEntryAdapterListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_note_entry_list, parent, false);
        return new NoteEntryAdapterListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteEntryAdapterListViewHolder holder, int position) {

        if(position!= RecyclerView.NO_POSITION)
        {
            NoteEntryM noteEntry = noteEntries.get(position);
            holder.titleTV.setText(noteEntry.getNoteTitle());
            holder.createDateTV.setText(noteEntry.getCreateDate());
            holder.categoryTV.setText(String.valueOf(noteEntry.getCategory()));
        }

    }

    public void sort(int sortMode)
    {
        switch (sortMode)
        {
            case SORT_MODE_CREATE_DATE:
                sortByCreateDate();
                break;
            case SORT_MODE_TITLE_AZ:
                sortByTitleAZ();
                break;
            case SORT_MODE_TITLE_ZA:
                sortByTitleZA();
                break;
            case SORT_MODE_CATEGORY:
                break;
            case SORT_MODE_MOOD:
                break;
            default:
                sortByCreateDate();
                break;
        }
    }

    private void sortByCreateDate()
    {
        Collections.sort(noteEntries, new Comparator<NoteEntryM>() {
            @Override
            public int compare(NoteEntryM o1, NoteEntryM o2) {
                return 0;
            }
        });
    }

    private void sortByTitleAZ()
    {
        Collections.sort(noteEntries, new Comparator<NoteEntryM>() {
            @Override
            public int compare(NoteEntryM o1, NoteEntryM o2) {
                return o1.getNoteTitle().compareTo(o2.getNoteTitle());
            }
        });
    }

    private void sortByTitleZA()
    {
        Collections.sort(noteEntries, new Comparator<NoteEntryM>() {
            @Override
            public int compare(NoteEntryM o1, NoteEntryM o2) {
                return o2.getNoteTitle().compareTo(o1.getNoteTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteEntries.size();
    }

    public NoteEntryM getNoteEntryByPosition(int pos)
    {
        return noteEntries.get(pos);
    }

    public static class NoteEntryAdapterListViewHolder extends RecyclerView.ViewHolder {


        public TextView titleTV;
        public TextView createDateTV;
        public TextView categoryTV;

        public NoteEntryAdapterListViewHolder(@NonNull View itemView) {

            super(itemView);
            titleTV = itemView.findViewById(R.id.note_entry_title_tv);
            createDateTV = itemView.findViewById(R.id.note_entry_create_date_tv);
            categoryTV = itemView.findViewById(R.id.note_entry_category_tv);

        }
    }
}
