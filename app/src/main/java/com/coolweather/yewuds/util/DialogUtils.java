package com.coolweather.yewuds.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

public class DialogUtils {
    private CustomDialog customDialog;
    public View inflate;
    private static DialogUtils dialogUtils;


    public static DialogUtils getDislogUtils() {
        synchronized (DialogUtils.class) {
            if (dialogUtils == null) {
                dialogUtils = new DialogUtils();
            }
        }
        return dialogUtils;
    }

    /***
     * 加载框
     ***/
    public static Dialog creatProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        return progressDialog;
    }

}
