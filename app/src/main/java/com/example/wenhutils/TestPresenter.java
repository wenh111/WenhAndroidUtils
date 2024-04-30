package com.example.wenhutils;

import android.annotation.SuppressLint;

import com.wenh.baselibrary.mvpbase.RxPresenter;
import com.wenh.baselibrary.network.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestPresenter extends RxPresenter<TestContract.View> implements TestContract.Presenter {
    @Override
    public void attachView(TestContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadData() {
        mView.showData("MyTestPresenter");
        List<MeetingMessageBean> list = new ArrayList<>();
        list.add(new MeetingMessageBean("1", "10.23", "ceshi"));
        list.add(new MeetingMessageBean("2", "10.25", "ceshi"));
        list.add(new MeetingMessageBean("3", "10.26", "ceshi"));
        list.add(new MeetingMessageBean("4", "10.24", "ceshi"));
        list.add(new MeetingMessageBean("5", "10.27", "ceshi"));
        MeetingListAdapter meetingListAdapter = new MeetingListAdapter(mView.getActivity(), R.layout.item_list, list);
        mView.showRecyclerView(meetingListAdapter);

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                super.onNext(aBoolean);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
