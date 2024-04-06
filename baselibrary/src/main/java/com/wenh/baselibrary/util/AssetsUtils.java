package com.wenh.baselibrary.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class AssetsUtils {

    /***
     * 调用方式
     *
     * String path = Environment.getExternalStorageDirectory().toString() + "/" + "Tianchaoxiong/useso";
     * String modelFilePath = "Model/seeta_fa_v1.1.bin";
     * Assets2Sd(this, modelFilePath, path + "/" + modelFilePath);
     *
     * @param context
     * @param fileAssetPath assets中的目录
     * @param fileSdPath 要复制到sd卡中的目录
     */
    public static void assets2Sd(Context context, String fileAssetPath, String fileSdPath){
        //测试把文件直接复制到sd卡中 fileSdPath完整路径
        File file = new File(fileSdPath);
        if(file.exists()){
            file.delete();
        }
        // 创建父级文件夹
        File parent = file.getParentFile();
        if(!parent.exists()){
            parent.mkdirs();
        }
        try {
            copyBigDataToSD(context, fileAssetPath, fileSdPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void copyBigDataToSD(Context context, String fileAssetPath, String strOutFileName) throws IOException
    {

        File outFile = new File(strOutFileName);
        File outParentFile = outFile.getParentFile();
        if(!outParentFile.exists()){
            outParentFile.mkdirs();
        }
        if(!outFile.exists()){
            outFile.createNewFile();
        }

        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(outFile);
        myInput = context.getAssets().open(fileAssetPath);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while(length > 0)
        {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }

        myOutput.flush();
        myInput.close();
        myOutput.close();
    }


    public static String readAssets(Context context, String file){
        try {
            InputStream inputStream = context.getAssets().open(file);

            String str = getString(inputStream);
            return str;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
