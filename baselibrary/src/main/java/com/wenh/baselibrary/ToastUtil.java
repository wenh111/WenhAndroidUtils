package com.wenh.baselibrary;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class ToastUtil {

    static ToastUtil td;

    public static void show(int resId) {
        show(Frame.getString(resId));
    }

    public static void show(String msg) {
        if (td == null) {
            td = new ToastUtil(Frame.getContext());
        }
        td.setText(msg);
        td.create().show();
    }



    Context context;
    Toast toast;
    String msg;

    public ToastUtil(Context context) {
        this.context = context;
    }

    public Toast create() {
        View contentView = View.inflate(context, R.layout.dialog_toast, null);
        TextView tvMsg = (TextView) contentView.findViewById(R.id.tv_toast_msg);
        toast = new Toast(context);
        toast.setView(contentView);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        tvMsg.setText(msg);
        return toast;
    }

    public Toast createShort() {
        View contentView = View.inflate(context, R.layout.dialog_toast, null);
        TextView tvMsg = (TextView) contentView.findViewById(R.id.tv_toast_msg);
        toast = new Toast(context);
        toast.setView(contentView);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        tvMsg.setText(msg);
        return toast;
    }

    public void show() {
        if (toast != null) {
            toast.show();
        }
    }

    public void setText(String text) {
        msg = text;
    }

    public static void showSnackBar(View view, String msg) {
        showSnackBar(view, msg, 0, 0);
    }

    public static void showSnackBar(View view, String msg, int bgRes, int iconRes) {

        Snackbar snackbar = Snackbar.make(view, msg, BaseTransientBottomBar.LENGTH_LONG);

        View snack_view = snackbar.getView();

        snack_view.setBackgroundColor(snack_view.getContext().getResources().getColor(android.R.color.transparent));

        Snackbar.SnackbarLayout snack_Layout = (Snackbar.SnackbarLayout) snack_view;//将获取的View转换成SnackbarLayout

        View add_view = LayoutInflater.from(snack_view.getContext()).inflate(R.layout.view_snackbar, null);//加载布局文件新建View

        if (bgRes > 0) add_view.setBackground(snack_view.getContext().getDrawable(bgRes));

        if (iconRes > 0) ((ImageView) add_view.findViewById(R.id.snack_icon)).setImageResource(iconRes);
        ((TextView) add_view.findViewById(R.id.snack_text)).setText(msg);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);//设置新建布局参数

        p.gravity = Gravity.CENTER_VERTICAL;//设置新建布局在Snackbar内垂直居中显示

        snack_Layout.addView(add_view, 1, p);//将新建布局添加进snackbarLayout相应位置


        snackbar.show();
    }

    public static void showShortSnackBar(View view, String msg) {
        Snackbar.make(view, msg, BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}
