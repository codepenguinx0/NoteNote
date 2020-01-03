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
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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

public class EditNoteActivity extends AppCompatActivity implements PickImagePopupFragment.PickImagePopupCallBack, CommonFragmentInterface {

    public static final String TAG = "EditNoteActivity";

    public static final int PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE = 101;
    public static final int PERMISSION_REQUEST_CODE_CAMERA = 102;
    public static final int REQUEST_CODE_IMAGE_CAPTURE = 201;
    public static final int REQUEST_CODE_PICK_IMAGE = 202;
    private static final String appDirectoryName = "NoteNote";

    private File imageRoot;

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

    private void initializeEditor() {
        editor.setEditorHeight(200);
        editor.setEditorFontSize(20);
        editor.setEditorFontColor(Color.BLACK);
        editor.setPadding(10, 10, 10, 10);
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

    @OnClick(R.id.editor_bold_iv)
    public void changeTextStyleBold() {
        editor.setBold();
    }

    @OnClick(R.id.editor_italic_iv)
    public void changeTextStyleItalic() {
        editor.setItalic();
    }

    @OnClick(R.id.editor_link_iv)
    public void insertLink() {


    }

    @OnClick(R.id.editor_image_iv)
    public void insertImage() {
        showImagePopup();
    }

    @OnClick(R.id.edit_note_back_iv)
    public void getEditorContent() {

        String htmlString = editor.getHtml();
        Log.d(TAG, "getEditorContent: " + htmlString);
    }

    private void showImagePopup() {
        Utils.hideSoftKeyboard(this);
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

    // <----------------interface methods---------------->

    @Override
    public void takePhoto() {

        closeFragmentWithTag(PickImagePopupFragment.TAG);

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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pick a photo"), REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void closeFragment(String tag) {
        closeFragmentWithTag(tag);
    }

    // <----------------inner classes---------------->

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
}
