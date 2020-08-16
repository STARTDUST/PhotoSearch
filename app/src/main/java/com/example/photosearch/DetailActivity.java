package com.example.photosearch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements Dialog.DialogListener {

    AppBarLayout app_bar_detail;
    Toolbar toolbar_detail;
    boolean app_bar_show = false;

//    ImageView iv_det;
    Integer id;
    String mImage;
    String mName;
    int position;

    ViewPager pager;
    ArrayList<Model> list;

    public static SQLiteHelper sqLiteHelper;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        Window window = getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

        final View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        sqLiteHelper = new SQLiteHelper(this, "FoodDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF  NOT EXISTS FOOD (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, image VARCHAR)");


//        iv_det = findViewById(R.id.iv_det);
        app_bar_detail = (AppBarLayout) findViewById(R.id.app_bar_detail);
        toolbar_detail = (Toolbar) findViewById(R.id.toolbar_detail);
        pager = (ViewPager) findViewById(R.id.pager);
        list = new ArrayList<>();

        setSupportActionBar(toolbar_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        app_bar_detail.setVisibility(View.GONE);


        Log.wtf("statusss", String.valueOf(getStatusBarHeight()));

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) app_bar_detail.getLayoutParams();
        layoutParams.setMargins(0, getStatusBarHeight(), 0, 0);

        Intent intent = getIntent();
        mName = intent.getStringExtra("iName");
        mImage = intent.getStringExtra("iImage");
        id = intent.getIntExtra("Id", 0);
        position = intent.getIntExtra("position", 0);

        Log.wtf("det","name=" + mName);

        toolbar_detail.setTitle(mName);
//        loadImageFromStorage(mImage, mName);

//        iv_det.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (app_bar_show){
//                    app_bar_detail.setVisibility(View.GONE);
//
//                    decorView.setSystemUiVisibility(
//                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
//
//                    app_bar_show = false;
//                }
//                else {
//                    app_bar_detail.setVisibility(View.VISIBLE);
//
//                    decorView.setSystemUiVisibility(
////                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                              View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//
//                    app_bar_show = true;
//                }
//            }
//        });

        Cursor cursor = sqLiteHelper.getData("SELECT * FROM FOOD");
        list.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String image = cursor.getString(2);

            list.add(new Model(id, name, image));
        }

        Collections.reverse(list);

        FullScreenAdapter fullScreenAdapter = new FullScreenAdapter(this, list, app_bar_detail, decorView);
        pager.setAdapter(fullScreenAdapter);
        pager.setCurrentItem(position, true);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolbar_detail.setTitle(list.get(position).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

//    private void loadImageFromStorage(String path, String name) {
//
//        try {
//            File f=new File(path);
//            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
//            iv_det.setImageBitmap(getResizedBitmap(b));
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_delete:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);

                builder.setMessage("Do you want to delete this photo??")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.wtf("dialog", "yes_pressed");

                                sqLiteHelper.deleteData(id.toString());
                                File file = new File(mImage);
                                if(file.exists()){
                                    boolean deleted = file.delete();
                                    if (deleted){
                                        Log.wtf("file", "deleted this file == " + mImage);
                                    }
                                    else {
                                        Log.wtf("file", "not deleted");
                                    }
                                }
                                else {
                                    Log.wtf("file", "not exists");
                                }

                                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", null);

                AlertDialog alert = builder.create();
                alert.show();
                break;
            }

            case R.id.item_edit:{
                openDialog();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDialog(){
        Log.wtf("image1", mImage.toString());
        Dialog dialog = new Dialog(id, mImage.toString(), mName, DetailActivity.this);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void applyText(String name) {
        toolbar_detail.setTitle(name);
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


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
