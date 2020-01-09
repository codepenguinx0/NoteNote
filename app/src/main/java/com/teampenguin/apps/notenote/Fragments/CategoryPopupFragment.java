package com.teampenguin.apps.notenote.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teampenguin.apps.notenote.Adapters.CategoryListAdapter;
import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.Utils.SharedPreferencesHelper;
import com.teampenguin.apps.notenote.Utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryPopupFragment extends Fragment implements CategoryListAdapter.CategoryAdapterCallBack {

    public static final String TAG = "CategoryPopupFragment";

    @BindView(R.id.category_popup_new_category_et)
    EditText newCategoryET;
    @BindView(R.id.category_popup_categories_rv)
    RecyclerView categoriesRV;

    private SharedPreferencesHelper spHelper;
    private CommonFragmentInterface commonListener = null;
    private CategoryListAdapter adapter = null;

    public CategoryPopupFragment(CommonFragmentInterface commonListener)
    {
        this.commonListener = commonListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note_category_popup, container, false);
        ButterKnife.bind(this, view);

        categoriesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        setAdapter();

        return view;
    }

    @OnClick(R.id.add_new_category_button)
    public void checkNewCategoryInput()
    {
        if(Utils.isEditTextEmpty(newCategoryET))
        {
            return;
        }

        String newCategoryName = newCategoryET.getText().toString();

        if(adapter!=null)
        {
            List<String> categories = adapter.getCurrentList();
            if(categories.contains(newCategoryName))
            {
                Toast.makeText(getActivity(), "This category already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            if(SharedPreferencesHelper.addNewNoteCategory(newCategoryName))
            {
                Toast.makeText(getActivity(), "Added!", Toast.LENGTH_SHORT).show();
                Utils.hideSoftKeyboard(getActivity());
                newCategoryET.setText("");
                newCategoryET.clearFocus();
                getListForAdapter();
            }else
            {
                Toast.makeText(getActivity(), "Cannot add category", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @OnClick(R.id.category_popup_close_iv)
    public void close()
    {
        if(commonListener!=null)
        {
            commonListener.closeFragment(TAG);
        }
    }

    private void setAdapter()
    {
        adapter = new CategoryListAdapter();
        adapter.setCallBackListener(this);
        getListForAdapter();
        categoriesRV.setAdapter(adapter);
    }

    private void getListForAdapter()
    {
        List<String> tempList = Arrays.asList(getActivity().getResources().getStringArray(R.array.noteCategory));
        ArrayList<String> categories = new ArrayList<>(tempList);
        Set<String> savedCategories = SharedPreferencesHelper.getNoteCategories();
        for(String category : savedCategories)
        {
            categories.add(category);
        }
        adapter.submitList(categories);
    }

    @Override
    public void onCategoryDelete(String category) {

        if(SharedPreferencesHelper.deleteNoteCategory(category))
        {
            Toast.makeText(getActivity(), "Removed " + category, Toast.LENGTH_SHORT).show();
            getListForAdapter();
        }
    }
}
