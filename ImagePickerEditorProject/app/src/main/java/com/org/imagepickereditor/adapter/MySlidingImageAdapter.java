package com.org.imagepickereditor.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nguyenhoanglam.imagepicker.model.Image;
import com.org.imagepickereditor.AppUtils;
import com.org.imagepickereditor.R;

import java.util.List;

public class MySlidingImageAdapter extends PagerAdapter {
    List<Image> mainuserPhotoList;
    private LayoutInflater inflater;
    private Context context;

    public void UpdateAdapter(List<Image> mainuserPhotoList){
        this.mainuserPhotoList=mainuserPhotoList;
        notifyDataSetChanged();
    }

    public MySlidingImageAdapter(Context context,  List<Image> mainuserPhotoList) {
        this.context = context;
        this.mainuserPhotoList=mainuserPhotoList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mainuserPhotoList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.image_slide, view, false);

        // final View imageLayout = inflater.inflate(R.layout.my_image_slide, null);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.zoomableImageView);

        final ProgressBar progressBar = (ProgressBar) imageLayout.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        if(mainuserPhotoList.get(position)!=null) {
            Uri imageUri = Uri.parse(mainuserPhotoList.get(position).getPath());

            try {
                AppUtils.loadUserImageGlideUri(context, imageView, imageUri);
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {
            imageView.setImageResource(R.drawable.image_placeholder);
        }
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}