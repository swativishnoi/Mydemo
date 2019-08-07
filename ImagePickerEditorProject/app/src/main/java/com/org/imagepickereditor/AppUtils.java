package com.org.imagepickereditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


public class AppUtils {
    public static void loadUserImageGlideUri(final Context context, final ImageView imageView, final Uri url) {
        if (url != null ) {
            try {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.DATA);
                String selectedImagePath = ImagePickerUtil.getPath(context, url);
                Bitmap bitmap = ImagePickerUtil.decodeSampledBitmapFromResource(selectedImagePath, 400, 400);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
