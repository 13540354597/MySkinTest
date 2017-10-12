package com.myskintest.Skin;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.myskintest.MainActivity;
import com.myskintest.MyFileUtil;
import com.myskintest.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ren.solid.skinloader.base.SkinBaseActivity;
import ren.solid.skinloader.listener.ILoaderListener;
import ren.solid.skinloader.load.SkinManager;

/**
 * Created by TR 105 on 2017/8/3.
 */

public class SkinTest extends SkinBaseActivity {
    private RecyclerView rv_skin;
    private String MY_SKIN = "";
    private List<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skin_test);

        intView();
        initViewData();


    }

    private void initViewData() {
        MY_SKIN = this.getExternalFilesDir(null).getAbsolutePath() + File.separator + "skin";
        /**
         * 將Assets中的文件拷貝到SD卡
         */
        List<String> list = MyFileUtil.getAssetsFileNameList(this, "skin");

        for (String name : list) {
            MyFileUtil.moveAssetsToDir(this, name, this.getExternalFilesDir(null).getAbsolutePath() + File.separator + "skin" + File.separator + name);
        }

        /**
         * 獲取SD卡中的皮膚
         */
        mData = MyFileUtil.getFileNameList(MY_SKIN, "skin");
        homeAdapter.notifyDataSetChanged();
    }

    HomeAdapter homeAdapter;

    private void intView() {
        rv_skin = findViewById(R.id.rv_skin);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_skin.setLayoutManager(linearLayoutManager);
        homeAdapter = new HomeAdapter();
        rv_skin.setAdapter(homeAdapter);
    }

    /**
     * HomeAdapter
     */
    class HomeAdapter extends RecyclerView.Adapter<ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //初始化Itme的布局
            ViewHolder holder = ViewHolder.createViewHolder(SkinTest.this, parent, R.layout.skin_test_item);

            return holder;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.setText(R.id.tv_skinName, mData.get(position));
            holder.getMyView(R.id.tv_skinName).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String path = MY_SKIN + File.separator + mData.get(position);
                    changeSkin(path);
                }
            });

        }

        @Override
        public int getItemCount() {
            //返回集合的长度
            return mData.size();
        }


    }


    private void changeSkin(String skinFullName) {
        SkinManager.getInstance().load(skinFullName,
                new ILoaderListener() {
                    @Override
                    public void onStart() {
                        Log.e("", "loadSkinStart");
                    }

                    @Override
                    public void onSuccess() {
                        Log.i("", "loadSkinSuccess");
                        Toast.makeText(SkinTest.this, "切换成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {
                        Log.i("", "loadSkinFail");
                        Toast.makeText(SkinTest.this, "切换失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
