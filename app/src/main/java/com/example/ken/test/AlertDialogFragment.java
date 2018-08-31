package com.example.ken.test;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class AlertDialogFragment extends DialogFragment {

    private AlertDialog dialog ;
    private AlertDialog.Builder alert;
    private String image_path;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        alert = new AlertDialog.Builder(getActivity());

        // カスタムレイアウトの生成
        View alertView = getActivity().getLayoutInflater().inflate(R.layout.alert_lyout, null);

        image_path = getArguments().getString("path");
        File file_path = new File(image_path);

        if(file_path.exists()){

            try {
                Bitmap bitmap = BitmapFactory.decodeFile(file_path.getAbsolutePath());
                ImageView clicked_image = alertView.findViewById(R.id.clicked_image);
                clicked_image.setImageBitmap(rotateImageIfRequired(bitmap, getActivity(), file_path));
                } catch (IOException e) {
                }
                }




/*
        // alert_layout.xmlにあるボタンIDを使う
        ImageView bag1 = (ImageView) alertView.findViewById(R.id.bag_1);
        bag1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("debug","bag1 clicked");

                setMassage("bag1 clicked");
                // Dialogを消す
                getDialog().dismiss();
            }
        });
        */
/*
        ImageView bag2 = (ImageView) alertView.findViewById(R.id.bag_2);
        bag2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("debug","bag2 clicked");

                setMassage("bag2 clicked");
                // Dialogを消す
                getDialog().dismiss();
            }
        });
*/

        // ViewをAlertDialog.Builderに追加
        alert.setView(alertView);

        // Dialogを生成
        dialog = alert.create();
        dialog.show();

        return dialog;
    }
/*
    private void setMassage(String message) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setSelection(message);
    }
  */

    public static Bitmap rotateImageIfRequired(Bitmap bitmap, Context context, File file_path) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(Uri.fromFile(file_path), "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

        ExifInterface ei = new ExifInterface(file_path.getAbsolutePath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        parcelFileDescriptor.close();

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);
            default:
                return bitmap;
        }
    }
    private static Bitmap rotateImage(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return rotatedImg;
    }
}
