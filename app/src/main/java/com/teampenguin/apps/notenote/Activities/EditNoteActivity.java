package com.teampenguin.apps.notenote.Activities;

import android.Manifest;
import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.teampenguin.apps.notenote.Fragments.ChooseTextColourPopupFragment;
import com.teampenguin.apps.notenote.Fragments.CommonFragmentInterface;
import com.teampenguin.apps.notenote.Fragments.InsertLinkPopupFragment;
import com.teampenguin.apps.notenote.Fragments.PickImagePopupFragment;
import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;

public class EditNoteActivity extends AppCompatActivity implements PickImagePopupFragment.PickImagePopupCallBack, CommonFragmentInterface,
        InsertLinkPopupFragment.InsertLinkPopupCallBack, ChooseTextColourPopupFragment.ChooseTextColourPopupCallBack {

    public static final String TAG = "EditNoteActivity";

    public static final int PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE = 101;
    public static final int PERMISSION_REQUEST_CODE_CAMERA = 102;
    public static final int REQUEST_CODE_IMAGE_CAPTURE = 201;
    public static final int REQUEST_CODE_PICK_IMAGE = 202;
    private static final String appDirectoryName = "NoteNote";
    public static final int POPUP_DELAY_MS = 300;

    private File imageRoot;

    @BindView(R.id.edit_note_editor)
    RichEditor editor;
    @BindView(R.id.edit_note_title_et)
    EditText noteTitleET;
    @BindView(R.id.edit_note_editor_options_hsv)
    HorizontalScrollView editorOptionsHSV;
    @BindView(R.id.edit_note_editor_options_ll)
    LinearLayout editorOptionsLL;

    private String currentPhotoPath;
    private int hsvWidth;
    private int llWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        ButterKnife.bind(this);

        checkPermissions();
        initializeEditor();
        checkLayout();

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
                if(llWidth!=0)
                {
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
                if(hsvWidth!=0)
                {
                    checkLayout();
                }
            }
        });
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

            if(requestCode==REQUEST_CODE_IMAGE_CAPTURE)
            {
                insertPhotoToEditor();
            }else if(requestCode == REQUEST_CODE_PICK_IMAGE)
            {
                if(data == null)
                {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }else
                {
                    Uri uri = data.getData();

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
    }

    private void clearAllFocus()
    {
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

    private void checkLayout()
    {
        if(hsvWidth > llWidth)
        {
            Log.d(TAG, "checkLayout: editorOptionsHSV.getWidth() > editorOptionsLL.getWidth()");
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)editorOptionsLL.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            editorOptionsLL.setLayoutParams(layoutParams);
        }else if(hsvWidth <= llWidth)
        {
            Log.d(TAG, "checkLayout: editorOptionsHSV.getWidth() <= editorOptionsLL.getWidth()");
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)editorOptionsLL.getLayoutParams();
            layoutParams.gravity = Gravity.NO_GRAVITY;
            editorOptionsLL.setLayoutParams(layoutParams);
        }
    }

    private void createPhotoFolder()
    {
        imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appDirectoryName);

        if(!imageRoot.exists())
        {
            if(imageRoot.mkdirs())
            {
                Log.d("mkdir","success");
            }
        }else
        {
            Log.d(TAG, "createPhotoFolder: imageRoot already existed");
        }
    }

    //region EDITOR BUTTONS

    @OnClick(R.id.editor_bold_iv)
    public void changeTextStyleBold() {
        editor.setBold();
    }

    @OnClick(R.id.editor_italic_iv)
    public void changeTextStyleItalic() {
        editor.setItalic();
    }

    @OnClick(R.id.editor_underline_iv)
    public void changeTextStyleUnderline()
    {
        editor.setUnderline();
    }

    @OnClick(R.id.editor_strikethrough_iv)
    public void changeTextStyleStrikeThrough()
    {
        editor.setStrikeThrough();

    }

    @OnClick(R.id.editor_undo_iv)
    public void changeTextUndo()
    {
        editor.undo();
    }

    @OnClick(R.id.editor_redo_iv)
    public void changeTextRedo()
    {
        editor.redo();
    }

    @OnClick(R.id.editor_link_iv)
    public void insertLink() {

        showInsertLinkPopup();
    }

    @OnClick(R.id.editor_bulleted_list_iv)
    public void changeTextBulletList()
    {
        editor.setBullets();
    }

    @OnClick(R.id.editor_numbered_list_iv)
    public void changeTextNumberList()
    {
        editor.setNumbers();
    }

    @OnClick(R.id.editor_image_iv)
    public void insertImage() {
        showImagePopup();
    }

    @OnClick(R.id.editor_text_colour_iv)
    public void changeTextColour()
    {
        showChangeColourPopup();
    }

    //endregion


    @OnClick(R.id.edit_note_back_iv)
    public void getEditorContent() {

        String htmlString = editor.getHtml();
        Log.d(TAG, "getEditorContent: " + htmlString);
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

        //delay the popup for 300ms for the keyboard to hide first
        Handler handler = new Handler();
        handler.postDelayed(r, POPUP_DELAY_MS);

    }

    private void showInsertLinkPopup()
    {
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

        //delay the popup for 300ms for the keyboard to hide first
        Handler handler = new Handler();
        handler.postDelayed(r, POPUP_DELAY_MS);
    }

    private void showChangeColourPopup()
    {
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

        //delay the popup for 300ms for the keyboard to hide first
        Handler handler = new Handler();
        handler.postDelayed(r, POPUP_DELAY_MS);
    }

    private void closeFragmentByTag(String tag) {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
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
        currentPhotoPath = image.getAbsolutePath();
        Log.d(TAG, "createPhotoFile: currentPhotoPath " + currentPhotoPath);
        return image;
    }

    private void insertPhotoToEditor() {
        savePhotoToExternalStorage();
        resizeCurrentPhoto();
        editor.insertImage(currentPhotoPath, "image");
        addEncodeImageToEditor();
    }

    private void savePhotoToExternalStorage(){

        SavePhotoToExternalStorageAsyncTask task = new SavePhotoToExternalStorageAsyncTask();
        task.execute(imageRoot.getAbsolutePath(),currentPhotoPath);
    }

    private void resizeCurrentPhoto() {
        Bitmap bmp = BitmapFactory.decodeFile(currentPhotoPath);

        if (bmp != null) {
            Log.d(TAG, "resizeCurrentPhoto: yo1");
            Bitmap resizedBmp = Utils.getResizedBitmap(bmp, (int) (bmp.getWidth() * 0.1), (int) (bmp.getHeight() * 0.1));
            try (FileOutputStream out = new FileOutputStream(currentPhotoPath)) {
                Log.d(TAG, "resizeCurrentPhoto: yo2");
                resizedBmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addEncodeImageToEditor()
    {
//        String encodedImageString = "<p hidden>" + Base64.encodeToString(getByteArray(), Base64.DEFAULT) + "</p>";
//        String encodedImageString = Base64.encodeToString(getByteArray(), Base64.DEFAULT);
//        String htmlString  = editor.getHtml() + encodedImageString;
//
//        editor.setHtml(htmlString);
        //TODO
        //do not include image in the editor
        //add involved image on the side
        //create an attachPhoto object
    }

    private byte[] getByteArray()
    {
        byte[] b;
        Bitmap bm = BitmapFactory.decodeFile(currentPhotoPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        b = baos.toByteArray();

        return b;
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
            Log.d(TAG, "takePhoto: photoURI.getPath()" + photoURI.getPath());
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
//        Utils.hideSoftKeyboard(this);
        closeFragmentByTag(tag);
    }

    @Override
    public void insertLink(String url, String displayName) {

        closeFragment(InsertLinkPopupFragment.TAG);
        editor.insertLink(url, displayName);
    }

    @Override
    public void changeTextColour(String colour) {
        Log.d(TAG, "changeTextColour: choose colour " + colour);
        editor.setTextColor(Color.parseColor(colour));
        closeFragment(ChooseTextColourPopupFragment.TAG);
        editor.requestFocus();
    }

    //endregion

    //region INNER CLASSES

    private static class SavePhotoToExternalStorageAsyncTask extends AsyncTask<String,Void,Void>
    {
        @Override
        protected Void doInBackground(String... strings) {

            FileOutputStream fos = null;
            Date currentTime = new Date();
            String fileName = "image_" + Utils.getTimeStringForFileName(currentTime) + ".jpg";
            File outputImage = new File(strings[0] + File.separator + fileName);

            try{
                outputImage.createNewFile();
                Bitmap bmp = BitmapFactory.decodeFile(strings[1]);
                fos = new FileOutputStream(outputImage);
                bmp.compress(Bitmap.CompressFormat.JPEG,100,fos);
                Log.d(TAG, "SavePhotoToExternalStorageAsyncTask doInBackground: saved!");
            } catch (IOException e)
            {
                e.printStackTrace();

            } finally {

                if(fos!=null)
                {
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
