package com.example.ken.test;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.provider.MediaStore;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> image_path = new ArrayList<>( );
    private ArrayList<Integer> image_path_id = new ArrayList<>( );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        readImage();

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),image_path_id,image_path);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void readImage() {

        Log.d("Confirmation","MainActivity：readImage");

        TrashImageDatabase trashImageDatabase = new TrashImageDatabase(this);
        SQLiteDatabase sqLiteDatabase = trashImageDatabase.getWritableDatabase();

        Cursor c = sqLiteDatabase.query("TIdb",new String[]{"image_path","image_id","score"},null,null,null,null,null,null);

        try {
            boolean isEof = c.moveToNext();
            while(isEof){

                Log.d("Confirmation","Cursor");

                image_path_id.add(c.getInt(c.getColumnIndex("image_id")));
                image_path.add(c.getString(c.getColumnIndex("image_path")));
                Log.d("Confirmation",String.valueOf(c.getInt(c.getColumnIndex("image_id"))));
                Log.d("Confirmation",c.getString(c.getColumnIndex("image_path")));

                isEof = c.moveToNext();
            }
        } finally {
            c.close();
            sqLiteDatabase.close();
        }




/*

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = null;
        // true: images, false:audio
        boolean flg = true;

        // 例外を受け取る
        try {
            if(flg){
                // images
                cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,null,null,null);
            }
            if (cursor != null && cursor.moveToFirst()) {

                do {

                    Log.d("Confirmation","ID："+ cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                    image_path_id.add(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID)));

                    Log.d("PATH",cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    image_path.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));

                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

            Toast toast = Toast.makeText(this, "例外が発生、Permissionを許可していますか？", Toast.LENGTH_SHORT);
            toast.show();

            //MainActivityに戻す
            finish();
        } finally{
            if(cursor != null){
                cursor.close();
            }
        }
*/
    }
}
