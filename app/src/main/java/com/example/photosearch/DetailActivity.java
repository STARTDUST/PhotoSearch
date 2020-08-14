package com.example.photosearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DetailActivity extends AppCompatActivity implements Dialog.DialogListener {

    ImageView iv_det;
    Integer id;
    String mImage;
    String mName;
    ActionBar actionBar;

    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        sqLiteHelper = new SQLiteHelper(this, "FoodDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF  NOT EXISTS FOOD (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, image VARCHAR)");

        actionBar = getSupportActionBar();

        iv_det = findViewById(R.id.iv_det);

        Intent intent = getIntent();
        mName = intent.getStringExtra("iName");
        mImage = intent.getStringExtra("iImage");
        id = intent.getIntExtra("Id", 0);

        Log.wtf("det","name=" + mName);

        actionBar.setTitle(mName);

//        iv_det.setImageBitmap(getResizedBitmap(mImage));
//
        loadImageFromStorage(mImage, mName);
//
//        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);
//        iv_det.setImageBitmap(bitmap);
    }

    private void loadImageFromStorage(String path, String name)
    {

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
        Dialog dialog = new Dialog(id, mImage.toString(), mName);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void applyText(String name) {
        actionBar.setTitle(name);
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
