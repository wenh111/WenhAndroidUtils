package com.example.wenhutils;

import com.wenh.baselibrary.mvpbase.RxPresenter;

import java.util.ArrayList;
import java.util.List;

public class TestPresenter extends RxPresenter<TestContract.View> implements TestContract.Presenter {
    @Override
    public void attachView(TestContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

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
    }
}
