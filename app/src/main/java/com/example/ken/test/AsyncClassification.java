package com.example.ken.test;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AsyncClassification extends AsyncTask<Void, Void, Void> {

    private ArrayList<String> image_path = new ArrayList<>();
    private ArrayList<Integer> image_path_id = new ArrayList<>();
    private CallBackTask callbacktask;
    private SplashScreenActivity splashScreenActivity;

    public AsyncClassification(SplashScreenActivity splashScreenActivity) {
        this.splashScreenActivity = splashScreenActivity;
    }

    // バックグラウンドの処理
    @Override
    protected Void doInBackground(Void... params) {
        Log.d("Confirmation", "AsyncClassification：doInBackground");

        readMediaStore();

        Classifier classifier = new Classifier(splashScreenActivity);

        TrashImageDatabase trashImageDatabase = new TrashImageDatabase(splashScreenActivity);
        SQLiteDatabase TIdb = trashImageDatabase.getWritableDatabase();

        //インサートするデータのArrayList
        List<ContentValues> valueList = new ArrayList<>();
        ContentValues values = new ContentValues();

        SharedPreferences image_cache = splashScreenActivity.getSharedPreferences("ImageCache", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = image_cache.edit();

        int count = 0;
        for (String image : image_path) {

            int model_score = classifier.classifyImageFromPath(image);

            if (model_score > 1) {

                Log.d("Confirmation", image);
                Log.d("Confirmation", String.valueOf(image_path_id.get(count)));
                Log.d("Confirmation", "Model_Score：" + String.valueOf(model_score));

                values.put("image_path", image);
                values.put("image_id", image_path_id.get(count));
                values.put("score", model_score);

                valueList.add(values);

            }
            values = new ContentValues();
            count++;
        }

        // トランザクション開始を宣言
        TIdb.beginTransaction();
        try {
            for (ContentValues val : valueList) {
                //TIdb.update("table_name", val, "id = 1", null);
                long ret = TIdb.insert("TIdb", null, val);
                if (ret < 0) {
                    Log.d("Confirmation", "インサート失敗！！！！！");
                }
            }
            //トランザクション成功を宣言。
            TIdb.setTransactionSuccessful();

        } finally {
            // トランザクション終了
            TIdb.endTransaction();
            TIdb.close();
        }

        return null;
        /*少し遅い
        int count = 0;
        for (String image : image_path) {

            //File path = new File(image);
            Float model_score = classifier.classifyImageFromPath(image);

            if (model_score < 0.5) {
                Log.d("Confirmation", image);
                Log.d("Confirmation", String.valueOf(image_path_id.get(count)));
                Log.d("Confirmation", "Model_Score：" + String.valueOf(model_score));

                values.put("image_path", image);
                values.put("image_id", image_path_id.get(count));
                values.put("score", model_score);
                TIdb.insert("TIdb", null, values);
            }
            count++;
        }
        TIdb.close();
        return null;

        */
    }

    // doInBackgroundの処理が終了した後に呼ばれる
    @Override
    protected void onPostExecute(Void result) {
        callbacktask.CallBack();
    }

    public void setOnCallBack(CallBackTask t_object) {
        callbacktask = t_object;
    }

    // コールバック用のインターフェース定義
    interface CallBackTask {
        void CallBack();
    }

    private void readMediaStore() {

        Log.d("Confirmation", "AsyncClassification：readContentActivity");

        ContentResolver contentResolver = splashScreenActivity.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {

                do {

                    Log.d("Confirmation", "AsyncClassification　ID：" + cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                    image_path_id.add(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID)));

                    Log.d("Confirmation", "AsyncClassification　PATH：" + cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    image_path.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));

                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }
}
