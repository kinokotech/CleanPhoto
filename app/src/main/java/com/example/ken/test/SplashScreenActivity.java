package com.example.ken.test;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SplashScreenActivity extends AppCompatActivity {

    static {
        System.loadLibrary("opencv_java3");
    }

    private final int REQUEST_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        } else {
            doAsync();
        }
    }

    // Permissionの確認
    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermission() {
        // 既に許可している
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

            doAsync();

        }
        // 拒否していた場合
        else{
            requestLocationPermission();
        }
    }

    // 許可を求める
    @TargetApi(Build.VERSION_CODES.M)
    private void requestLocationPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

        } else {

            Toast toast = Toast.makeText(this, "許可されないとアプリが実行できません", Toast.LENGTH_SHORT);
            toast.show();

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //許可を取れた場合
                    doAsync();
                } else {
                    //許可を取れなかった場合
                }
            }
        }
    }

    private void doAsync() {

        Log.d("Confirmation","SplashScreenActivity：doAsync");

        // 非同期タスク初期化
        AsyncClassification ac = new AsyncClassification(this);
        // 非同期タスクを実行
        ac.execute();
        // コールバック設定
        ac.setOnCallBack(new AsyncClassification.CallBackTask() {

            @Override
            public void CallBack() {
                Log.d("Confirmation","SplashScreenActivity：CallBack");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}

