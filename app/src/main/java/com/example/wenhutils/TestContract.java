package com.example.wenhutils;

import com.wenh.baselibrary.mvpbase.BasePresenter;
import com.wenh.baselibrary.mvpbase.BaseView;

public class TestContract {
    public interface View extends BaseView {
        void showData(String data);

        void showRecyclerView(MeetingListAdapter meetingListAdapter);
    }

    public interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
