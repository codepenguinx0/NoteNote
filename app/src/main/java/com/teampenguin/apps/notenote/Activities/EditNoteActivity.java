package com.teampenguin.apps.notenote.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.teampenguin.apps.notenote.Fragments.CommonFragmentInterface;
import com.teampenguin.apps.notenote.Fragments.PickImagePopupFragment;
import com.teampenguin.apps.notenote.R;
import com.teampenguin.apps.notenote.Utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;

public class EditNoteActivity extends AppCompatActivity implements PickImagePopupFragment.PickImagePopupCallBack, CommonFragmentInterface {

    public static final String TAG = "EditNoteActivity";

    public static final int PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE = 101;
    public static final int PERMISSION_REQUEST_CODE_CAMERA = 102;
    public static final int REQUEST_CODE_IMAGE_CAPTURE = 201;

    @BindView(R.id.edit_note_editor)
    RichEditor editor;
    @BindView(R.id.edit_note_title_et)
    EditText noteTitleET;

    private String currentPhotoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        checkPermissions();
        initializeEditor();
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

        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

//            Uri uri = data.getData();
//
//            Log.d(TAG, "onActivityResult: uri.getPath()" + uri.getPath());
//            Log.d(TAG, "onActivityResult: currentPhotoPath" + currentPhotoPath);
            insertPhotoToEditor();

        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            // editor.RestoreState();
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

    private void initializeEditor() {
        editor.setEditorHeight(200);
        editor.setEditorFontSize(20);
        editor.setEditorFontColor(Color.BLACK);
        editor.setPadding(10, 10, 10, 10);
        editor.setBackground(getDrawable(R.drawable.half_transparent_white_rounded_bg));
        editor.setPlaceholder("Insert text here...");
        editor.setInputEnabled(true);
        editor.focusEditor();
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
        } else {
            Log.d(TAG, "checkPermissions: all permissions granted!");
        }
    }

    @OnClick(R.id.editor_bold_iv)
    public void changeTextStyleBold() {
//        editor.updateTextStyle(EditorTextStyle.BOLD);
        editor.setBold();
    }

    @OnClick(R.id.editor_italic_iv)
    public void changeTextStyleItalic() {
//        editor.updateTextStyle(EditorTextStyle.ITALIC);
        editor.setItalic();
    }

    @OnClick(R.id.editor_link_iv)
    public void insertLink() {
//        editor.insertLink();

    }

    @OnClick(R.id.editor_image_iv)
    public void insertImage() {
//        editor.openImagePicker();
        showImagePopup();

    }

    @OnClick(R.id.edit_note_back_iv)
    public void getEditorContent() {
//        String s = editor.getContentAsSerialized();
//        String s2 = editor.getContentAsHTML(s);
//        Log.d(TAG, "getEditorContent: " + s);
//        Log.d(TAG, "getEditorContent: " + s2);
    }

    private void showImagePopup() {
        PickImagePopupFragment fragment = new PickImagePopupFragment(this, this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.edit_note_frame, fragment, PickImagePopupFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(PickImagePopupFragment.TAG)
                .commit();
    }

    private void closeFragmentWithTag(String tag) {
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

    private void insertPhotoToEditor()
    {
        //TODO
        savePhotoToExternalStorage();
        resizeCurrentPhoto();
        editor.insertImage(currentPhotoPath,"image");
    }

    private void savePhotoToExternalStorage()
    {
//        Log.d(TAG, "savePhotoToGallery: ?");
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
        //TODO
    }

    private void resizeCurrentPhoto()
    {
        Bitmap bmp = BitmapFactory.decodeFile(currentPhotoPath);
        
        if(bmp!=null)
        {
            Log.d(TAG, "resizeCurrentPhoto: yo1");
            Bitmap resizedBmp = Utils.getResizedBitmap(bmp,(int)(bmp.getWidth()*0.1),(int)(bmp.getHeight()*0.1));
            try (FileOutputStream out = new FileOutputStream(currentPhotoPath)) {
                Log.d(TAG, "resizeCurrentPhoto: yo2");
                resizedBmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // <----------------interface methods---------------->

    @Override
    public void takePhoto() {
        closeFragmentWithTag(PickImagePopupFragment.TAG);
//        Toast.makeText(this, "take photo!", Toast.LENGTH_SHORT).show();

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
        closeFragmentWithTag(PickImagePopupFragment.TAG);
        Toast.makeText(this, "open gallery!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeFragment(String tag) {
        closeFragmentWithTag(tag);
    }
}
