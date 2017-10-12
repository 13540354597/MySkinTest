package com.myskintest;

import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ren.solid.library.utils.FileUtils;
import ren.solid.skinloader.base.SkinBaseActivity;
import ren.solid.skinloader.listener.ILoaderListener;
import ren.solid.skinloader.load.SkinManager;

public class MainActivity extends SkinBaseActivity {
    private String TAG = "MainActivity";
    private Button btn_Skin_black, btn_Skin_brown;
    private static String SKIN_DIR;
    private String MY_SKIN = this.getExternalFilesDir(null).getAbsolutePath() + File.separator + "skin" + File.separator;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


        btn_Skin_black = findViewById(R.id.btn_Skin_black);
        btn_Skin_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setSkin1();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_Skin_brown = findViewById(R.id.btn_Skin_brown);
        btn_Skin_brown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setSkin2();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        SKIN_DIR = FileUtils.getSkinDirPath(this);

    }

    private void initView() {
        String getExternalCacheDir = this.getExternalCacheDir().getAbsolutePath();
        String getExternalFilesDir = this.getExternalFilesDir(null).getAbsolutePath();
        String getFilesDir = this.getFilesDir().getAbsolutePath();
        String getCacheDir = this.getCacheDir().getAbsolutePath();


        //  MyFileUtil.moveAssetsToDir(this, "skin_brown.skin", this.getExternalFilesDir(null).getAbsolutePath() + File.separator + "skin_brown.skin");
        List<String> list = MyFileUtil.getAssetsFileNameList(this);

        for (String name : list) {
            MyFileUtil.moveAssetsToDir(this, name, this.getExternalFilesDir(null).getAbsolutePath() + File.separator + "skin" + File.separator + name);
        }


    }


    private void setSkin1() throws IOException {


        String skinFullName = SKIN_DIR + File.separator + "skin_brown.skin";
        FileUtils.moveRawToDir(this, "skin_brown.skin", skinFullName);
        File skin = new File(skinFullName);
        if (!skin.exists()) {
            Toast.makeText(this, "请检查" + skinFullName + "是否存在", Toast.LENGTH_SHORT).show();
            return;
        }
        String path = "file:///android_asset/skin_brown.skin";
        Log.i(TAG, "=========1" + skinFullName);
        SkinManager.getInstance().load(skinFullName,
                new ILoaderListener() {
                    @Override
                    public void onStart() {
                        Log.e(TAG, "loadSkinStart");
                    }

                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "loadSkinSuccess");
                        Toast.makeText(MainActivity.this, "切换成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {
                        Log.i(TAG, "loadSkinFail");
                        Toast.makeText(MainActivity.this, "切换失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setSkin2() throws IOException {


        String skinFullName = SKIN_DIR + File.separator + "skin_black.skin";
        FileUtils.moveRawToDir(this, "skin_black.skin", skinFullName);
        File skin = new File(skinFullName);
        if (!skin.exists()) {
            Toast.makeText(this, "请检查" + skinFullName + "是否存在", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "=========1" + skinFullName);
        SkinManager.getInstance().load(skinFullName,
                new ILoaderListener() {
                    @Override
                    public void onStart() {
                        Log.e(TAG, "loadSkinStart");
                    }

                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "loadSkinSuccess");
                        Toast.makeText(MainActivity.this, "切换成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {
                        Log.i(TAG, "loadSkinFail");
                        Toast.makeText(MainActivity.this, "切换失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    // 获取当前目录下所有的mp4文件
    public static Vector<String> GetVideoFileName(String fileAbsolutePath) {
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                // 判断是否为MP4结尾
                if (filename.trim().toLowerCase().endsWith(".skin")) {
                    vecFile.add(filename);
                }
            }
        }
        return vecFile;
    }


}
