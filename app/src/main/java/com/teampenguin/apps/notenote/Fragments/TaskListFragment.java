package com.teampenguin.apps.notenote.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teampenguin.apps.notenote.Models.TaskEntryM;
import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.Utils.Utils;
import com.teampenguin.apps.notenote.ViewModels.TaskEntryViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskListFragment extends Fragment {


    @BindView(R.id.todo_list_rv)
    RecyclerView todoListRV;

    private TaskEntryViewModel viewModel = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(TaskEntryViewModel.class);
        viewModel.getAllTaskEntries().observe(this, new Observer<List<TaskEntryM>>() {
            @Override
            public void onChanged(List<TaskEntryM> taskEntryMS) {
                sortTasks(taskEntryMS);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);
        ButterKnife.bind(this,view);
        todoListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void sortTasks(List<TaskEntryM> taskEntries)
    {
        ArrayList<TaskEntryM> dueTasks = new ArrayList<>();
        ArrayList<TaskEntryM> advanceTasks = new ArrayList<>();

        for (int i = 0; i < taskEntries.size(); i++) {

            Date taskDeadline = Utils.convertDateStringToDate(taskEntries.get(i).getDeadline());
            if(new Date().after(taskDeadline))
            {
                dueTasks.add(taskEntries.get(i));
            }else
            {
                advanceTasks.add(taskEntries.get(i));
            }
        }

        //TODO submit the list to the adapters of the two RVs
    }
}
