package com.example.wenhutils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.consumer.SlidingConsumer;
import com.wenh.baselibrary.adapter.CommonAdapter;
import com.wenh.baselibrary.adapter.adapterbase.ViewHolder;


import java.util.List;

public class MeetingListAdapter extends CommonAdapter<MeetingMessageBean> {
    private TextView textView;
    private SlidingConsumer mSlidingConsumer;

    public MeetingListAdapter(Context context, int layoutId, List<MeetingMessageBean> datas) {
        super(context, layoutId, datas);
//        View horizontalMenu = LayoutInflater.from(context).inflate(R.layout.window_side_menu, null);
        textView = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(context.getResources().getColor(R.color.red));
        textView.setText("删除");
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(context.getResources().getColor(R.color.white));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


    }

    @Override
    protected void convert(ViewHolder viewHolder, MeetingMessageBean item, int position) {
        TextView meetingTextView = viewHolder.getView(R.id.meeting_name);
        TextView meetingTimeTextView = viewHolder.getView(R.id.meeting_time);
        meetingTextView.setText(item.getMeetingName());
        meetingTimeTextView.setText(item.getMeetingTime());
        /*SmartSwipe.wrap(constraintLayout)
                .addConsumer(new SlidingConsumer())
                .setHorizontalDrawerView(textView)
                .setScrimColor(0x2F000000);*/
    }
}
