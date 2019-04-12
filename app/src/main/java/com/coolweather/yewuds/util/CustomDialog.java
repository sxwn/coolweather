package com.coolweather.yewuds.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomDialog extends Dialog {
    private Context context;
    private int height, width;
    private boolean cancelTouchout;
    private View view;
    public volatile static CustomDialog customDialog;


    private CustomDialog(Builder builder) {
        super(builder.context);
        context = builder.context;
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
    }


    private CustomDialog(Builder builder, int resStyle) {
        super(builder.context, resStyle);
        context = builder.context;
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCanceledOnTouchOutside(cancelTouchout);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.CENTER;
        if (width > 0 && height > 0) {
            lp.height = height;
            lp.width = width;
        }
        win.setAttributes(lp);
    }

    public static final class Builder {

        private Context context;
        private int height, width;
        private boolean cancelTouchout;
        private View view;
        private int resStyle = -1;
        private volatile static Builder builder;

        public static Builder getInstance(Context context) {
            if (builder == null) {
                synchronized (Builder.class) {
                    if (builder == null) {
                        builder = new CustomDialog.Builder(context);
                    }
                }
            }
            return builder;
        }

        public Builder(Context context) {
            this.context = context;
        }

        public Builder view(int resView) {
            view = LayoutInflater.from(context).inflate(resView, null);
            return this;
        }

        public Builder setImageSize(int resouceId) {
            ImageView topBg = view.findViewById(resouceId);
            ViewGroup.LayoutParams layoutParams = topBg.getLayoutParams();
            int width = 544;
            int height = 739;
            DisplayMetrics displayMetrics = ScreenUtils.getDisplayMetrics(context);
            int widthPixels = displayMetrics.widthPixels - 320;
            float heightPixels = widthPixels * height / width;
            layoutParams.width = widthPixels;
            layoutParams.height = (int) heightPixels;
            topBg.setLayoutParams(layoutParams);
            return this;
        }

        public Builder setTextSize(int resourceId) {
            TextView textView = view.findViewById(resourceId);
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            DisplayMetrics displayMetrics = ScreenUtils.getDisplayMetrics(context);
            layoutParams.width = displayMetrics.widthPixels - 480;
            textView.setLayoutParams(layoutParams);
            return this;
        }


        public Builder heightpx(int val) {
            height = val;
            return this;
        }

        public Builder widthpx(int val) {
            width = val;
            return this;
        }

        public Builder heightdp(int val) {
            height = DensityUtil.dip2px(context, val);
            return this;
        }

        public Builder widthdp(int val) {
            width = DensityUtil.dip2px(context, val);
            return this;
        }

        public Builder heightDimenRes(int dimenRes) {
            height = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder widthDimenRes(int dimenRes) {
            width = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder style(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        public Builder cancelTouchout(boolean val) {
            cancelTouchout = val;
            return this;
        }

        public Builder addViewOnclick(int viewRes, View.OnClickListener listener) {
            view.findViewById(viewRes).setOnClickListener(listener);
            return this;
        }

        public CustomDialog build() {
            return getParentInstance(context, this, resStyle);
        }

    }

    public static CustomDialog getParentInstance(Context context, Builder builder, int resStyle) {
        if (customDialog == null) {
            synchronized (CustomDialog.class) {
                if (customDialog == null) {
                    if (resStyle != -1) {
                        customDialog = new CustomDialog(builder, resStyle);
                    } else {
                        customDialog = new CustomDialog(builder);
                    }
                }
            }
        }
        return customDialog;
    }
}
