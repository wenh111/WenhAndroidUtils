package com.wenh.baselibrary.LoggerWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenh.baselibrary.R;
import com.wenh.baselibrary.Viewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Keyboard extends LinearLayout {

    public static final int KEY_DELETE = -1;
    public static final int KEY_CLEAN = -9;
    private static final int KEY_SHIFT = -6;
    private static final int KEY_MODE = -7;    //模式

    private static final int KEY_A = 10;
    private static final int KEY_B = 11;
    private static final int KEY_C = 12;
    private static final int KEY_D = 13;
    private static final int KEY_E = 14;
    private static final int KEY_F = 15;
    private static final int KEY_G = 16;
    private static final int KEY_H = 17;
    private static final int KEY_I = 18;
    private static final int KEY_J = 19;
    private static final int KEY_K = 20;
    private static final int KEY_L = 21;
    private static final int KEY_M = 22;
    private static final int KEY_N = 23;
    private static final int KEY_O = 24;
    private static final int KEY_P = 25;
    private static final int KEY_Q = 26;
    private static final int KEY_R = 27;
    private static final int KEY_S = 28;
    private static final int KEY_T = 29;
    private static final int KEY_U = 30;
    private static final int KEY_V = 31;
    private static final int KEY_W = 32;
    private static final int KEY_X = 33;
    private static final int KEY_Y = 34;
    private static final int KEY_Z = 35;

    private static final int KEY_DOT = 36;
    private static final int KEY_COLON = 37;
    private static final int KEY_SLASH = 38;
    private static final int KEY_UNDERSCORE = 39;
    private static final int KEY_MINUS = 40;

    private static final int SHIFT_UPPERCASE = -4;    //大写
    private static final int SHIFT_LOWERCASE = -5;    //小写

    public static final int MODE_NUMBER = -2;       //数字
    public static final int MODE_LETTER = -3;       //字母
    private static final int MODE_ALL = -23;

    private int keyColor = 0xff666666;
    private int keyPressColor = 0xff333333;

    private int keyShift = SHIFT_LOWERCASE;

    private int onlyMode = MODE_ALL;
    private int keyMode = MODE_LETTER;

    String[] CHARACTER = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            ".", ":", "/", "_", "-"};

    Integer[] NUMBER_KEYS = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, KEY_MODE, 0, KEY_DELETE};
    Integer[] CHARACTER_KEYS = new Integer[]{KEY_Q, KEY_W, KEY_E, KEY_R, KEY_T, KEY_Y, KEY_U, KEY_I, KEY_O, KEY_P,
        KEY_A, KEY_S, KEY_D, KEY_F, KEY_G, KEY_H, KEY_J, KEY_K, KEY_L,
        KEY_SHIFT, KEY_Z, KEY_X, KEY_C, KEY_V, KEY_B, KEY_N, KEY_M,
        KEY_DOT, KEY_COLON, KEY_SLASH, KEY_UNDERSCORE, KEY_MINUS, KEY_MODE, KEY_DELETE};

    Context mContext;


    public Keyboard(Context context) {
        super(context, null);
    }

    public Keyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Keyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        init();
    }

    List<String> texts = new ArrayList<>();

    LinearLayout numberLayout;
    LinearLayout letterLayout;
    TextView modeView;

    private int maxNumberWidth;
    private int maxHiddenWidth;
    private int keyMargin;
    private int keyHeight;
    private float fontSize;

    private int width;

    public void init() {
        maxNumberWidth = Viewer.dip2px(getContext(), 360);
        maxHiddenWidth = Viewer.dip2px(getContext(), 600);
        keyMargin = Viewer.dip2px(getContext(), 3);
        keyHeight = Viewer.dip2px(getContext(), 50);
        fontSize = Viewer.dip2px(getContext(), 26);

        //setBackgroundColor(Color.LTGRAY);
        setGravity(Gravity.CENTER);

        post(() -> {
            if (width == 0) {
                createView(NUMBER_KEYS);
                if (keyMode == MODE_LETTER) switchMode(modeView);
            }
        });

    }

    private void createView(Integer[] keys) {

        width = getWidth();

        MaxLinearLayout wrap = new MaxLinearLayout(getContext());

        wrap.setOrientation(LinearLayout.VERTICAL);
        addView(wrap, 0);

        LinearLayout.LayoutParams wrapParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        wrapParams.weight = keys == NUMBER_KEYS ? 2 : 5;

        if (keys == NUMBER_KEYS) {
            wrap.setMaxWidth(maxNumberWidth);
            numberLayout = wrap;
            if (onlyMode == MODE_LETTER) Viewer.hide(numberLayout);
        } else {
            letterLayout = wrap;
            if (onlyMode != MODE_LETTER && width >= maxHiddenWidth)
                wrapParams.rightMargin = Viewer.dip2px(getContext(), 10);
        }


        wrap.setLayoutParams(wrapParams);


        LinearLayout layout = null;
        for (int i = 0; i < keys.length; i++) {
            int key = keys[i];
            if ((keys == NUMBER_KEYS && i % 3 == 0) || key == KEY_Q || key == KEY_A || key == KEY_SHIFT || key == KEY_DOT) {
                layout = new LinearLayout(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                layout.setLayoutParams(layoutParams);
                layout.setGravity(Gravity.CENTER);
                wrap.addView(layout);
            }

            if (keys == CHARACTER_KEYS) {
                if (key == KEY_DELETE && (onlyMode != MODE_LETTER && width >= maxHiddenWidth)) continue;
                if (key == KEY_MODE && (onlyMode == MODE_LETTER || width >= maxHiddenWidth)) continue;
            } else {
                if (key == KEY_MODE && onlyMode == MODE_NUMBER) key = KEY_CLEAN;
            }

            TextView textView = new TextView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, keyHeight);
            lp.weight = 1;
            lp.leftMargin = keyMargin;
            lp.rightMargin = keyMargin;
            lp.topMargin = keyMargin;
            lp.bottomMargin = keyMargin;
            textView.setLayoutParams(lp);
            textView.getPaint().setFakeBoldText(true);
            textView.setLineSpacing(0, 0.7f);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.use_keyboard_key);
            drawable.setColorFilter(keyColor, PorterDuff.Mode.SRC_ATOP);
            textView.setBackground(drawable);

            if (key == KEY_CLEAN) textView.setText("清除");
            else if (key == KEY_DELETE) textView.setText("←");
            else if (key == KEY_SHIFT) textView.setText("↑");
            else if (key == KEY_MODE) textView.setText(keyMode == MODE_NUMBER ? "abc" : "123");
            else textView.setText(keyShift == SHIFT_UPPERCASE ? CHARACTER[key].toUpperCase() : CHARACTER[key]);

            setPressColor(textView);
            layout.addView(textView);

            int value = key;
            Viewer.click(view -> click(value), textView);
            if (key == KEY_MODE && keys == NUMBER_KEYS) modeView = textView;
        }
    }

    private void switchMode(TextView view) {
        if (keyMode == MODE_LETTER) {
            createView(CHARACTER_KEYS);
            if (width < maxHiddenWidth) Viewer.hide(numberLayout);
            else view.setText("123");
        } else {
            view.setText("abc");
            if (letterLayout != null) Viewer.hide(letterLayout);
            Viewer.show(numberLayout);
        }
    }

    private void click(int key) {
        if (key == KEY_MODE) {
            keyMode = keyMode == MODE_NUMBER ? MODE_LETTER : MODE_NUMBER;
            switchMode(modeView);
            return;
        } else if (key == KEY_SHIFT) {
            keyShift = keyShift == SHIFT_UPPERCASE ? SHIFT_LOWERCASE : SHIFT_UPPERCASE;
            removeView(letterLayout);
            createView(CHARACTER_KEYS);
            return;
        }
        onClick(key);
    }

    private void onClick(int value) {
        if (value == KEY_DELETE) {
            if (texts.size() > 0) texts.remove(texts.size() - 1);
        } else if (value == KEY_CLEAN) {
            texts.clear();
        } else {
            if (max <= 0 || texts.size() < max)
                texts.add(keyShift == SHIFT_UPPERCASE ? CHARACTER[value].toUpperCase() : CHARACTER[value]);
        }
        if (onEventListener != null) onEventListener.onEvent(getText());
    }

    private void setPressColor(View view) {
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Drawable drawable = getContext().getResources().getDrawable(R.drawable.use_keyboard_key);
                    drawable.setColorFilter(keyPressColor, PorterDuff.Mode.SRC_ATOP);
                    v.setBackground(drawable);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable drawable = getContext().getResources().getDrawable(R.drawable.use_keyboard_key);
                    drawable.setColorFilter(keyColor, PorterDuff.Mode.SRC_ATOP);
                    v.setBackground(drawable);
                }

                return false;
            }
        });
    }

    public List<String> getTexts() {
        return texts;
    }

    public String getText() {
        return String.join("", texts);
    }

    public void setText(String text) {
        if (text.isEmpty()) {
            texts.clear();
            return;
        }
        texts = new ArrayList<>(Arrays.asList(text.split("")));
    }

    public void setOnlyMode(int onlyMode) {
        this.onlyMode = onlyMode;
        if (onlyMode == MODE_NUMBER || onlyMode == MODE_LETTER) keyMode = onlyMode;
    }

    public void setKeyMode(int keyMode) {
        if (onlyMode == MODE_NUMBER || onlyMode == MODE_LETTER) return;
        this.keyMode = keyMode;
    }

    public void setMaxNumberWidth(int maxNumberWidth) {
        this.maxNumberWidth = maxNumberWidth;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public void setKeyColor(int keyColor) {
        this.keyColor = keyColor;
    }

    public void setKeyPressColor(int keyPressColor) {
        this.keyPressColor = keyPressColor;
    }

    public void setKeyHeight(int keyHeight) {
        this.keyHeight = keyHeight;
    }

    public void setKeyMargin(int keyMargin) {
        this.keyMargin = keyMargin;
    }

    int max = 0;
    KeyEventListener onEventListener;

    public void setOnEventListener(KeyEventListener onEventListener) {
        this.onEventListener = onEventListener;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public interface KeyEventListener {
        void onEvent(String text);
    }
}