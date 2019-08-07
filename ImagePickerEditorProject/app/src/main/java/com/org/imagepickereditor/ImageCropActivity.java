package com.org.imagepickereditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;

public class ImageCropActivity extends AppCompatActivity implements View.OnClickListener {

    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override
        public void onSuccess() {
            Log.d("LoadCallback","onSuccess");
        }

        @Override
        public void onError(Throwable e) {
            Log.d("LoadCallback","onError"+e.toString());
        }
    };
    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {
            Log.d("LoadCallback",outputUri.toString());
            Intent i = new Intent();
            i.setData(outputUri);
            setResult(RESULT_OK, i);
            finish();
        }

        @Override
        public void onError(Throwable e) {
        }
    };
    ImageView imgCross;
    private Uri mSourceUri = null;
    private CropImageView mCropView;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;

    private final CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap cropped) {
            mCropView.save(cropped)
                    .compressFormat(mCompressFormat)
                    .execute(mSourceUri, mSaveCallback);
        }

        @Override
        public void onError(Throwable e) {
        }
    };
    private ImageButton rotateLeft, rotateRight, doneBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);

        imgCross = findViewById(R.id.imgCross);
        mCropView = findViewById(R.id.cropImageView);
        rotateLeft = findViewById(R.id.buttonRotateLeft);
        rotateRight = findViewById(R.id.buttonRotateRight);
        doneBtn = findViewById(R.id.buttonDone);
        cancelBtn = findViewById(R.id.buttonCancel);

        mSourceUri = getIntent().getData();
        rotateLeft.setOnClickListener(this);
        rotateRight.setOnClickListener(this);
        doneBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        imgCross.setOnClickListener(this);

        // load image
        mCropView.setCropMode(CropImageView.CropMode.SQUARE);

        String selectedImagePath1 = ImagePickerUtil.getPath(ImageCropActivity.this, mSourceUri);
        Bitmap bitmap = ImagePickerUtil.decodeSampledBitmapFromResource(selectedImagePath1, 400, 400);

      //  mCropView.setImageBitmap(bitmap);

         mCropView.load(mSourceUri)
                .useThumbnail(true)
                .execute(mLoadCallback);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRotateLeft:
                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                break;
            case R.id.buttonRotateRight:
                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                break;
            case R.id.buttonDone:
                mCropView.crop(mSourceUri).execute(mCropCallback);
                break;

            case R.id.buttonCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.imgCross:
                onBackPressed();
                break;
        }
    }

}

