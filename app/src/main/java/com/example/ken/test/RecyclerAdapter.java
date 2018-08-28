package com.example.ken.test;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<String> image_path;
    private ArrayList<Integer> image_path_id;
    private Context mContext;
    private OnRecyclerListener mListener;
    private List<Integer> mImages;
    //private Bitmap bitmap;


    public RecyclerAdapter(Context context,ArrayList<Integer> image_path_id, ArrayList<String>image_path, OnRecyclerListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.image_path_id = image_path_id;
        this.image_path = image_path;
        this.mListener = listener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 表示するレイアウトを設定
        return new ViewHolder(mInflater.inflate(R.layout.list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        /*
        File f = new File(image_path.get(i));
        Glide.with(mContext).load(f).thumbnail(1).into(viewHolder.imageView);
        */

        /*
        Picasso.with(mContext)
                .load(f)
                .into(viewHolder.imageView);
        */

        /*サムネイル表示（重い）
        ContentResolver cr = mContext.getContentResolver();
        Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(cr, image_path_id.get(i), MediaStore.Images.Thumbnails.MINI_KIND, null);
        viewHolder.imageView.setImageBitmap(bmp);
        */

        Log.d("confirmation","RecyclerAdapter：onBindViewHolder" + String.valueOf(image_path_id.get(i)));
        ContentResolver cr = mContext.getContentResolver();
        Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(cr, image_path_id.get(i), MediaStore.Images.Thumbnails.MINI_KIND, null);
        viewHolder.imageView.setImageBitmap(thumbnail);

        // クリック処理
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRecyclerClicked(v, i);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (image_path != null) {
            return image_path.size();
        } else {
            return 0;
        }
    }

    // ViewHolder(固有ならインナークラスでOK)
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_item_imageView);

        }
    }

    /*
    @Override
    public void onViewRecycled(final ViewHolder viewHolder) {
        Log.d("RecyclerAdapter","onViewRecycled()");
        Glide.clear(viewHolder.imageView);
    }
    */

    public void remove(int position) {
        image_path_id.remove(position);
        image_path.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public long getItemId(int position){

        return position;
    }


}

