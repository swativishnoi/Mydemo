package com.org.imagepickereditor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int REQUEST_CODE_STORAGE_PERMISSION=201;
    int REQUEST_CODE_PICKER=202;
    int REQUEST_CODE_IMAGE_EDITED=203;
    private ArrayList<Image> images = null;

    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text=findViewById(R.id.text);
    }


    public void openGallery(View view) {

       // openImagePicker();

        int hasWriteExternalPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteExternalPermission == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        } else {
            addStoragePermission();
        }
    }

    private void addStoragePermission() {
        List<String> permissions = new ArrayList<String>();
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        ActivityCompat.requestPermissions(MainActivity.this, permissions.toArray(new String[permissions.size()]),
                REQUEST_CODE_STORAGE_PERMISSION);
    }

    public void openImagePicker() {
        ArrayList images = new ArrayList<>();
        com.nguyenhoanglam.imagepicker.activity.ImagePicker.create(MainActivity.this)
                .folderMode(false) // set folder mode (false by default)
                .folderTitle("ImagePickerEditor") // folder selection title
                .imageTitle("Tap to select") // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(10) // max images can be selected (999 by default)
                .showCamera(false) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .origin(images) // original selected images, used in multi mode
                .start(REQUEST_CODE_PICKER); // start image picker activity with request code
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
         if (requestCode ==REQUEST_CODE_STORAGE_PERMISSION) {
            int hasWriteExternalPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (hasWriteExternalPermission == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICKER) {
            if (data != null) {
                /*get images list and send it to Edit operation */
                images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
                Log.e("images", "" + images);
                Intent intent=new Intent(MainActivity.this,ImageEditActivity.class);
                intent.putParcelableArrayListExtra("images",images);
                startActivityForResult(intent,REQUEST_CODE_IMAGE_EDITED);
            }
        }else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_IMAGE_EDITED) {
            if (data != null) {
                /*images list having all edited image URI*/
                images = data.getParcelableArrayListExtra("images");
                Log.e("images", "" + images);
                text.setText("All Selected Images are ");
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
