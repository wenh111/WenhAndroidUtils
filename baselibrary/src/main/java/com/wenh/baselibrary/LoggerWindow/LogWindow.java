package com.wenh.baselibrary.LoggerWindow;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wenh.baselibrary.BaseWindow;
import com.wenh.baselibrary.R;
import com.wenh.baselibrary.Use;
import com.wenh.baselibrary.Viewer;
import com.wenh.baselibrary.adapter.CommonAdapter;
import com.wenh.baselibrary.adapter.MultiItemTypeAdapter;
import com.wenh.baselibrary.adapter.adapterbase.ViewHolder;
import com.wenh.baselibrary.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class LogWindow extends BaseWindow implements View.OnClickListener, View.OnLongClickListener {


    Context appContext;

    private int mWidth;
    private int mHeight;

    View preview;
    TextView textView;

    final static String SCALE = "scale";
    float mScale = 1f;

    public static void open(Context context) {
        open(context, 1f);
    }

    public static void open(Context context, float scale) {
        if (context instanceof Activity)
            if (!WindowAccess.check((Activity) context, true)) return;
        Intent intent = new Intent(context, LogWindow.class);

        intent.putExtra(SCALE, scale);

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //    context.startForegroundService(intent);
        //} else
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mScale = Math.max(intent == null ? mScale : intent.getFloatExtra(SCALE, mScale), 0.8f);

        init();

        return super.onStartCommand(intent, flags, startId);
    }

    void init(){
        if (mWidth > 0) return;
        
        appContext = Logger.getContext();
        int screenWidth = Viewer.getScreenWidth(appContext);

        mWidth = Viewer.dip2px(300 * mScale);
        mHeight = Viewer.dip2px(400 * mScale);
        setLastX(screenWidth - mWidth - Viewer.dip2px(20 * mScale));
        setLastY(Viewer.dip2px(20 * mScale));

        setContentView(R.layout.log_window,
            getLastX(), getLastY(),
            mWidth, mHeight,
        Gravity.LEFT | Gravity.TOP);

        TextView logTitle = findViewById(R.id.log_title);
        logTitle.setTextSize(13 * mScale);
        logTitle.setPadding(0, Viewer.dip2px(3 * mScale), 0, Viewer.dip2px(4 * mScale));

        preview = findViewById(R.id.preview);
        textView = findViewById(R.id.text);
        textView.setTextSize(20 * mScale);
        Viewer.setWidthHeight(findViewById(R.id.min_btn), Viewer.dip2px(18 * mScale), Viewer.dip2px(18 * mScale));

        Viewer.setWidthHeight(findViewById(R.id.close_btn), Viewer.dip2px(22 * mScale), Viewer.dip2px(22 * mScale));
        Viewer.setWidthHeight(findViewById(R.id.close_line1), Viewer.dip2px(22 * mScale), Viewer.dip2px(4 * mScale));
        Viewer.setWidthHeight(findViewById(R.id.close_line2), Viewer.dip2px(22 * mScale), Viewer.dip2px(4 * mScale));
        Viewer.setMarginTop(findViewById(R.id.close_line1), Viewer.dip2px(9 * mScale));
        Viewer.setMarginTop(findViewById(R.id.close_line2), Viewer.dip2px(9 * mScale));

        Viewer.setWidth(findViewById(R.id.search_btn), Viewer.dip2px(26 * mScale));
        Viewer.setWidthHeight(findViewById(R.id.search_circle), Viewer.dip2px(16 * mScale), Viewer.dip2px(16 * mScale));
        Viewer.setWidthHeight(findViewById(R.id.search_line), Viewer.dip2px(4 * mScale), Viewer.dip2px(10 * mScale));
        Viewer.setMargin(findViewById(R.id.search_line), Viewer.dip2px(15 * mScale), Viewer.dip2px(12 * mScale), 0, 0);

        findViewById(R.id.min_btn).setOnClickListener(this);
        findViewById(R.id.close_btn).setOnClickListener(this);
        findViewById(R.id.close_btn).setOnLongClickListener(this);
        findViewById(R.id.search_btn).setOnClickListener(this);
        titleView = findViewById(R.id.title);

        listView = findViewById(R.id.log_list);

        listView.setVisibility(View.VISIBLE);


        initList();

        Logger.getInstance().setCallback(result -> {
            if (System.currentTimeMillis() - lastTime >= Logger.getInstance().showInterval) {
                lastTime = System.currentTimeMillis();
                showList(Logger.getInstance().getRecordList());
            }
        });


        Keyboard keyboard = findViewById(R.id.keyboard);
        keyboard.setFontSize(Viewer.dip2px(20 * mScale));
        keyboard.setKeyHeight(Viewer.dip2px(44 * mScale));
        keyboard.setKeyMargin(Viewer.dip2px(1.5f * mScale));
        keyboard.setOnEventListener(new Keyboard.KeyEventListener() {
            @Override
            public void onEvent(String s) {
                keyword = s;
                logTitle.setText(s);
                lastTime = System.currentTimeMillis();
                showList(Logger.getInstance().getRecordList());
            }
        });
        Viewer.click(v -> {
            if (Viewer.isVisible(keyboard)) {
                Viewer.hide(keyboard);
                if (keyword == null || keyword.isEmpty())
                    logTitle.setText(Use.getString(R.string.log_window));
            } else Viewer.show(keyboard);
        }, findViewById(R.id.search_btn));
    }

    String keyword;

    public void initList() {
        logList = new ArrayList<>();
        logList.addAll(Logger.getInstance().getRecordList());
        logAdapter = new CommonAdapter<Logger.LogInfo>(appContext, R.layout.log_window_item, logList) {
            @Override
            protected void convert(ViewHolder holder, Logger.LogInfo logInfo, int position) {
                holder.setText(R.id.content, logInfo.getContent());
                holder.setText(R.id.time, TimeUtil.format(logInfo.getTime(), TimeUtil.H_M_S));
                ((TextView) holder.getView(R.id.content)).setTextSize(10 * mScale);
                ((TextView) holder.getView(R.id.time)).setTextSize(10 * mScale);
            }
        };
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(logAdapter);

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        logAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int i) {
                if (i < logList.size()) {
                    Logger.LogInfo logBean = logList.get(i);
                    if (logBean != null) {
                        ClipData clipData = ClipData.newPlainText(null, logBean.getContent());
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(appContext, appContext.getString(R.string.copied), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                Viewer.hide(listView);
                Viewer.show(preview);
                textView.setText(logList.get(position).getContent());
                return false;
            }
        });

    }

    public void showList(List<Logger.LogInfo> list) {
        new Handler(Looper.getMainLooper()).post(() -> {
            logList.clear();
            if (keyword == null || keyword.isEmpty())
                logList.addAll(list);
            else {
                for(Logger.LogInfo info : list) {
                    if (info.getContent().contains(keyword)) logList.add(info);
                }
            }
            logAdapter.notifyDataSetChanged();
        });

    }

    long lastTime = 0;
    RecyclerView listView;
    List<Logger.LogInfo> logList;
    CommonAdapter logAdapter;

    View titleView;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean isMinimum() {
        return mWidth == getWmParams().width;
    }

    @Override
    public void minimum() {
        min(mWidth, mHeight);
    }

    public void min() {
        int size = Viewer.dip2px(18 * mScale);
        min(size, size);
    }

    public void min(int width, int height) {
        if (getWmParams() != null) {
            getWmParams().width = width;
            getWmParams().height = height;
            getWmParams().x = getLastX();
            getWmParams().y = getLastY();
            updateWindowLayout(getWmParams());
            onWindowMinimum();
            updateView();
        }
    }

    private void updateView() {
        if (getWmParams() != null) {
            View view = Viewer.isVisible(preview) ? preview : listView;
            Viewer.visible(isFullScreen() || isMinimum(), titleView, view);
        }
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.min_btn) min();
        else if (view.getId() == R.id.close_btn) {
            if (Viewer.isVisible(listView))
                finish();
            else {
                Viewer.show(listView);
                Viewer.hide(preview);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.close_btn) {
            Logger.getInstance().getRecordList().clear();
            logList.clear();
            logAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (isFullScreen() || !isMinimum()) minimum();
        else maximum();
        updateView();
        return false;
    }
}