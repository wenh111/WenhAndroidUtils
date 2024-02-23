package com.example.wenhutils;

import com.example.wenhutils.databinding.ActivityMainBinding;
import com.wenh.baselibrary.mvpbase.BaseActivity;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainPresenter> implements MainContract.View {

//    private RecyclerView recyclerView;
/*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        List<MeetingMessageBean> list = new ArrayList<>();
        list.add(new MeetingMessageBean("1", "10.23", "ceshi"));
        list.add(new MeetingMessageBean("2", "10.25", "ceshi"));
        list.add(new MeetingMessageBean("3", "10.26", "ceshi"));
        list.add(new MeetingMessageBean("4", "10.24", "ceshi"));
        list.add(new MeetingMessageBean("5", "10.27", "ceshi"));
        MeetingListAdapter meetingListAdapter = new MeetingListAdapter(this, R.layout.item_list, list);
        recyclerView.setAdapter(meetingListAdapter);

        MyHeader myHeader = new MyHeader(this);

        SmartSwipeRefresh.SmartSwipeRefreshDataLoader loader = new SmartSwipeRefresh.SmartSwipeRefreshDataLoader() {
            @Override
            public void onRefresh(final SmartSwipeRefresh ssr) {
                //加载刷新数据

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                ssr.finished(true);
                                //刷新完成后，如果数据不足一页，可以提前设置已加载完成的状态
                                boolean loadCompleted = false;
                                ssr.setNoMoreData(loadCompleted);
                            }
                        });
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(final SmartSwipeRefresh ssr) {
                //加载下一页数据
                */
/*loadMoreData(new Callback() {
                    void success() {
                        ssr.finished(true);
                        // 是否已全部加载完成
                        boolean loadCompleted = true;
                        ssr.setNoMoreData(loadCompleted);
                    }
                    void failed() {
                        ssr.finished(false);
                    }
                })*/
/*

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                ssr.finished(true);
                                boolean loadCompleted = false;
                                ssr.setNoMoreData(loadCompleted);
                            }
                        });
                    }
                }, 2000);
            }
        };

        SmartSwipeRefresh.behindMode(recyclerView, false)
                .setDataLoader(loader)
                .setHeader(myHeader)
                .setFooter(new ClassicFooter(this))
                .getSwipeConsumer().as(SlidingConsumer.class).setDrawerExpandable(true);



    }*/

    @Override
    protected void initial() {
        mPresenter.init();
    }

    @Override
    public void changeFragment() {
        loadRootFragment(R.id.frameLayout, new TestFragment());
    }
}