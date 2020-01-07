package com.teampenguin.apps.notenote.Fragments;

import android.content.Intent;
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

import com.teampenguin.apps.notenote.Activities.EditNoteActivity;
import com.teampenguin.apps.notenote.Activities.MainActivity;
import com.teampenguin.apps.notenote.Adapters.NoteEntriesAdapter;
import com.teampenguin.apps.notenote.Models.NoteEntryM;
import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.ViewModels.NoteEntryViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyNotesFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, NoteEntriesAdapter.NoteEntriesAdapterCallBack {

    public static final String TAG = "MyNotesFragment";

    @BindView(R.id.my_notes_rv)
    RecyclerView myNotesRV;
    @BindView(R.id.my_notes_sort_iv)
    ImageView sortIV;

//    private LiveData<List<NoteEntryM>> noteEntries;
    private NoteEntriesAdapter adapter;
    private NoteEntryViewModel noteEntryViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteEntryViewModel = ViewModelProviders.of(this).get(NoteEntryViewModel.class);

        LiveData<List<NoteEntryM>> allNoteEntries = noteEntryViewModel.getAllNoteEntries();

        if(allNoteEntries!=null)
        {
//            Log.d(TAG, "onCreate: note entries count " + allNoteEntries.getValue().size());
            noteEntryViewModel.getAllNoteEntries().observe(this, new Observer<List<NoteEntryM>>() {
                @Override
                public void onChanged(List<NoteEntryM> noteEntries) {

                    if(adapter!=null)
                    {
                        adapter.submitList(noteEntries);
                    }
                }
            });
        }else
        {
            Log.d(TAG, "onCreate: allNoteEntries is null");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_notes, container, false);
        ButterKnife.bind(this,view);
        myNotesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        myNotesRV.setHasFixedSize(true);

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
        adapter = new NoteEntriesAdapter();
        adapter.setCallBackListener(this);
        myNotesRV.setAdapter(adapter);
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

    @Override
    public void onNoteEntryClicked(NoteEntryM noteEntry) {
        Intent intent = new Intent(getActivity(), EditNoteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(MainActivity.EXTRA_NOTE_ENTRY,noteEntry);
        startActivity(intent);
    }
}
