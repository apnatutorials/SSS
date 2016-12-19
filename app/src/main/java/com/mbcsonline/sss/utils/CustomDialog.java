package com.mbcsonline.sss.utils;


import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.mbcsonline.sss.R;

public class CustomDialog {
    Context context ;
    public CustomDialog(Context ctx){
        this.context = ctx ;
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.error_dialog_title));
        builder.setMessage( message );
        builder.show();
    }

}
