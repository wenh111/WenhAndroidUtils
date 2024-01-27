package com.example.hwhutillibrary.bean;

import android.content.Context;
import android.os.Build;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;

import androidx.annotation.RequiresApi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class UDiskUtils {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<UDisKBean> uDiskName(Context context) {
        StorageManager storageManager = context.getSystemService(StorageManager.class);
        List<StorageVolume> volumeList = storageManager.getStorageVolumes();
        List<UDisKBean> udiskBeanList = new ArrayList<>();
        for (StorageVolume volume : volumeList) {
            if (null != volume && volume.isRemovable()) {
                //这个其实就是U盘的名称
                String label = volume.getDescription(context);
                //设备挂载的状态，如:mounted、unmounted
                String status = volume.getState();
                //是否是内部存储设备
                boolean isEmulated = volume.isEmulated();
                //是否是可移除的外部存储设备
                boolean isRemovable = volume.isRemovable();
                //设备的路径
                String mPath = "";
                try {
                    Class myclass = Class.forName(volume.getClass().getName());
                    Method getPath = myclass.getDeclaredMethod("getPath", null);
                    getPath.setAccessible(true);
                    mPath = (String) getPath.invoke(volume);
                    System.out.println("name: " + label + ", status: " + status
                            + ", isEmulated: " + isEmulated + ", isRemovable: " + isRemovable + ", mPath: " + mPath);
                    UDisKBean uDisKBean = new UDisKBean();
                    uDisKBean.setName(label);
                    uDisKBean.setmPath(mPath);
                    udiskBeanList.add(uDisKBean);
                } catch (ClassNotFoundException | NoSuchMethodException |
                         InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();

                }
            }
        }
        return udiskBeanList;
    }
}
