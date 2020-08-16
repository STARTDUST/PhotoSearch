package com.example.photosearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class FullScreenAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Model> list;
    private LayoutInflater inflater;
    private AppBarLayout app_bar_detail;
    private View decorView;

    private boolean app_bar_show = true;

    public FullScreenAdapter(Context context, ArrayList<Model> list, AppBarLayout app_bar_detail, View decorView) {
        this.context = context;
        this.list = list;
        this.app_bar_detail = app_bar_detail;
        this.decorView = decorView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.full_item, null);

        ImageView iv_det = (ImageView) v.findViewById(R.id.iv_det);
        loadImageFromStorage(list.get(position).getImg(), list.get(position).getTitle(), iv_det);
//        toolbar_detail.setTitle(list.get(position).getTitle());

        ViewPager vp = (ViewPager) container;
        vp.addView(v, 0);

        iv_det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (app_bar_show){
                    app_bar_detail.setVisibility(View.GONE);

                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);

                    app_bar_show = false;
                }
                else {
                    app_bar_detail.setVisibility(View.VISIBLE);

                    decorView.setSystemUiVisibility(
//                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

                    app_bar_show = true;
                }
            }
        });

        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);

        ViewPager viewPager = (ViewPager) container;
        View v = (View) object;
        viewPager.removeView(v);
    }

    private void loadImageFromStorage(String path, String name, ImageView iv_det) {

        try {
            File f=new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            iv_det.setImageBitmap(getResizedBitmap(b));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    private Bitmap getResizedBitmap(Bitmap image) {
        int maxSize = 1920;
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


}
