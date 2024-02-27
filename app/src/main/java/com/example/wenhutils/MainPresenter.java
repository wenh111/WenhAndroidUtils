package com.example.wenhutils;

import android.annotation.SuppressLint;

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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter, Callback<Boolean>,OnPermissionCallback {
    private Logger logger = Logger.getLogger(MainPresenter.class);
    private Apis apis;

    @Override
    public void init() {
        Mods.prefers().setServiceAddress("192.168.2.166");
        Mods.prefers().setHttpPort("80");
        Mods.prefers().setMqttPort("1883");
        mView.changeFragment();

        apis = Mods.apis();
        connectMqtt();
        EventBus.getDefault().register(this);
        LoggerInit.init(mView.getActivity());
        //PermissionUtils.permission(MANAGE_EXTERNAL_STORAGE).request();
        //获取所需要的权限
        String[] permissions = {
                /*Permission.READ_MEDIA_IMAGES,
                Permission.READ_MEDIA_VIDEO,
                Permission.READ_MEDIA_AUDIO,*/
                Permission.READ_PHONE_STATE,//获取手机状态
                //Permission.WRITE_EXTERNAL_STORAGE,
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
                if(aBoolean){
                    Threads.ui(new Runnable() {
                        @Override
                        public void run() {
                            NoticeUtil.ins().success("登录成功");
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MeetingMessageArgs myEvent) {
        logger.info("收到消息：" + myEvent.toString());
    }

    @Override
    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
        if(allGranted){
            deviceLogin();
        }
    }

    @Override
    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
        OnPermissionCallback.super.onDenied(permissions, doNotAskAgain);
    }
}
