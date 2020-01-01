package com.teampenguin.apps.notenote.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teampenguin.apps.notenote.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodoListFragment extends Fragment {


    @BindView(R.id.todo_list_rv)
    RecyclerView todoListRV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);
        ButterKnife.bind(this,view);
        todoListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
}
