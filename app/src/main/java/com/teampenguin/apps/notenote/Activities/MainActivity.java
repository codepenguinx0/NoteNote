package com.teampenguin.apps.notenote.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.teampenguin.apps.notenote.Adapters.MainViewPagerAdapter;
import com.teampenguin.apps.notenote.Fragments.MyNotesFragment;
import com.teampenguin.apps.notenote.Fragments.TodoListFragment;
import com.teampenguin.apps.notenote.Models.NoteEntryM;
import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.Utils.Utils;
import com.teampenguin.apps.notenote.ViewModels.NoteEntryViewModel;

import java.util.List;


public class MainActivity extends BaseActivity{

//    private NoteEntryViewModel noteEntryViewModel;
    private static final String TAG = "MainActivity";
    private ViewPager viewPager;
    private EditText searchBarET;
    private ImageView menuIV;
    private ImageView settingsIV;
    private LinearLayout goToMyNotesLL;
    private LinearLayout goToTodoListLL;
    private ImageView addIV;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolBar = findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getViews();
        setViewPagerAdapter();
        setButtons();

//        noteEntryViewModel = ViewModelProviders.of(this).get(NoteEntryViewModel.class);
//        noteEntryViewModel.getAllNoteEntries().observe(this, new Observer<List<NoteEntryM>>() {
//            @Override
//            public void onChanged(List<NoteEntryM> noteEntries) {
//                updateList(noteEntries);
//            }
//        });
    }

    private void getViews()
    {
        viewPager = findViewById(R.id.main_view_pager);
        searchBarET = findViewById(R.id.tool_bar_search_bar_et);
        menuIV = findViewById(R.id.tool_bar_menu_iv);
        settingsIV = findViewById(R.id.tool_bar_settings_iv);
        goToMyNotesLL = findViewById(R.id.bottom_bar_my_notes_ll);
        goToTodoListLL = findViewById(R.id.bottom_bar_todo_list_ll);
        addIV = findViewById(R.id.main_add_iv);

        //for removing focus from the search bar edit text when the keyboard is hidden
        final View activityRootView = findViewById(R.id.main_activity_root_rl);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                Log.d(TAG, "onGlobalLayout: heightDiff" + heightDiff);
                //the normal difference between the activityRootView and the real RootView is around 200 (189)
                if(heightDiff > 100 && heightDiff < 300)
                {
                    searchBarET.clearFocus();
                }
            }
        });
        //end

    }

    private void setViewPagerAdapter()
    {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());

        TodoListFragment todoListFragment = new TodoListFragment();
        MyNotesFragment myNotesFragment = new MyNotesFragment();
        adapter.addFragment(todoListFragment);
        adapter.addFragment(myNotesFragment);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0)
                {
                    onTodoListPageSelected();
                }else if(position==1)
                {
                    onMyNotesPageSelected();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //todo list selected by default
        onTodoListPageSelected();
    }

    private void setButtons()
    {
        goToTodoListLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
                onTodoListPageSelected();
            }
        });

        goToMyNotesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                onMyNotesPageSelected();
            }
        });
        
        addIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Toast.makeText(MainActivity.this, "Add!", Toast.LENGTH_SHORT).show();
            }
        });

        menuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Toast.makeText(MainActivity.this, "Open Drawer!", Toast.LENGTH_SHORT).show();
            }
        });

        settingsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Toast.makeText(MainActivity.this, "Open Settings!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onTodoListPageSelected()
    {
        goToTodoListLL.setBackgroundColor(getResources().getColor(R.color.main_dark_yellow));
        goToMyNotesLL.setBackgroundColor(getResources().getColor(R.color.main_yellow));
    }

    private void onMyNotesPageSelected()
    {
        goToMyNotesLL.setBackgroundColor(getResources().getColor(R.color.main_dark_yellow));
        goToTodoListLL.setBackgroundColor(getResources().getColor(R.color.main_yellow));
    }

//    private void updateList(List<NoteEntryM> noteEntries)
//    {
//        //TODO update recycler view adapter
//    }

}
