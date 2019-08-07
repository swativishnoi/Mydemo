package com.org.imagepickereditor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyenhoanglam.imagepicker.model.Image;
import com.org.imagepickereditor.adapter.MySlidingImageAdapter;
import com.org.imagepickereditor.adapter.MySlidingImageRecyAdapter;

import java.util.ArrayList;
import java.util.List;


public class ImageEditActivity extends AppCompatActivity {

    ViewPager viewPager;
    List<Image> mainuserPhotoList=new ArrayList<>();
    boolean isMyPhotos;
    int currentPos=0;
    String fname;
    RecyclerView bottom_recycler_view;
    private int REQUEST_CODE_IMAGE_EDITED = 2001;
    ImageView sendImg,imgCrop;
    MySlidingImageRecyAdapter adapter;
    MySlidingImageAdapter viewPagerAdapter;
    boolean needTochange=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);
        init();
    }

    private void init(){
        final TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        sendImg= (ImageView) findViewById(R.id.sendImg);
        txtTitle.setText("Images");
        ImageView imgBack = (ImageView) findViewById(R.id.imgCross);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(getIntent().getExtras()!=null){
            mainuserPhotoList= getIntent().getExtras().getParcelableArrayList("images");
        }

        if(getIntent().getExtras()!=null){
            currentPos=  getIntent().getExtras().getInt("INDEX");
        }

       adapter=new MySlidingImageRecyAdapter(ImageEditActivity.this, mainuserPhotoList, new AppListener.ItemClickListner() {
            @Override
            public void onItemClick(int pos) {
                needTochange=false;
                viewPager.setCurrentItem(pos);
            }
        });

        bottom_recycler_view=findViewById(R.id.bottom_recycler_view);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(ImageEditActivity.this,LinearLayoutManager.HORIZONTAL,false);
        bottom_recycler_view.setLayoutManager(mLayoutManager);
        //bottom_recycler_view.addItemDecoration(new DividerItemDecoration(bottom_recycler_view.getContext(), DividerItemDecoration.HORIZONTAL));
        bottom_recycler_view.setItemAnimator(new DefaultItemAnimator());
        bottom_recycler_view.setAdapter(adapter);

        viewPager=findViewById(R.id.pager);
        viewPagerAdapter=new MySlidingImageAdapter(ImageEditActivity.this,mainuserPhotoList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(currentPos);

        viewPager.setOffscreenPageLimit(mainuserPhotoList.size()-1);

        sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putParcelableArrayListExtra("images", (ArrayList<? extends Parcelable>) mainuserPhotoList);
                setResult(RESULT_OK,intent);
                onBackPressed();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                //needTochange = true;
            }

            @Override
            public void onPageSelected(final int i) {

               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clickPos = i;
                        mainuserPhotoList.get(i).setSelected(true);
                        bottom_recycler_view.scrollToPosition(i);
                        adapter.notifyDataSetChanged();
                    }
                }, 200);*/

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        imgCrop=findViewById(R.id.imgCrop);
        imgCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=viewPager.getCurrentItem();
                Uri uri=Uri.parse("file://"+mainuserPhotoList.get(pos).getPath());
                Intent intent=new Intent(ImageEditActivity.this,ImageCropActivity.class);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_CODE_IMAGE_EDITED);

              //  startCropImageActivity(uri);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE_EDITED ){
            if (resultCode == RESULT_OK ) {
                try {
                    Uri myUri = data.getData();

                    Log.d("New IMG", myUri.toString());

                    int pos = viewPager.getCurrentItem();
                    mainuserPhotoList.get(pos).setPath(myUri.toString());

                    viewPagerAdapter.UpdateAdapter(mainuserPhotoList);
                    adapter.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



}
