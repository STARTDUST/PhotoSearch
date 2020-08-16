package com.example.photosearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Add extends AppCompatActivity {
    ImageView iv_add;
    TextInputEditText et_add;
    MaterialButton btn_add;
    Bitmap bitmap;

    public  static SQLiteHelper sqLiteHelper;
    List<String> list;
    boolean name_repeat = false;

    private static final int IMAGE_PICK_COD = 1000;
    private static final int PERMISSION_COD = 1000;

    final static Pattern PATTERN = Pattern.compile("(.*?)(?:\\((\\d+)\\))?(\\.[^.]*)?");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        iv_add = findViewById(R.id.iv_add);
        et_add = findViewById(R.id.et_add);
        btn_add = findViewById(R.id.btn_add);

        sqLiteHelper = new SQLiteHelper(this, "FoodDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF  NOT EXISTS FOOD (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, image VARCHAR)");
        list = new ArrayList<>();

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_COD);
                    }
                    else {
                        pickImageFromGallery();
                    }
                }

                else {
                    pickImageFromGallery();
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                //name not repeats
                if (!fileExists(et_add.getText().toString())){
//                  saving in Internal Storage
                    File dir = getApplicationContext().getDir("Images",MODE_PRIVATE);
                    File file = new File(dir, et_add.getText().toString().trim()+".jpg");
                    Log.wtf("add", file.getAbsolutePath());
                    try{
                        OutputStream stream = null;
                        stream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                        stream.flush();
                        stream.close();
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    //saving in SQLite
                    try {
                        sqLiteHelper.insertData(et_add.getText().toString().trim(), file.getAbsolutePath());
                        Toast.makeText(Add.this, "Added Sucs", Toast.LENGTH_LONG).show();
                        et_add.setText("");

                        Intent intent = new Intent(Add.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

                //name repeats
                else {
                    //saving in Internal Storage
                    File dir = getApplicationContext().getDir("Images",MODE_PRIVATE);
                    File file = new File(dir, getNewName(et_add.getText().toString()).trim()+".jpg");
                    Log.wtf("add", file.getAbsolutePath());
                    try{
                        OutputStream stream = null;
                        stream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                        stream.flush();
                        stream.close();
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    //saving in SQLite
                    try {
                        sqLiteHelper.insertData(getNewName(et_add.getText().toString()).trim(), file.getAbsolutePath());
                        Toast.makeText(Add.this, "Added Sucs", Toast.LENGTH_LONG).show();
                        et_add.setText("");

                        Intent intent = new Intent(Add.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_COD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_COD) {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                iv_add.setImageBitmap(getResizedBitmap(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_COD:
            {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }

                else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
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


    String getNewName(String filename) {
        if (fileExists(filename)) {
            Matcher m = PATTERN.matcher(filename);
            if (m.matches()) {
                String prefix = m.group(1);
                String last = m.group(2);
                String suffix = m.group(3);
                if (suffix == null) suffix = "";

                int count = last != null ? Integer.parseInt(last) : 0;

                do {
                    count++;
                    filename = prefix + "(" + count + ")" + suffix;
                } while (fileExists(filename));
            }
        }
        return filename;
    }

    private boolean fileExists(String filename) {
        name_repeat = false;

        Cursor cursor = sqLiteHelper.getData("SELECT * FROM FOOD");
        list.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String image = cursor.getString(2);

            list.add(name);
        }

        for (int i = 0; i <list.size() ; i++) {
            if (filename.equals(list.get(i))){
                name_repeat = true;
            }
        }

        return name_repeat;
    }
}


//        data/user/0/com.example.photosearch/app_Images/UniqueFileName.jpg