package com.teampenguin.apps.notenote.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.irshulx.Editor;
import com.github.irshulx.models.EditorTextStyle;
import com.teampenguin.apps.notenote.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditNoteActivity extends BaseActivity {

    public static final String TAG = "EditNoteActivity";

    public static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 101;
    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 102;

    @BindView(R.id.edit_note_editor)
    Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        ButterKnife.bind(this);

        checkPermissions();
    }

    @OnClick(R.id.editor_bold_iv)
    public void changeTextStyleBold() {
        editor.updateTextStyle(EditorTextStyle.BOLD);
    }

    @OnClick(R.id.editor_italic_iv)
    public void changeTextStyleItalic() {
        editor.updateTextStyle(EditorTextStyle.ITALIC);
    }

    @OnClick(R.id.editor_link_iv)
    public void insertLink() {
        editor.insertLink();
    }

    @OnClick(R.id.editor_image_iv)
    public void insertImage() {
        editor.openImagePicker();
    }

    @OnClick(R.id.edit_note_back_iv)
    public void getEditorContent() {
        String s = editor.getContentAsSerialized();
        String s2 = editor.getContentAsHTML(s);
        Log.d(TAG, "getEditorContent: " + s);
        Log.d(TAG, "getEditorContent: " + s2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                if(bitmap!=null)
                {
                    Log.d(TAG, "bitmap " + String.valueOf(bitmap));
                    editor.insertImage(bitmap);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            // editor.RestoreState();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }else
        {
            Log.d(TAG, "checkPermissions: all permissions granted!");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults!=null)
        {
            if(requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE)
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Log.d(TAG, "onRequestPermissionsResult: read ok!!");
                    checkPermissions();
                }
                else
                {
                    Toast.makeText(this, "You must grant permission to read external storage", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }else if(requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Log.d(TAG, "onRequestPermissionsResult: write ok!!");
                }
                else
                {
                    Toast.makeText(this, "You must grant permission to write external storage", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
