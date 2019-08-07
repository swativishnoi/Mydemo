package com.nguyenhoanglam.imagepicker.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nguyenhoanglam.imagepicker.R;
import com.nguyenhoanglam.imagepicker.listeners.OnImageClickListener;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.File;
import java.util.List;

/**
 * Created by hoanglam on 7/31/16.
 */
public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ImageViewHolder> {

    private static Context context;
    private LayoutInflater inflater;
    private static List<Image> images;
    private List<Image> selectedImages;
    private OnImageClickListener itemClickListener;

    public ImagePickerAdapter(Context context, List<Image> images, List<Image> selectedImages, OnImageClickListener itemClickListener) {
        this.context = context;
        this.images = images;
        this.selectedImages = selectedImages;
        this.itemClickListener = itemClickListener;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder viewHolder, int position) {

        Image image = images.get(position);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).asBitmap().apply(requestOptions).load(image.getPath()).into(viewHolder.imageView);

        /*Glide.with(context)
                .load(image.getPath())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(viewHolder.imageView);*/

        if (isSelected(image)) {
            viewHolder.alphaView.setAlpha(0.2f);
            viewHolder.image_view_checkbox.setImageResource(R.drawable.tick_icon_selected);//setForeground(ContextCompat.getDrawable(context, R.drawable.checkboxselected));
        } else {
            viewHolder.alphaView.setAlpha(0.0f);
            viewHolder.image_view_checkbox.setImageResource(R.drawable.tick_icon_unselected);
            //((FrameLayout) viewHolder.itemView).setForeground(ContextCompat.getDrawable(context, R.drawable.checkbox));
        }

    }

    private boolean isSelected(Image image) {
        for (Image selectedImage : selectedImages) {
            if (selectedImage.getPath().equals(image.getPath())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public void setData(List<Image> images) {
        this.images.clear();
        this.images.addAll(images);
    }

    public void addAll(List<Image> images) {
        int startIndex = this.images.size();
        this.images.addAll(startIndex, images);
        notifyItemRangeInserted(startIndex, images.size());
    }

    public void addSelected(Image image) {
        selectedImages.add(image);
        notifyItemChanged(images.indexOf(image));
    }

    public void removeSelectedImage(Image image) {
        selectedImages.remove(image);
        notifyItemChanged(images.indexOf(image));
    }

    public void removeSelectedPosition(int position, int clickPosition) {
        selectedImages.remove(position);
        notifyItemChanged(clickPosition);
    }

    public void removeAllSelectedSingleClick() {
        selectedImages.clear();
        notifyDataSetChanged();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView, image_view_checkbox;
        //  CheckBox ;
        private View alphaView;
        private final OnImageClickListener itemClickListener;

        public ImageViewHolder(View itemView, OnImageClickListener itemClickListener) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            image_view_checkbox = (ImageView) itemView.findViewById(R.id.image_view_checkbox);
            alphaView = itemView.findViewById(R.id.view_alpha);
            this.itemClickListener = itemClickListener;
            image_view_checkbox.setOnClickListener(this);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = images.get(getAdapterPosition()).getPath();//AppConstants.SENT_IMAGE_FOLDER_PATH + message.getMessageId() + ".jpg";
                    File file = new File(name);
                    if (file.exists()) {
                        showFullScreenImageDialog(context, file);
                    }
                }
            });
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            view.setSelected(true);
            itemClickListener.onClick(view, getAdapterPosition());
        }
    }

    public static void showFullScreenImageDialog(final Context context, File file) {
        final Dialog dialog = new Dialog(context, R.style.FullScreenDialog);
        dialog.setContentView(R.layout.layout_full_screen_image);
        final ImageView zoomableImageView = (ImageView) dialog.findViewById(R.id.image);
        final ImageView imageViewClose = (ImageView) dialog.findViewById(R.id.imageViewClose);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Glide.with(context).load(file).into(zoomableImageView);
        dialog.show();
    }
}
