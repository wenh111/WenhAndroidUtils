package com.example.wenhutils;

import android.annotation.SuppressLint;

import com.wenh.baselibrary.bean.DeviceBean;
import com.wenh.baselibrary.callback.Callback;
import com.wenh.baselibrary.log4j.LoggerInit;
import com.wenh.baselibrary.mqtt.MqttHelper;
import com.wenh.baselibrary.mvpbase.RxPresenter;
import com.wenh.baselibrary.network.Apis;
import com.wenh.baselibrary.network.HttpModule;
import com.wenh.baselibrary.network.HttpResponse;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter, Callback<Boolean> {
    private Logger logger = Logger.getLogger(MainPresenter.class);
    private Apis apis;

    @Override
    public void init() {
        connectMqtt();
        mView.changeFragment();
        HttpModule httpModule = new HttpModule("6c1524579851", "192.168.2.166", "80");
        apis = httpModule.provideService();

        EventBus.getDefault().register(this);
        LoggerInit.init(mView.getActivity(), mView.getActivity().getPackageName());
        Logger logger = Logger.getLogger(MainPresenter.class);
        logger.info("myinit");
        deviceLogin();
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
                }
                return null;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Throwable {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {

            }
        });
    }

    private void connectMqtt() {
        MqttHelper.getInstance().connect(mView.getActivity(), "6c1524579851", "6c1524579851", "192.168.2.166", "1883");
        MqttHelper.getInstance().setConnectCallback(this);
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
        MqttHelper.getInstance().subscribe("venus/commit/commit_approved", MeetingMessageArgs.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MeetingMessageArgs myEvent) {
        logger.info("收到消息：" + myEvent.toString());
    }
}
