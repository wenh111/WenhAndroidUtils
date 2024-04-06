package com.example.wenhutils;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.wenh.baselibrary.Mods;
import com.wenh.baselibrary.NoticeUtil;
import com.wenh.baselibrary.bean.DeviceBean;
import com.wenh.baselibrary.callback.Callback;
import com.wenh.baselibrary.log4j.LoggerInit;
import com.wenh.baselibrary.mvpbase.RxPresenter;
import com.wenh.baselibrary.network.Apis;
import com.wenh.baselibrary.network.HttpResponse;
import com.wenh.baselibrary.util.thread.Threads;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter, Callback<Boolean>, OnPermissionCallback {
    private Logger logger = Logger.getLogger(MainPresenter.class);
    private Apis apis;

    @Override
    public void init() {
        Mods.prefers().setServiceAddress("192.168.2.166");
        Mods.prefers().setHttpPort("80");
        Mods.prefers().setMqttPort("1883");
//        mView.changeFragment();

        apis = Mods.apis();
        connectMqtt();
        EventBus.getDefault().register(this);
        LoggerInit.init(mView.getActivity());
        //PermissionUtils.permission(MANAGE_EXTERNAL_STORAGE).request();
        //获取所需要的权限
        String[] permissions = {
                /*Permission.READ_MEDIA_IMAGES,
                Permission.READ_MEDIA_VIDEO,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.READ_MEDIA_AUDIO,*/
                Permission.READ_PHONE_STATE,//获取手机状态
                Permission.CAMERA,//相机
                Permission.SYSTEM_ALERT_WINDOW,//悬浮窗
                Permission.SCHEDULE_EXACT_ALARM,//定时
                Permission.MANAGE_EXTERNAL_STORAGE//管理外部存储
        };

        XXPermissions.with(mView.getActivity())
                .permission(permissions)
                .request(this);

    }

    @SuppressLint("CheckResult")
    private void deviceLogin() {
        Observable.just(0).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.io()).map(new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer integer) throws Throwable {
                Response<HttpResponse<DeviceBean>> execute = apis.longin("6c1524579851").execute();
                if (execute != null && execute.isSuccessful()) {
                    DeviceBean data = execute.body().getData();
                    logger.info("data.getMac():" + data.getMac());
                    return true;
                }
                return false;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Throwable {
                if (aBoolean) {
                    Threads.ui(new Runnable() {
                        @Override
                        public void run() {
                            NoticeUtil.ins().error("登录成功");
                        }
                    });
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                throwable.printStackTrace();
            }
        });
    }

    private void connectMqtt() {
        Mods.mqttModel().connect();
        Mods.mqttModel().setConnectCallback(this);
    }

    @Override
    public void attachView(MainContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }


    @Override
    public void onCallback(Boolean result) {
        Mods.mqttModel().subscribe("venus/commit/commit_approved", MeetingMessageArgs.class);
        Mods.mqttModel().subscribe("venus/meeting/addNotice/" + "room_d4DPHkcJf7PijajDRlKFxwkm977", MeetingBean.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MeetingMessageArgs myEvent) {
        logger.info("收到消息：" + myEvent.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MeetingBean myEvent) {
        logger.info("收到消息：" + myEvent.toString());
    }

    @Override
    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
        if (allGranted) {
//            deviceLogin();
            getExcelMessage();
        }
    }

    private void getExcelMessage() {
        // 执行任务
        new Thread(new Runnable() {
            @Override
            public void run() {
                File excelFile = new File("/storage/emulated/0/guofang/messagetable.xlsx");
                try {
                    FileInputStream fis = new FileInputStream(excelFile);
                    XSSFWorkbook workbook = new XSSFWorkbook(fis);
                    // 获取第一个工作表
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    // 迭代每一行
                    for (Row row : sheet) {
                        // 迭代每一列
                        StringBuilder stringBuffer = new StringBuilder();
                        for (Cell cell : row) {
                            // 获取单元格内容并打印
                            stringBuffer.append(cell.toString());
                            stringBuffer.append("     ");
                        }
                        Log.d("ExcelDemo", stringBuffer.toString());
                    }

                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
        OnPermissionCallback.super.onDenied(permissions, doNotAskAgain);
    }
}
