package com.teampenguin.apps.notenote.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class MyNotesFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, NoteEntriesAdapter.NoteEntriesAdapterCallBack {

    public static final String TAG = "MyNotesFragment";

    @BindView(R.id.my_notes_rv)
    RecyclerView myNotesRV;
    @BindView(R.id.my_notes_sort_iv)
    ImageView sortIV;
    @BindView(R.id.my_notes_no_results_rl)
    RelativeLayout noResultsRL;

    private NoteEntriesAdapter adapter;
    private NoteEntryViewModel noteEntryViewModel;
    private ArrayList<NoteEntryM> originalList = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteEntryViewModel = ViewModelProviders.of(this).get(NoteEntryViewModel.class);

        noteEntryViewModel.getAllNoteEntries().observe(this, new Observer<List<NoteEntryM>>() {
            @Override
            public void onChanged(List<NoteEntryM> noteEntries) {

                if(adapter!=null)
                {
                    adapter.submitList(noteEntries);
                }
            }
        });


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

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.sorting_popup_menu_create_date_lto:
                Log.d(TAG, "onMenuItemClick: create date lto");
                sortByCreateDateLToO();
                return true;
            case R.id.sorting_popup_menu_create_date_otl:
                Log.d(TAG, "onMenuItemClick: otl ");
                sortByCreateDateOToL();
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
                sortByCategory();
                Log.d(TAG, "onMenuItemClick: category");
                return true;
            case R.id.sorting_popup_menu_mood:
                sortByMood();
                Log.d(TAG, "onMenuItemClick: mood");
                return true;
            default:
                return false;
        }
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
        ArrayList<NoteEntryM> sortedList = new ArrayList<>(adapter.getCurrentList());
        Collections.sort(sortedList, new Comparator<NoteEntryM>() {
            @Override
            public int compare(NoteEntryM o1, NoteEntryM o2) {
//                Date d1 = Utils.convertDateStringToDate(o1.getCreateDate());
//                Date d2 = Utils.convertDateStringToDate(o2.getCreateDate());
//                return d2.compareTo(d1);
                return o2.getId() - o1.getId();
            }
        });

        adapter.submitList(sortedList);
    }

    private void sortByCreateDateOToL()
    {
        ArrayList<NoteEntryM> sortedList = new ArrayList<>(adapter.getCurrentList());
        Collections.sort(sortedList, new Comparator<NoteEntryM>() {
            @Override
            public int compare(NoteEntryM o1, NoteEntryM o2) {
//                Date d1 = Utils.convertDateStringToDate(o1.getCreateDate());
//                Date d2 = Utils.convertDateStringToDate(o2.getCreateDate());
//                return d1.compareTo(d2);
                return o1.getId() - o2.getId();
            }
        });

        adapter.submitList(sortedList);
    }

    private void sortByTitleAToZ()
    {
        ArrayList<NoteEntryM> sortedList = new ArrayList<>(adapter.getCurrentList());
        Collections.sort(sortedList, new Comparator<NoteEntryM>() {
            @Override
            public int compare(NoteEntryM o1, NoteEntryM o2) {
                return o1.getNoteTitle().compareTo(o2.getNoteTitle());
            }
        });

        adapter.submitList(sortedList);
    }

    private void sortByTitleZToA()
    {
        ArrayList<NoteEntryM> sortedList = new ArrayList<>(adapter.getCurrentList());
        Collections.sort(sortedList, new Comparator<NoteEntryM>() {
            @Override
            public int compare(NoteEntryM o1, NoteEntryM o2) {
                return o2.getNoteTitle().compareTo(o1.getNoteTitle());
            }
        });

        adapter.submitList(sortedList);
    }

    private void sortByCategory()
    {
        ArrayList<NoteEntryM> sortedList = new ArrayList<>(adapter.getCurrentList());
        Collections.sort(sortedList, new Comparator<NoteEntryM>() {
            @Override
            public int compare(NoteEntryM o1, NoteEntryM o2) {
                return o1.getCategory().compareTo(o2.getCategory());
            }
        });

        adapter.submitList(sortedList);
    }

    private void sortByMood()
    {
        ArrayList<NoteEntryM> sortedList = new ArrayList<>(adapter.getCurrentList());
        Collections.sort(sortedList, new Comparator<NoteEntryM>() {
            @Override
            public int compare(NoteEntryM o1, NoteEntryM o2) {
                return o1.getMood() - o2.getMood();
            }
        });

        adapter.submitList(sortedList);
    }

    private void showDeleteNoteEntryAlert(final NoteEntryM noteEntry)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Delete Note Entry")
                .setMessage("Are you sure to delete this note entry?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNoteEntry(noteEntry);
                    }
                })
                .setNegativeButton("Cancel",null)
                .create();

        alertDialog.show();
    }

    private void deleteNoteEntry(NoteEntryM noteEntryM)
    {
        noteEntryViewModel.delete(noteEntryM);
    }

    public void searchNotes(String keyword)
    {
        if(originalList == null)
        {
            originalList = new ArrayList<>(adapter.getCurrentList());
        }

        ArrayList<NoteEntryM> searchList = new ArrayList<>();

        for (int i = 0; i < originalList.size(); i++) {
            if(originalList.get(i).getNoteTitle().toLowerCase().contains(keyword) || originalList.get(i).getCategory().toLowerCase().contains(keyword))
            {
                searchList.add(originalList.get(i));
            }
        }

        adapter.submitList(searchList);

        if(searchList.size() == 0)
        {
            //show no results found
            noResultsRL.setVisibility(View.VISIBLE);
        }else
        {
            //hide no results found
            noResultsRL.setVisibility(View.GONE);
        }
    }

    public void clearSearchResult()
    {
        adapter.submitList(originalList);
        originalList = null;
        noResultsRL.setVisibility(View.GONE);
        //hide no results found
    }


    @Override
    public void onNoteEntryClicked(NoteEntryM noteEntry) {

        Intent intent = new Intent(getActivity(), EditNoteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(MainActivity.EXTRA_NOTE_ENTRY,noteEntry);
        if(getActivity()!=null)
        {
            getActivity().startActivity(intent);
        }else
        {
            startActivity(intent);
        }
    }

    @Override
    public void onDeleteClicked(NoteEntryM noteEntry) {

        showDeleteNoteEntryAlert(noteEntry);
    }
}
