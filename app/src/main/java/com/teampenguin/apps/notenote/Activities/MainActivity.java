package com.teampenguin.apps.notenote.Activities;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.teampenguin.apps.notenote.Adapters.MainViewPagerAdapter;
import com.teampenguin.apps.notenote.Fragments.AddNotesToDoPopupFragment;
import com.teampenguin.apps.notenote.Fragments.CommonFragmentInterface;
import com.teampenguin.apps.notenote.Fragments.CreateNewTaskFragment;
import com.teampenguin.apps.notenote.Fragments.MyNotesFragment;
import com.teampenguin.apps.notenote.Fragments.TaskListFragment;
import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements CommonFragmentInterface, AddNotesToDoPopupFragment.AddPopupFragmentCallBack {

    public static final String TAG = "MainActivity";
    public static final String EXTRA_NOTE_ENTRY = "existingNoteEntry";
    private static final int PAGE_TASKS = 0;
    private static final int PAGE_NOTES = 1;

    @BindView(R.id.main_view_pager)
    ViewPager viewPager;
    @BindView(R.id.tool_bar_search_bar_et)
    EditText searchBarET;
    @BindView(R.id.tool_bar_menu_iv)
    ImageView menuIV;
    @BindView(R.id.tool_bar_settings_iv)
    ImageView settingsIV;
    @BindView(R.id.bottom_bar_my_notes_ll)
    LinearLayout goToMyNotesLL;
    @BindView(R.id.bottom_bar_todo_list_ll)
    LinearLayout goToTodoListLL;
    @BindView(R.id.main_add_iv)
    ImageView addIV;
    @BindView(R.id.main_tool_bar)
    Toolbar toolBar;

//    private NoteEntryViewModel noteEntryViewModel;

    private MyNotesFragment myNotesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        toolBar = findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getViews();
        setViewPagerAdapter();

    }

    @OnClick(R.id.bottom_bar_todo_list_ll)
    public void goToTaskList() {
        viewPager.setCurrentItem(PAGE_TASKS);
        onTodoListPageSelected();
    }

    @OnClick(R.id.bottom_bar_my_notes_ll)
    public void goToNoteList() {
        viewPager.setCurrentItem(PAGE_NOTES);
        onMyNotesPageSelected();
    }

    @OnClick(R.id.main_add_iv)
    public void showAddPopup() {
        searchBarET.setEnabled(false);
        AddNotesToDoPopupFragment fragment = new AddNotesToDoPopupFragment();
        fragment.setCommonListener(this);
        fragment.setCallBackListener(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container_popup, fragment, AddNotesToDoPopupFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(AddNotesToDoPopupFragment.TAG)
                .commit();
    }

    @OnClick(R.id.tool_bar_menu_iv)
    public void openDrawer() {
        Toast.makeText(this, "Open Drawer!", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tool_bar_settings_iv)
    public void openSettings() {
        Toast.makeText(this, "Open Settings!", Toast.LENGTH_SHORT).show();
    }

    private void getViews() {
        //for removing focus from the search bar edit text when the keyboard is hidden
        final View activityRootView = findViewById(R.id.main_activity_root_rl);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                Log.d(TAG, "onGlobalLayout: heightDiff" + heightDiff);
                //the normal difference between the activityRootView and the real RootView is around 200 (189)
                if (heightDiff > 10 && heightDiff < 300) {
                    searchBarET.clearFocus();
                }
            }
        });

        searchBarET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(Utils.isEditTextEmpty(searchBarET))
                {
                    myNotesFragment.clearSearchResult();
                }else
                {
                    myNotesFragment.searchNotes(s.toString().toLowerCase());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setViewPagerAdapter() {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());

        TaskListFragment taskListFragment = new TaskListFragment();
        myNotesFragment = new MyNotesFragment();
        adapter.addFragment(taskListFragment);
        adapter.addFragment(myNotesFragment);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == PAGE_TASKS) {
                    onTodoListPageSelected();
                } else if (position == PAGE_NOTES) {
                    onMyNotesPageSelected();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //task list selected by default
        onTodoListPageSelected();
    }

    private void onTodoListPageSelected() {
        goToTodoListLL.setBackgroundColor(getResources().getColor(R.color.main_dark_yellow));
        goToMyNotesLL.setBackgroundColor(getResources().getColor(R.color.main_yellow));
    }

    private void onMyNotesPageSelected() {
        goToMyNotesLL.setBackgroundColor(getResources().getColor(R.color.main_dark_yellow));
        goToTodoListLL.setBackgroundColor(getResources().getColor(R.color.main_yellow));
    }

    private void openCreateTaskPopup()
    {
        CreateNewTaskFragment fragment = new CreateNewTaskFragment();
        fragment.setCommonListener(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container_popup, fragment, CreateNewTaskFragment.TAG)
                .addToBackStack(CreateNewTaskFragment.TAG)
                .commit();
    }

    @Override
    public void closeFragment(String tag) {

        searchBarET.setEnabled(true);
        closeFragmentByTag(tag);

    }

    @Override
    public void createNewNote() {

        closeFragmentByTag(AddNotesToDoPopupFragment.TAG);
        searchBarET.setEnabled(true);
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void createNewTask() {
        closeFragmentByTag(AddNotesToDoPopupFragment.TAG);
        searchBarET.setEnabled(true);
        openCreateTaskPopup();
    }


}
