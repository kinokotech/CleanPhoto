package com.example.ken.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;


public class Fragment1 extends Fragment implements OnRecyclerListener{

    private static final int READ_REQUEST_CODE = 42;
    private ArrayList<String> image_path = new ArrayList<>( );
    private ArrayList<Integer> image_path_id = new ArrayList<>( );


    private Activity mActivity = null;
    private View mView;
    private RecyclerFragmentListener mFragmentListener = null;

    // RecyclerViewとAdapter
    private RecyclerView mRecyclerView = null;
    private RecyclerAdapter mAdapter = null;


    public interface RecyclerFragmentListener {
        void onRecyclerEvent();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_fragment1, container, false);


        Bundle bundle = getArguments();
        image_path = bundle.getStringArrayList("URI");
        image_path_id = bundle.getIntegerArrayList("URI_ID");

        // RecyclerViewの参照を取得
        mRecyclerView = mView.findViewById(R.id.recycler_view);
        // レイアウトマネージャを設定(ここで縦方向の標準リストであることを指定)
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        mRecyclerView.setItemViewCacheSize(image_path.size());


        //mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        mAdapter = new RecyclerAdapter(getActivity(),image_path_id,image_path, this);
        (new ItemTouchHelper(callback)).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onRecyclerClicked(View v, int position) {
        // セルクリック処理
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            // 横にスワイプされたら要素を消す
            int swipedPosition = viewHolder.getAdapterPosition();
            RecyclerAdapter adapter = (RecyclerAdapter) mRecyclerView.getAdapter();
            adapter.remove(swipedPosition);
        }
    };


}
