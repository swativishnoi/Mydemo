package com.org.imagepickereditor.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nguyenhoanglam.imagepicker.model.Image;
import com.org.imagepickereditor.AppListener;
import com.org.imagepickereditor.AppUtils;
import com.org.imagepickereditor.R;

import java.util.List;

public class MySlidingImageRecyAdapter extends RecyclerView.Adapter<MySlidingImageRecyAdapter.MyViewHolder> {
    private Context context;
    private List<Image> imageList;
    AppListener.ItemClickListner listner;

//   public int clickPos=0;
    public MySlidingImageRecyAdapter(Context context, List<Image> imageList, AppListener.ItemClickListner listner) {
        this.context = context;
        this.imageList = imageList;
        this.listner = listner;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_image, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        if(imageList.get(i)!=null) {
            Uri imageUri = Uri.parse(imageList.get(i).getPath());

            try {
                AppUtils.loadUserImageGlideUri(context,myViewHolder.smallImageView, imageUri);
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {
            myViewHolder.smallImageView.setImageResource(R.drawable.image_placeholder);
        }

        /*if(clickPos==i&&imageList.get(i).isSelected()){
            myViewHolder.lnrClick.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }else {
            myViewHolder.lnrClick.setBackgroundColor(context.getResources().getColor(R.color.transparent_color_code));
        }*/

        myViewHolder.lnrClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageList.get(i).setSelected(true);
               // clickPos=i;

                if(listner!=null)
                    listner.onItemClick(i);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView smallImageView;
        RelativeLayout lnrClick;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lnrClick=itemView.findViewById(R.id.lnrClick);
            smallImageView=itemView.findViewById(R.id.smallImageView);
        }
    }
}
