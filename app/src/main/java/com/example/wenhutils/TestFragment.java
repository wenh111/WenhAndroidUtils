package com.example.wenhutils;


import com.billy.android.swipe.SmartSwipeRefresh;
import com.billy.android.swipe.consumer.SlidingConsumer;
import com.example.wenhutils.databinding.FragmentTestBinding;
import com.wenh.baselibrary.mvpbase.BaseFragment;
import com.wenh.baselibrary.recyclerview.SmartFooter;
import com.wenh.baselibrary.recyclerview.SmartHeader;

import java.util.Timer;
import java.util.TimerTask;


public class TestFragment extends BaseFragment<FragmentTestBinding, TestPresenter> implements TestContract.View, SmartSwipeRefresh.SmartSwipeRefreshDataLoader {


    @Override
    protected void initial() {
        mPresenter.loadData();
    }

    @Override
    public void showData(String data) {
        views.textView.setText(data);
    }

    @Override
    public void showRecyclerView(MeetingListAdapter meetingListAdapter) {
        views.recyclerView.setAdapter(meetingListAdapter);
        SmartHeader smartHeader = new SmartHeader(requireContext());
        SmartSwipeRefresh.behindMode(views.recyclerView, false)
                .setDataLoader(this)
                .setHeader(smartHeader)
                .setFooter(new SmartFooter(requireContext()))
                .getSwipeConsumer().as(SlidingConsumer.class).setDrawerExpandable(true);
    }

    @Override
    public void empty(String msg) {

    }

    //加载刷新数据
    @Override
    public void onRefresh(SmartSwipeRefresh ssr) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ssr.finished(true);
                    }
                });
            }
        }, 2000);
    }


    //加载下一页数据
    @Override
    public void onLoadMore(SmartSwipeRefresh ssr) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ssr.finished(true);
                        boolean loadCompleted = false;
                        ssr.setNoMoreData(loadCompleted);
                    }
                });
            }
        }, 2000);
    }
}