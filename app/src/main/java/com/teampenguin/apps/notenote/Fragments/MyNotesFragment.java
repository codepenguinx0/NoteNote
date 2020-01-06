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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teampenguin.apps.notenote.Adapters.NoteEntryListAdapter;
import com.teampenguin.apps.notenote.Models.NoteEntryM;
import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.Utils.Utils;
import com.teampenguin.apps.notenote.ViewModels.NoteEntryViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyNotesFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    public static final String TAG = "MyNotesFragment";

    @BindView(R.id.my_notes_rv)
    RecyclerView myNotesRV;
    @BindView(R.id.my_notes_sort_iv)
    ImageView sortIV;

//    private LiveData<List<NoteEntryM>> noteEntries;
    private NoteEntryListAdapter adapter;
    private NoteEntryViewModel noteEntryViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        NoteEntryM note1 = new NoteEntryM();
//        note1.setNoteTitle("Note 1");
//        NoteEntryM note2 = new NoteEntryM();
//        note2.setNoteTitle("Note 2");
//        NoteEntryM note3 = new NoteEntryM();
//        note3.setNoteTitle("Note 3");
//
//        noteEntries.add(note1);
//        noteEntries.add(note2);
//        noteEntries.add(note3);

        noteEntryViewModel = ViewModelProviders.of(this).get(NoteEntryViewModel.class);

        LiveData<List<NoteEntryM>> allNoteEntries = noteEntryViewModel.getAllNoteEntries();

        if(allNoteEntries!=null)
        {
            Log.d(TAG, "onCreate: note entries count " + allNoteEntries.getValue().size());
            noteEntryViewModel.getAllNoteEntries().observe(this, new Observer<List<NoteEntryM>>() {
                @Override
                public void onChanged(List<NoteEntryM> noteEntries) {

                    updateList();
                }
            });
        }



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_notes, container, false);
        ButterKnife.bind(this,view);
        myNotesRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter();
    }

    @OnClick(R.id.my_notes_sort_iv)
    public void showSortingPopupMenu()
    {
        PopupMenu popupMenu = new PopupMenu(getActivity(),sortIV);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.sorting_popup_menu);
        popupMenu.show();
    }

    private void setAdapter()
    {
        //TODO NoteEntryListAdapter should be a ListAdapter
//        adapter = new NoteEntryListAdapter(noteEntries);
//        myNotesRV.setAdapter(adapter);
    }

    private void updateList()
    {
//        adapter.updateNoteEntries(noteEntries);
//        adapter.notifyDataSetChanged();
    }


    private void sortByCreateDateLToO()
    {
//        ArrayList<NoteEntryM> sortedNoteEntries = new ArrayList<>(noteEntries);
//        Collections.sort(sortedNoteEntries, new Comparator<NoteEntryM>() {
//            @Override
//            public int compare(NoteEntryM o1, NoteEntryM o2) {
//                Date d1 = Utils.convertDateStringToDate(o1.getCreateDate());
//                Date d2 = Utils.convertDateStringToDate(o2.getCreateDate());
//                return d1.compareTo(d2);
//            }
//        });
//
//        adapter.setNewSortedNoteEntries(sortedNoteEntries);
//        adapter.notifyDataSetChanged();
    }

    private void sortByCreateDateOToL()
    {
//        ArrayList<NoteEntryM> sortedNoteEntries = new ArrayList<>(noteEntries);
//        Collections.sort(sortedNoteEntries, new Comparator<NoteEntryM>() {
//            @Override
//            public int compare(NoteEntryM o1, NoteEntryM o2) {
//                Date d1 = Utils.convertDateStringToDate(o1.getCreateDate());
//                Date d2 = Utils.convertDateStringToDate(o2.getCreateDate());
//                return d2.compareTo(d1);
//            }
//        });
//
//        adapter.setNewSortedNoteEntries(sortedNoteEntries);
//        adapter.notifyDataSetChanged();
    }

    private void sortByTitleAToZ()
    {
//        ArrayList<NoteEntryM> sortedNoteEntries = new ArrayList<>(noteEntries);
//        Collections.sort(sortedNoteEntries, new Comparator<NoteEntryM>() {
//            @Override
//            public int compare(NoteEntryM o1, NoteEntryM o2) {
//                return o1.getNoteTitle().compareTo(o2.getNoteTitle());
//            }
//        });
//
//        adapter.setNewSortedNoteEntries(sortedNoteEntries);
//        adapter.notifyDataSetChanged();
    }

    private void sortByTitleZToA()
    {
//        ArrayList<NoteEntryM> sortedNoteEntries = new ArrayList<>(noteEntries);
//        Collections.sort(sortedNoteEntries, new Comparator<NoteEntryM>() {
//            @Override
//            public int compare(NoteEntryM o1, NoteEntryM o2) {
//                return o2.getNoteTitle().compareTo(o1.getNoteTitle());
//            }
//        });
//
//        adapter.setNewSortedNoteEntries(sortedNoteEntries);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.sorting_popup_menu_create_date:
                Log.d(TAG, "onMenuItemClick: create date");
                sortByCreateDateLToO();
                return true;
            case R.id.sorting_popup_menu_title_a_z:
                Log.d(TAG, "onMenuItemClick: title a to z");
                sortByTitleAToZ();
                return true;
            case R.id.sorting_popup_menu_title_z_a:
                Log.d(TAG, "onMenuItemClick: title z to a");
                sortByTitleZToA();
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
