package com.example.photosearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
        actionBar.setTitle(mName);

        loadImageFromStorage(mImage, mName);

//        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);
//        iv_det.setImageBitmap(bitmap);
    }

    private void loadImageFromStorage(String path, String name)
    {

        try {
            File f=new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            iv_det.setImageBitmap(b);
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
                sqLiteHelper.deleteData(id.toString());

                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

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
        Log.wtf("image1", mImage);
        Dialog dialog = new Dialog(id, mImage, mName);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void applyText(String name) {
        actionBar.setTitle(name);
    }
}
