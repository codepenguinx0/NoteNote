package com.teampenguin.apps.notenote.Activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teampenguin.apps.notenote.Adapters.EditNotePhotoListAdapter;
import com.teampenguin.apps.notenote.Fragments.CategoryPopupFragment;
import com.teampenguin.apps.notenote.Fragments.ChooseMoodFragment;
import com.teampenguin.apps.notenote.Fragments.ChooseTextColourPopupFragment;
import com.teampenguin.apps.notenote.Fragments.CommonFragmentInterface;
import com.teampenguin.apps.notenote.Fragments.InsertLinkPopupFragment;
import com.teampenguin.apps.notenote.Fragments.PickImagePopupFragment;
import com.teampenguin.apps.notenote.Fragments.ViewPhotoFragment;
import com.teampenguin.apps.notenote.Models.NoteEntryM;
import com.teampenguin.apps.notenote.Models.NoteEntryPhoto;
import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.Repositories.NoteEntryPhotoRepository;
import com.teampenguin.apps.notenote.Utils.Utils;
import com.teampenguin.apps.notenote.ViewModels.NoteEntryPhotoViewModel;
import com.teampenguin.apps.notenote.ViewModels.NoteEntryViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;

public class EditNoteActivity extends AppCompatActivity implements PickImagePopupFragment.PickImagePopupCallBack, CommonFragmentInterface,
        InsertLinkPopupFragment.InsertLinkPopupCallBack, ChooseTextColourPopupFragment.ChooseTextColourPopupCallBack, CategoryPopupFragment.CategoryPopupCallBack,
        NoteEntryPhotoRepository.PhotoRepoResultsCallBack, EditNotePhotoListAdapter.PhotoListAdapterCallBack, ViewPhotoFragment.ViewPhotoFragmentCallBack,
        ChooseMoodFragment.ChooseMoodFragmentCallBack {

    public static final String TAG = "EditNoteActivity";

    public static final int PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE = 101;
    public static final int PERMISSION_REQUEST_CODE_CAMERA = 102;
    public static final int REQUEST_CODE_IMAGE_CAPTURE = 201;
    public static final int REQUEST_CODE_PICK_IMAGE = 202;

    private static final String appDirectoryName = "NoteNote";
    public static final int POPUP_DELAY_MS = 300;
    public static final int PHOTO_BAR_ANIMATION_DURATION = 500;

    public static final int MOOD_HAPPY = 0;
    public static final int MOOD_GRATEFUL = 1;
    public static final int MOOD_CONTENT = 2;
    public static final int MOOD_SAD = 3;
    public static final int MOOD_SHOCKED = 4;
    public static final int MOOD_ANGRY = 5;

    private File imageRoot;

    @BindView(R.id.edit_note_editor)
    RichEditor editor;
    @BindView(R.id.edit_note_title_et)
    EditText noteTitleET;
    @BindView(R.id.edit_note_editor_options_hsv)
    HorizontalScrollView editorOptionsHSV;
    @BindView(R.id.edit_note_editor_options_ll)
    LinearLayout editorOptionsLL;
    @BindView(R.id.edit_note_category_tv)
    TextView categoryTV;
    @BindView(R.id.edit_note_show_photos_rl)
    RelativeLayout showPhotosRL;
    @BindView(R.id.edit_note_title_hide_area_rl)
    RelativeLayout titleAreaRL;
    @BindView(R.id.edit_note_hide_title_iv)
    ImageView hideTitleArrowIV;
    @BindView(R.id.edit_note_photos_rv)
    RecyclerView photosRV;
    @BindView(R.id.edit_note_mood_iv)
    ImageView moodIconIV;

    private String currentInternalPhotoPath;

    private int hsvWidth;
    private int llWidth;

    private NoteEntryViewModel noteEntryViewModel;
    private NoteEntryPhotoViewModel noteEntryPhotoViewModel;
    private NoteEntryM currentNote = null;
    private String newChosenCategory = null;
    private EditNotePhotoListAdapter photoListAdapter = null;
    private int currentMood = 0;

    private boolean isShowingPhotos = false;
    private boolean isShowingTitle =true;

    //<----------------Super class methods---------------->
    //region SUPER CLASS METHODS
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        ButterKnife.bind(this);
        noteEntryViewModel = ViewModelProviders.of(this).get(NoteEntryViewModel.class);
        noteEntryPhotoViewModel = ViewModelProviders.of(this).get(NoteEntryPhotoViewModel.class);

        Intent intent = getIntent();
        if (intent != null) {
            currentNote = intent.getParcelableExtra(MainActivity.EXTRA_NOTE_ENTRY);
            if (currentNote != null) {
                mapNoteData();
            }
        }

        if (currentNote == null) {
            categoryTV.setText(NoteEntryM.DEFAULT_CATEGORY);
        }

        checkPermissions();
        initializeEditor();
        setOnGlobalLayoutListener();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.animation_activity_slide_from_right, R.anim.animation_activity_slide_to_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.animation_activity_slide_from_left, R.anim.animation_activity_slide_to_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {

                handleTakenPhoto();

            } else if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                if (data == null) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    //TODO how to convert a uri to absolute path
                    Uri uri = data.getData();

                    convertUriToBitmap(uri);

                    Log.d(TAG, "onActivityResult: pick photo from " + uri.getPath());
                }
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults != null) {
            if (requestCode == PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: read ok!!");
                    checkPermissions();
                } else {
                    Toast.makeText(this, "You must grant permission to read external storage", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else if (requestCode == PERMISSION_REQUEST_CODE_CAMERA) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: read ok!!");
                    checkPermissions();
                } else {
                    Toast.makeText(this, "You must grant permission to read external storage", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {

        back();
//        super.onBackPressed();
    }

    //endregion

    //<----------------Buttons---------------->
    //region BUTTONS

    //BOLD
    @OnClick(R.id.editor_bold_iv)
    public void changeTextStyleBold() {
        editor.setBold();
    }

    //ITALIC
    @OnClick(R.id.editor_italic_iv)
    public void changeTextStyleItalic() {
        editor.setItalic();
    }

    //UNDERLINE
    @OnClick(R.id.editor_underline_iv)
    public void changeTextStyleUnderline() {
        editor.setUnderline();
    }

    //STRIKETHROUGH
    @OnClick(R.id.editor_strikethrough_iv)
    public void changeTextStyleStrikeThrough() {
        editor.setStrikeThrough();
    }

    //UNDO
    @OnClick(R.id.editor_undo_iv)
    public void changeTextUndo() {
        editor.undo();
    }

    //REDO
    @OnClick(R.id.editor_redo_iv)
    public void changeTextRedo() {
        editor.redo();
    }

    //INSERT LINK
    @OnClick(R.id.editor_link_iv)
    public void insertLink() {
        showInsertLinkPopup();
    }

    //BULLET LIST
    @OnClick(R.id.editor_bulleted_list_iv)
    public void changeTextBulletList() {
        editor.setBullets();
    }

    //NUMBER LIST
    @OnClick(R.id.editor_numbered_list_iv)
    public void changeTextNumberList() {
        editor.setNumbers();
    }

    @OnClick(R.id.edit_note_show_photos_add_iv)
    public void insertImage() {
        showImagePopup();
    }

    //CHANGE TEXT COLOUR
    @OnClick(R.id.editor_text_colour_iv)
    public void changeTextColour() {
        showChangeColourPopup();
    }

    //SAVE ICON
    @OnClick(R.id.edit_note_save_iv)
    public void saveNote() {
        if (currentNote == null) {
            createNewNote();
        } else {
            updateNote();
        }
    }

    //BACK
    @OnClick(R.id.edit_note_back_iv)
    public void back() {

        //show not save alert if either content/title/category has been changed
        String content = getEditorContent();

         if (newChosenCategory != null) {

            if (currentNote!=null && !newChosenCategory.equals(currentNote.getCategory())) {
                showNotSaveAlert();
                return;
            }
        }

        if (content != null && !content.isEmpty()) {
            if (currentNote == null || !content.equals(currentNote.getContent())) {
                showNotSaveAlert();
                return;
            }
        }

        if (!Utils.isEditTextEmpty(noteTitleET)) {

            if(currentNote == null || !noteTitleET.getText().toString().equals(currentNote.getNoteTitle()))
            {
                showNotSaveAlert();
                return;
            }
        }

        if(currentNote!=null && currentMood!=currentNote.getMood())
        {
            showNotSaveAlert();
            return;
        }

        endActivity();
    }

    //CATEGORY
    @OnClick(R.id.edit_note_category_rl)
    public void chooseCategory() {
        showCategoryPopup();
    }

    //SHOW PHOTOS
    @OnClick(R.id.edit_note_show_photos_iv)
    public void showOrHidePhotos()
    {
        if(isShowingPhotos)
        {
            isShowingPhotos = false;
            closePhotosBar();
        }else
        {
            isShowingPhotos = true;
            openPhotoBar();
        }
    }

    //HIDE OR SHOW TITLE AND CATEGORY AREA
    @OnClick(R.id.edit_note_hide_title_rl)
    public void showOrHideTitle()
    {
        if(isShowingTitle)
        {
            hideTitle();
        }else
        {
            showTitle();
        }
    }

    //MOOD
    @OnClick(R.id.edit_note_choose_mood_ll)
    public void chooseMood()
    {
        showChooseMoodFragment();
    }
    
    //UPLOAD
    @OnClick(R.id.edit_note_upload_iv)
    public void upload()
    {
        Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show();
    }

    //endregion

    //<----------------functions---------------->
    //region FUNCTIONS

    private void initializeEditor() {
//        editor.setEditorHeight(200);
        editor.setEditorFontSize(20);
        editor.setEditorFontColor(Color.BLACK);
        editor.setPadding(20, 20, 20, 20);
        editor.setPlaceholder("Insert text here...");
        editor.setInputEnabled(true);
        editor.setBackgroundColor(getResources().getColor(R.color.transparent));
        clearAllFocus();
    }

    private void clearAllFocus() {
        editor.clearFocus();
        noteTitleET.clearFocus();
    }

    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE);
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE_CAMERA);
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE_CAMERA);
        } else {

            Log.d(TAG, "checkPermissions: all permissions granted!");
            createPhotoFolder();
        }
    }

    private void checkLayout() {

        if (hsvWidth > llWidth) {
            Log.d(TAG, "checkLayout: editorOptionsHSV.getWidth() > editorOptionsLL.getWidth()");
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) editorOptionsLL.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            editorOptionsLL.setLayoutParams(layoutParams);
        } else if (hsvWidth <= llWidth) {
            Log.d(TAG, "checkLayout: editorOptionsHSV.getWidth() <= editorOptionsLL.getWidth()");
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) editorOptionsLL.getLayoutParams();
            layoutParams.gravity = Gravity.NO_GRAVITY;
            editorOptionsLL.setLayoutParams(layoutParams);
        }

        closePhotosBarInitial();
    }

    private void setOnGlobalLayoutListener() {
        final View activityRootView = findViewById(R.id.edit_note_activity_root);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                Log.d(TAG, "onGlobalLayout: heightDiff" + heightDiff);
                //the normal difference between the activityRootView and the real RootView is around 200 (189)
                if (heightDiff > 10 && heightDiff < 300) {
                    clearAllFocus();
                }
            }
        });

        editorOptionsHSV.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                editorOptionsHSV.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                hsvWidth = editorOptionsHSV.getWidth();
                Log.d(TAG, "onGlobalLayout: HSV width " + hsvWidth);
                if (llWidth != 0) {
                    checkLayout();
                }
            }
        });

        editorOptionsLL.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                editorOptionsLL.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                llWidth = editorOptionsLL.getWidth();
                Log.d(TAG, "onGlobalLayout: LL width " + llWidth);
                if (hsvWidth != 0) {
                    checkLayout();
                }
            }
        });
    }

    private void mapNoteData() {

        noteTitleET.setText(currentNote.getNoteTitle());
        editor.setHtml(currentNote.getContent());
        categoryTV.setText(currentNote.getCategory());
        currentMood = currentNote.getMood();
        setMoodIcon();
        getPhotosForThisNote();

    }

    private void setMoodIcon(){

        switch (currentMood)
        {
            case MOOD_HAPPY:
                moodIconIV.setImageDrawable(getResources().getDrawable(R.drawable.mood_happy));
                break;
            case MOOD_GRATEFUL:
                moodIconIV.setImageDrawable(getResources().getDrawable(R.drawable.mood_grateful));
                break;
            case MOOD_CONTENT:
                moodIconIV.setImageDrawable(getResources().getDrawable(R.drawable.mood_content));
                break;
            case MOOD_SAD:
                moodIconIV.setImageDrawable(getResources().getDrawable(R.drawable.mood_sad));
                break;
            case MOOD_SHOCKED:
                moodIconIV.setImageDrawable(getResources().getDrawable(R.drawable.mood_shocked));
                break;
            case MOOD_ANGRY:
                moodIconIV.setImageDrawable(getResources().getDrawable(R.drawable.mood_angry));
                break;
            default:
                moodIconIV.setImageDrawable(getResources().getDrawable(R.drawable.mood_happy));
        }
    }

    private void setAdapterForPhotoList(List<NoteEntryPhoto> photos)
    {
        if(photoListAdapter==null)
        {
            photoListAdapter = new EditNotePhotoListAdapter();
            photoListAdapter.setCallBackListener(this);
        }

        photoListAdapter.submitList(photos);

        if(photosRV.getAdapter()==null)
        {
            photosRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            photosRV.setAdapter(photoListAdapter);
        }
    }

    private void endActivity()
    {
        this.finish();
    }

    private void closePhotosBarInitial()
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(showPhotosRL, "translationX", hsvWidth - hsvWidth/5f);
        animator.setDuration(0);
        animator.start();
    }

    private void closePhotosBar()
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(showPhotosRL, "translationX", hsvWidth - hsvWidth/5f);
        animator.setDuration(PHOTO_BAR_ANIMATION_DURATION);
        animator.start();
    }

    private void openPhotoBar()
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(showPhotosRL, "translationX", 0);
        animator.setDuration(PHOTO_BAR_ANIMATION_DURATION);
        animator.start();
    }

    private void hideTitle()
    {
        isShowingTitle = false;
        titleAreaRL.setVisibility(View.GONE);
        hideTitleArrowIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_down));

        if(noteTitleET.hasFocus())
        {
            Utils.hideSoftKeyboard(this);
            noteTitleET.clearFocus();
        }

    }

    private void showTitle()
    {
        isShowingTitle = true;
        titleAreaRL.setVisibility(View.VISIBLE);
        hideTitleArrowIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_up));
    }

    private void createPhotoFolder() {
        imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appDirectoryName);

        if (!imageRoot.exists()) {
            if (imageRoot.mkdirs()) {
                Log.d("mkdir", "success");
            }
        } else {
            Log.d(TAG, "createPhotoFolder: imageRoot already existed");
        }
    }

    private void showImagePopup() {

        Utils.hideSoftKeyboard(this);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                PickImagePopupFragment fragment = new PickImagePopupFragment(EditNoteActivity.this, EditNoteActivity.this);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.edit_note_frame, fragment, PickImagePopupFragment.TAG)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(PickImagePopupFragment.TAG)
                        .commit();
            }
        };

        delayPopup(r);

    }

    private void showInsertLinkPopup() {

        Utils.hideSoftKeyboard(this);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                InsertLinkPopupFragment fragment = new InsertLinkPopupFragment(EditNoteActivity.this, EditNoteActivity.this);
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.edit_note_frame, fragment, InsertLinkPopupFragment.TAG)
                        .addToBackStack(InsertLinkPopupFragment.TAG)
                        .commit();
            }
        };

        delayPopup(r);
    }

    private void showChangeColourPopup() {

//        Utils.hideSoftKeyboard(this);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                ChooseTextColourPopupFragment fragment = new ChooseTextColourPopupFragment(EditNoteActivity.this, EditNoteActivity.this);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.edit_note_frame, fragment, ChooseTextColourPopupFragment.TAG)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(ChooseTextColourPopupFragment.TAG)
                        .commit();
            }
        };

        delayPopup(r);
    }

    private void showCategoryPopup() {

        Utils.hideSoftKeyboard(this);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                CategoryPopupFragment fragment = new CategoryPopupFragment(EditNoteActivity.this, EditNoteActivity.this);

                if (currentNote != null) {
                    if (newChosenCategory != null) {
                        fragment.setNoteCategory(newChosenCategory);
                    } else {
                        fragment.setNoteCategory(currentNote.getCategory());
                    }
                } else {
                    if (newChosenCategory != null) {
                        fragment.setNoteCategory(newChosenCategory);
                    } else {
                        fragment.setNoteCategory(NoteEntryM.DEFAULT_CATEGORY);
                    }
                }

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.edit_note_frame, fragment, CategoryPopupFragment.TAG)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(CategoryPopupFragment.TAG)
                        .commit();
            }
        };

        delayPopup(r);

    }

    private void showChooseMoodFragment()
    {
        //TODO
        ChooseMoodFragment fragment = ChooseMoodFragment.getInstance(currentNote==null? 0:currentNote.getMood());
        fragment.setCallBackListener(this);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.edit_note_frame, fragment, ChooseMoodFragment.TAG)
                .addToBackStack(ChooseMoodFragment.TAG)
                .commit();
    }

    private void delayPopup(Runnable r) {
        //delay the popup for 300ms for the keyboard to hide first
        Handler handler = new Handler();
        handler.postDelayed(r, POPUP_DELAY_MS);
    }

    private void closeFragmentByTag(String tag) {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .remove(fragment)
                    .commit();
            getSupportFragmentManager().popBackStack();
        }
    }

    private File createPhotoFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentInternalPhotoPath = image.getAbsolutePath();
        Log.d(TAG, "createPhotoFile: currentInternalPhotoPath " + currentInternalPhotoPath);
        return image;
    }

    private void handleTakenPhoto() {

        savePhotoToExternalStorage();
        createNewPhotoObject();
    }

    private void convertUriToBitmap(Uri uri)
    {
        Bitmap bmp = null;

        try {
            bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(bmp == null)
        {
            Toast.makeText(this, "Cannot load image", Toast.LENGTH_SHORT).show();
            return;
        }

        createNewPhotoObject(bmp);
    }


    private void savePhotoToExternalStorage() {

        SavePhotoToExternalStorageAsyncTask task = new SavePhotoToExternalStorageAsyncTask();
        task.execute(imageRoot.getAbsolutePath(), currentInternalPhotoPath);
    }

//    private void resizeCurrentPhoto() {
//        Bitmap bmp = BitmapFactory.decodeFile(currentInternalPhotoPath);
//
//        if (bmp != null) {
//            Log.d(TAG, "resizeCurrentPhoto: yo1");
//            Bitmap resizedBmp = Utils.getResizedBitmap(bmp, (int) (bmp.getWidth() * 0.1), (int) (bmp.getHeight() * 0.1));
//            try (FileOutputStream out = new FileOutputStream(currentInternalPhotoPath)) {
//                Log.d(TAG, "resizeCurrentPhoto: yo2");
//                resizedBmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void createNewPhotoObject(Bitmap bmp)
    {
        //must save the note before a new photo object is added
        saveNote();

        NoteEntryPhoto newPhoto = new NoteEntryPhoto();
        newPhoto.setCreatedDate(Utils.convertDateToString(new Date()));
        newPhoto.setOwnerNoteId(currentNote==null? -1: currentNote.getId());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();

        newPhoto.setImage(byteArray);

        Log.d(TAG, "createNewPhotoObject: newPhoto " + newPhoto.toString());

        noteEntryPhotoViewModel.insert(newPhoto);
        getPhotosForThisNote();
    }

    private void createNewPhotoObject() {

        //must save the note before a new photo object is added
        saveNote();

        NoteEntryPhoto newPhoto = new NoteEntryPhoto();
        newPhoto.setCreatedDate(Utils.convertDateToString(new Date()));
        newPhoto.setOwnerNoteId(currentNote==null? -1: currentNote.getId());
        newPhoto.setImage(Utils.getByteArrayFromPath(currentInternalPhotoPath));

        Log.d(TAG, "createNewPhotoObject: newPhoto " + newPhoto.toString());

        long insertId = noteEntryPhotoViewModel.insert(newPhoto);
        Log.d(TAG, "createNewPhotoObject: insertId " + insertId);
        getPhotosForThisNote();
    }

    private void getPhotosForThisNote()
    {
        noteEntryPhotoViewModel.getNoteEntryPhotosByOwnerNoteId(currentNote.getId(), this);
        Log.d(TAG, "getPhotosForThisNote: current note id" + currentNote.getId());
//        Log.d(TAG, "getPhotosForThisNote: number of photos" + listOfPhotos.size());
//        setAdapterForPhotoList(listOfPhotos);

        Log.d(TAG, "getPhotosForThisNote: all photos number " + noteEntryPhotoViewModel.getAllPhotos().size());
    }

    private String getEditorContent() {

        if (editor.getHtml() == null) {
            return "";
        }

        return editor.getHtml();
    }

    private void createNewNote() {

        NoteEntryM newNoteEntry = new NoteEntryM();

        newNoteEntry.setNoteTitle(Utils.isEditTextEmpty(noteTitleET) ? "" : noteTitleET.getText().toString());
        newNoteEntry.setContent(getEditorContent());
        newNoteEntry.setCreateDate(Utils.convertDateToString(new Date()));
        newNoteEntry.setModifiedDate(Utils.convertDateToString(new Date()));
        newNoteEntry.setCategory(newChosenCategory == null ? NoteEntryM.DEFAULT_CATEGORY : newChosenCategory);
        newNoteEntry.setMood(currentMood);

        long newNoteEntryId = noteEntryViewModel.insert(newNoteEntry);

        Log.d(TAG, "createNewNote: new note entry id " + newNoteEntryId);

        currentNote = noteEntryViewModel.getNoteEntryById(newNoteEntryId);

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    private void updateNote() {

        NoteEntryM updatedNoteEntry = new NoteEntryM();

        updatedNoteEntry.setId(currentNote.getId());
        updatedNoteEntry.setAuthorId(currentNote.getAuthorId());
        updatedNoteEntry.setAuthorName(currentNote.getAuthorName());
        updatedNoteEntry.setCreateDate(currentNote.getCreateDate());
        updatedNoteEntry.setModifiedDate(Utils.convertDateToString(new Date()));
        updatedNoteEntry.setNoteTitle(Utils.isEditTextEmpty(noteTitleET) ? "" : noteTitleET.getText().toString());
        updatedNoteEntry.setContent(getEditorContent());
        updatedNoteEntry.setCategory(newChosenCategory == null ? currentNote.getCategory() : newChosenCategory);
        updatedNoteEntry.setMood(currentMood);

        noteEntryViewModel.update(updatedNoteEntry);
        currentNote = updatedNoteEntry;

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    private void showNotSaveAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Note not saved")
                .setMessage("Do you wish to quit without saving?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        endActivity();
                    }
                })
                .setNeutralButton("Save and Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNote();
                        endActivity();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        alertDialog.show();
    }

    private void openViewPhotoFragment(NoteEntryPhoto photo)
    {
        //TODO
        ViewPhotoFragment fragment = new ViewPhotoFragment();
        fragment.setNoteEntryPhoto(photo);
        fragment.setListeners(this, this);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.edit_note_frame, fragment, ViewPhotoFragment.TAG)
                .addToBackStack(ViewPhotoFragment.TAG)
                .commit();
    }
    //endregion

    // <----------------interface methods---------------->
    //region INTERFACE METHODS
    @Override
    public void takePhoto() {

        closeFragmentByTag(PickImagePopupFragment.TAG);

        File photoFile = null;

        try {

            photoFile = createPhotoFile();

        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(this, "Failed to create file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (photoFile != null) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.teampenguin.apps.fileprovider",
                    photoFile);
//            Log.d(TAG, "takePhoto: photoURI.getPath()" + photoURI.getPath());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE);
        }

    }

    @Override
    public void openGallery() {

        closeFragmentByTag(PickImagePopupFragment.TAG);
        Toast.makeText(this, "open gallery!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pick a photo"), REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void closeFragment(String tag) {
        closeFragmentByTag(tag);
    }

    @Override
    public void insertLink(String url, String displayName) {

        closeFragment(InsertLinkPopupFragment.TAG);
        editor.insertLink(url, displayName);
    }

    @Override
    public void changeTextColour(String colour) {

//        Log.d(TAG, "changeTextColour: choose colour " + colour);
        editor.setTextColor(Color.parseColor(colour));
        closeFragment(ChooseTextColourPopupFragment.TAG);
        editor.requestFocus();
    }

    @Override
    public void onCategoryChosen(String category) {

        newChosenCategory = category;
        categoryTV.setText(category);
    }

    @Override
    public void onCategoryRemovedFromList(String category) {

//        Log.d(TAG, "onCategoryRemovedFromList: ");
        noteEntryViewModel.resetNotesCategory(category);
        newChosenCategory = NoteEntryM.DEFAULT_CATEGORY;
    }

    @Override
    public void returnListOfPhotos(final List<NoteEntryPhoto> photos) {

//        Log.d(TAG, "returnListOfPhotos: photos.size() " + photos.size());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setAdapterForPhotoList(photos);
            }
        });

    }

    @Override
    public void onPhotoChosen(NoteEntryPhoto photo) {
        Utils.hideSoftKeyboard(this);
        openViewPhotoFragment(photo);
    }

    @Override
    public void onSavePhoto(NoteEntryPhoto photo) {

        closeFragment(ViewPhotoFragment.TAG);
        noteEntryPhotoViewModel.update(photo);
        getPhotosForThisNote();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeletePhoto(NoteEntryPhoto photo) {

        closeFragment(ViewPhotoFragment.TAG);
        noteEntryPhotoViewModel.delete(photo);
        getPhotosForThisNote();
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChooseMoodFragmentClosed(int mood) {
        closeFragment(ChooseMoodFragment.TAG);
        currentMood = mood;
        setMoodIcon();
    }
    //endregion

    //region INNER CLASSES

    private static class SavePhotoToExternalStorageAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {

            FileOutputStream fos = null;
            Date currentTime = new Date();
            String fileName = "image_" + Utils.getTimeStringForFileName(currentTime) + ".jpg";
            Log.d(TAG, "SavePhotoToExternalStorageAsyncTask doInBackground: fileName " + fileName);
            File outputImage = new File(strings[0] + File.separator + fileName);

            try {
                outputImage.createNewFile();
                Bitmap bmp = BitmapFactory.decodeFile(strings[1]);
                fos = new FileOutputStream(outputImage);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (IOException e) {
                e.printStackTrace();

            } finally {

                if (fos != null) {
                    try {
                        Log.d(TAG, "doInBackground: close outputstream");
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }
    }

    //endregion
}
