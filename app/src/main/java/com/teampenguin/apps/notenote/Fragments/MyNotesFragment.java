package com.teampenguin.apps.notenote.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teampenguin.apps.notenote.Adapters.NoteEntryListAdapter;
import com.teampenguin.apps.notenote.Models.NoteEntryM;
import com.teampenguin.apps.notenote.R;

import java.util.ArrayList;

public class MyNotesFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "MyNotesFragment";
    private RecyclerView myNotesRV;
    private ImageView sortIV;
    private ArrayList<NoteEntryM> noteEntries;
    private NoteEntryListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteEntries = new ArrayList<>();

        NoteEntryM note1 = new NoteEntryM();
        note1.setNoteTitle("Note 1");
        NoteEntryM note2 = new NoteEntryM();
        note2.setNoteTitle("Note 2");
        NoteEntryM note3 = new NoteEntryM();
        note3.setNoteTitle("Note 3");

        noteEntries.add(note1);
        noteEntries.add(note2);
        noteEntries.add(note3);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_notes, container, false);
        myNotesRV = view.findViewById(R.id.my_notes_rv);
        myNotesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        sortIV = view.findViewById(R.id.my_notes_sort_iv);
        sortIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSortingPopupMenu(v);
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter();
    }

    private void setAdapter()
    {
        adapter = new NoteEntryListAdapter(noteEntries);
        myNotesRV.setAdapter(adapter);
    }

    private void showSortingPopupMenu(View view)
    {
        PopupMenu popupMenu = new PopupMenu(getActivity(),view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.sorting_popup_menu);
        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.sorting_popup_menu_create_date:
                Log.d(TAG, "onMenuItemClick: create date");
//                adapter.sort(0);
                return true;
            case R.id.sorting_popup_menu_title_a_z:
                Log.d(TAG, "onMenuItemClick: title a to z");
                adapter.sort(1);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.sorting_popup_menu_title_z_a:
                Log.d(TAG, "onMenuItemClick: title z to a");
                adapter.sort(2);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.sorting_popup_menu_category:
                Log.d(TAG, "onMenuItemClick: category");
                return true;
            case R.id.sorting_popup_menu_mood:
                Log.d(TAG, "onMenuItemClick: mood");
                return true;
            default:
                return false;
        }
    }
}
