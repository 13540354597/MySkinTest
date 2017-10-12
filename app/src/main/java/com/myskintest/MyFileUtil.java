package com.myskintest;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TR 105 on 2017/8/2.
 */

public class MyFileUtil {
    //==============================================================================================
    //===================================================================================SD卡文件查询
    //==============================================================================================

    /**
     * 得到SD卡中指定后缀名为xxx的文件集合
     */
    public static List<String> getFileNameList(String path, String name) {
        List<String> list = new ArrayList<>();
        File file = new File(path);
        if (file != null) {
            File[] files = file.listFiles(new FileNameSelector(name));
            for (int i = 0; i < files.length; ++i) {
                Log.i("System.out", files[i].getName());
                list.add(files[i].getName());
            }
        }
        return list;
    }

    /**
     * 后缀名过滤器
     *
     * @author ZLQ
     */
    public static class FileNameSelector implements FilenameFilter {
        String extension = ".";

        public FileNameSelector(String fileExtensionNoDot) {
            extension += fileExtensionNoDot;
        }

        public boolean accept(File dir, String name) {
            return name.endsWith(extension);
        }
    }


    //==============================================================================================
    //================================================================================Assets文件查询
    //==============================================================================================

    /**
     * 查询Assets中同种类型的文件名
     *
     * @param context
     * @param type    文件类型
     * @return
     */
    public static List<String> getAssetsFileNameList(Context context, String type) {

        List<String> list = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");


            for (String name : files) {
                Log.e("tag", name);

                if (getFileType(name).equals(type)) {
                    list.add(name);
                }


            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return list;
    }

    public static String getFileType(String name) {
        return name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
    }

    //==============================================================================================
    //======================================================================================文件移动
    //==============================================================================================


    /**
     * 移動Assets中的文件到SD卡中
     *
     * @param context
     * @param rawName
     * @param dir
     */
    public static void moveAssetsToDir(Context context, String rawName, String dir) {
        try {
            writeFile(context.getAssets().open(rawName), dir, true);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 将Assets中的文件写入App缓存中
     *
     * @param is
     * @param path
     * @param isOverride
     * @return
     * @throws Exception
     */
    public static File writeFile(InputStream is, String path, boolean isOverride) throws Exception {
        String sPath = extractFilePath(path);
        if (!pathExists(sPath)) {
            makeDir(sPath, true);
        }

        if (!isOverride && fileExists(path)) {
            if (path.contains(".")) {
                String suffix = path.substring(path.lastIndexOf("."));
                String pre = path.substring(0, path.lastIndexOf("."));
                path = pre + "_" + System.currentTimeMillis() + suffix;
            } else {
                path = path + "_" + System.currentTimeMillis();
            }
        }

        FileOutputStream os = null;
        File file = null;

        try {
            file = new File(path);
            os = new FileOutputStream(file);
            int byteCount = 0;
            byte[] bytes = new byte[1024];

            while ((byteCount = is.read(bytes)) != -1) {
                os.write(bytes, 0, byteCount);
            }
            os.flush();

            return file;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("写文件错误", e);
        } finally {
            try {
                if (os != null)
                    os.close();
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 从文件的完整路径名（路径+文件名）中提取 路径（包括：Drive+Directroy )
     *
     * @param _sFilePathName
     * @return
     */
    public static String extractFilePath(String _sFilePathName) {
        int nPos = _sFilePathName.lastIndexOf('/');
        if (nPos < 0) {
            nPos = _sFilePathName.lastIndexOf('\\');
        }

        return (nPos >= 0 ? _sFilePathName.substring(0, nPos + 1) : "");
    }


    /**
     * 检查指定文件的路径是否存在
     *
     * @param _sPathFileName 文件名称(含路径）
     * @return 若存在，则返回true；否则，返回false
     */
    public static boolean pathExists(String _sPathFileName) {
        String sPath = extractFilePath(_sPathFileName);
        return fileExists(sPath);
    }

    /**
     * 得到指定路徑的文件
     *
     * @param _sPathFileName
     * @return
     */
    public static boolean fileExists(String _sPathFileName) {
        File file = new File(_sPathFileName);
        return file.exists();
    }


    /**
     * 创建目录
     *
     * @param _sDir             目录名称
     * @param _bCreateParentDir 如果父目录不存在，是否创建父目录
     * @return
     */
    public static boolean makeDir(String _sDir, boolean _bCreateParentDir) {
        boolean zResult = false;
        File file = new File(_sDir);
        if (_bCreateParentDir)
            zResult = file.mkdirs(); // 如果父目录不存在，则创建所有必需的父目录
        else
            zResult = file.mkdir(); // 如果父目录不存在，不做处理
        if (!zResult)
            zResult = file.exists();
        return zResult;
    }

    //==============================================================================================
    //================================================================================SD卡中文件删除
    //==============================================================================================

    /**
     * 删除指定文件或文件夹
     *
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    //==============================================================================================
    //================================================================================复制文件
    //==============================================================================================

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public boolean copyFile(String oldPath, String newPath) {
        boolean isOk = true;
        try {
            int byteSum = 0;
            int byteRead = 0;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteRead = inStream.read(buffer)) != -1) {
                    byteSum += byteRead; //字节数 文件大小
                    //System.out.println(bytesum);
                    fs.write(buffer, 0, byteRead);
                }
                fs.flush();
                fs.close();
                inStream.close();
            } else {
                isOk = false;
            }
        } catch (Exception e) {
            // System.out.println("复制单个文件操作出错");
            // e.printStackTrace();
            isOk = false;
        }
        return isOk;

    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public boolean copyFolder(String oldPath, String newPath) {
        boolean isOk = true;
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            isOk = false;
        }
        return isOk;
    }

    //==============================================================================================
    //================================================================================创建文件
    //==============================================================================================

    /**
     * 创建一个文件夹
     *
     * @param path 文件的 路径+文件名
     */
    public void createFile(String path) {
        File file = new File(path);
        if (!file.exists())

        {
            file.mkdir();
        }

    }

    //==============================================================================================
    //===============================================================================保存文件
    //==============================================================================================


    /**
     * 保存文件到sdcard中某个路径下
     *
     * @param path=路径+文件名+后缀
     * @param content
     */
    public void writeContentToPath(String path, String content) {
        File f = new File(path);
        try {
            FileOutputStream fileOS = new FileOutputStream(f);
            try {
                fileOS.write(content.getBytes());
                fileOS.close();
                BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(
                        fileOS));
                buf.write(content, 0, content.length());
                buf.flush();
                buf.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //==============================================================================================
    //================================================================================读取文件
    //==============================================================================================

    /**
     * 从sdcard中的某个路径下读取文件
     *
     * @param path=路径+文件名+后缀
     * @return
     */
    public String readFromPath(String path) {
        byte[] data = new byte[1024];
        try {
            FileInputStream fileIS = new FileInputStream(path);
            // BufferedReader buf = new BufferedReader(new
            // InputStreamReader(in))
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int len = -1;
            while ((len = fileIS.read(buffer)) != -1) {// 读取文件到缓冲区
                byteArrayOutputStream.write(buffer, 0, len);// 写入到内存
            }
            data = byteArrayOutputStream.toByteArray();// 将内存中的二进制数据转为数组
            byteArrayOutputStream.close();
            fileIS.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new String(data);
    }

}
